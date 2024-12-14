package com.progressive.minds.chimera.core.temporal.example;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ChildWorkflow {
    @WorkflowMethod
    String executeChildWorkflow(String childInput);
}