package com.progressive.minds.chimera.core.parentChild;

public class DataSourceActivitiesImpl implements DataSourceActivities {
    @Override
    public String readData(String sourceType) {
        // Simulate reading data from the source
        System.out.println("Reading data from " + sourceType);
        return "Data from " + sourceType;
    }

    @Override
    public void writeData(String targetType, String data) {
        // Simulate writing data to the target
        System.out.println("Writing data to " + targetType + ": " + data);
    }
}