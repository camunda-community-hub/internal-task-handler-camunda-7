package org.camunda.community.extension.internalTaskHandler.springBoot;

import java.util.concurrent.ExecutorService;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.community.extension.internalTaskHandler.BackoffStrategy;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClient;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClientConfiguration;
import org.camunda.community.extension.internalTaskHandler.InternalTaskService;
import org.camunda.community.extension.internalTaskHandler.impl.InternalTaskClientProperties;
import org.camunda.community.extension.internalTaskHandler.impl.InternalTaskServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("camunda.bpm.extension.internal-task-client")
public class InternalTaskClientSpringBootConfiguration extends InternalTaskClientProperties {

  @Bean
  public InternalTaskClient internalTaskClient(
      ExternalTaskService externalTaskService,
      RuntimeService runtimeService,
      BackoffStrategy backoffStrategy,
      @Qualifier("internalTaskClientExecutor") ExecutorService executor) {
    return InternalTaskClient.create(
        InternalTaskClientConfiguration.usingProperties(
            this, externalTaskService, runtimeService, backoffStrategy, executor));
  }

  @Bean
  public InternalTaskService internalTaskService(
      ExternalTaskService externalTaskService, RuntimeService runtimeService) {
    return new InternalTaskServiceImpl(externalTaskService, this.getWorkerId(), runtimeService);
  }

  @Bean
  @ConditionalOnMissingBean
  public BackoffStrategy backoffStrategy() {
    return InternalTaskClientConfiguration.DEFAULT_BACKOFF_STRATEGY;
  }

  @Bean
  @Qualifier("internalTaskClientExecutor")
  @ConditionalOnMissingBean(name = "internalTaskClientExecutor")
  public ExecutorService executor() {
    return InternalTaskClientConfiguration.DEFAULT_EXECUTOR;
  }
}
