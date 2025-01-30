package com.progressive.minds.chimera.capabilities.DataQuality.entities

import java.sql.Timestamp
import java.util.Calendar
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

case class DeequRunnerMetrics() {
  var runnerStartTime: Calendar = _
  var getRulesStartTime: Calendar = _
  var getRulesEndTime: Calendar = _
  var executeRulesStartTime: Calendar = _
  var executeRulesEndTime: Calendar = _
  var persistAnalysersStartTime: Calendar = _
  var persistAnalysersEndTime: Calendar = _
  var persistChecksStartTime: Calendar = _
  var persistChecksEndTime: Calendar = _
  var runnerEndTime: Calendar = _
  var processStatus: DeequRunnerProcessStatus.Value = DeequRunnerProcessStatus.Unknown
  var processStatusDescription: String = _
  var deequRules: Long = _
  var deequErrors: Long = _
  var deequWarnings: Long = _
  var deequPasses: Long = _
  var sourceDuplicateRecordCount: Long = _
  var tableCount: Long = _
  var blank_row: Long = _

  protected case class DeequRunnerMetricsDf (runner_start_time: java.sql.Timestamp,
                                             get_rules_start_time: java.sql.Timestamp,
                                             get_rules_end_time: java.sql.Timestamp,
                                             execute_rules_start_time: java.sql.Timestamp,
                                             execute_rules_end_time: java.sql.Timestamp,
                                             persist_analysers_start_time: java.sql.Timestamp,
                                             persist_analysers_end_time: java.sql.Timestamp,
                                             persist_checks_start_time: java.sql.Timestamp,
                                             persist_checks_end_time: java.sql.Timestamp,
                                             runner_end_time: java.sql.Timestamp,
                                             deequ_status: String,
                                             deequ_status_message: String,
                                             deequ_rules: Long,
                                             deequ_errors:Long,
                                             deequ_warnings: Long,
                                             deequ_passes:Long,
                                             source_duplicate_count: Long,
                                             table_count: Long,
                                             blank_row: Long
                                            )

  def toDf(spark: SparkSession): DataFrame ={
    val obj = DeequRunnerMetricsDf(
    checkForNullorReturnTimestamp(runnerStartTime),
    checkForNullorReturnTimestamp(getRulesStartTime),
    checkForNullorReturnTimestamp(getRulesEndTime),
    checkForNullorReturnTimestamp(executeRulesStartTime),
    checkForNullorReturnTimestamp(executeRulesEndTime),
    checkForNullorReturnTimestamp(persistAnalysersStartTime),
    checkForNullorReturnTimestamp(persistAnalysersEndTime),
    checkForNullorReturnTimestamp(persistChecksStartTime),
    checkForNullorReturnTimestamp(persistChecksEndTime),
    checkForNullorReturnTimestamp(runnerEndTime),
    processStatus.toString,
    processStatusDescription,
    deequRules,
    deequErrors,
    deequWarnings,
    deequPasses,
    sourceDuplicateRecordCount,
    tableCount,
    blank_row)
    val rdd:RDD[DeequRunnerMetricsDf] = spark.sparkContext.parallelize(Seq(obj))
    spark.createDataFrame(rdd)

  }

  def checkForNullorReturnTimestamp(value: Calendar): Timestamp ={
    if(!value.eq(null)) {
      new Timestamp(value.getTime.getTime)
    } else null
  }


}
