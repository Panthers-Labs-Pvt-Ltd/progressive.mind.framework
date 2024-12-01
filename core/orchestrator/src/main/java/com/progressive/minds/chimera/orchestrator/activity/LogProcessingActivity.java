package com.progressive.minds.chimera.orchestrator.activity;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface LogProcessingActivity {
    void aggregateLogs();
}
