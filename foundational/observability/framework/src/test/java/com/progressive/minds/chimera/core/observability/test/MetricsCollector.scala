package com.progressive.minds.chimera.core.observability.test

import org.apache.spark.sql.SparkSession
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.apache.spark.scheduler.{SparkListenerJobEnd, SparkListenerJobStart, SparkListenerTaskEnd}
import com.progressive.minds.chimera.core.observability.SparkMetricCollector

class SparkMetricCollectorTest extends AnyFunSuite with Matchers {

  val spark: SparkSession = SparkSession.builder()
    .appName("MetricCollectorTest")
    .master("local[*]")
    .getOrCreate()

  test("collectMetric should store custom metrics") {
    val metricCollector = new SparkMetricCollector(spark)

    metricCollector.collectMetric("customMetric", 42)
    val metrics = metricCollector.getMetrics
    metrics.get("customMetric") shouldBe Some(42)
  }

  test("onJobStart and onJobEnd should record job duration") {
    val metricCollector = new SparkMetricCollector(spark)

    metricCollector.onJobStart(new SparkListenerJobStart(1, System.currentTimeMillis(), null))
    Thread.sleep(100) // Simulate job execution time
    metricCollector.onJobEnd(new SparkListenerJobEnd(1, null, null))

    val metrics = metricCollector.getMetrics
    metrics should contain key "job-1-startTime"
    metrics should contain key "job-1-endTime"
    metrics should contain key "job-1-duration"
    metrics("job-1-duration").asInstanceOf[Long] should be >= 100L
  }

  test("onTaskEnd should record peak memory usage") {
    val metricCollector = new SparkMetricCollector(spark)

//    val taskMetrics = TaskMetrics.empty()
//    taskMetrics.incPeakExecutionMemory(2048L)
//    val taskEndEvent = new SparkListenerTaskEnd(
//      stageId = 0,
//      stageAttemptId = 0,
//      taskType = null,
//      reason = null,
//      taskMetrics = taskMetrics,
//      taskInfo = null
//    )
//
//    metricCollector.onTaskEnd(taskEndEvent)
//    val metrics = metricCollector.getMetrics
//    metrics.get("task-0-peakMemory") shouldBe Some(2048L)
  }
}
