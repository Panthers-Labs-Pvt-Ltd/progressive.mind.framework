package com.progressive.minds.chimera.dataquality.controls

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DataUniquenessControl() extends DataControls {
//  final private val edlLogger = new EDLLogger(this.getClass)
//  final private val errorStr: StringBuffer = new StringBuffer()
//  private var sourceDf: DataFrame = _
//  private var targetDf: DataFrame = _
//  private var processTypeName: String = _
//  private var batchPipelineDetails: PipelineDetails = _
//  private var sparkSession: SparkSession = _
//  private var checkLevel: String = _
//
//
//
//override def apply(sparkSession: SparkSession, sourceDF: DataFrame,
  // targetDf: DataFrame,processTypeName: String, instanceId: String, partitionColumn: String,
// databaseName: String, tableName: String, checkLevel: String,
// inboundSchema: StructType,batchPipelineDetails: PipelineDetails): Unit = {
//    this.sourceDf = sourceDF
//    this.targetDf = targetDf
//    this.processTypeName = processTypeName
//    this.batchPipelineDetails = batchPipelineDetails
//    this.sparkSession = sparkSession
//    this.checkLevel = checkLevel
//  }
//
//  override def validate(): Boolean = {
//    val loggerTag = "DataUniquenessControl"
//    edlLogger.logInfo(loggerTag + "Started")
//    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//    val startTime = LocalDateTime.now().format(formatter)
//    val result = uniquenessCheck(sparkSession, sourceDf, targetDf)
//    val endTime = LocalDateTime.now().format(formatter)
//    var resultMessage: String = ""
//    if (result) {
//      resultMessage = s"Data Uniqueness Control Completed Successfully"
//    }
//    else {
//      resultMessage = s"Data Uniqueness Control has failed. " + errorStr
//    }
//    val format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//
//    val controlResult: EdlDataControlsLog = new EdlDataControlsLog(batchPipelineDetails.getBatchId,
//      processTypeName, "Uniqueness", format.parse(startTime), format.parse(endTime),
//      resultMessage,
//      if (result) EdlDataControlsLog.SUCCESS
//      else EdlDataControlsLog.FAILED
//    )
//    super.registerResult(controlResult)
//
//    if (checkLevel == "Error" && !result) {
//      throw new EDLException(errorClass = "EEDLDataQualityException.DATA_UNIQUENESS_EXCEPTION",
//        messageParameters = Map("exception" -> resultMessage), cause = null)
//    }
//    true
//  }
//
//  def uniquenessCheck(spark: SparkSession, sourceDf: DataFrame, targetDf: DataFrame): Boolean = {
//    var result: Boolean = false
//    val loggerTag = "uniquenessCheck"
//    edlLogger.logInfo(loggerTag + "Started")
//    val sourceUniqueCount = sourceDf.dropDuplicates().count
//    val targetUniqueCount = targetDf.dropDuplicates().count
//
//    var sourceCount: Long = 0L
//    var targetCount: Long = 0L
//
//    var metricsDf = spark.emptyDataFrame
//
//    if (spark.catalog.tableExists("dqMetrics")) {
//      edlLogger.logInfo(loggerTag + "dqMetrics Exists")
//      metricsDf = spark.sql("select * from dqMetrics")
//      edlLogger.logInfo(loggerTag + "metricsDf created")
//      if (metricsDf.limit(1).count > 0) {
//        val firstMetric = metricsDf.first()
//        edlLogger.logInfo(loggerTag + "first element from Metric fetched")
//        if (metricsDf.select("source_count").count() > 0) {
//          edlLogger.logInfo(loggerTag + "source count exist")
//          sourceCount = firstMetric.getAs[Long]("source_count")
//          edlLogger.logInfo(loggerTag + "source count from dqMetric is : " + sourceCount)
//        }
//        if (metricsDf.select("target_count").count() > 0) {
//          edlLogger.logInfo(loggerTag + "target count exist")
//          targetCount = firstMetric.getAs[Long]("target_count")
//          edlLogger.logInfo(loggerTag + "target count from dqMetric is : " + targetCount)
//        }
//      }
//    }
//    if (sourceCount == 0) {
//      sourceCount = sourceDf.count()
//      edlLogger.logInfo(loggerTag + "source count from dataframe is :: " + sourceCount)
//    }
//    if (targetCount == 0) {
//      targetCount = targetDf.count()
//      edlLogger.logInfo(loggerTag + "target count from dataframe is :: " + targetCount)
//    }
//
//    edlLogger.logInfo(loggerTag + s"SourceCount :: $sourceCount and SourceUniquenessCount :: $sourceUniqueCount")
//    edlLogger.logInfo(loggerTag + s"TargetCount :: $targetCount and TargetUniquenessCount :: $targetUniqueCount")
//
//    if (sourceCount == targetCount) {
//      if ((sourceUniqueCount == sourceCount) && (targetUniqueCount == targetCount)) {
//        edlLogger.logInfo(loggerTag + "DataFrame has unique values and not duplicated.")
//        result = true
//      } else {
//        edlLogger.logInfo(loggerTag + "DataFrame has duplicate Values.")
//        edlLogger.logInfo(loggerTag + s"DataFrame has duplicate Values. Source " +
//          s"Source duplicate: " + (sourceCount - sourceUniqueCount) +
//          s"Target duplicate: " + (targetCount - targetUniqueCount))
//        errorStr.append(s"Number of Duplicate records Source: " + (sourceCount - sourceUniqueCount) +
//          s"Target : " + (targetCount - targetUniqueCount))
//      }
//    }
//      else
//      {
//        errorStr.append(s"Source Count and Target Count is not Matching. Source Count : " + sourceCount +
//          "Target Count : " + targetCount)
//      }
//      result
//    }
  override def validate(): Boolean = true
}
