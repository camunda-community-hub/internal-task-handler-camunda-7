package org.camunda.community.extension.internalTaskHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.temporal.ChronoUnit;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.junit5.ProcessEngineExtension;
import org.camunda.community.extension.internalTaskHandler.impl.InternalTaskClientProperties;
import org.camunda.community.extension.internalTaskHandler.impl.InternalTaskClientPropertiesFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ProcessEngineExtension.class)
public class InternalTaskClientTest {
  @Test
  @Deployment(resources = "test.bpmn")
  public void shouldRun() throws InterruptedException {
    InternalTaskClientConfiguration configuration = InternalTaskClientConfiguration.defaultConfig();
    InternalTaskClient client = InternalTaskClient.create(configuration);
    client.start();
    InternalTaskClientSubscription subscription =
        InternalTaskClientSubscription.builder()
            .withTaskHandler(
                (internalTask, internalTaskService) -> {
                  internalTaskService.complete(internalTask);
                })
            .withTopicName("example")
            .build();
    client.subscribe(subscription);
    ProcessInstance testProcess = runtimeService().startProcessInstanceByKey("TestProcess");
    Thread.sleep(10000L);
    HistoricProcessInstance historicProcessInstance =
        historyService()
            .createHistoricProcessInstanceQuery()
            .processInstanceId(testProcess.getId())
            .singleResult();
    assertEquals("COMPLETED", historicProcessInstance.getState());
  }

  @Test
  void shouldBootstrapFromProperties() throws IOException {
    InternalTaskClientConfiguration configuration =
        InternalTaskClientConfiguration.usingProperties(
            loadProperties(),
            externalTaskService(),
            runtimeService(),
            InternalTaskClientConfiguration.DEFAULT_BACKOFF_STRATEGY,
            InternalTaskClientConfiguration.DEFAULT_EXECUTOR);
    assertThat(configuration.getWorkerId()).isEqualTo("MyWorkerId");
    assertThat(configuration.getLockDuration().get(ChronoUnit.SECONDS)).isEqualTo(10);
    assertThat(configuration.getMaxTasks()).isEqualTo(42);
  }

  private InternalTaskClientProperties loadProperties() throws IOException {
    try (InputStream in = getClass().getClassLoader().getResourceAsStream("client.properties")) {
      return InternalTaskClientPropertiesFactory.fromPropertiesFile(in);
    }
  }
}
