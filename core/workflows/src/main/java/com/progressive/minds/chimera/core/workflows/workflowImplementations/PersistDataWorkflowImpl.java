package com.progressive.minds.chimera.core.workflows.workflowImplementations;

import com.progressive.minds.chimera.core.workflows.PersistDataWorkflow;
import com.progressive.minds.chimera.core.workflows.activities.PersistDataActivity;
import com.progressive.minds.chimera.dto.PersistMetadata;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class PersistDataWorkflowImpl implements PersistDataWorkflow {
    private final PersistDataActivity activities = Workflow.newActivityStub(
        PersistDataActivity.class,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(2))
            .build()
    );

    @Override
    public void persistData(PersistMetadata config) throws Exception {
        activities.persistData(config);
    }

}
