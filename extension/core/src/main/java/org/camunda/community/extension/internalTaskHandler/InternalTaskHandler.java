package org.camunda.community.extension.internalTaskHandler;

public interface InternalTaskHandler {
  void execute(InternalTask internalTask, InternalTaskService internalTaskService);
}
