package org.camunda.community.extension.internalTaskHandler.example.springBoot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfiguration {

  @Bean
  @Qualifier("dmsExecutor")
  public ExecutorService dmsExecutor() {
    return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1));
  }

  @Bean
  @Qualifier("internalTaskClientExecutor")
  public ExecutorService internalTaskClientExecutor() {
    return new ThreadPoolExecutor(1, 30, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(60));
  }
}
