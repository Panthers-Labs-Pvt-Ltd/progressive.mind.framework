package com.progressive.minds.chimera.core.temporal.example.workflow;

import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;

public class ParentWorkflowImpl implements ParentWorkflow {

    @Override
    public void executeParentWorkflow(String parentInput) {
        // Set up child workflow options (e.g., task queue)
        ChildWorkflowOptions childOptions = ChildWorkflowOptions.newBuilder()
                .setTaskQueue("childTaskQueue")
                .build();

        // Create a child workflow stub
        ChildWorkflow childWorkflow = Workflow.newChildWorkflowStub(ChildWorkflow.class, childOptions);

        // Call child workflow asynchronously and get the result
        String childResult = childWorkflow.executeChildWorkflow(parentInput + " -> Child Input");

        // Use the result in the parent workflow
        Workflow.getLogger(ParentWorkflowImpl.class).info("Child Result: " + childResult);
    }
}


