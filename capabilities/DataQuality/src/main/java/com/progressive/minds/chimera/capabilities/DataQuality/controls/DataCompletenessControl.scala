package com.progressive.minds.chimera.capabilities.DataQuality.controls

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.nwg.edl.tachyon.common.metadata.dto.PipelineDetails
import org.nwg.edl.tachyon.core.dbmgmt.modal.EdlDataControlsLog
import org.nwg.edl.tachyon.core.exception.EDLException
import org.nwg.edl.tachyon.core.logging.EDLLogger

import org.apache.spark.sql.{DataFrame, SparkSession}

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._


class DataCompletenessControl() extends DataControls {
  final private val edlLogger = new EDLLogger(this.getClass)
  val errorStr: StringBuffer = new StringBuffer()
  private var sourceDf: DataFrame = _
  private var targetDf: DataFrame = _
  private var partitionColumn: String = _
  private var processTypeName: String = _
  private var batchPipelineDetails: PipelineDetails = _
  private var sparkSession: SparkSession = _
  private var checkLevel: String = _


  override def apply(sparkSession: SparkSession, sourceDF: DataFrame, targetDf: DataFrame,
                     processTypeName: String, instanceId: String, partitionColumn: String,
                     databaseName: String, tableName: String, checkLevel: String, inboundSchema: StructType,
                     batchPipelineDetails: PipelineDetails): Unit = {
    this.sourceDf = sourceDF
    this.targetDf = targetDf
    this.partitionColumn = partitionColumn
    this.processTypeName = processTypeName
    this.batchPipelineDetails = batchPipelineDetails
    this.sparkSession = sparkSession
    this.checkLevel = checkLevel
  }

  override def validate(): Boolean = {
    val loggerTag = "validate"
    edlLogger.logInfo(loggerTag, "Started")
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val startTime = LocalDateTime.now().format(formatter)
    val result = completenessCheck(sparkSession, sourceDf, targetDf)
    val endTime = LocalDateTime.now().format(formatter)
    var resultMessage: String = ""
    if (result) {
      resultMessage = s"Data Completeness Control Completed Successfully"
    }
    else {
      resultMessage = s"Data Completeness Control has failed. " + errorStr
    }
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    val controlResult: EdlDataControlsLog = new EdlDataControlsLog(batchPipelineDetails.getBatchId,
      processTypeName, "Completeness", format.parse(startTime), format.parse(endTime),
      resultMessage,
      if (result) EdlDataControlsLog.SUCCESS
      else EdlDataControlsLog.FAILED
    )
    super.registerResult(controlResult)

    if (checkLevel == "Error" && !result) {
      throw new EDLException(errorClass = "EDLDataQualityException.DATA_COMPLETENESS_EXCEPTION",
        messageParameters = Map("exception" -> resultMessage), cause = null)
    }
    true
  }

