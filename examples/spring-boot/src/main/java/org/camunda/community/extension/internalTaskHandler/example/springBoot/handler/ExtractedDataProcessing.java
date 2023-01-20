package org.camunda.community.extension.internalTaskHandler.example.springBoot.handler;

import org.camunda.community.extension.internalTaskHandler.InternalTask;
import org.camunda.community.extension.internalTaskHandler.InternalTaskHandler;
import org.camunda.community.extension.internalTaskHandler.InternalTaskService;
import org.camunda.community.extension.internalTaskHandler.springBoot.InternalTaskSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@InternalTaskSubscription(topicName = "extracted-data-processing")
public class ExtractedDataProcessing implements InternalTaskHandler {
  private static final Logger LOG = LoggerFactory.getLogger(ExtractedDataProcessing.class);

  @Override
  public void execute(InternalTask internalTask, InternalTaskService internalTaskService) {
    LOG.info("Processing extracted data");
    internalTaskService.complete(internalTask);
  }
}
