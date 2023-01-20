package org.camunda.community.extension.internalTaskHandler.springBoot;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.camunda.bpm.spring.boot.starter.event.PreUndeployEvent;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClient;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClientSubscription;
import org.camunda.community.extension.internalTaskHandler.InternalTaskHandler;
import org.camunda.community.extension.internalTaskHandler.springBoot.InternalTaskSubscription.VariableEquals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class InternalTaskClientSubscriptionLifecycle {
  private static final Logger LOG =
      LoggerFactory.getLogger(InternalTaskClientSubscriptionLifecycle.class);
  private final InternalTaskClient internalTaskClient;
  private final Set<InternalTaskClientSubscription> internalTaskClientSubscriptions;
  private final ApplicationContext applicationContext;

  @Autowired
  public InternalTaskClientSubscriptionLifecycle(
      InternalTaskClient internalTaskClient,
      Set<InternalTaskHandler> internalTaskHandlers,
      ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    this.internalTaskClient = internalTaskClient;
    this.internalTaskClientSubscriptions = fromHandlerBeans(internalTaskHandlers);
  }

  @EventListener(PostDeployEvent.class)
  public void postDeploy() {
    LOG.info("Subscribing Handlers");
    internalTaskClientSubscriptions.forEach(internalTaskClient::subscribe);
    internalTaskClientSubscriptions.forEach(
        s -> LOG.info("{}: {}", s.getTopicName(), s.getTaskHandler()));
    LOG.info("Handlers subscribed");
  }

  private Set<InternalTaskClientSubscription> fromHandlerBeans(
      Set<InternalTaskHandler> internalTaskHandlers) {
    return internalTaskHandlers.stream()
        .map(
            internalTaskHandler ->
                Optional.ofNullable(
                        internalTaskHandler
                            .getClass()
                            .getAnnotation(InternalTaskSubscription.class))
                    .map(annotation -> fromAnnotation(annotation, internalTaskHandler)))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toSet());
  }

  private InternalTaskClientSubscription fromAnnotation(
      InternalTaskSubscription annotation, InternalTaskHandler internalTaskHandler) {
    return InternalTaskClientSubscription.builder()
        .withBusinessKey(annotation.businessKey().equals("") ? null : annotation.businessKey())
        .withLockDuration(Duration.parse(annotation.lockDuration()))
        .withProcessDefinitionIds(
            annotation.processDefinitionIds().length == 0
                ? null
                : Arrays.asList(annotation.processDefinitionIds()))
        .withProcessDefinitionKeys(
            annotation.processDefinitionKeys().length == 0
                ? null
                : Arrays.asList(annotation.processDefinitionKeys()))
        .withProcessDefinitionVersionTag(
            annotation.processDefinitionVersionTag().equals("")
                ? null
                : annotation.processDefinitionVersionTag())
        .withProcessInstanceVariableEquals(
            annotation.processInstanceVariableEquals().length == 0
                ? null
                : Arrays.stream(annotation.processInstanceVariableEquals())
                    .collect(
                        Collectors.toMap(
                            VariableEquals::variableName,
                            v -> {
                              try {
                                return v.variableValue().getDeclaredConstructor().newInstance();
                              } catch (InstantiationException
                                  | IllegalAccessException
                                  | InvocationTargetException
                                  | NoSuchMethodException e) {
                                throw new RuntimeException(e);
                              }
                            })))
        .withTenantIds(
            annotation.tenantIds().length == 0 ? null : Arrays.asList(annotation.tenantIds()))
        .withTopicName(annotation.topicName())
        .withVariables(
            annotation.variables().length == 0 ? null : Arrays.asList(annotation.variables()))
        .withTaskHandler(internalTaskHandler)
        .withExecutor(findExecutor(annotation.executorBeanName()))
        .build();
  }

  private ExecutorService findExecutor(String executorBeanName) {
    if (StringUtils.hasText(executorBeanName)) {
      try {
        return applicationContext.getBean(executorBeanName, ExecutorService.class);
      } catch (BeansException e) {
        LOG.error("Did not find 'executorBeanName', not setting it.", e);
      }
    }
    return null;
  }

  @EventListener(PreUndeployEvent.class)
  public void preUndeploy() {
    LOG.info("Unsubscribing Handlers");
    internalTaskClientSubscriptions.forEach(internalTaskClient::unsubscribe);
    internalTaskClientSubscriptions.forEach(
        s -> LOG.info("{}: {}", s.getTopicName(), s.getTaskHandler()));
    LOG.info("Handlers unsubscribed");
  }
}
