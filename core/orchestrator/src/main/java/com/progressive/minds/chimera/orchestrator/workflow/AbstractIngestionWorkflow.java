package com.progressive.minds.chimera.orchestrator.workflow;

import io.temporal.workflow.Async;
import io.temporal.workflow.Workflow;

public abstract class AbstractIngestionWorkflow implements BaseWorkflow {

  @Override
  public void execute(String input) {
    // Notify workflow started
    notifyStart(input);

    // Start child workflows in parallel for each data source
    String[] dataSources = getDataSources(input);
    for (String dataSource : dataSources) {
      ChildWorkflow childWorkflow = Workflow.newChildWorkflowStub(ChildWorkflow.class);
      Async.procedure(childWorkflow::executeChild, dataSource);
    }

    // Finalize and send metrics
    trackMetrics();
    notifyCompletion();
  }

  protected abstract void notifyStart(String input);

  protected abstract String[] getDataSources(String input);

  protected abstract void trackMetrics();

  protected abstract void notifyCompletion();
}
