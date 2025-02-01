package com.progressive.minds.chimera.core.temporal.activities;


import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface PipelineActivities {

    @ActivityMethod(name = "Fetch Pipeline Metadata")
    Boolean getPipelineMetadata();

    @ActivityMethod(name = "Fetch Pipeline Execution Engine")
    Boolean getExecutionEngine();

    @ActivityMethod(name = "Initialize Pipeline Activities")
    void init();

    @ActivityMethod(name = "Execute  Pipeline Activities")
    void execute();

    @ActivityMethod(name = "Complete Pipeline Activities")
    void complete();
}