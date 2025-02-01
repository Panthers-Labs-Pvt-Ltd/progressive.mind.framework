package com.progressive.minds.chimera.core.parentChild;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import java.util.ArrayList;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class DataProcessingWorkflowImpl implements DataProcessingWorkflow {
    private final DataSourceActivities activities = Workflow.newActivityStub(
            DataSourceActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofMinutes(2))
                    .build()
    );

    @Override
    public void processData(List<String> sources, List<String> targets) {
        List<Promise<String>> dataPromises = new ArrayList<>();

        for (String source : sources) {
            dataPromises.add(Async.function(() -> activities.readData(source)));
        }

        // Wait for all read operations to complete
        List<String> dataList = dataPromises.stream()
                .map(Promise::get)
                .collect(Collectors.toList());

        // Write data to all target databases
        for (String data : dataList) {
            for (String target : targets) {
                activities.writeData(target, data);
            }
        }
    }

/*    @Override
    public void processData(List<String> sources, List<String> targets) {
        List<Promise<String>> dataPromises = sources.stream()
                .map(source -> Workflow.async(() -> activities.readData(source)))
                .map(source -> Workflow.async(() -> activities.readData(source)))
                .toList();

        // Wait for all read operations to complete
        List<String> dataList = Promise.allOf(dataPromises).get().stream()
                .map(Promise::get)
                .toList();

        // Write data to all target databases
        for (String data : dataList) {
            for (String target : targets) {
                activities.writeData(target, data);
            }
        }
    }*/
}