package com.progressive.minds.chimera.core.temporal.example.workflow;



public class ChildWorkflowImpl implements ChildWorkflow {

    @Override
    public String executeChildWorkflow(String childInput) {
        // Perform child workflow logic
        return "Processed by Child: " + childInput;
    }
}