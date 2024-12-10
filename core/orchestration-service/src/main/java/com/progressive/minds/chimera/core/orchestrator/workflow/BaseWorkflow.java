package com.progressive.minds.chimera.core.orchestrator.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface BaseWorkflow {

  @WorkflowMethod
  void execute(String input);

}
