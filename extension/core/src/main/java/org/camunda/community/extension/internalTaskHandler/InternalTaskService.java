package org.camunda.community.extension.internalTaskHandler;

public interface InternalTaskService {

  /**
   * Locks a task by a given amount of time.
   *
   * <p>Note: This method should be used to lock external tasks that have been obtained without
   * using the fetch & lock API.
   *
   * @param internalTaskId the id of the external task whose lock will be extended
   * @param lockDuration specifies the lock duration in milliseconds
   */
  void lock(String internalTaskId, long lockDuration);

  /**
   * Locks a task by a given amount of time.
   *
   * <p>Note: This method should be used to lock external tasks that have been obtained without
   * using the fetch & lock API.
   *
   * @param internalTask which lock will be extended
   * @param lockDuration specifies the lock duration in milliseconds
   */
  void lock(InternalTask internalTask, long lockDuration);

  /**
   * Unlocks a task and clears the tasks lock expiration time and worker id.
   *
   * @param internalTask which will be unlocked
   */
  void unlock(InternalTask internalTask);

  /**
   * Completes a task.
   *
   * @param internalTask which will be completed
   */
  void complete(InternalTask internalTask);

  /**
   * Completes a task.
   *
   * @param internalTask which will be completed
   * @param variables are set in the tasks ancestor execution hierarchy. If the object is an
   *     instance of <code>Map&lt;String,Object&gt;</code>, the key and the value represent the
   *     variable name and its value. Map can consist of both typed and untyped variables. In any
   *     other case, the fields of the given object will be treated as result variables.
   */
  void complete(InternalTask internalTask, Object variables);

  /**
   * Completes a task.
   *
   * @param internalTask which will be completed
   * @param variables are set in the tasks ancestor execution hierarchy. If the object is an
   *     instance of <code>Map&lt;String,Object&gt;</code>, the key and the value represent the
   *     variable name and its value. Map can consist of both typed and untyped variables. In any
   *     other case, the fields of the given object will be treated as result variables.
   * @param localVariables are set in the execution of the external task instance. If the object is
   *     an instance of <code>Map&lt;String,Object&gt;</code>, the key and the value represent the
   *     variable name and its value. Map can consist of both typed and untyped variables. In any
   *     other case, the fields of the given object will be treated as result variables.
   */
  void complete(InternalTask internalTask, Object variables, Object localVariables);

  /**
   * Completes a task.
   *
   * @param internalTaskId the id of the external task which will be completed
   * @param variables are set in the tasks ancestor execution hierarchy. If the object is an
   *     instance of <code>Map&lt;String,Object&gt;</code>, the key and the value represent the
   *     variable name and its value. Map can consist of both typed and untyped variables. In any
   *     other case, the fields of the given object will be treated as result variables.
   * @param localVariables are set in the execution of the external task instance. If the object is
   *     an instance of <code>Map&lt;String,Object&gt;</code>, the key and the value represent the
   *     variable name and its value. Map can consist of both typed and untyped variables. In any
   *     other case, the fields of the given object will be treated as result variables.
   */
  void complete(String internalTaskId, Object variables, Object localVariables);

  /**
   * Reports a failure to execute a task. A number of retries and a timeout until the task can be
   * specified. If the retries are set to 0, an incident for this task is created.
   *
   * @param internalTask external task for which a failure will be reported
   * @param errorMessage indicates the reason of the failure.
   * @param errorDetails provides a detailed error description.
   * @param retries specifies how often the task should be retried. Must be &gt;= 0. If 0, an
   *     incident is created and the task cannot be fetched anymore unless the retries are increased
   *     again. The incident's message is set to the errorMessage parameter.
   * @param retryTimeout specifies a timeout in milliseconds before the external task becomes
   *     available again for fetching. Must be &gt;= 0.
   */
  void handleFailure(
      InternalTask internalTask,
      String errorMessage,
      String errorDetails,
      int retries,
      long retryTimeout);

  /**
   * Reports a failure to execute a task. A number of retries and a timeout until the task can be
   * specified. If the retries are set to 0, an incident for this task is created.
   *
   * @param internalTaskId the id of the external task for which a failure will be reported
   * @param errorMessage indicates the reason of the failure.
   * @param errorDetails provides a detailed error description.
   * @param retries specifies how often the task should be retried. Must be &gt;= 0. If 0, an
   *     incident is created and the task cannot be fetched anymore unless the retries are increased
   *     again. The incident's message is set to the errorMessage parameter.
   * @param retryTimeout specifies a timeout in milliseconds before the external task becomes
   *     available again for fetching. Must be &gt;= 0.
   */
  void handleFailure(
      String internalTaskId,
      String errorMessage,
      String errorDetails,
      int retries,
      long retryTimeout);

