package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.InterfacesImplementations;


import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.*;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.Promise;

public class IngestionWorkflowImpl implements IngestionWorkflow {
    @Override
    public void executeIngestionWorkflow() {
        Workflow.getLogger(IngestionWorkflowImpl.class).info("Ingestion workflow started.");

        // Define options for child workflows
        ChildWorkflowOptions options = ChildWorkflowOptions.newBuilder()
                .setTaskQueue("IngestionStepsWorkflowQueue") // Task queue for child workflows
                .build();

        // Create child workflow stubs
        PreInitializationWorkflow preInitializationWorkflow = Workflow.newChildWorkflowStub(PreInitializationWorkflow.class, options);
        ExtractWorkflow extractWorkflow = Workflow.newChildWorkflowStub(ExtractWorkflow.class, options);
        TransformWorkflow transformWorkflow = Workflow.newChildWorkflowStub(TransformWorkflow.class, options);
        LoadWorkflow loadWorkflow = Workflow.newChildWorkflowStub(LoadWorkflow.class, options);
        CleanupWorkflow cleanupWorkflow = Workflow.newChildWorkflowStub(CleanupWorkflow.class, options);

        if (System.getProperty("PARALLEL_EXECUTION", "SERIAL").equalsIgnoreCase("SERIAL")) {
            // Sequential execution (this can be changed to parallel)
            preInitializationWorkflow.executePreInitialization();
            extractWorkflow.executeExtract();
            transformWorkflow.executeTransform();
            loadWorkflow.executeLoad();
            cleanupWorkflow.executeCleanup();
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
    }
}
