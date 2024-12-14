package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.InterfacesImplementations;

import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.ExtractWorkflow;
import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.TransformWorkflow;
import io.temporal.workflow.Workflow;

public class TransformWorkflowImpl implements TransformWorkflow {
    @Override
    public void executeTransform() {
        Workflow.getLogger(TransformWorkflowImpl.class).info("Executing Transformation workflow.");
        // Simulate extraction logic here
        // e.g., Extract data from a database or file
        Workflow.getLogger(TransformWorkflowImpl.class).info("Transformation completed.");
    }


}
