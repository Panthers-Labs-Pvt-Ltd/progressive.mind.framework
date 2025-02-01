package com.progressive.minds.chimera.core.parentChild;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.workflow.*;
 import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

// ======================== Activity Interfaces ========================
@ActivityInterface
public interface DataSourceActivities {
    @ActivityMethod
    String readData(String sourceType);

    @ActivityMethod
    void writeData(String targetType, String data);
}