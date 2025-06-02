package com.progressive.minds.chimera.core.executionEngine;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.SparkSession.Builder;

public class SparkSessionBuilder extends Builder {

    @Override
    public synchronized OptimizedSparkSession getOrCreate() {
        SparkSession sparkSession = super.getOrCreate();
        return new OptimizedSparkSession(sparkSession.sparkContext());
    }
}