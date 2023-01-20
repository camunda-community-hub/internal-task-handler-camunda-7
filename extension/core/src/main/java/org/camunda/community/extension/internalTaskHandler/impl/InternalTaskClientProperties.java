package org.camunda.community.extension.internalTaskHandler.impl;

import java.time.Duration;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClientConfiguration;

public class InternalTaskClientProperties {
  private String workerId = InternalTaskClientConfiguration.DEFAULT_WORKER_ID;
  private int maxTasks = InternalTaskClientConfiguration.DEFAULT_MAX_TASKS;
  private Duration lockDuration = InternalTaskClientConfiguration.DEFAULT_LOCK_DURATION;

  public String getWorkerId() {
    return workerId;
  }

  public void setWorkerId(String workerId) {
    this.workerId = workerId;
  }

  public int getMaxTasks() {
    return maxTasks;
  }

  public void setMaxTasks(int maxTasks) {
    this.maxTasks = maxTasks;
  }

  public Duration getLockDuration() {
    return lockDuration;
  }

  public void setLockDuration(Duration lockDuration) {
    this.lockDuration = lockDuration;
  }
}
