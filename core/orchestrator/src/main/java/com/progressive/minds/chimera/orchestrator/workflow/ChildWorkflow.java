package com.progressive.minds.chimera.orchestrator.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ChildWorkflow {

    @WorkflowMethod
    void executeChild(String dataSource);
}
