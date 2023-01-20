package org.camunda.community.extension.internalTaskHandler.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionUtil {
  private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtil.class);

  private ReflectionUtil() {}

  public static <T> T createInstance(Class<T> type) {
    try {
      return type.getDeclaredConstructor().newInstance();
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw new RuntimeException(
          "Error while instantiating type '" + type + "' for variables as type", e);
    }
  }

  public static void decorate(Object object, Map<String, Object> variables) {
    variables
        .keySet()
        .forEach(
            key -> {
              findField(object.getClass(), key, variables.get(key).getClass(), object.getClass())
                  .ifPresent(
                      field -> {
                        setValue(object, variables.get(key), field);
                      });
            });
  }

  public static Map<String, Object> createMap(Object instance) {
    if (instance == null) {
      return null;
    }
    if (instance instanceof Map) {
      // TODO find a way to handle generics
      return (Map<String, Object>) instance;
    }
    return extractFields(instance, instance.getClass());
  }

  private static Map<String, Object> extractFields(Object instance, Class<?> type) {
    Map<String, Object> fields =
        !type.getSuperclass().equals(Object.class)
            ? extractFields(instance, type.getSuperclass())
            : new HashMap<>();
    Arrays.asList(type.getDeclaredFields())
        .forEach(
            field ->
                extractFieldValue(instance, type, field)
                    .ifPresent(
                        value -> {
                          fields.put(field.getName(), value);
                        }));
    return fields;
  }

  private static Optional<Object> extractFieldValue(Object instance, Class<?> type, Field field) {
    try {
      return Optional.of(field.get(instance));
    } catch (IllegalAccessException e) {
      return findGetter(type, field.getName(), type)
          .map(
              method -> {
                try {
                  return method.invoke(instance);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                  LOG.debug(
                      "Error while getting a value for field '{}' on type '{}'",
                      field.getName(),
                      type);
                  return null;
                }
              });
    }
  }

  private static Optional<Field> findField(
      Class<?> clazz, String fieldName, Class<?> fieldType, Class<?> originalClass) {
    try {
      Field field = clazz.getDeclaredField(fieldName);
      if (field.getType().isAssignableFrom(fieldType)) {
        return Optional.of(field);
      } else {
        throw new NoSuchFieldException(
            "Type mismatch: Field is of type '"
                + field.getType()
                + "', but required is type '"
                + fieldType
                + "'");
      }
    } catch (NoSuchFieldException e) {
      if (!clazz.getSuperclass().equals(Object.class)) {
        return findField(clazz.getSuperclass(), fieldName, fieldType, originalClass);
      } else {
        LOG.debug(
            "Did not find field '{}' with type '{}' on type '{}' or its supertypes",
            fieldName,
            fieldType,
            originalClass);
        return Optional.empty();
      }
    }
  }

  private static Optional<Method> findSetter(
      Class<?> clazz, Class<?> fieldType, String fieldName, Class<?> originalClass) {
    try {
      return Optional.of(clazz.getDeclaredMethod(composeSetterName(fieldName), fieldType));
    } catch (NoSuchMethodException e) {
      if (!clazz.getSuperclass().equals(Object.class)) {
        return findSetter(clazz.getSuperclass(), fieldType, fieldName, originalClass);
      } else {
        LOG.debug(
            "Did not find setter for field '{}' called '{}' on type '{}' or its supertypes",
            fieldName,
            composeSetterName(fieldName),
            originalClass);
        return Optional.empty();
      }
    }
  }

  private static Optional<Method> findGetter(
      Class<?> clazz, String fieldName, Class<?> originalClass) {
    try {
      return Optional.of(clazz.getDeclaredMethod(composeGetterName(fieldName)));
    } catch (NoSuchMethodException e) {
      if (!clazz.getSuperclass().equals(Object.class)) {
        return findGetter(clazz.getSuperclass(), fieldName, originalClass);
      } else {
        LOG.debug(
            "Did not find getter for field '{}' called '{}' on type '{}' or its supertypes",
            fieldName,
            composeGetterName(fieldName),
            originalClass);
        return Optional.empty();
      }
    }
  }

  private static String composeSetterName(String fieldName) {
    return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
  }

  private static String composeGetterName(String fieldName) {
    return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
  }

  private static void setValue(Object object, Object value, Field field) {
    try {
      field.set(object, value);
    } catch (Exception e) {
      findSetter(object.getClass(), value.getClass(), field.getName(), object.getClass())
          .ifPresent(
              setter -> {
                try {
                  setter.invoke(object, value);
                } catch (Exception ex) {
                  LOG.debug(
                      "Error while setting a value for field '{}' on type '{}'",
                      field.getName(),
                      object.getClass());
                }
              });
    }
  }
}
