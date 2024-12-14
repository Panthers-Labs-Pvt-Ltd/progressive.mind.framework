package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.InterfacesImplementations;

import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.CleanupWorkflow;
import io.temporal.workflow.Workflow;

public class CleanupWorkflowImpl implements CleanupWorkflow {
    /**
     *
     */
    @Override
    public void getInputDatasets() {
        Workflow.getLogger(CleanupWorkflowImpl.class).info("Executing CleanupWorkflowImpl workflow.");
    }

    /**
     * @return
     */
    @Override
    public boolean validateInputDatasets() {
        return false;
    }

    /**
     * @return
     */
    @Override
    public boolean init() {
        return false;
    }

    /**
     * @return
     */
    @Override
    public boolean controlChecks() {
        return false;
    }

    /**
     * @return
     */
    @Override
    public boolean execute() {
        return false;
    }

    /**
     * @return
     */
    @Override
    public boolean isIdempotent() {
        return false;
    }

    /**
     *
     */
    @Override
    public void monitorPipeline() {

    }
    //
}
