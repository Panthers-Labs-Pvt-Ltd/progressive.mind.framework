package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.InterfacesImplementations;


import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.*;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class IngestionWorkflowImpl implements IngestionWorkflow {

    @Override
    public boolean executeIngestionWorkflow(String inEvent) {
        boolean returnStatus = true;
        Workflow.getLogger(IngestionWorkflowImpl.class).info("Ingestion workflow started.");

        // Define options for child workflows
        ChildWorkflowOptions options = ChildWorkflowOptions.newBuilder()
                .setTaskQueue("IngestionStepsWorkflowQueue") // Task queue for child workflows
                .setWorkflowId("Spark_Ingest_" + System.currentTimeMillis())
                .setWorkflowRunTimeout(Duration.ofMinutes(20))
                .setRetryOptions(RetryOptions.newBuilder().setMaximumInterval(Duration.ofMinutes(5)).setMaximumAttempts(4).build())
                .setWorkflowExecutionTimeout(Duration.ofHours(1))
                .setWorkflowTaskTimeout(Duration.ofMinutes(15))
                .build();
        // Create child workflow stubs
        PreInitializationWorkflow preInitializationWorkflow = Workflow.newChildWorkflowStub(PreInitializationWorkflow.class, options);
        ExtractWorkflow extractWorkflow = Workflow.newChildWorkflowStub(ExtractWorkflow.class, options);
        TransformWorkflow transformWorkflow = Workflow.newChildWorkflowStub(TransformWorkflow.class, options);
        LoadWorkflow loadWorkflow = Workflow.newChildWorkflowStub(LoadWorkflow.class, options);
        CleanupWorkflow cleanupWorkflow = Workflow.newChildWorkflowStub(CleanupWorkflow.class, options);

        if (System.getProperty("PARALLEL_EXECUTION", "SERIAL").equalsIgnoreCase("SERIAL")) {
            // Sequential execution (this can be changed to parallel)
            preInitializationWorkflow.getInputDatasets();
            preInitializationWorkflow.validateInputDatasets();
            preInitializationWorkflow.init();
            preInitializationWorkflow.execute();
            preInitializationWorkflow.controlChecks();
            preInitializationWorkflow.isIdempotent();
            preInitializationWorkflow.monitorPipeline();

            extractWorkflow.getInputDatasets();
            extractWorkflow.validateInputDatasets();
            extractWorkflow.init();
            extractWorkflow.execute();
            extractWorkflow.controlChecks();
            extractWorkflow.isIdempotent();
            extractWorkflow.monitorPipeline();

            transformWorkflow.getInputDatasets();
            transformWorkflow.validateInputDatasets();
            transformWorkflow.init();
            transformWorkflow.execute();
            transformWorkflow.controlChecks();
            transformWorkflow.isIdempotent();
            transformWorkflow.monitorPipeline();

            loadWorkflow.getInputDatasets();
            loadWorkflow.validateInputDatasets();
            loadWorkflow.init();
            loadWorkflow.execute();
            loadWorkflow.controlChecks();
            loadWorkflow.isIdempotent();
            loadWorkflow.monitorPipeline();

            cleanupWorkflow.getInputDatasets();
            cleanupWorkflow.validateInputDatasets();
            cleanupWorkflow.init();
            cleanupWorkflow.execute();
            cleanupWorkflow.controlChecks();
            cleanupWorkflow.isIdempotent();
            cleanupWorkflow.monitorPipeline();


        }
        else
        {
            System.out.print("Run some workflows in parallel");

 /*           // Run some workflows in parallel using Promise

            Promise<Void> preInitPromise = Workflow.newPromise(() -> preInitializationWorkflow.executePreInitialization());
            Promise<Void> extractPromise = Workflow.newPromise(() -> extractWorkflow.executeExtract());
            Promise<Void> transformPromise = Workflow.newPromise(() -> transformWorkflow.executeTransform());
            Promise<Void> loadPromise = Workflow.newPromise(() -> loadWorkflow.executeLoad());
            Promise<Void> cleanupPromise = Workflow.newPromise(() -> cleanupWorkflow.executeCleanup());

            // Wait for parallel workflows to complete
            preInitPromise.get();
            extractPromise.get();
            transformPromise.get();
            loadPromise.get();
            cleanupPromise.get();*/

        }
        Workflow.getLogger(IngestionWorkflowImpl.class).info("Ingestion workflow completed.");
        return returnStatus;
    }
}
