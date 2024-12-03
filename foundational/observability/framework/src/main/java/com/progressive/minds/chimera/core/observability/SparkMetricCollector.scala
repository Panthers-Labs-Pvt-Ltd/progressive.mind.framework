package com.progressive.minds.chimera.core.observability

import org.apache.spark.sql.SparkSession
import org.apache.spark.scheduler.{SparkListener, SparkListenerJobEnd, SparkListenerJobStart, SparkListenerTaskEnd}
import scala.collection.mutable

class SparkMetricCollector(spark: SparkSession) extends SparkListener with MetricCollector {

  private val metrics: mutable.Map[String, Any] = mutable.Map.empty

  // Register this collector as a Spark listener
  spark.sparkContext.addSparkListener(this)

  override def onJobStart(jobStart: SparkListenerJobStart): Unit = {
    metrics += s"job-${jobStart.jobId}-startTime" -> System.currentTimeMillis()
  }

  override def onJobEnd(jobEnd: SparkListenerJobEnd): Unit = {
    val jobEndTime = System.currentTimeMillis()
    metrics += s"job-${jobEnd.jobId}-endTime" -> jobEndTime
    val jobStartTime = metrics.getOrElse(s"job-${jobEnd.jobId}-startTime", jobEndTime).asInstanceOf[Long]
    metrics += s"job-${jobEnd.jobId}-duration" -> (jobEndTime - jobStartTime)
  }

  override def onTaskEnd(taskEnd: SparkListenerTaskEnd): Unit = {
    val peakMemory = taskEnd.taskMetrics.peakExecutionMemory
    metrics += s"task-${taskEnd.taskInfo.taskId}-peakMemory" -> peakMemory
  }

  override def collectMetric(name: String, value: Any): Unit = {
    metrics += name -> value
  }

  override def report(): Unit = {
    metrics.foreach { case (key, value) => println(s"$key: $value") }
  }

  // Expose metrics for testing
  def getMetrics: Map[String, Any] = metrics.toMap
}