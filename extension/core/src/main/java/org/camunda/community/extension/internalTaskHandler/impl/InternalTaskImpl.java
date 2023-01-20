package org.camunda.community.extension.internalTaskHandler.impl;

import static org.camunda.community.extension.internalTaskHandler.util.ReflectionUtil.*;

import java.util.Date;
import java.util.Map;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.community.extension.internalTaskHandler.InternalTask;

public class InternalTaskImpl implements InternalTask {
  private final LockedExternalTask lockedExternalTask;

  public InternalTaskImpl(LockedExternalTask lockedExternalTask) {
    this.lockedExternalTask = lockedExternalTask;
  }

  @Override
  public String getActivityId() {
    return lockedExternalTask.getActivityId();
  }

  @Override
  public String getActivityInstanceId() {
    return lockedExternalTask.getActivityInstanceId();
  }

  @Override
  public String getErrorMessage() {
    return lockedExternalTask.getErrorMessage();
  }

  @Override
  public String getErrorDetails() {
    return lockedExternalTask.getErrorDetails();
  }

  @Override
  public String getExecutionId() {
    return lockedExternalTask.getExecutionId();
  }

  @Override
  public String getId() {
    return lockedExternalTask.getId();
  }

  @Override
  public Date getLockExpirationTime() {
    return lockedExternalTask.getLockExpirationTime();
  }

  @Override
  public String getProcessDefinitionId() {
    return lockedExternalTask.getProcessDefinitionId();
  }

  @Override
  public String getProcessDefinitionKey() {
    return lockedExternalTask.getProcessDefinitionKey();
  }

  @Override
  public String getProcessDefinitionVersionTag() {
    return lockedExternalTask.getProcessDefinitionVersionTag();
  }

  @Override
  public String getProcessInstanceId() {
    return lockedExternalTask.getProcessInstanceId();
  }

  @Override
  public Integer getRetries() {
    return lockedExternalTask.getRetries();
  }

  @Override
  public String getWorkerId() {
    return lockedExternalTask.getWorkerId();
  }

  @Override
  public String getTopicName() {
    return lockedExternalTask.getTopicName();
  }

  @Override
  public String getTenantId() {
    return lockedExternalTask.getTenantId();
  }

  @Override
  public long getPriority() {
    return lockedExternalTask.getPriority();
  }

  @Override
  public <T> T getVariable(String variableName) {
    return (T) lockedExternalTask.getVariables().get(variableName);
  }

  @Override
  public <T> T getVariablesAsType(Class<T> type) {
    T container = createInstance(type);
    decorate(container, lockedExternalTask.getVariables());
    return container;
  }

  @Override
  public String getBusinessKey() {
    return lockedExternalTask.getBusinessKey();
  }

  @Override
  public String getExtensionProperty(String propertyKey) {
    return lockedExternalTask.getExtensionProperties().get(propertyKey);
  }

  @Override
  public Map<String, String> getExtensionProperties() {
    return lockedExternalTask.getExtensionProperties();
  }
}
