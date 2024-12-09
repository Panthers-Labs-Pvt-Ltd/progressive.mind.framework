package com.progressive.minds.chimera;

import org.apache.spark.scheduler.JobResult;
import org.apache.spark.scheduler.SparkListenerJobEnd;
import org.apache.spark.scheduler.SparkListenerJobStart;
import org.apache.spark.scheduler.StageInfo;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SparkMetricCollectorTest {

    @Test
    void testJobMetrics() {
        // Initialize Spark session
        SparkSession spark = SparkSession.builder()
                .appName("TestMetricCollector")
                .master("local[*]")
                .getOrCreate();

        SparkMetricCollector collector = new SparkMetricCollector(spark);

        // Create an empty list for StageInfo
        List<StageInfo> stageInfoList = new ArrayList<>();
        Seq<StageInfo> emptyStages = JavaConverters.asScalaBuffer(stageInfoList).toSeq();

        long startTime = System.currentTimeMillis();

        // Correctly create SparkListenerJobStart
        SparkListenerJobStart jobStartEvent = new SparkListenerJobStart(1, startTime, emptyStages, new Properties());
        collector.onJobStart(jobStartEvent);

        // Simulate a successful job result
        // JobResult jobResult = JobResult.JobSucceeded.INSTANCE;

        // Correctly create SparkListenerJobEnd
        long endTime = System.currentTimeMillis();
        // SparkListenerJobEnd jobEndEvent = new SparkListenerJobEnd(1, endTime, "SUCCESS");
        // collector.onJobEnd(jobEndEvent);

        // Validate metrics
        Map<String, Object> metrics = collector.getMetrics();
        assertTrue(metrics.containsKey("job-1-startTime"));
        assertTrue(metrics.containsKey("job-1-endTime"));
        assertTrue(metrics.containsKey("job-1-duration"));

        spark.stop();
    }
}
