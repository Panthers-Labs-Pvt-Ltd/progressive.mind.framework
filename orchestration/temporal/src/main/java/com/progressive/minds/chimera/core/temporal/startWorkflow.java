package com.progressive.minds.chimera.core.temporal;

import com.progressive.minds.chimera.core.temporal.controller.IngestionWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class startWorkflow {
    public static boolean runWorkflow(String TaskQueue)
    {

        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Get a stub for the parent workflow
        IngestionWorkflow ingestionWorkflow = client.newWorkflowStub(
                IngestionWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue("IngestionTaskQueue")
                        .setWorkflowId("Spark Read")
                        .build()
        );

        // Start the workflow execution
        ingestionWorkflow.startIngestion();
        return true;
    }
 }

