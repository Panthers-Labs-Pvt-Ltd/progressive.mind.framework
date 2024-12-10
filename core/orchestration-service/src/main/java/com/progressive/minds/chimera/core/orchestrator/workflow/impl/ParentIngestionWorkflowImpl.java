package com.progressive.minds.chimera.core.orchestrator.workflow.impl;

import com.progressive.minds.chimera.core.orchestrator.activity.DecisionServiceActivity;
import com.progressive.minds.chimera.core.orchestrator.workflow.AbstractParentIngestionWorkflow;
import io.temporal.workflow.Workflow;

public class ParentIngestionWorkflowImpl extends AbstractParentIngestionWorkflow {

  private final DecisionServiceActivity decisionServiceActivity = Workflow.newActivityStub(
      DecisionServiceActivity.class);
  @Override
  protected void notifyStart(String input) {
    System.out.println("initializing the connection for : " + input);
  }


  @Override
  protected String callDecisionEngine(String input) {
    return decisionServiceActivity.execute(input);
  }


  @Override
  protected String[] getDataSources(String input) {
    // Extract data sources logic
    return new String[]{"file data source", " relational DB as source", "s3 as data source", input};
  }

  @Override
  protected void trackMetrics() {
    System.out.println("Tracking workflow metrics");
  }

  @Override
  protected void notifyCompletion() {
    System.out.println("Workflow completed successfully");
  }
}
