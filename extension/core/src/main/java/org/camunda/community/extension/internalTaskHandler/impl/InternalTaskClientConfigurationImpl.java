package org.camunda.community.extension.internalTaskHandler.impl;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.community.extension.internalTaskHandler.BackoffStrategy;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClientConfiguration;
import org.camunda.community.extension.internalTaskHandler.builder.InternalTaskClientConfigurationBuilder;

public class InternalTaskClientConfigurationImpl extends InternalTaskClientProperties
    implements InternalTaskClientConfiguration, InternalTaskClientConfigurationBuilder {
  private ExternalTaskService externalTaskService =
      ProcessEngines.getDefaultProcessEngine(false).getExternalTaskService();
  private BackoffStrategy backoffStrategy =
      InternalTaskClientConfiguration.DEFAULT_BACKOFF_STRATEGY;

  private ExecutorService executor = InternalTaskClientConfiguration.DEFAULT_EXECUTOR;

  public InternalTaskClientConfigurationImpl() {}

  public InternalTaskClientConfigurationImpl(
      InternalTaskClientProperties properties,
      ExternalTaskService externalTaskService,
      BackoffStrategy backoffStrategy,
      ExecutorService executor) {
    setWorkerId(properties.getWorkerId());
    setMaxTasks(properties.getMaxTasks());
    setLockDuration(properties.getLockDuration());
    this.externalTaskService = externalTaskService;
    this.backoffStrategy = backoffStrategy;
    this.executor = executor;
  }

  @Override
  public ExternalTaskService getExternalTaskService() {
    return externalTaskService;
  }

  public void setExternalTaskService(ExternalTaskService externalTaskService) {
    this.externalTaskService = externalTaskService;
  }

  @Override
  public BackoffStrategy getBackoffStrategy() {
    return backoffStrategy;
  }

  public void setBackoffStrategy(BackoffStrategy backoffStrategy) {
    this.backoffStrategy = backoffStrategy;
  }

  @Override
  public InternalTaskClientConfiguration build() {
    return this;
  }

  @Override
  public InternalTaskClientConfigurationBuilder withExternalTaskService(
      ExternalTaskService externalTaskService) {
    this.externalTaskService = externalTaskService;
    return this;
  }

  @Override
  public InternalTaskClientConfigurationBuilder withWorkerId(String workerId) {
    setWorkerId(workerId);
    return this;
  }

  @Override
  public InternalTaskClientConfigurationBuilder withMaxTasks(int maxTasks) {
    setMaxTasks(maxTasks);
    return this;
  }

  @Override
  public InternalTaskClientConfigurationBuilder withLockDuration(Duration lockDuration) {
    setLockDuration(lockDuration);
    return this;
  }

  @Override
  public InternalTaskClientConfigurationBuilder withBackoffStrategy(
      BackoffStrategy backoffStrategy) {
    this.backoffStrategy = backoffStrategy;
    return this;
  }

  @Override
  public ExecutorService getExecutor() {
    return executor;
  }

  public void setExecutor(ExecutorService executor) {
    this.executor = executor;
  }

  @Override
  public InternalTaskClientConfigurationBuilder withExecutor(ExecutorService executor) {
    this.executor = executor;
    return this;
  }
}
