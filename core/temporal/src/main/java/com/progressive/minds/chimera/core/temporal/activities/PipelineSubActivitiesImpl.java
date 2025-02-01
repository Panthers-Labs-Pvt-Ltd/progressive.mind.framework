package com.progressive.minds.chimera.core.temporal.activities;

public class PipelineSubActivitiesImpl implements PipelineSubActivities {
    @Override
    public void ChildInit() {
        System.out.println("CHILD Initializing...");
    }

    @Override
    public void ChildExecute() {
        System.out.println("CHILD Executing...");
    }

    @Override
    public void ChildComplete() {
        System.out.println("CHILD  Completing...");
    }
}
