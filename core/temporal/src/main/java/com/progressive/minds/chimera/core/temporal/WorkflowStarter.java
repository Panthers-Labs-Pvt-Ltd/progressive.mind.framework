package com.progressive.minds.chimera.core.temporal;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import com.progressive.minds.chimera.core.temporal.controller.IngestionWorkflow;

public class WorkflowStarter {
    public static void main(String[] args) {
        // Create service and client
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);
        // Get a stub for the parent workflow
        IngestionWorkflow ingestionWorkflow = client.newWorkflowStub(
                IngestionWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue("IngestionTaskQueue")
                        .build()
        );

        // Start the workflow execution
        ingestionWorkflow.startIngestion();
    }
}
