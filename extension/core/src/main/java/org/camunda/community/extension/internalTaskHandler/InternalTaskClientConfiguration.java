package org.camunda.community.extension.internalTaskHandler;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.community.extension.internalTaskHandler.builder.InternalTaskClientConfigurationBuilder;
import org.camunda.community.extension.internalTaskHandler.impl.DefaultBackoffStrategy;
import org.camunda.community.extension.internalTaskHandler.impl.InternalTaskClientConfigurationImpl;
import org.camunda.community.extension.internalTaskHandler.impl.InternalTaskClientProperties;

public interface InternalTaskClientConfiguration {
  BackoffStrategy DEFAULT_BACKOFF_STRATEGY = new DefaultBackoffStrategy();
  String DEFAULT_WORKER_ID = "internal";
  Duration DEFAULT_LOCK_DURATION = Duration.ofSeconds(60);
  int DEFAULT_MAX_TASKS = 500;

  ExecutorService DEFAULT_EXECUTOR =
      new ThreadPoolExecutor(1, 10, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(30));

  static InternalTaskClientConfiguration defaultConfig() {
    return new InternalTaskClientConfigurationImpl();
  }

  static InternalTaskClientConfigurationBuilder builder() {
    return new InternalTaskClientConfigurationImpl();
  }

  static InternalTaskClientConfiguration usingProperties(
      InternalTaskClientProperties properties,
      ExternalTaskService externalTaskService,
      BackoffStrategy backoffStrategy,
      ExecutorService executor) {
    return new InternalTaskClientConfigurationImpl(
        properties, externalTaskService, backoffStrategy, executor);
  }

  ExternalTaskService getExternalTaskService();

  String getWorkerId();

  int getMaxTasks();

  Duration getLockDuration();

  BackoffStrategy getBackoffStrategy();

  ExecutorService getExecutor();
}
