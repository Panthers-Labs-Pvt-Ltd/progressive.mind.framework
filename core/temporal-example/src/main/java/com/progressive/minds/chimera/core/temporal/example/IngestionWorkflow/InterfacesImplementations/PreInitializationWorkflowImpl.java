package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.InterfacesImplementations;

import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.ExtractWorkflow;
import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.PreInitializationWorkflow;
import io.temporal.workflow.Workflow;

public class PreInitializationWorkflowImpl implements PreInitializationWorkflow {

    /**
     *
     */
    @Override
    public void getInputDatasets() {
        Workflow.getLogger(PreInitializationWorkflowImpl.class).info("Executing executePreInitialization workflow.");
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
