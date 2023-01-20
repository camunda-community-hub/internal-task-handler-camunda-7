package org.camunda.community.extension.internalTaskHandler.impl;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;

public class InternalTaskClientPropertiesFactoryTest {
  private static final String[] TEST_PROPERTY =
      new String[] {"my", "property", "with", "100", "parts"};

  @Test
  void shouldCreateProperLowerCamelCase() {
    String lowerCamelCase = InternalTaskClientPropertiesFactory.createLowerCamelCase(TEST_PROPERTY);
    assertThat(lowerCamelCase).isEqualTo("myPropertyWith100Parts");
  }

  @Test
  void shouldCreateProperUpperCamelCase() {
    String upperCamelCase = InternalTaskClientPropertiesFactory.createUpperCamelCase(TEST_PROPERTY);
    assertThat(upperCamelCase).isEqualTo("MyPropertyWith100Parts");
  }

  @Test
  void shouldCreateProperLowerKebapCase() {
    String lowerKebapCase = InternalTaskClientPropertiesFactory.createLowerKebapCase(TEST_PROPERTY);
    assertThat(lowerKebapCase).isEqualTo("my-property-with-100-parts");
  }

  @Test
  void shouldCreateUpperKebapCase() {
    String upperKebapCase = InternalTaskClientPropertiesFactory.createUpperKebapCase(TEST_PROPERTY);
    assertThat(upperKebapCase).isEqualTo("My-Property-With-100-Parts");
  }

  @Test
  void shouldCreateSnakeCase() {
    String snakeCase = InternalTaskClientPropertiesFactory.createSnakeCase(TEST_PROPERTY);
    assertThat(snakeCase).isEqualTo("MY_PROPERTY_WITH_100_PARTS");
  }

  @Test
  void shouldCreatePossiblePropertyNames() {
    List<String> possiblePropertyNames =
        InternalTaskClientPropertiesFactory.createPossiblePropertyNames(
            "my-property-with-100-parts");
    assertThat(possiblePropertyNames)
        .hasSize(5)
        .containsExactlyInAnyOrder(
            "MY_PROPERTY_WITH_100_PARTS",
            "My-Property-With-100-Parts",
            "my-property-with-100-parts",
            "MyPropertyWith100Parts",
            "myPropertyWith100Parts");
  }

  @Test
  void shouldCreateProperties() throws IOException {
    InternalTaskClientProperties properties;
    try (InputStream in = getClass().getClassLoader().getResourceAsStream("client.properties")) {
      properties = InternalTaskClientPropertiesFactory.fromPropertiesFile(in);
    }
    assertThat(properties).isNotNull();
    assertThat(properties.getWorkerId()).isEqualTo("MyWorkerId");
    assertThat(properties.getMaxTasks()).isEqualTo(42);
    assertThat(properties.getLockDuration()).isEqualByComparingTo(Duration.ofSeconds(10));
  }
}
