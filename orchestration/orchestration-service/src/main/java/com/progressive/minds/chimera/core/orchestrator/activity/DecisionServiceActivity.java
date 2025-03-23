package com.progressive.minds.chimera.core.orchestrator.activity;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface DecisionServiceActivity {
    String execute(String dataSource);
}