  def aggregateCheck(sourceDf: DataFrame, targetDf: DataFrame): Boolean = {
    var result = true
    val loggerTag = "aggregateCheck"

    val columns = sourceDf.columns.filter(col => sourceDf.schema(col).dataType.isInstanceOf[DoubleType] ||
      sourceDf.schema(col).dataType.isInstanceOf[FloatType] ||
      sourceDf.schema(col).dataType.isInstanceOf[LongType] ||
      sourceDf.schema(col).dataType.isInstanceOf[IntegerType])

    if (columns.isEmpty) {
      return true
    }

    val sourceSum = sourceDf.select(columns.map(col => sum(col) as col): _*)
    val targetSum = targetDf.select(columns.map(col => sum(col) as col): _*)
    val sumDiff = sourceSum.except(targetSum)

    if (sumDiff.limit(1).count() > 0) {
      edlLogger.logInfo(loggerTag, s"Sum values are different in source and target Dataframe" + sumDiff.select("*").show())
      errorStr.append(s"Sum between Source and Target is not matching.")
      result = false
    }

    // Compare Min Values

    val sourceMin = sourceDf.select(columns.map(col => min(col) as col): _*)
    val targetMin = targetDf.select(columns.map(col => min(col) as col): _*)
    val minDiff = sourceMin.except(targetMin)

    if (minDiff.limit(1).count() > 0) {
      edlLogger.logInfo(loggerTag, s"Minimum values are different in source and target Dataframe" + minDiff.show())
      errorStr.append(s"Minimum Values between Source and Target is not matching." + minDiff.show())
      result = false
    }

    // Compare Max Values

    val sourceMax = sourceDf.select(columns.map(col => max(col) as col): _*)
    val targetMax = targetDf.select(columns.map(col => max(col) as col): _*)
    val maxDiff = sourceMax.except(targetMax)

    if (maxDiff.limit(1).count() > 0) {
      edlLogger.logInfo(loggerTag, s"Maximum values are different in source and target Dataframe" + maxDiff.show())
      errorStr.append(s"Maximum Values between Source and Target is not matching." + maxDiff.show())
      result = false
    }

    // Compare Max Values

    val sourceAvg = sourceDf.select(columns.map(col => avg(col) as col): _*)
    val targetAvg = targetDf.select(columns.map(col => avg(col) as col): _*)
    val avgDiff = sourceAvg.except(targetAvg)

    if (avgDiff.limit(1).count() > 0) {
      edlLogger.logInfo(loggerTag, s"Average values are different in source and target Dataframe" + avgDiff.show())
      errorStr.append(s"Average Values between Source and Target is not matching." + avgDiff.show())
      result = false
    }

    result
  }

  def completenessCheck(spark: SparkSession, sourceDf: DataFrame, targetDf: DataFrame): Boolean ={
    var result: Boolean = false
    val loggerTag = "completenessCheck"
    var aggregateResult: Boolean = false
    try {
      var sourceDfCount: Long = 0L
      var targetDfCount: Long = 0L

      var metricsDf = spark.emptyDataFrame

      if (spark.catalog.tableExists("dqMetrics")) {
        metricsDf = spark.sql("select * from dqMetrics")
        if (metricsDf.limit(1).count() > 0) {
          val firstMetric = metricsDf.first()
          if (metricsDf.select("source_count").count() > 0) {
            sourceDfCount = firstMetric.getAs[Long]("source_count")
            edlLogger.logInfo(loggerTag, "source Count from DQMetrics is :: " + sourceDfCount)
          }
          if (metricsDf.select("target_count").count() > 0) {
            targetDfCount = firstMetric.getAs[Long]("target_count")
            edlLogger.logInfo(loggerTag, "target Count from DQMetrics is :: " + sourceDfCount)
          }
        }
      }
      if (sourceDfCount == 0) {
        sourceDfCount = sourceDf.count()
        edlLogger.logInfo(loggerTag, "source count from dataframe is :: " + sourceDfCount)
      }
      if (targetDfCount == 0) {
        targetDfCount = targetDf.count()
        edlLogger.logInfo(loggerTag, "target count from daatframe is :: " + targetDfCount)
      }

      edlLogger.logInfo(loggerTag, "SourceDF count is :: " + sourceDfCount)
      edlLogger.logInfo(loggerTag, "TargetDf count is :: " + targetDfCount)

      if (sourceDfCount == targetDfCount) {
        edlLogger.logInfo(loggerTag, "source and target count matched")
        aggregateResult = aggregateCheck(sourceDf, targetDf)

        if (aggregateResult == true) {
          edlLogger.logInfo(loggerTag, "Source And Target Aggregation Matched")
          result = true
        }
        else {
          edlLogger.logInfo(loggerTag, "Source and Target Aggregation Not Matched")
          errorStr.append(s"Aggregation is not matching between Source and Target")
        }
      }
      else {
        edlLogger.logInfo(loggerTag, "Source and Target Count not matched")
        errorStr.append(s"Count is not matching between Source and Target. Source Count:" + sourceDfCount +
          s"Target Count: " + targetDfCount)
      }
    } catch {
      case e: Exception =>
        edlLogger.logWarning(loggerTag, "Exception During Completeness Check" + e)
    }
    result

      }
    }