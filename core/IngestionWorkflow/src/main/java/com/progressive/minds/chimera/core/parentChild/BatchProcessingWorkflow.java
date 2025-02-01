package com.progressive.minds.chimera.core.parentChild;
import io.temporal.workflow.*;
import java.util.List;

@WorkflowInterface
public interface BatchProcessingWorkflow {
    @WorkflowMethod
    void executeBatchProcessing(List<String> sources, List<String> targets);
}

