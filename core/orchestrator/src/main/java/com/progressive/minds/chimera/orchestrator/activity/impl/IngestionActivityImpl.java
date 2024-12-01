package com.progressive.minds.chimera.orchestrator.activity.impl;

import com.progressive.minds.chimera.orchestrator.activity.IngestionActivity;

public class IngestionActivityImpl implements IngestionActivity {

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
