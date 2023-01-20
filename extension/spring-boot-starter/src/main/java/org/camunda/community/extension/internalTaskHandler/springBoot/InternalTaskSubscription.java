package org.camunda.community.extension.internalTaskHandler.springBoot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface InternalTaskSubscription {
  String topicName();

  String lockDuration() default "PT30S";

  String[] variables() default {};

  VariableEquals[] processInstanceVariableEquals() default {};

  String businessKey() default "";

  String[] processDefinitionIds() default {};

  String[] processDefinitionKeys() default {};

  String processDefinitionVersionTag() default "";

  String[] tenantIds() default {};

  boolean withoutTenantId() default false;

  boolean localVariables() default false;

  boolean includeExtensionProperties() default false;

  String executorBeanName() default "";

  public @interface VariableEquals {
    String variableName();

    Class<Supplier<Object>> variableValue();
  }
}
