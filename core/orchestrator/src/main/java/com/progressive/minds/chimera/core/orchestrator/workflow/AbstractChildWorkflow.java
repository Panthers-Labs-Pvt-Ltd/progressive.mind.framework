package com.progressive.minds.chimera.core.orchestrator.workflow;

import com.progressive.minds.chimera.core.orchestrator.activity.IngestionActivity;
import io.temporal.workflow.Workflow;

public abstract class AbstractChildWorkflow implements ChildWorkflow {

  private final IngestionActivity ingestionActivity = Workflow.newActivityStub(
      IngestionActivity.class);

  @Override
  public void executeChild(String dataSource) {
    // Fetch data
    ingestionActivity.fetchData(dataSource);

    // Process data
    String data = processData(dataSource);

    // Write to raw layer
    ingestionActivity.writeToRawLayer(data);
  }

  protected abstract String processData(String dataSource);
}
