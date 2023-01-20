package org.camunda.community.extension.internalTaskHandler.springBoot;

import java.util.concurrent.atomic.AtomicBoolean;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class InternalTaskClientLifecycle implements SmartLifecycle {
  private static final Logger LOG = LoggerFactory.getLogger(InternalTaskClientLifecycle.class);
  private final InternalTaskClient internalTaskClient;
  private final AtomicBoolean isRunning = new AtomicBoolean(false);

  @Autowired
  public InternalTaskClientLifecycle(InternalTaskClient internalTaskClient) {
    this.internalTaskClient = internalTaskClient;
  }

  @Override
  public void start() {
    LOG.info("Starting Internal Task Client");
    internalTaskClient.start();
    isRunning.set(true);
    LOG.info("Internal Task Client started");
  }

  @Override
  public void stop() {
    LOG.info("Stopping Internal Task Client");
    internalTaskClient.stop();
    isRunning.set(false);
    LOG.info("Internal Task Client stopped");
  }

  @Override
  public boolean isRunning() {
    return isRunning.get();
  }
}
