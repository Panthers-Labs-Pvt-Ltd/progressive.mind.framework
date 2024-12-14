package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.InterfacesImplementations;

import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.CleanupWorkflow;
import io.temporal.workflow.Workflow;

public class CleanupWorkflowImpl implements CleanupWorkflow {
    @Override
    public void executeCleanup() {
        Workflow.getLogger(CleanupWorkflowImpl.class).info("Executing CleanupWorkflowImpl workflow.");
        // Simulate extraction logic here
        // e.g., Extract data from a database or file
        Workflow.getLogger(CleanupWorkflowImpl.class).info("CleanupWorkflowImpl completed.");
    }


}
