package com.progressive.minds.chimera.core.orchestrator.activity;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface ChildIngestionActivity {

  void intialize(String dataSource);

  void fetchData(String dataSource);

  void writeToRawLayer(String data);
}
