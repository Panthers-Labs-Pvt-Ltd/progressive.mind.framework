package com.progressive.minds.chimera.core.temporal.workflows.implementations;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import com.progressive.minds.chimera.core.temporal.activities.PipelineActivities;
import com.progressive.minds.chimera.core.temporal.workflows.ExtractWorkflow;

import java.time.Duration;

public class ExtractWorkflowImpl implements ExtractWorkflow {
    @Override
    public void execute() {
        // PipelineActivities activities = Workflow.newActivityStub(PipelineActivities.class);
        PipelineActivities activities = Workflow.newActivityStub(
                PipelineActivities.class,
                ActivityOptions.newBuilder()
                        .setStartToCloseTimeout(Duration.ofMinutes(2)) // Set a reasonable timeout for execution
                        .setScheduleToCloseTimeout(Duration.ofMinutes(5)) // Optional: Set a maximum time for the activity including retries
                        .build()
        );


        // Execute activities
        activities.init();
        activities.execute();
        activities.complete();
    }
}