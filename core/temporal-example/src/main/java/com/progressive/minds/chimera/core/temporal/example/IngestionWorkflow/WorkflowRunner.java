package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow;

import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.IngestionWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class WorkflowRunner {
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);

        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("IngestionWorkflowQueue")
                .build();

        IngestionWorkflow ingestionWorkflow = client.newWorkflowStub(IngestionWorkflow.class, options);

        // Start the ingestion workflow
        ingestionWorkflow.executeIngestionWorkflow();
        System.out.println("Ingestion workflow started.");
    }
}
