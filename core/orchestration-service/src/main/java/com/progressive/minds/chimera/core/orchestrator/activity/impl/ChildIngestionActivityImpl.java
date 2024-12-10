package com.progressive.minds.chimera.core.orchestrator.activity.impl;

import com.progressive.minds.chimera.core.orchestrator.activity.ChildIngestionActivity;

public class ChildIngestionActivityImpl implements ChildIngestionActivity {

  @Override
  public void intialize(String dataSource) {
    System.out.println(" initializing all the dependency" + dataSource);
    // Fetch data logic
  }

  @Override
  public void fetchData(String dataSource) {
    System.out.println("Fetching data for: " + dataSource);
    // Fetch data logic
  }

  @Override
  public void writeToRawLayer(String data) {
    System.out.println("Writing data to raw layer: " + data);
    // Write data logic
  }
}
