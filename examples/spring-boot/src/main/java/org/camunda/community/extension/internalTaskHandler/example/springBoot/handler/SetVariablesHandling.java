package org.camunda.community.extension.internalTaskHandler.example.springBoot.handler;

import org.camunda.community.extension.internalTaskHandler.InternalTask;
import org.camunda.community.extension.internalTaskHandler.InternalTaskHandler;
import org.camunda.community.extension.internalTaskHandler.InternalTaskService;
import org.camunda.community.extension.internalTaskHandler.springBoot.InternalTaskSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@InternalTaskSubscription(topicName = "set-variables")
public class SetVariablesHandling implements InternalTaskHandler {
  private static final Logger LOG = LoggerFactory.getLogger(SetVariablesHandling.class);
  @Override
  public void execute(InternalTask internalTask, InternalTaskService internalTaskService) {
    String item = internalTask.getVariable("item");
    LOG.info("Item: {}",item);
    internalTaskService.setVariable(internalTask.getExecutionId(), "myVarLocal", "local");
    internalTaskService.setVariable(internalTask.getProcessInstanceId(), "myVarGlobal", "global");
    Boolean complete = internalTask.getVariable("complete");
    if (complete != null && complete) {
      internalTaskService.complete(internalTask);
    }
  }
}
