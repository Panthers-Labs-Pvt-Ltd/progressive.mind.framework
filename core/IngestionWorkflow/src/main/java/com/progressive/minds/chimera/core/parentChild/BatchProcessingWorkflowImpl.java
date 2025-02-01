package com.progressive.minds.chimera.core.parentChild;

import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;

import java.util.List;

public class BatchProcessingWorkflowImpl implements BatchProcessingWorkflow {
    @Override
    public void executeBatchProcessing(List<String> sources, List<String> targets) {
        ChildWorkflowOptions childOptions = ChildWorkflowOptions.newBuilder()
                .build();

        DataProcessingWorkflow childWorkflow = Workflow.newChildWorkflowStub(DataProcessingWorkflow.class, childOptions);
        childWorkflow.processData(sources, targets);
    }
}
