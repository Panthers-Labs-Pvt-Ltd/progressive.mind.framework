package com.progressive.minds.chimera.core.temporal.controller.implementations;

import io.temporal.workflow.Workflow;
import com.progressive.minds.chimera.core.temporal.controller.IngestionWorkflow;
import com.progressive.minds.chimera.core.temporal.workflows.ExtractWorkflow;
import com.progressive.minds.chimera.core.temporal.workflows.LoadWorkflow;
import com.progressive.minds.chimera.core.temporal.workflows.TransformWorkflow;

public class IngestionWorkflowImpl implements IngestionWorkflow {
    @Override
    public void startIngestion() {
        // Create child workflow stubs
        ExtractWorkflow extractWorkflow = Workflow.newChildWorkflowStub(ExtractWorkflow.class);
        TransformWorkflow transformWorkflow = Workflow.newChildWorkflowStub(TransformWorkflow.class);
        LoadWorkflow loadWorkflow = Workflow.newChildWorkflowStub(LoadWorkflow.class);

        // Sequentially execute child workflows
        extractWorkflow.execute();
        transformWorkflow.execute();
        loadWorkflow.execute();
    }
}
