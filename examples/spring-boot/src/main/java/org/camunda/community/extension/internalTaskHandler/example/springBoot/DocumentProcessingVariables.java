package org.camunda.community.extension.internalTaskHandler.example.springBoot;

public class DocumentProcessingVariables {
  private String documentName;
  private Boolean clear;

  public String getDocumentName() {
    return documentName;
  }

  public void setDocumentName(String documentName) {
    this.documentName = documentName;
  }

  public Boolean getClear() {
    return clear;
  }

  public void setClear(Boolean clear) {
    this.clear = clear;
  }
}
