package org.camunda.community.extension.internalTaskHandler.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.externaltask.ExternalTaskQueryBuilder;
import org.camunda.bpm.engine.externaltask.ExternalTaskQueryTopicBuilder;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.community.extension.internalTaskHandler.BackoffStrategy;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClientConfiguration;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClientSubscription;
import org.camunda.community.extension.internalTaskHandler.InternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopicSubscriptionManager implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(TopicSubscriptionManager.class);
  private final CopyOnWriteArrayList<InternalTaskClientSubscription> subscriptions =
      new CopyOnWriteArrayList<>();
  private final AtomicBoolean isRunning = new AtomicBoolean(false);
  private final ExternalTaskService externalTaskService;
  private final InternalTaskService internalTaskService;
  private final String workerId;
  private final int maxTasks;
  private final Duration lockDuration;
  private final ReentrantLock ACQUISITION_MONITOR = new ReentrantLock(false);
  private final Condition IS_WAITING = ACQUISITION_MONITOR.newCondition();
  private final BackoffStrategy backoffStrategy;
  private final ExecutorService executor;

  private Thread runner;

  public TopicSubscriptionManager(InternalTaskClientConfiguration configuration) {
    this.externalTaskService = configuration.getExternalTaskService();
    this.workerId = configuration.getWorkerId();
    this.maxTasks = configuration.getMaxTasks();
    this.lockDuration = configuration.getLockDuration();
    this.internalTaskService =
        new InternalTaskServiceImpl(
            externalTaskService, workerId, configuration.getRuntimeService());
    this.backoffStrategy = configuration.getBackoffStrategy();
    this.executor = configuration.getExecutor();
  }

  @Override
  public void run() {
    while (isRunning.get()) {
      try {
        acquire();
      } catch (Throwable e) {
        LOG.error("Exception while acquiring tasks:", e);
      }
    }
  }

  public void start() {
    if (isRunning.compareAndSet(false, true)) {
      runner = new Thread(this, getClass().getName());
      runner.start();
    }
  }

  public void stop() {
    if (isRunning.compareAndSet(true, false)) {
      executor.shutdown();
      resume();
      try {
        runner.join();
        executor.awaitTermination(60, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        LOG.error("Exception while shutting down:", e);
      }
    }
  }

  public boolean isRunning() {
    return isRunning.get();
  }

  protected void subscribe(InternalTaskClientSubscription subscription) {
    if (!subscriptions.addIfAbsent(subscription)) {
      String topicName = subscription.getTopicName();
      throw new RuntimeException("Topic '" + topicName + "' already has an open subscription");
    }
    resume();
  }

  protected void unsubscribe(InternalTaskClientSubscription subscription) {
    subscriptions.remove(subscription);
    ExecutorService executorService = subscription.getExecutor();
    if (executorService != null) {
      executorService.shutdown();
      try {
        executorService.awaitTermination(1, TimeUnit.MINUTES);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void acquire() {
    ExternalTaskQueryBuilder fetchAndLock =
        externalTaskService.fetchAndLock(maxTasks, workerId, true);
    Map<String, InternalTaskClientSubscription> handlers = new HashMap<>();
    for (InternalTaskClientSubscription subscription : subscriptions) {
      handlers.put(subscription.getTopicName(), subscription);
      ExternalTaskQueryTopicBuilder topic =
          fetchAndLock.topic(
              subscription.getTopicName(),
              subscription.getLockDuration() == null
                  ? lockDuration.getSeconds() * 1000
                  : subscription.getLockDuration().getSeconds() * 1000);
      setIfPresent(subscription.getBusinessKey(), topic::businessKey);
      setIfPresent(subscription.getVariables(), topic::variables);
      setIfPresent(
          subscription.getProcessInstanceVariableEquals(), topic::processInstanceVariableEquals);
      setIfPresent(subscription.getProcessDefinitionIds(), toArray(), topic::processDefinitionIdIn);
      setIfPresent(
          subscription.getProcessDefinitionKeys(), toArray(), topic::processDefinitionKeyIn);
      setIfPresent(
          subscription.getProcessDefinitionVersionTag(), topic::processDefinitionVersionTag);
      setIfPresent(subscription.getTenantIds(), toArray(), topic::tenantIdIn);
      setIf(subscription.isWithoutTenantId(), topic::withoutTenantId);
      setIf(subscription.isLocalVariables(), topic::localVariables);
      setIf(subscription.isIncludeExtensionProperties(), topic::includeExtensionProperties);
    }
    List<LockedExternalTask> externalTasks = fetchAndLock.execute();
    for (LockedExternalTask externalTask : new ArrayList<>(externalTasks)) {
      InternalTaskClientSubscription subscription = handlers.get(externalTask.getTopicName());
      if (subscription != null) {
        ExecutorService executorToUse =
            subscription.getExecutor() == null ? executor : subscription.getExecutor();
        try {
          LOG.debug("Handling task {}", externalTask.getId());
          executorToUse.submit(
              () -> {
                subscription
                    .getTaskHandler()
                    .execute(new InternalTaskImpl(externalTask), internalTaskService);
                resume();
              });
          LOG.debug("Handled internal task {}", externalTask.getId());
        } catch (RejectedExecutionException e) {
          externalTasks.remove(externalTask);
          LOG.debug(
              "Executor for subscription {} is not ready to execute tasks",
              externalTask.getTopicName());
          externalTaskService.unlock(externalTask.getId());
        } catch (BpmnError e) {
          LOG.debug(
              "BPMN error {} was thrown while handling internal task {}",
              e.getErrorCode(),
              externalTask.getId());
          externalTaskService.handleBpmnError(
              externalTask.getId(), workerId, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
          LOG.debug(
              "There was an exception {} while handling internal task {}",
              e.getMessage(),
              externalTask.getId());
          externalTaskService.handleFailure(
              externalTask.getId(), workerId, e.getMessage(), buildErrorDetails(e), 0, 0);
        }
      } else {
        LOG.error("There was no handler found for topic {}", externalTask.getTopicName());
        externalTaskService.unlock(externalTask.getId());
        externalTasks.remove(externalTask);
      }
    }
    runBackoffStrategy(externalTasks);
  }

  private String buildErrorDetails(Exception e) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    return sw.toString();
  }

  private void setIf(boolean condition, Runnable invocation) {
    if (condition) {
      invocation.run();
    }
  }

  private <T> void setIfPresent(T value, Consumer<T> setterInvocation) {
    if (value == null) {
      return;
    }
    setterInvocation.accept(value);
  }

  private <T, V> void setIfPresent(T value, Function<T, V> mapper, Consumer<V> setterInvocation) {
    if (value == null) {
      return;
    }
    setterInvocation.accept(mapper.apply(value));
  }

  private Function<List<String>, String[]> toArray() {
    return list -> list.toArray(new String[] {});
  }

  protected void suspend(long waitTime) {
    if (waitTime > 0 && isRunning.get()) {
      ACQUISITION_MONITOR.lock();
      try {
        if (isRunning.get()) {
          IS_WAITING.await(waitTime, TimeUnit.MILLISECONDS);
        }
      } catch (InterruptedException e) {
        LOG.error("Exception while executing backoff strategy", e);
      } finally {
        ACQUISITION_MONITOR.unlock();
      }
    }
  }

  protected void resume() {
    ACQUISITION_MONITOR.lock();
    try {
      IS_WAITING.signal();
    } finally {
      ACQUISITION_MONITOR.unlock();
    }
  }

  protected void runBackoffStrategy(List<LockedExternalTask> externalTasks) {
    try {
      backoffStrategy.reconfigure(externalTasks);
      long waitTime = backoffStrategy.calculateBackoffTime();
      suspend(waitTime);
    } catch (Throwable e) {
      LOG.error("Exception while executing backoff strategy method", e);
    }
  }
}
