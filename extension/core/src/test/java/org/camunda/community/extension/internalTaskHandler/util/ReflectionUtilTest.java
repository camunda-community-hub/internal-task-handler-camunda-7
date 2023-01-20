package org.camunda.community.extension.internalTaskHandler.util;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Map;
import org.camunda.community.extension.internalTaskHandler.util.ReflectionUtilTestTypes.ExtendedHiddenFields;
import org.camunda.community.extension.internalTaskHandler.util.ReflectionUtilTestTypes.ExtendedSimplePojo;
import org.camunda.community.extension.internalTaskHandler.util.ReflectionUtilTestTypes.HiddenFields;
import org.camunda.community.extension.internalTaskHandler.util.ReflectionUtilTestTypes.NoDefaultConstructor;
import org.camunda.community.extension.internalTaskHandler.util.ReflectionUtilTestTypes.NoGetterField;
import org.camunda.community.extension.internalTaskHandler.util.ReflectionUtilTestTypes.NoSetterField;
import org.camunda.community.extension.internalTaskHandler.util.ReflectionUtilTestTypes.SimplePojo;
import org.junit.jupiter.api.Test;

public class ReflectionUtilTest {
  @Test
  void shouldCreateObjectFromType() {
    SimplePojo instance = ReflectionUtil.createInstance(SimplePojo.class);
    assertThat(instance).isNotNull().isOfAnyClassIn(SimplePojo.class);
  }

  @Test
  void shouldThrowIfNoDefaultConstructor() {
    assertThrows(
        RuntimeException.class, () -> ReflectionUtil.createInstance(NoDefaultConstructor.class));
  }

  @Test
  void shouldIgnoreUnknownFields() {
    SimplePojo instance = new SimplePojo();
    Map<String, Object> source = Collections.singletonMap("simpleField1", "someValue");
    ReflectionUtil.decorate(instance, source);
    assertThat(instance.simpleField).isNull();
  }

  @Test
  void shouldSetFieldDirectly() {
    SimplePojo instance = new SimplePojo();
    Map<String, Object> source = Collections.singletonMap("simpleField", "someValue");
    ReflectionUtil.decorate(instance, source);
    assertThat(instance.simpleField).isEqualTo("someValue");
  }

  @Test
  void shouldSetFieldUsingSetter() {
    HiddenFields instance = new HiddenFields();
    Map<String, Object> source = Collections.singletonMap("hiddenField", "someValue");
    ReflectionUtil.decorate(instance, source);
    assertThat(instance.getHiddenField()).isEqualTo("someValue");
  }

  @Test
  void shouldIgnoreIfNoSetterPresent() {
    NoSetterField instance = new NoSetterField();
    Map<String, Object> source = Collections.singletonMap("inaccessible", "someValue");
    ReflectionUtil.decorate(instance, source);
    assertThat(instance.getInaccessible()).isNull();
  }

  @Test
  void shouldIgnoreIfFieldTypeNotCompatible() {
    SimplePojo instance = new SimplePojo();
    Map<String, Object> source = Collections.singletonMap("simpleField", 10);
    ReflectionUtil.decorate(instance, source);
    assertThat(instance.simpleField).isNull();
  }

  @Test
  void shouldSetFieldOnSuperType() {
    ExtendedSimplePojo instance = new ExtendedSimplePojo();
    Map<String, Object> source = Collections.singletonMap("simpleField", "someValue");
    ReflectionUtil.decorate(instance, source);
    assertThat(instance.simpleField).isEqualTo("someValue");
  }

  @Test
  void shouldUseSetterOnSuperType() {
    ExtendedHiddenFields instance = new ExtendedHiddenFields();
    Map<String, Object> source = Collections.singletonMap("hiddenField", "someValue");
    ReflectionUtil.decorate(instance, source);
    assertThat(instance.getHiddenField()).isEqualTo("someValue");
  }

  @Test
  void shouldGetFieldValueDirectly() {
    SimplePojo instance = new SimplePojo();
    instance.simpleField = "someValue";
    Map<String, Object> instanceMap = ReflectionUtil.createMap(instance);
    assertThat(instanceMap).contains(entry("simpleField", "someValue"));
  }

  @Test
  void shouldGetFieldValueUsingGetter() {
    HiddenFields instance = new HiddenFields();
    instance.setHiddenField("someValue");
    Map<String, Object> instanceMap = ReflectionUtil.createMap(instance);
    assertThat(instanceMap).contains(entry("hiddenField", "someValue"));
  }

  @Test
  void shouldIgnoreIfNoGetterPresent() {
    NoGetterField instance = new NoGetterField();
    instance.setInaccessible("someValue");
    Map<String, Object> instanceMap = ReflectionUtil.createMap(instance);
    assertThat(instanceMap).isEmpty();
  }

  @Test
  void shouldGetFieldOnSuperType() {
    ExtendedSimplePojo instance = new ExtendedSimplePojo();
    instance.simpleField = "someValue";
    Map<String, Object> instanceMap = ReflectionUtil.createMap(instance);
    assertThat(instanceMap).contains(entry("simpleField", "someValue"));
  }

  @Test
  void shouldUseGetterOnSuperType() {
    ExtendedHiddenFields instance = new ExtendedHiddenFields();
    instance.setHiddenField("someValue");
    Map<String, Object> instanceMap = ReflectionUtil.createMap(instance);
    assertThat(instanceMap).contains(entry("hiddenField", "someValue"));
  }
}
