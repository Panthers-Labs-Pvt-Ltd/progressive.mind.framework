package com.progressive.minds.chimera;

public interface MetricCollector {
    void collectMetric(String name, Object value);
    void report();
}
