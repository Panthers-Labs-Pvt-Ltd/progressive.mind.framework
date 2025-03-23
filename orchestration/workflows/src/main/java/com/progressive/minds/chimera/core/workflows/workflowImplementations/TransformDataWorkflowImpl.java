package com.progressive.minds.chimera.core.workflows.workflowImplementations;

import com.progressive.minds.chimera.core.workflows.TransformDataWorkflow;
import com.progressive.minds.chimera.core.workflows.activities.TransformDataActivity;
import com.progressive.minds.chimera.core.api_service.dto.TransformMetadataConfig;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class TransformDataWorkflowImpl implements TransformDataWorkflow {
    private final TransformDataActivity activities = Workflow.newActivityStub(
            TransformDataActivity.class,
            ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(2))
            .build()
    );

    @Override
    public void transformData(TransformMetadataConfig config) throws Exception {
        activities.transformData(config);
    }

}
