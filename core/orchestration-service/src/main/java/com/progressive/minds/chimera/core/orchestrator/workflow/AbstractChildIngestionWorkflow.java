package com.progressive.minds.chimera.core.orchestrator.workflow;

import com.progressive.minds.chimera.core.orchestrator.activity.ChildIngestionActivity;
import io.temporal.workflow.Workflow;

public abstract class AbstractChildIngestionWorkflow implements ChildWorkflow {

  private final ChildIngestionActivity childIngestionActivity = Workflow.newActivityStub(
      ChildIngestionActivity.class);

  @Override
  public void executeChild(String dataSource) {
    //initialize
    childIngestionActivity.intialize(dataSource);

    // Fetch data
    childIngestionActivity.fetchData(dataSource);

    // Process data
    String data = processData(dataSource);

    // Write to raw layer
    childIngestionActivity.writeToRawLayer(data);
  }

  protected abstract String processData(String dataSource);
}
