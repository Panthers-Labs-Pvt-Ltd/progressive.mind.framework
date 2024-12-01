package com.progressive.minds.chimera.orchestrator.activity;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface IngestionActivity {
    void fetchData(String dataSource);

    void writeToRawLayer(String data);
}
