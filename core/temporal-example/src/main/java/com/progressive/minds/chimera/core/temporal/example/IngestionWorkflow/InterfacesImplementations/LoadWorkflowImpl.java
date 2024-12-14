package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.InterfacesImplementations;

import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.LoadWorkflow;
import io.temporal.workflow.Workflow;

public class LoadWorkflowImpl implements LoadWorkflow {
    @Override
    public void executeLoad() {
        Workflow.getLogger(LoadWorkflowImpl.class).info("Executing LoadWorkflowImpl workflow.");
        // Simulate extraction logic here
        // e.g., Extract data from a database or file
        Workflow.getLogger(LoadWorkflowImpl.class).info("LoadWorkflowImpl completed.");
    }


}
