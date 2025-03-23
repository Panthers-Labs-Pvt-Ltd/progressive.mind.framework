package com.progressive.minds.chimera.core.orchestrator.workflow.impl;

import com.progressive.minds.chimera.core.orchestrator.workflow.AbstractChildIngestionWorkflow;

public class ChildIngestionWorkflowImpl extends AbstractChildIngestionWorkflow {

  @Override
  protected String processData(String dataSource) {
    System.out.println("Processing data for: " + dataSource);
    return "ProcessedDataFor:" + dataSource;
  }
}
