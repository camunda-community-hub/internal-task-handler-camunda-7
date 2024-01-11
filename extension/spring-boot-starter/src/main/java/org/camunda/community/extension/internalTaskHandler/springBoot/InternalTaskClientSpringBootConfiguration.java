package org.camunda.community.extension.internalTaskHandler.springBoot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.community.extension.internalTaskHandler.BackoffStrategy;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClient;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClientConfiguration;
import org.camunda.community.extension.internalTaskHandler.InternalTaskService;
import org.camunda.community.extension.internalTaskHandler.impl.DefaultBackoffStrategy;
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
      BackoffStrategy backoffStrategy,
      @Qualifier("internalTaskClientExecutor") ExecutorService executor) {
    return InternalTaskClient.create(
        InternalTaskClientConfiguration.usingProperties(
            this, externalTaskService, backoffStrategy, executor));
  }

  @Bean
  public InternalTaskService internalTaskService(ExternalTaskService externalTaskService) {
    return new InternalTaskServiceImpl(externalTaskService, this.getWorkerId());
  }

  @Bean
  @ConditionalOnMissingBean
  public BackoffStrategy backoffStrategy() {
    return new DefaultBackoffStrategy();
  }

  @Bean
  @Qualifier("internalTaskClientExecutor")
  @ConditionalOnMissingBean(name = "internalTaskClientExecutor")
  public ExecutorService executor() {
    return Executors.newSingleThreadExecutor();
  }
}
