package com.progressive.minds.chimera.core.temporal.example;


import io.temporal.activity.ActivityMethod;

import java.util.List;

public interface IngestionActivities {
    @ActivityMethod
    List<String> readFile(String inputPath);

    @ActivityMethod
    List<String> transformData(List<String> data);

    @ActivityMethod
    void writeFile(String outputPath, List<String> data);
}
