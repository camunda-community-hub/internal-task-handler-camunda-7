package org.camunda.community.extension.internalTaskHandler.impl;

import java.util.List;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.community.extension.internalTaskHandler.BackoffStrategy;

public class DefaultBackoffStrategy implements BackoffStrategy {
  private long backoffTime;

  @Override
  public void reconfigure(List<LockedExternalTask> externalTasks) {
    if (externalTasks == null || externalTasks.isEmpty()) {
      backoffTime = 5000L;
    } else {
      backoffTime = 0L;
    }
  }

  @Override
  public long calculateBackoffTime() {
    return backoffTime;
  }
}
