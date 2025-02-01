package com.progressive.minds.chimera.core.parentChild;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import io.temporal.activity.ActivityOptions;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.workflow.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WorkflowInterface
public interface DataProcessingWorkflow {
    @WorkflowMethod
    void processData(List<String> sources, List<String> targets);
}

