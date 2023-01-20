package org.camunda.community.extension.internalTaskHandler.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternalTaskClientPropertiesFactory {
  public static final String PROPERTY_WORKER_ID = "worker-id";
  public static final String PROPERTY_MAX_TASKS = "max-tasks";
  public static final String PROPERTY_LOCK_DURATION = "lock-duration";

  private static final Logger LOG =
      LoggerFactory.getLogger(InternalTaskClientPropertiesFactory.class);
  private static final String SEPARATOR = "-";

  public static InternalTaskClientProperties defaultProperties() {
    return new InternalTaskClientProperties();
  }

  public static InternalTaskClientProperties fromPropertiesFile(InputStream inputStream)
      throws IOException {
    Properties properties = new Properties();
    properties.load(inputStream);
    InternalTaskClientProperties response = defaultProperties();
    resolveProperty(properties, PROPERTY_WORKER_ID, response::setWorkerId);
    resolveProperty(properties, PROPERTY_MAX_TASKS, toInt(response::setMaxTasks));
    resolveProperty(properties, PROPERTY_LOCK_DURATION, toDuration(response::setLockDuration));
    return response;
  }

  private static Consumer<String> toInt(Consumer<Integer> setter) {
    return to(Integer::parseInt, setter);
  }

  private static Consumer<String> toDuration(Consumer<Duration> setter) {
    return to(Duration::parse, setter);
  }

  private static <T> Consumer<String> to(Function<String, T> mapper, Consumer<T> setter) {
    return value -> setter.accept(mapper.apply(value));
  }

  private static void resolveProperty(
      Properties properties, String propertyName, Consumer<String> setter) {
    List<String> possiblePropertyNames = createPossiblePropertyNames(propertyName);
    Map<String, String> foundProperties = new HashMap<>();
    for (String property : possiblePropertyNames) {
      String value = properties.getProperty(property);
      if (value != null) {
        foundProperties.put(property, value);
      }
    }
    if (foundProperties.isEmpty()) {
      LOG.info("No properties found for {}", possiblePropertyNames);
    }
    if (foundProperties.size() > 1 && valuesAreNotEqual(foundProperties.values())) {
      throw new IllegalStateException(
          "There were multiple not equal values found for property '"
              + propertyName
              + "': "
              + foundProperties);
    }
    setter.accept(foundProperties.values().stream().findFirst().get());
  }

  private static boolean valuesAreNotEqual(Collection<String> values) {
    String[] v1s = values.toArray(new String[] {});
    String[] v2s = values.toArray(new String[] {});
    for (String v1 : v1s) {
      for (String v2 : v2s) {
        if (!v1.equals(v2)) {
          return true;
        }
      }
    }
    return false;
  }

  static List<String> createPossiblePropertyNames(String propertyName) {
    String[] parts = propertyName.split(SEPARATOR);
    List<String> result = new ArrayList<>();
    result.add(createLowerCamelCase(parts));
    result.add(createUpperCamelCase(parts));
    result.add(createLowerKebapCase(parts));
    result.add(createUpperKebapCase(parts));
    result.add(createSnakeCase(parts));
    return result;
  }

  static String createSnakeCase(String[] parts) {
    return createCase(
        parts,
        (i, part) -> {
          if (i == 0) {
            return part.toUpperCase();
          } else {
            return "_" + part.toUpperCase();
          }
        });
  }

  static String createUpperKebapCase(String[] parts) {
    return createCase(
        parts,
        (i, part) -> {
          if (i == 0) {
            return part.substring(0, 1).toUpperCase() + part.substring(1);
          } else {
            return "-" + part.substring(0, 1).toUpperCase() + part.substring(1);
          }
        });
  }

  static String createLowerKebapCase(String[] parts) {
    return createCase(
        parts,
        (i, part) -> {
          if (i == 0) {
            return part;
          } else {
            return "-" + part;
          }
        });
  }

  static String createUpperCamelCase(String[] parts) {
    return createCase(parts, (i, part) -> part.substring(0, 1).toUpperCase() + part.substring(1));
  }

  static String createLowerCamelCase(String[] parts) {
    return createCase(
        parts,
        (i, part) -> {
          if (i == 0) {
            return part;
          } else {
            return part.substring(0, 1).toUpperCase() + part.substring(1);
          }
        });
  }

  private static String createCase(String[] parts, BiFunction<Integer, String, String> applier) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < parts.length; i++) {
      result.append(applier.apply(i, parts[i]));
    }
    return result.toString();
  }
}
