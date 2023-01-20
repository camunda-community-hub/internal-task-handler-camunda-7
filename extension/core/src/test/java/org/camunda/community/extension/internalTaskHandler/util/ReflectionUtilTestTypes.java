package org.camunda.community.extension.internalTaskHandler.util;

public class ReflectionUtilTestTypes {
  public static class SimplePojo {
    public String simpleField;
  }

  public static class NoDefaultConstructor {
    public NoDefaultConstructor(String parameter) {}
  }

  public static class HiddenFields {
    private String hiddenField;

    public String getHiddenField() {
      return hiddenField;
    }

    public void setHiddenField(String hiddenField) {
      this.hiddenField = hiddenField;
    }
  }

  public static class NoSetterField {
    private String inaccessible;

    public String getInaccessible() {
      return inaccessible;
    }
  }

  public static class NoGetterField {
    private String inaccessible;

    public void setInaccessible(String inaccessible) {
      this.inaccessible = inaccessible;
    }
  }

  public static class ExtendedSimplePojo extends SimplePojo {}

  public static class ExtendedHiddenFields extends HiddenFields {}
}
