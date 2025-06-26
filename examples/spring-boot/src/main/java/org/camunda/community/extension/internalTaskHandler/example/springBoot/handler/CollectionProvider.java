package org.camunda.community.extension.internalTaskHandler.example.springBoot.handler;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CollectionProvider {
  public List<String> getCollection() {
    //throw new RuntimeException("This did not work :(");
    return List.of("A", "B", "C");
  }
}
