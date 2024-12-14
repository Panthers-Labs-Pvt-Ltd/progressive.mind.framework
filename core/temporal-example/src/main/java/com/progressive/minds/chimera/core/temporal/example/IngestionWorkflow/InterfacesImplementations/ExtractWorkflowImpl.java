package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.InterfacesImplementations;

import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.ExtractWorkflow;
import io.temporal.workflow.Workflow;

public class ExtractWorkflowImpl implements ExtractWorkflow {
    @Override
    public void executeExtract() {
        Workflow.getLogger(ExtractWorkflowImpl.class).info("Executing extract workflow.");
        // Simulate extraction logic here
        // e.g., Extract data from a database or file
        Workflow.getLogger(ExtractWorkflowImpl.class).info("Extraction completed.");
    }
}
