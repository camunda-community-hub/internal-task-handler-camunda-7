package org.camunda.community.extension.internalTaskHandler.springBoot.app;

import org.camunda.community.extension.internalTaskHandler.InternalTask;
import org.camunda.community.extension.internalTaskHandler.InternalTaskHandler;
import org.camunda.community.extension.internalTaskHandler.InternalTaskService;
import org.camunda.community.extension.internalTaskHandler.springBoot.InternalTaskSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@InternalTaskSubscription(topicName = "test")
@Component
public class TestHandler implements InternalTaskHandler {
  private static final Logger LOG = LoggerFactory.getLogger(TestHandler.class);

  @Override
  public void execute(InternalTask internalTask, InternalTaskService internalTaskService) {
    LOG.info("Completing task 'test'");
    internalTaskService.complete(internalTask);
  }
}
