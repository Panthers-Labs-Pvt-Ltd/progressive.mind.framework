package com.progressive.minds.chimera.core.workflows;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

import java.io.IOException;

import com.progressive.minds.chimera.dto.PipelineMetadata;


@WorkflowInterface
public interface FetchPipelineMetadata {

    @WorkflowMethod
    public PipelineMetadata getPipelineMetadata(String pipelineName) throws IOException, InterruptedException;

}







