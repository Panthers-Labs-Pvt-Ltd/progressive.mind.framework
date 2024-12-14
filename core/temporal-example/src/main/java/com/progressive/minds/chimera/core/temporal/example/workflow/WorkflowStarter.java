package com.progressive.minds.chimera.core.temporal.example.workflow;


import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class WorkflowStarter {
    public static void main(String[] args) {
        // Connect to the Temporal service
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();

        // Create a WorkflowClient
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Define WorkflowOptions (task queue)
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("parentTaskQueue") // Task queue for the parent workflow
                .build();

        // Create a stub for the parent workflow
        ParentWorkflow parentWorkflow = client.newWorkflowStub(ParentWorkflow.class, options);

        // Start the parent workflow
        parentWorkflow.executeParentWorkflow("Parent Input");
    }
}
