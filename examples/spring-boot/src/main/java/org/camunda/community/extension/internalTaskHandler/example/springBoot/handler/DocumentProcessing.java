package org.camunda.community.extension.internalTaskHandler.example.springBoot.handler;

import org.camunda.community.extension.internalTaskHandler.InternalTask;
import org.camunda.community.extension.internalTaskHandler.InternalTaskHandler;
import org.camunda.community.extension.internalTaskHandler.InternalTaskService;
import org.camunda.community.extension.internalTaskHandler.example.springBoot.DocumentProcessingVariables;
import org.camunda.community.extension.internalTaskHandler.springBoot.InternalTaskSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@InternalTaskSubscription(topicName = "document-processing")
public class DocumentProcessing implements InternalTaskHandler {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentProcessing.class);

  @Override
  public void execute(InternalTask internalTask, InternalTaskService internalTaskService) {
    DocumentProcessingVariables variables =
        internalTask.getVariablesAsType(DocumentProcessingVariables.class);
    variables.setClear(true);
    if (variables.getDocumentName().contains("dirty")) {
      variables.setClear(false);
    }
    LOG.info("Document information is {} clear", variables.getClear() ? "" : "not ");
    internalTaskService.complete(internalTask, variables);
  }
}
