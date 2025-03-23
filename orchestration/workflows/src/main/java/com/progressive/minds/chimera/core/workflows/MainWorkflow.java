package com.progressive.minds.chimera.core.workflows;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

import java.io.IOException;

@WorkflowInterface
public interface MainWorkflow {
    @WorkflowMethod
    void executeMainWorkflow(String workflowId, String pipelineName) throws IOException, InterruptedException;
}
