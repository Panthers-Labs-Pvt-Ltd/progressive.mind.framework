package com.progressive.minds.chimera.core.orchestrator.workflow;

import io.temporal.workflow.Async;
import io.temporal.workflow.Workflow;

public abstract class AbstractParentIngestionWorkflow implements BaseWorkflow {

  @Override
  public void execute(String input) {
    // Notify workflow started
    notifyStart(input);

    String string = callDecisionEngine(input);

    // Start child workflows in parallel for each data source
    String[] dataSources = getDataSources(input);
    for (String dataSource : dataSources) {
      //this is how we can trigger child workflow

        ChildWorkflow childWorkflow = Workflow.newChildWorkflowStub(ChildWorkflow.class);
        Async.procedure(childWorkflow::executeChild, dataSource);
    }

    // Finalize and send metrics
    trackMetrics();
    notifyCompletion();
  }

  protected abstract String callDecisionEngine(String input);

  protected abstract void notifyStart(String input);

  protected abstract String[] getDataSources(String input);

  protected abstract void trackMetrics();

  protected abstract void notifyCompletion();
}
