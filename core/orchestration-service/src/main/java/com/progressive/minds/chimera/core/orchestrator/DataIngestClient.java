package com.progressive.minds.chimera.core.orchestrator;


import com.progressive.minds.chimera.core.orchestrator.workflow.BaseWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class DataIngestClient {

  public static void main(String[] args) {

    WorkflowServiceStubs serviceStub = WorkflowServiceStubs.newLocalServiceStubs();

    WorkflowClient client = WorkflowClient.newInstance(serviceStub);

    WorkflowOptions options = WorkflowOptions.newBuilder()
        .setTaskQueue("IngestionTaskQueue")
        .setWorkflowId("ingest-workflow-id")
        .build();

    BaseWorkflow workflow = client.newWorkflowStub(BaseWorkflow.class, options);

    WorkflowClient.start(workflow::execute, "kafka-topic");
  }
}
