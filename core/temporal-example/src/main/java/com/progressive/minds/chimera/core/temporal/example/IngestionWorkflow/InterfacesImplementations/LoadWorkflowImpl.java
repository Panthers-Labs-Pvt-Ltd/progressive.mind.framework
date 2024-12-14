package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.InterfacesImplementations;

import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.LoadWorkflow;
import io.temporal.workflow.Workflow;

public class LoadWorkflowImpl implements LoadWorkflow {

    /**
     *
     */
    @Override
    public void getInputDatasets() {
        Workflow.getLogger(LoadWorkflowImpl.class).info("Executing LoadWorkflowImpl workflow.");

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
}
