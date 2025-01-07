package com.progressive.minds.chimera.core.apacheComet.TemporalSamples;

import org.apache.spark.sql.SparkSession;

public interface SharedSparkSession {

    SparkSession  spark = SparkSession.builder()
                            .appName("Shared Spark Session")
                            .master("local[*]") // Change this as per your cluster setup
                            .getOrCreate();
                }