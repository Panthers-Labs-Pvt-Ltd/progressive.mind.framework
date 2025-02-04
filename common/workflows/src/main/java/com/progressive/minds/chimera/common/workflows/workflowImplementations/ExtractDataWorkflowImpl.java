package com.progressive.minds.chimera.common.workflows.workflowImplementations;

import java.time.Duration;

import com.progressive.minds.chimera.common.workflows.ExtractDataWorkflow;
import com.progressive.minds.chimera.common.workflows.activities.ExtractDataActivity;
import com.progressive.minds.chimera.dto.ExtractMetadata;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

public class ExtractDataWorkflowImpl implements ExtractDataWorkflow {
    private final ExtractDataActivity activities = Workflow.newActivityStub(
        ExtractDataActivity.class,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(2))
            .build()
    );

    @Override
    public void extractData(ExtractMetadata config) {
        activities.extractData(config);
    }

}
