package org.camunda.community.extension.internalTaskHandler.builder;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClientSubscription;
import org.camunda.community.extension.internalTaskHandler.InternalTaskHandler;

public interface InternalTaskClientSubscriptionBuilder
    extends Builder<InternalTaskClientSubscription> {
  InternalTaskClientSubscriptionBuilder withVariables(List<String> variables);

  InternalTaskClientSubscriptionBuilder withProcessDefinitionIds(List<String> processDefinitionIds);

  InternalTaskClientSubscriptionBuilder withProcessDefinitionKeys(
      List<String> processDefinitionKeys);

  InternalTaskClientSubscriptionBuilder withProcessDefinitionVersionTag(
      String processDefinitionVersionTag);

  InternalTaskClientSubscriptionBuilder withLockDuration(Duration lockDuration);

  InternalTaskClientSubscriptionBuilder withTaskHandler(InternalTaskHandler taskHandler);

  InternalTaskClientSubscriptionBuilder withTenantIds(List<String> tenantIds);

  InternalTaskClientSubscriptionBuilder withProcessInstanceVariableEquals(
      Map<String, Object> processInstanceVariableEquals);

  InternalTaskClientSubscriptionBuilder withTopicName(String topicName);

  InternalTaskClientSubscriptionBuilder withBusinessKey(String businessKey);

  InternalTaskClientSubscriptionBuilder withExecutor(ExecutorService executor);
}
