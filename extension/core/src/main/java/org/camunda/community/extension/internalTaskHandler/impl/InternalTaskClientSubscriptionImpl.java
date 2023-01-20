package org.camunda.community.extension.internalTaskHandler.impl;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClientSubscription;
import org.camunda.community.extension.internalTaskHandler.InternalTaskHandler;
import org.camunda.community.extension.internalTaskHandler.builder.InternalTaskClientSubscriptionBuilder;

public class InternalTaskClientSubscriptionImpl
    implements InternalTaskClientSubscription, InternalTaskClientSubscriptionBuilder {
  private String topicName;
  private Duration lockDuration;
  private List<String> variables;
  private Map<String, Object> processInstanceVariableEquals;
  private String businessKey;
  private List<String> processDefinitionIds;
  private List<String> processDefinitionKeys;
  private String processDefinitionVersionTag;
  private List<String> tenantIds;
  private boolean withoutTenantId;
  private boolean localVariables;
  private boolean includeExtensionProperties;
  private InternalTaskHandler taskHandler;

  private ExecutorService executor;

  @Override
  public String getTopicName() {
    return topicName;
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  @Override
  public Duration getLockDuration() {
    return lockDuration;
  }

  public void setLockDuration(Duration lockDuration) {
    this.lockDuration = lockDuration;
  }

  @Override
  public List<String> getVariables() {
    return variables;
  }

  public void setVariables(List<String> variables) {
    this.variables = variables;
  }

  @Override
  public Map<String, Object> getProcessInstanceVariableEquals() {
    return processInstanceVariableEquals;
  }

  public void setProcessInstanceVariableEquals(Map<String, Object> processInstanceVariableEquals) {
    this.processInstanceVariableEquals = processInstanceVariableEquals;
  }

  @Override
  public String getBusinessKey() {
    return businessKey;
  }

  public void setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
  }

  @Override
  public List<String> getProcessDefinitionIds() {
    return processDefinitionIds;
  }

  public void setProcessDefinitionIds(List<String> processDefinitionIds) {
    this.processDefinitionIds = processDefinitionIds;
  }

  @Override
  public List<String> getProcessDefinitionKeys() {
    return processDefinitionKeys;
  }

  public void setProcessDefinitionKeys(List<String> processDefinitionKeys) {
    this.processDefinitionKeys = processDefinitionKeys;
  }

  @Override
  public String getProcessDefinitionVersionTag() {
    return processDefinitionVersionTag;
  }

  public void setProcessDefinitionVersionTag(String processDefinitionVersionTag) {
    this.processDefinitionVersionTag = processDefinitionVersionTag;
  }

  @Override
  public List<String> getTenantIds() {
    return tenantIds;
  }

  public void setTenantIds(List<String> tenantIds) {
    this.tenantIds = tenantIds;
  }

  @Override
  public boolean isWithoutTenantId() {
    return withoutTenantId;
  }

  public void setWithoutTenantId(boolean withoutTenantId) {
    this.withoutTenantId = withoutTenantId;
  }

  @Override
  public boolean isLocalVariables() {
    return localVariables;
  }

  public void setLocalVariables(boolean localVariables) {
    this.localVariables = localVariables;
  }

  @Override
  public boolean isIncludeExtensionProperties() {
    return includeExtensionProperties;
  }

  public void setIncludeExtensionProperties(boolean includeExtensionProperties) {
    this.includeExtensionProperties = includeExtensionProperties;
  }

  @Override
  public InternalTaskHandler getTaskHandler() {
    return taskHandler;
  }

  public void setTaskHandler(InternalTaskHandler taskHandler) {
    this.taskHandler = taskHandler;
  }

  @Override
  public ExecutorService getExecutor() {
    return executor;
  }

  public void setExecutor(ExecutorService executor) {
    this.executor = executor;
  }

  @Override
  public InternalTaskClientSubscription build() {
    return this;
  }

  @Override
  public InternalTaskClientSubscriptionBuilder withVariables(List<String> variables) {
    this.variables = variables;
    return this;
  }

  @Override
  public InternalTaskClientSubscriptionBuilder withProcessDefinitionIds(
      List<String> processDefinitionIds) {
    this.processDefinitionIds = processDefinitionIds;
    return this;
  }

  @Override
  public InternalTaskClientSubscriptionBuilder withProcessDefinitionKeys(
      List<String> processDefinitionKeys) {
    this.processDefinitionKeys = processDefinitionKeys;
    return this;
  }

  @Override
  public InternalTaskClientSubscriptionBuilder withProcessDefinitionVersionTag(
      String processDefinitionVersionTag) {
    this.processDefinitionVersionTag = processDefinitionVersionTag;
    return this;
  }

  @Override
  public InternalTaskClientSubscriptionBuilder withLockDuration(Duration lockDuration) {
    this.lockDuration = lockDuration;
    return this;
  }

  @Override
  public InternalTaskClientSubscriptionBuilder withTaskHandler(InternalTaskHandler taskHandler) {
    this.taskHandler = taskHandler;
    return this;
  }

  @Override
  public InternalTaskClientSubscriptionBuilder withTenantIds(List<String> tenantIds) {
    this.tenantIds = tenantIds;
    return this;
  }

  @Override
  public InternalTaskClientSubscriptionBuilder withProcessInstanceVariableEquals(
      Map<String, Object> processInstanceVariableEquals) {
    this.processInstanceVariableEquals = processInstanceVariableEquals;
    return this;
  }

  @Override
  public InternalTaskClientSubscriptionBuilder withTopicName(String topicName) {
    this.topicName = topicName;
    return this;
  }

  @Override
  public InternalTaskClientSubscriptionBuilder withBusinessKey(String businessKey) {
    this.businessKey = businessKey;
    return this;
  }

  @Override
  public InternalTaskClientSubscriptionBuilder withExecutor(ExecutorService executor) {
    this.executor = executor;
    return this;
  }
}
