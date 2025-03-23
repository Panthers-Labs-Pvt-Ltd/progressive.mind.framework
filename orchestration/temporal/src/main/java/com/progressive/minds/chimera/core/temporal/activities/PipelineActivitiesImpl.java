package com.progressive.minds.chimera.core.temporal.activities;

public class PipelineActivitiesImpl implements PipelineActivities {
    @Override
    public void init() {
        System.out.println("Initializing...");
    }

    @Override
    public void execute() {
        System.out.println("Executing...");
    }

    @Override
    public void complete() {
        System.out.println("Completing...");
    }
}
