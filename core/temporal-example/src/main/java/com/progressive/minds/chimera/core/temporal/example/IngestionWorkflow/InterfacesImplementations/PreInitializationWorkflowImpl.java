package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.InterfacesImplementations;

import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.ExtractWorkflow;
import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.PreInitializationWorkflow;
import io.temporal.workflow.Workflow;

public class PreInitializationWorkflowImpl implements PreInitializationWorkflow {
    @Override
    public void executePreInitialization() {
        Workflow.getLogger(PreInitializationWorkflowImpl.class).info("Executing executePreInitialization workflow.");
        // Simulate extraction logic here
        // e.g., Extract data from a database or file
        Workflow.getLogger(PreInitializationWorkflowImpl.class).info("executePreInitialization completed.");
    }
}
