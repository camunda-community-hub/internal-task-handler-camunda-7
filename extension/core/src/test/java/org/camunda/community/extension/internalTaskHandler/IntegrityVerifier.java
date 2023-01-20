package org.camunda.community.extension.internalTaskHandler;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.camunda.community.extension.internalTaskHandler.builder.Builder;

public class IntegrityVerifier {

  public static void generateBuilder(Class<?> interfaceClass) {
    Map<String, String> interfaceClassFields = collectFields(interfaceClass);
    String builderClassName = interfaceClass.getSimpleName() + "Builder";
    Set<String> imports = getImports(interfaceClass);
    // package
    System.out.println("package " + interfaceClass.getPackage().getName() + ".builder;");
    System.out.println();
    // imports
    imports.forEach(importName -> System.out.println("import " + importName + ";"));
    System.out.println();
    // interface declaration
    System.out.println("public interface " + builderClassName + " {");
    // builder methods
    interfaceClassFields.forEach(
        (fieldName, fieldType) -> {
          System.out.println(
              "  "
                  + builderClassName
                  + " with"
                  + fieldName
                  + "("
                  + fieldType
                  + " "
                  + fieldName.substring(0, 1).toLowerCase()
                  + fieldName.substring(1)
                  + ");");
          System.out.println();
        });
    System.out.println("}");
  }

  public static <T> void verifyIntegrity(
      Class<T> interfaceClass, Class<? extends Builder<T>> builderClass) {
    // collect all fields on interfaceClass + builderClass
    Map<String, String> interfaceClassFields = collectFields(interfaceClass);
    Map<String, String> builderClassFields = collectMethods(builderClass);
    Set<String> imports = getImports(interfaceClass);
    System.out.println("---");
    // check if they match
    Assertions.assertThat(interfaceClassFields)
        .containsExactlyInAnyOrderEntriesOf(builderClassFields);
    String className = interfaceClass.getSimpleName() + "Impl";
    System.out.println("package " + interfaceClass.getPackage().getName() + ".impl;");
    System.out.println();
    imports.forEach(importName -> System.out.println("import " + importName + ";"));
    System.out.println();
    System.out.println(
        "public class "
            + className
            + " implements "
            + interfaceClass.getSimpleName()
            + ", "
            + builderClass.getSimpleName()
            + " {");
    interfaceClassFields.forEach(
        (fieldName, fieldType) -> {
          // list field
          System.out.println(
              "  private "
                  + fieldType
                  + " "
                  + fieldName.substring(0, 1).toLowerCase()
                  + fieldName.substring(1)
                  + ";");
          System.out.println();
          // list builder method
          System.out.println("  @Override");
          System.out.println(
              "  public "
                  + builderClass.getSimpleName()
                  + " with"
                  + fieldName
                  + "("
                  + fieldType
                  + " "
                  + fieldName.substring(0, 1).toLowerCase()
                  + fieldName.substring(1)
                  + ") {");
          System.out.println(
              "    this."
                  + fieldName.substring(0, 1).toLowerCase()
                  + fieldName.substring(1)
                  + " = "
                  + fieldName.substring(0, 1).toLowerCase()
                  + fieldName.substring(1)
                  + ";");
          System.out.println("    return this;");
          System.out.println("  }");
          System.out.println();
          // list getter
          System.out.println("  @Override");
          System.out.println("  public " + fieldType + " get" + fieldName + "() {");
          System.out.println(
              "    return "
                  + fieldName.substring(0, 1).toLowerCase()
                  + fieldName.substring(1)
                  + ";");
          System.out.println("  }");
          System.out.println();
          // list setter
          System.out.println("  @Override");
          System.out.println(
              "  public void set"
                  + fieldName
                  + "("
                  + fieldType
                  + " "
                  + fieldName.substring(0, 1).toLowerCase()
                  + fieldName.substring(1)
                  + ") {");
          System.out.println(
              "    this."
                  + fieldName.substring(0, 1).toLowerCase()
                  + fieldName.substring(1)
                  + " = "
                  + fieldName.substring(0, 1).toLowerCase()
                  + fieldName.substring(1)
                  + ";");
          System.out.println("  }");
          System.out.println();
        });
    System.out.println("}");
    System.out.println("---");
  }

  private static Map<String, String> collectFields(Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredMethods())
        .filter(method -> method.getName().startsWith("get"))
        .collect(
            Collectors.toMap(
                method -> method.getName().substring("get".length()),
                method -> buildGenericType(method.getGenericReturnType().getTypeName())));
  }

  private static Map<String, String> collectMethods(Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredMethods())
        .filter(method -> method.getName().startsWith("with"))
        .collect(
            Collectors.toMap(
                method -> method.getName().substring("with".length()),
                method ->
                    buildGenericType(
                        method.getParameters()[0].getParameterizedType().getTypeName())));
  }

  private static Set<String> getImports(Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredMethods())
        .filter(method -> method.getName().startsWith("get"))
        .flatMap(method -> extractTypes(method.getGenericReturnType().getTypeName()))
        .collect(Collectors.toSet());
  }

  private static Stream<String> extractTypes(String parameterizedTypeName) {
    return Arrays.stream(parameterizedTypeName.split("[<>,]"))
        .map(String::trim)
        .filter(s -> s.contains("."))
        .filter(s -> !s.startsWith("java.lang"))
        .sorted()
        .distinct();
  }

  private static String buildGenericType(String parameterizedTypeName) {
    String[] blocks = parameterizedTypeName.split("[<>,]");
    for (int i = 0; i < blocks.length; i++) {
      String block = blocks[i];
      String[] split = block.trim().split("\\.");
      parameterizedTypeName = parameterizedTypeName.replace(block, split[split.length - 1]);
    }
    return parameterizedTypeName;
  }
}
