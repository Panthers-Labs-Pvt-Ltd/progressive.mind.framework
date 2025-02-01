package com.progressive.minds.chimera.core.databaseOps.apacheComet;

import org.apache.spark.sql.SparkSession;

public interface sparkSession {
    SparkSession spark = SparkSession.builder()
            .appName("Shared Spark Session for Data Read")
            .master("local[*]")
            .config("spark.driver.extraJavaOptions", "add-opens=java.base/sun.nio.ch=ALL-UNNAMED")
            .config("spark.executor.extraJavaOptions", "add-opens=java.base/sun.nio.ch=ALL-UNNAMED")
            .config ("jdk.module.addopens", "java.base/sun.nio.ch=ALL-UNNAMED")
            .getOrCreate();
}
