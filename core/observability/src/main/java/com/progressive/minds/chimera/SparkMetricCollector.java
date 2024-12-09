package com.progressive.minds.chimera;

import org.apache.spark.scheduler.*;
import org.apache.spark.sql.SparkSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SparkMetricCollector extends SparkListener implements MetricCollector {

    private final SparkSession sparkSession;
    private final Map<String, Object> metrics = Collections.synchronizedMap(new HashMap<>());

    public SparkMetricCollector(SparkSession sparkSession) {
        this.sparkSession = sparkSession;
        sparkSession.sparkContext().addSparkListener(this);
    }

    @Override
    public void onJobStart(SparkListenerJobStart jobStart) {
        metrics.put("job-" + jobStart.jobId() + "-startTime", System.currentTimeMillis());
    }

    @Override
    public void onJobEnd(SparkListenerJobEnd jobEnd) {
        long endTime = System.currentTimeMillis();
        metrics.put("job-" + jobEnd.jobId() + "-endTime", endTime);

        Long startTime = (Long) metrics.get("job-" + jobEnd.jobId() + "-startTime");
        if (startTime != null) {
            metrics.put("job-" + jobEnd.jobId() + "-duration", endTime - startTime);
        }
    }

    @Override
    public void onTaskEnd(SparkListenerTaskEnd taskEnd) {
        long peakMemory = taskEnd.taskMetrics().peakExecutionMemory();
        metrics.put("task-" + taskEnd.taskInfo().taskId() + "-peakMemory", peakMemory);
    }

    @Override
    public void collectMetric(String name, Object value) {
        metrics.put(name, value);
    }

    @Override
    public void report() {
        synchronized (metrics) {
            metrics.forEach((key, value) -> System.out.println(key + ": " + value));
        }
    }

    public Map<String, Object> getMetrics() {
        return new HashMap<>(metrics); // Return a copy to ensure immutability for external callers
    }
}
