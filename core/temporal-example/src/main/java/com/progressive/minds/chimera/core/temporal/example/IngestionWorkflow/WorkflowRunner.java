package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow;

import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.IngestionWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

import java.time.Duration;

public class WorkflowRunner {
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);

        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("IngestionWorkflowQueue")
                .setWorkflowId("Spark_Ingest_" + System.currentTimeMillis())
                .setWorkflowRunTimeout(Duration.ofMinutes(20))
                .setRetryOptions(RetryOptions.newBuilder().setMaximumInterval(Duration.ofMinutes(5)).setMaximumAttempts(4).build())
                .setWorkflowExecutionTimeout(Duration.ofHours(1))
                .setWorkflowTaskTimeout(Duration.ofMinutes(15))
                .build();


        IngestionWorkflow ingestionWorkflow = client.newWorkflowStub(IngestionWorkflow.class, options);

        // Start the ingestion workflow
        boolean status = ingestionWorkflow.executeIngestionWorkflow("My Input Event");
        System.out.println("Ingestion workflow started." + status);
    }
}
