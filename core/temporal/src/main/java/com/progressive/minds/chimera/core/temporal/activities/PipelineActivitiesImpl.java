package com.progressive.minds.chimera.core.temporal.activities;

public class PipelineActivitiesImpl implements PipelineActivities {
    @Override
    public void init() {
        System.out.println("Initializing...");
    }

    @Override
    public Boolean getPipelineMetadata() {
        return true;
    }

    @Override
    public Boolean getExecutionEngine() {
        return true;
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
