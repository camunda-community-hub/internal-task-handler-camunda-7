package org.camunda.community.extension.internalTaskHandler.example.springBoot;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Supplier;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppTest {

  private static void waitForProcessInstanceAtActivity(
      ProcessInstance processInstance, String activityId, Duration limit)
      throws InterruptedException {
    waitFor(
        () ->
            runtimeService()
                .createExecutionQuery()
                .processInstanceId(processInstance.getId())
                .list()
                .stream()
                .flatMap(
                    execution -> runtimeService().getActiveActivityIds(execution.getId()).stream())
                .anyMatch(actId -> actId.equals(activityId)),
        limit);
  }

  private static void waitForProcessInstanceComplete(
      ProcessInstance processInstance, Duration limit) throws InterruptedException {
    waitFor(
        () ->
            Objects.requireNonNull(
                    historyService()
                        .createHistoricProcessInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .singleResult())
                .getState()
                .equals(HistoricProcessInstance.STATE_COMPLETED),
        limit);
  }

  private static void waitFor(Supplier<Boolean> condition, Duration limit)
      throws InterruptedException {
    LocalDateTime startTime = LocalDateTime.now();
    Supplier<Boolean> timeout = () -> LocalDateTime.now().isAfter(startTime.plus(limit));
    while (!condition.get() && !timeout.get()) {
      Thread.sleep(1000L);
    }
  }

  @Test
  void shouldExecuteProcess() throws InterruptedException {
    ProcessInstance processInstance =
        runtimeService()
            .startProcessInstanceByKey(
                "DocumentHandlingProcess", withVariables("documentName", "cleanDocumentName"));
    waitForProcessInstanceComplete(processInstance, Duration.ofSeconds(60));
    assertThat(processInstance).isEnded();
  }

  @Test
  void shouldWaitForReview() throws InterruptedException {
    ProcessInstance processInstance =
        runtimeService()
            .startProcessInstanceByKey(
                "DocumentHandlingProcess", withVariables("documentName", "dirtyDocumentName"));
    waitForProcessInstanceAtActivity(
        processInstance, findId("Perform review and verify data"), Duration.ofSeconds(60));
    complete(task(processInstance));
    waitForProcessInstanceComplete(processInstance, Duration.ofSeconds(60));
    assertThat(processInstance).isEnded();
  }
}
