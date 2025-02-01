package com.progressive.minds.chimera.core.temporal.controller.implementations;

import com.progressive.minds.chimera.core.temporal.workflows.*;
import io.temporal.workflow.Workflow;
import com.progressive.minds.chimera.core.temporal.controller.IngestionWorkflow;

public class IngestionWorkflowImpl implements IngestionWorkflow {
    @Override
    public void startIngestion() {
        // Create child workflow stubs
        ExtractWorkflow extractWorkflow = Workflow.newChildWorkflowStub(ExtractWorkflow.class);
        TransformWorkflow transformWorkflow = Workflow.newChildWorkflowStub(TransformWorkflow.class);
        LoadWorkflow loadWorkflow = Workflow.newChildWorkflowStub(LoadWorkflow.class);
        PreProcessing PreWorkflow = Workflow.newChildWorkflowStub(PreProcessing.class);
        PostProcessing PostWorkflow = Workflow.newChildWorkflowStub(PostProcessing.class);
        // Sequentially execute child workflows
        PreWorkflow.execute();
        extractWorkflow.execute();
        transformWorkflow.execute();
        loadWorkflow.execute();
        PostWorkflow.execute();
    }
}
