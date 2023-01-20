package org.camunda.community.extension.internalTaskHandler;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.camunda.community.extension.internalTaskHandler.builder.InternalTaskClientSubscriptionBuilder;
import org.camunda.community.extension.internalTaskHandler.impl.InternalTaskClientSubscriptionImpl;

public interface InternalTaskClientSubscription {

  static InternalTaskClientSubscriptionBuilder builder() {
    return new InternalTaskClientSubscriptionImpl();
  }

  String getTopicName();

  Duration getLockDuration();

  List<String> getVariables();

  Map<String, Object> getProcessInstanceVariableEquals();

  String getBusinessKey();

  List<String> getProcessDefinitionIds();

  List<String> getProcessDefinitionKeys();

  String getProcessDefinitionVersionTag();

  List<String> getTenantIds();

  boolean isWithoutTenantId();

  boolean isLocalVariables();

  boolean isIncludeExtensionProperties();

  InternalTaskHandler getTaskHandler();

  ExecutorService getExecutor();
}
