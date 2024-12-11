package com.progressive.minds.chimera.workflow;

import com.progressive.minds.chimera.core.orchestrator.workflow.BaseWorkflow;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

public class DecisionEngineWorkflowImpl implements BaseWorkflow {

  @Override
  public void execute(String dataSource) {
    Logger logger = Workflow.getLogger(this.getClass().getName());
    logger.info("This is DecisionEngineWorkflowImpl");
  }
}
