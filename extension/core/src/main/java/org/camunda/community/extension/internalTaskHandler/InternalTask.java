package org.camunda.community.extension.internalTaskHandler;

import java.util.Date;
import java.util.Map;

public interface InternalTask {
  /**
   * @return the id of the activity that this external task belongs to
   */
  String getActivityId();

  /**
   * @return the id of the activity instance that the external task belongs to
   */
  String getActivityInstanceId();

  /**
   * @return the error message that was supplied when the last failure of this task was reported
   */
  String getErrorMessage();

  /**
   * @return the error details submitted with the latest reported failure executing this task
   */
  String getErrorDetails();

  /**
   * @return the id of the execution that the external task belongs to
   */
  String getExecutionId();

  /**
   * @return the id of the external task
   */
  String getId();

  /**
   * @return the date that the task's most recent lock expires or has expired
   */
  Date getLockExpirationTime();

  /**
   * @return the id of the process definition the external task is defined in
   */
  String getProcessDefinitionId();

  /**
   * @return the key of the process definition the external task is defined in
   */
  String getProcessDefinitionKey();

  /**
   * @return the version tag of the process definition the tasks activity belongs to
   */
  String getProcessDefinitionVersionTag();

  /**
   * @return the id of the process instance the external task belongs to
   */
  String getProcessInstanceId();

  /**
   * @return the number of retries the task currently has left
   */
  Integer getRetries();

  /**
   * @return the id of the worker that possesses or possessed the most recent lock
   */
  String getWorkerId();

  /**
   * @return the topic name of the external task
   */
  String getTopicName();

  /**
   * @return the id of the tenant the external task belongs to
   */
  String getTenantId();

  /**
   * @return the priority of the external task
   */
  long getPriority();

  /**
   * Returns an untyped variable of the task's ancestor execution hierarchy
   *
   * @param variableName of the variable to be returned
   * @param <T> the type of the variable
   * @return
   *     <ul>
   *       <li>an untyped variable if such a named variable exists
   *       <li>null if such a named variable not exists
   *     </ul>
   */
  <T> T getVariable(String variableName);

  /**
   * Returns variables packaged as type
   *
   * @param type the type class that contains the variables
   * @param <T> the type of the variable container
   * @return an instance of the type class
   */
  <T> T getVariablesAsType(Class<T> type);

  /**
   * Returns the business key of the process instance the external task is associated with
   *
   * @return the business key
   */
  String getBusinessKey();

  /**
   * Returns the value of the extension property for a given key or <code>null</code> if the
   * property was not available.
   *
   * @see InternalTask#getExtensionProperties()
   * @return the extension property, or <code>null</code> if not available
   */
  String getExtensionProperty(String propertyKey);

  /**
   * Returns all available extension properties. Extension properties must be defined at the
   * external task activity inside the BPMN model and explicitly fetched (e.g. by setting {@link
   * InternalTaskClientSubscription#isIncludeExtensionProperties()}) true to be available.
   *
   * <p>If no extension properties are available the returned map will be empty.
   *
   * @return a map of available extension properties, never <code>null</code>
   */
  Map<String, String> getExtensionProperties();
}