  /**
   * Reports a failure to execute a task. A number of retries and a timeout until the task can be
   * specified. If the retries are set to 0, an incident for this task is created.
   *
   * @param internalTaskId the id of the external task for which a failure will be reported
   * @param errorMessage indicates the reason of the failure.
   * @param errorDetails provides a detailed error description.
   * @param retries specifies how often the task should be retried. Must be &gt;= 0. If 0, an
   *     incident is created and the task cannot be fetched anymore unless the retries are increased
   *     again. The incident's message is set to the errorMessage parameter.
   * @param retryTimeout specifies a timeout in milliseconds before the external task becomes
   *     available again for fetching. Must be &gt;= 0.
   * @param variables variables to set on the execution the external task is assigned to. If the
   *     object is an instance of <code>Map&lt;String,Object&gt;</code>, the key and the value
   *     represent the variable name and its value. Map can consist of both typed and untyped
   *     variables. In any other case, the fields of the given object will be treated as result
   *     variables.
   * @param localVariables variables to set on the execution locally. If the object is an instance
   *     of <code>Map&lt;String,Object&gt;</code>, the key and the value represent the variable name
   *     and its value. Map can consist of both typed and untyped variables. In any other case, the
   *     fields of the given object will be treated as result variables.
   */
  void handleFailure(
      String internalTaskId,
      String errorMessage,
      String errorDetails,
      int retries,
      long retryTimeout,
      Object variables,
      Object localVariables);

  /**
   * Reports a business error in the context of a running task. The error code must be specified to
   * identify the BPMN error handler.
   *
   * @param internalTask external task for which a BPMN error will be reported
   * @param errorCode that indicates the predefined error. The error code is used to identify the
   *     BPMN error handler.
   */
  void handleBpmnError(InternalTask internalTask, String errorCode);

  /**
   * Reports a business error in the context of a running task. The error code must be specified to
   * identify the BPMN error handler.
   *
   * @param internalTask external task for which a BPMN error will be reported
   * @param errorCode that indicates the predefined error. The error code is used to identify the
   *     BPMN error handler.
   * @param errorMessage which will be passed when the BPMN error is caught
   */
  void handleBpmnError(InternalTask internalTask, String errorCode, String errorMessage);

  /**
   * Reports a business error in the context of a running task. The error code must be specified to
   * identify the BPMN error handler.
   *
   * @param internalTask external task for which a BPMN error will be reported
   * @param errorCode that indicates the predefined error. The error code is used to identify the
   *     BPMN error handler.
   * @param errorMessage which will be passed when the BPMN error is caught
   * @param variables which will be passed to the execution when the BPMN error is caught. If the
   *     object is an instance of <code>Map&lt;String,Object&gt;</code>, the key and the value
   *     represent the variable name and its value. Map can consist of both typed and untyped
   *     variables. In any other case, the fields of the given object will be treated as result
   *     variables.
   */
  void handleBpmnError(
      InternalTask internalTask, String errorCode, String errorMessage, Object variables);

  /**
   * Reports a business error in the context of a running task. The error code must be specified to
   * identify the BPMN error handler.
   *
   * @param internalTaskId the id of the external task for which a BPMN error will be reported
   * @param errorCode that indicates the predefined error. The error code is used to identify the
   *     BPMN error handler.
   * @param errorMessage which will be passed when the BPMN error is caught
   * @param variables which will be passed to the execution when the BPMN error is caught. If the
   *     object is an instance of <code>Map&lt;String,Object&gt;</code>, the key and the value
   *     represent the variable name and its value. Map can consist of both typed and untyped
   *     variables. In any other case, the fields of the given object will be treated as result
   *     variables.
   */
  void handleBpmnError(
      String internalTaskId, String errorCode, String errorMessage, Object variables);

  /**
   * Extends the timeout of the lock by a given amount of time.
   *
   * @param internalTask which lock will be extended
   * @param newDuration specifies the new lock duration in milliseconds
   */
  void extendLock(InternalTask internalTask, long newDuration);

  /**
   * Extends the timeout of the lock by a given amount of time.
   *
   * @param internalTaskId the id of the external task which lock will be extended
   * @param newDuration specifies the the new lock duration in milliseconds
   */
  void extendLock(String internalTaskId, long newDuration);
}
