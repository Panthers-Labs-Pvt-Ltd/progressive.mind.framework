package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface IngestionWorkflow {
    @WorkflowMethod
    void executeIngestionWorkflow();
}