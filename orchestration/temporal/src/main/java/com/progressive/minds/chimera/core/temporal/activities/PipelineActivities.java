package com.progressive.minds.chimera.core.temporal.activities;


import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface PipelineActivities {
    void init();
    void execute();
    void complete();
}