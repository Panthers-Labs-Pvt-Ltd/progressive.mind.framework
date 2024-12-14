package com.progressive.minds.chimera.core.temporal.example.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ParentWorkflow {
    @WorkflowMethod
    void executeParentWorkflow(String parentInput);
}