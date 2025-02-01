package com.progressive.minds.chimera.core.temporal.activities;


import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface PipelineSubActivities {
    void ChildInit();
    void ChildExecute();
    void ChildComplete();
}