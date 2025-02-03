package com.progressive.minds.chimera.common.workflows;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

import java.io.IOException;


@WorkflowInterface
public interface FetchPipelineMetadata {

    @WorkflowMethod
    public void getPipelineMetadata(String pipelineName) throws IOException, InterruptedException;

}







