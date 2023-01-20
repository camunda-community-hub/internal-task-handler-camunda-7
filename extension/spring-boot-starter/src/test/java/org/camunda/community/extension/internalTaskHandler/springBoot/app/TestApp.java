package org.camunda.community.extension.internalTaskHandler.springBoot.app;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.community.extension.internalTaskHandler.springBoot.EnableInternalTaskClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableInternalTaskClient
@EnableProcessApplication
public class TestApp {
  public static void main(String[] args) {
    SpringApplication.run(TestApp.class, args);
  }
}
