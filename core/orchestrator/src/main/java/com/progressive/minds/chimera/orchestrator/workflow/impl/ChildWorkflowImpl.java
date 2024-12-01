package com.progressive.minds.chimera.orchestrator.workflow.impl;

import com.progressive.minds.chimera.orchestrator.workflow.AbstractChildWorkflow;

public class ChildWorkflowImpl extends AbstractChildWorkflow {

  @Override
  protected String processData(String dataSource) {
    System.out.println("Processing data for: " + dataSource);
    return "ProcessedDataFor:" + dataSource;
  }
}
