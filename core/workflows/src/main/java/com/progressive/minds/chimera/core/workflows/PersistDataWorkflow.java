package com.progressive.minds.chimera.core.workflows;

import com.progressive.minds.chimera.dto.PersistMetadata;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;


@WorkflowInterface
public interface PersistDataWorkflow {

    @WorkflowMethod
    public void persistData(PersistMetadata config) throws Exception;

}







