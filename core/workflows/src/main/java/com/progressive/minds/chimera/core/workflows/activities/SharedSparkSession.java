package com.progressive.minds.chimera.core.workflows.activities;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

public class SharedSparkSession {
    private static SparkSession spark;

    private SharedSparkSession() {
        // Private constructor to prevent instantiation
    }

    public static SparkSession getSparkSession() {
        if (spark == null) {
            synchronized (SharedSparkSession.class) {
                if (spark == null) {
                    spark = SparkSession.builder()
                            .appName("Shared Spark Session")
                            .master(getMasterUrl()) // Dynamically fetch master URL
                            .config("spark.ui.enabled", "false") // Disable UI if not needed
                            .getOrCreate();
                }
            }
        }
        return spark;
    }

    private static String getMasterUrl() {
        String master = System.getenv("SPARK_MASTER_URL"); // Use environment variable
        return (master != null && !master.isEmpty()) ? master : "local[*]";
    }
}
