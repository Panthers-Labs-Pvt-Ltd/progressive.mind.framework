package com.progressive.minds.chimera.core.workflows;

import com.progressive.minds.chimera.core.api_service.dto.TransformMetadataConfig;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;


@WorkflowInterface
public interface TransformDataWorkflow {

    @WorkflowMethod
    public void transformData(TransformMetadataConfig config) throws Exception;

}







