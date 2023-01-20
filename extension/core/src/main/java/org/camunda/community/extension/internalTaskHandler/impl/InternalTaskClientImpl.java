package org.camunda.community.extension.internalTaskHandler.impl;

import org.camunda.community.extension.internalTaskHandler.InternalTaskClient;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClientConfiguration;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClientSubscription;

public class InternalTaskClientImpl implements InternalTaskClient {
  private final TopicSubscriptionManager subscriptionManager;

  public InternalTaskClientImpl(InternalTaskClientConfiguration configuration) {
    subscriptionManager = new TopicSubscriptionManager(configuration);
  }

  @Override
  public InternalTaskClient subscribe(InternalTaskClientSubscription subscription) {
    subscriptionManager.subscribe(subscription);
    return this;
  }

  @Override
  public InternalTaskClient unsubscribe(InternalTaskClientSubscription subscription) {
    subscriptionManager.unsubscribe(subscription);
    return this;
  }

  @Override
  public void stop() {
    subscriptionManager.stop();
  }

  @Override
  public void start() {
    subscriptionManager.start();
  }

  @Override
  public boolean isActive() {
    return subscriptionManager.isRunning();
  }
}
