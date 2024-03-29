package org.camunda.community.extension.internalTaskHandler.springBoot;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration({
  InternalTaskClientSpringBootConfiguration.class,
  InternalTaskClientLifecycle.class,
  InternalTaskClientSubscriptionLifecycle.class,
})
public @interface EnableInternalTaskClient {}
