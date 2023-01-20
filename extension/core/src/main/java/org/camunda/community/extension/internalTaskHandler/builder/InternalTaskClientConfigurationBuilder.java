package org.camunda.community.extension.internalTaskHandler.builder;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.community.extension.internalTaskHandler.BackoffStrategy;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClientConfiguration;

public interface InternalTaskClientConfigurationBuilder
    extends Builder<InternalTaskClientConfiguration> {
  InternalTaskClientConfigurationBuilder withMaxTasks(int maxTasks);

  InternalTaskClientConfigurationBuilder withExternalTaskService(
      ExternalTaskService externalTaskService);

  InternalTaskClientConfigurationBuilder withLockDuration(Duration lockDuration);

  InternalTaskClientConfigurationBuilder withBackoffStrategy(BackoffStrategy backoffStrategy);

  InternalTaskClientConfigurationBuilder withWorkerId(String workerId);

  InternalTaskClientConfigurationBuilder withExecutor(ExecutorService executor);
}
