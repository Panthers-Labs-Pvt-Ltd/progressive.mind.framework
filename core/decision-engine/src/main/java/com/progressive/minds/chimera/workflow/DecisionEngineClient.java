package com.progressive.minds.chimera.workflow;

import com.progressive.minds.chimera.core.orchestrator.workflow.BaseWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class DecisionEngineClient implements BaseWorkflow {
  public static void main(String[] args) {
    WorkflowServiceStubs serviceStub = WorkflowServiceStubs.newLocalServiceStubs();

    WorkflowClient client = WorkflowClient.newInstance(serviceStub);

    WorkflowOptions options = WorkflowOptions.newBuilder()
        .setTaskQueue("IngestionTaskQueue")
        .setWorkflowId("Ingest_" + System.currentTimeMillis())
        .build();
    BaseWorkflow workflow = client.newWorkflowStub(BaseWorkflow.class, options);
    String folder="/home/manish/Chimera/core/dataSource/src/test/resources/flight_parquet";
    WorkflowClient.start(workflow::execute, folder);
  }

  @Override
  public void execute(String input) {
    System.out.println("Total Records from base workflow execute " + 100);
  }
}
