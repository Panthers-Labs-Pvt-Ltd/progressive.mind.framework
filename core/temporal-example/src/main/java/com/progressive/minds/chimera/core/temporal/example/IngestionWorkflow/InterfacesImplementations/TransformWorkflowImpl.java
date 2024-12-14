package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.InterfacesImplementations;

import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.ExtractWorkflow;
import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.Interfaces.TransformWorkflow;
import io.temporal.workflow.Workflow;

public class TransformWorkflowImpl implements TransformWorkflow {
    /**
     *
     */
    @Override
    public void getInputDatasets() {
        Workflow.getLogger(TransformWorkflowImpl.class).info("Executing TransformWorkflowImpl workflow.");
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
