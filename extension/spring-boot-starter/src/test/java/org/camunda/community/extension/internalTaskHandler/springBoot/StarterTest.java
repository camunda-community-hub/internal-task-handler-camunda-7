package org.camunda.community.extension.internalTaskHandler.springBoot;

import static org.junit.jupiter.api.Assertions.*;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.community.extension.internalTaskHandler.InternalTaskClient;
import org.camunda.community.extension.internalTaskHandler.springBoot.app.TestApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestApp.class)
public class StarterTest {
  @Autowired InternalTaskClient internalTaskClient;
  @Autowired ProcessEngine processEngine;

  @Test
  public void shouldRun() {}

  @Test
  public void shouldComplete() throws InterruptedException {
    processEngine
        .getRepositoryService()
        .createDeployment()
        .source("unit-test")
        .addClasspathResource("test.bpmn")
        .deploy();
    ProcessInstance processInstance =
        processEngine.getRuntimeService().startProcessInstanceByKey("TestProcess");
    Thread.sleep(10000L);
    HistoricProcessInstance historicProcessInstance =
        processEngine
            .getHistoryService()
            .createHistoricProcessInstanceQuery()
            .processInstanceId(processInstance.getId())
            .singleResult();
    assertEquals("COMPLETED", historicProcessInstance.getState());
  }
}
