package com.progressive.minds.chimera.core.workflows.workflowImplementations;

import com.progressive.minds.chimera.core.workflows.FetchPipelineMetadata;
import com.progressive.minds.chimera.core.workflows.activities.FetchPipelineMetadataActivity;
import com.progressive.minds.chimera.core.api_service.dto.PipelineMetadata;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.io.IOException;
import java.time.Duration;

public class FetchPipelineMetadataWorkflowImpl implements FetchPipelineMetadata {
    private final FetchPipelineMetadataActivity activities = Workflow.newActivityStub(
        FetchPipelineMetadataActivity.class,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(2))
            .build()
    );

    @Override
    public PipelineMetadata getPipelineMetadata(String pipelineName) throws IOException, InterruptedException {
        return activities.getPipelineMetadata(pipelineName);
    }

}

