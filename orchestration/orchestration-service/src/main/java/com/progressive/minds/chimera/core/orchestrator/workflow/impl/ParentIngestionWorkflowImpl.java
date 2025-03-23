package com.progressive.minds.chimera.core.orchestrator.workflow.impl;

import com.progressive.minds.chimera.core.orchestrator.workflow.AbstractParentIngestionWorkflow;

public class ParentIngestionWorkflowImpl extends AbstractParentIngestionWorkflow {

  @Override
  protected void notifyStart(String input) {
    System.out.println("initializing the connection for : " + input);
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
