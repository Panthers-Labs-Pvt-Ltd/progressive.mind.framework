package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface PreInitializationWorkflow {
    // Input Data
    @WorkflowMethod
    void getInputDatasets();

    @WorkflowMethod
    boolean validateInputDatasets();

    // To Add the Batch details
    @WorkflowMethod
    boolean init();

    // Control Checks on Output Data
    @WorkflowMethod
    boolean controlChecks();

    // Execution
    @WorkflowMethod
    boolean execute();

    @WorkflowMethod
    boolean isIdempotent();

    @WorkflowMethod
    void monitorPipeline();
}