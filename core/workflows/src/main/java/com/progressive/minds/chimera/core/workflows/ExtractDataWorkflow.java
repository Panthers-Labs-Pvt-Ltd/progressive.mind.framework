package com.progressive.minds.chimera.core.workflows;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

import com.progressive.minds.chimera.dto.ExtractMetadata;


@WorkflowInterface
public interface ExtractDataWorkflow {

    @WorkflowMethod
    public void extractData(ExtractMetadata config) throws Exception;

}







