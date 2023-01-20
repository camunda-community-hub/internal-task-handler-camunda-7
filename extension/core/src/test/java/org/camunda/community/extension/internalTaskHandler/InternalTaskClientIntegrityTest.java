package org.camunda.community.extension.internalTaskHandler;

import org.camunda.community.extension.internalTaskHandler.builder.InternalTaskClientConfigurationBuilder;
import org.camunda.community.extension.internalTaskHandler.builder.InternalTaskClientSubscriptionBuilder;
import org.junit.jupiter.api.Test;

public class InternalTaskClientIntegrityTest {
  @Test
  public void verifyIntegrity_InternalTaskClientConfiguration() {
    IntegrityVerifier.generateBuilder(InternalTaskClientConfiguration.class);
    IntegrityVerifier.verifyIntegrity(
        InternalTaskClientConfiguration.class, InternalTaskClientConfigurationBuilder.class);
  }

  @Test
  public void verifyIntegrity_InternalTaskClientSubscription() {
    IntegrityVerifier.verifyIntegrity(
        InternalTaskClientSubscription.class, InternalTaskClientSubscriptionBuilder.class);
  }
}
