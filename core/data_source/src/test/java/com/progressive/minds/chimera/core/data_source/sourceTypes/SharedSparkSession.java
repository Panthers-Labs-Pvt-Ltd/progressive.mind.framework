package com.progressive.minds.chimera.core.data_source.sourceTypes;

import org.apache.spark.sql.SparkSession;

import org.apache.spark.sql.SparkSession;

public interface SharedSparkSession {

    SparkSession  spark = SparkSession.builder()
                            .appName("Shared Spark Session")
                            .master("local[*]") // Change this as per your cluster setup
                            .getOrCreate();
                }