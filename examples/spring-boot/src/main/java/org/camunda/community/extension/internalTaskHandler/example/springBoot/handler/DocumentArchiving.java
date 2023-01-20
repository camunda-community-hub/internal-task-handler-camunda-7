package org.camunda.community.extension.internalTaskHandler.example.springBoot.handler;

import java.util.Random;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.camunda.community.extension.internalTaskHandler.InternalTask;
import org.camunda.community.extension.internalTaskHandler.InternalTaskHandler;
import org.camunda.community.extension.internalTaskHandler.InternalTaskService;
import org.camunda.community.extension.internalTaskHandler.springBoot.InternalTaskSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@InternalTaskSubscription(topicName = "document-archiving", executorBeanName = "dmsExecutor")
public class DocumentArchiving implements InternalTaskHandler {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentArchiving.class);

  @Override
  public void execute(InternalTask internalTask, InternalTaskService internalTaskService) {
    int duration = new Random().nextInt(6) + 5;
    LOG.info("Saving document to archive");
    try {
      Thread.sleep(duration * 1000L);
    } catch (InterruptedException e) {
      internalTaskService.handleFailure(
          internalTask, e.getMessage(), ExceptionUtils.getStackTrace(e), 0, 0);
    }
    LOG.info("Saved document to archive");
    internalTaskService.complete(internalTask);
  }
}
