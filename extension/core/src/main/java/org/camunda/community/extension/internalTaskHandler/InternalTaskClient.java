package org.camunda.community.extension.internalTaskHandler;

import org.camunda.community.extension.internalTaskHandler.impl.InternalTaskClientImpl;

public interface InternalTaskClient {
  static InternalTaskClient create() {
    return create(InternalTaskClientConfiguration.defaultConfig());
  }

  static InternalTaskClient create(InternalTaskClientConfiguration configuration) {
    return new InternalTaskClientImpl(configuration);
  }

  InternalTaskClient subscribe(InternalTaskClientSubscription subscription);

  InternalTaskClient unsubscribe(InternalTaskClientSubscription subscription);

  /** Stops continuous fetching and locking of tasks */
  void stop();

  /** Starts continuous fetching and locking of tasks */
  void start();

  /**
   * @return
   *     <ul>
   *       <li>{@code true} if the client is actively fetching for tasks
   *       <li>{@code false} if the client is not actively fetching for tasks
   *     </ul>
   */
  boolean isActive();
}
