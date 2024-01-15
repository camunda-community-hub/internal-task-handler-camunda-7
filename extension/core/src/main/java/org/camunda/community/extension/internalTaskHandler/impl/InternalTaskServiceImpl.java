package org.camunda.community.extension.internalTaskHandler.impl;

import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.community.extension.internalTaskHandler.InternalTask;
import org.camunda.community.extension.internalTaskHandler.InternalTaskService;
import org.camunda.community.extension.internalTaskHandler.util.ReflectionUtil;

public class InternalTaskServiceImpl implements InternalTaskService {
  private final ExternalTaskService externalTaskService;
  private final String workerId;
  private final RuntimeService runtimeService;

  public InternalTaskServiceImpl(
      ExternalTaskService externalTaskService, String workerId, RuntimeService runtimeService) {
    this.externalTaskService = externalTaskService;
    this.workerId = workerId;
    this.runtimeService = runtimeService;
  }

  @Override
  public void lock(String internalTaskId, long lockDuration) {
    externalTaskService.lock(internalTaskId, workerId, lockDuration);
  }

  @Override
  public void lock(InternalTask externalTask, long lockDuration) {
    lock(externalTask.getId(), lockDuration);
  }

  @Override
  public void unlock(InternalTask externalTask) {
    externalTaskService.unlock(externalTask.getId());
  }

  @Override
  public void unlock(String internalTaskId) {}

  @Override
  public void complete(InternalTask externalTask) {
    complete(externalTask, null, null);
  }

  @Override
  public void complete(InternalTask externalTask, Object variables) {
    complete(externalTask, variables, null);
  }

  @Override
  public void complete(InternalTask externalTask, Object variables, Object localVariables) {
    complete(externalTask.getId(), variables, localVariables);
  }

  @Override
  public void complete(String internalTaskId, Object variables, Object localVariables) {
    externalTaskService.complete(
        internalTaskId,
        workerId,
        ReflectionUtil.createMap(variables),
        ReflectionUtil.createMap(localVariables));
  }

  @Override
  public void handleFailure(
      InternalTask externalTask,
      String errorMessage,
      String errorDetails,
      int retries,
      long retryTimeout) {
    handleFailure(externalTask.getId(), errorMessage, errorMessage, retries, retryTimeout);
  }

  @Override
  public void handleFailure(
      String internalTaskId,
      String errorMessage,
      String errorDetails,
      int retries,
      long retryTimeout) {
    handleFailure(internalTaskId, errorMessage, errorDetails, retries, retryTimeout, null, null);
  }

  @Override
  public void handleFailure(
      String internalTaskId,
      String errorMessage,
      String errorDetails,
      int retries,
      long retryTimeout,
      Object variables,
      Object localVariables) {
    externalTaskService.handleFailure(
        internalTaskId,
        workerId,
        errorMessage,
        errorDetails,
        retries,
        retryTimeout,
        ReflectionUtil.createMap(variables),
        ReflectionUtil.createMap(localVariables));
  }

  @Override
  public void handleBpmnError(InternalTask externalTask, String errorCode) {
    handleBpmnError(externalTask, errorCode, null);
  }

  @Override
  public void handleBpmnError(InternalTask externalTask, String errorCode, String errorMessage) {
    handleBpmnError(externalTask, errorCode, errorMessage, null);
  }

  @Override
  public void handleBpmnError(
      InternalTask externalTask, String errorCode, String errorMessage, Object variables) {
    handleBpmnError(externalTask.getId(), errorCode, errorMessage, variables);
  }

  @Override
  public void handleBpmnError(
      String internalTaskId, String errorCode, String errorMessage, Object variables) {
    externalTaskService.handleBpmnError(
        internalTaskId, workerId, errorCode, errorMessage, ReflectionUtil.createMap(variables));
  }

  @Override
  public void extendLock(InternalTask externalTask, long newDuration) {
    extendLock(externalTask.getId(), newDuration);
  }

  @Override
  public void extendLock(String internalTaskId, long newDuration) {
    externalTaskService.extendLock(internalTaskId, workerId, newDuration);
  }

  @Override
  public void setVariable(String executionId, String variableName, Object variableValue) {
    runtimeService.setVariable(executionId, variableName, variableValue);
  }
}
