package com.progressive.minds.chimera.core.temporal.workflows;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class startDataIngestionWorkflow {

    public static void main(String[] args)
    {

        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Get a stub for the parent workflow
        DataIngestionWorkflow workflow = client.newWorkflowStub(
            DataIngestionWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue("IngestionTaskQueue")
                        .setWorkflowId("Spark Read")
                        .build()
        );

        // Start the workflow execution
        workflow.runPipeline("TestPipeline");
    }
 }

