package org.camunda.community.extension.internalTaskHandler.example.springBoot;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.community.extension.internalTaskHandler.springBoot.EnableInternalTaskClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableProcessApplication
@EnableInternalTaskClient
@SpringBootApplication
public class App {
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
