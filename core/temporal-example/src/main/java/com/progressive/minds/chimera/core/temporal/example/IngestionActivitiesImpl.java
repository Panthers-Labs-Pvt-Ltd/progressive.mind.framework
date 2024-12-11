package com.progressive.minds.chimera.core.temporal.example;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class IngestionActivitiesImpl implements IngestionActivities {

    private final FileReader fileReader;
    private final FileWriter fileWriter;

    public IngestionActivitiesImpl(FileReader fileReader, FileWriter fileWriter) {
        this.fileReader = fileReader;
        this.fileWriter = fileWriter;
    }

    @Override
    public List<String> readFile(String inputPath) {
        return fileReader.read(inputPath);
    }

    @Override
    public List<String> transformData(List<String> data) {
        // Add custom transformation logic here
        return data.stream().map(String::toUpperCase).toList();
    }

    @Override
    public void writeFile(String outputPath, List<String> data) {
        fileWriter.write(outputPath, data);
    }
}
