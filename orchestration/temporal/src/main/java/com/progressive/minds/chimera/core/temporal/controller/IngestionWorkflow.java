package com.progressive.minds.chimera.core.temporal.controller;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface IngestionWorkflow {
    @WorkflowMethod
    void startIngestion();
}
