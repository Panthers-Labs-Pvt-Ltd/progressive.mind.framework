package com.progressive.minds.chimera.capabilities.DataQuality.controls

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.nwg.edl.tachyon.common.metadata.dto.PipelineDetails
import org.nwg.edl.tachyon.core.dbmgmt.modal.EdlDataControlsLog
import org.nwg.edl.tachyon.core.exception.EDLException
import org.nwg.edl.tachyon.core.logging.EDLLogger

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types.StructType

class DataConsistencyControl() extends DataControls {
  final private val edlLogger = new EDLLogger(this.getClass)
  val errorStr: StringBuffer = new StringBuffer()
  private var sourceDf: DataFrame = _
  private var targetDf: DataFrame = _
  private var partitionColumn: String = _
  private var processTypeName: String = _
  private var batchPipelineDetails: PipelineDetails = _
  private var sparkSession: SparkSession = _
  private var databaseName: String = _
  private var tableName: String =_
  private var instanceId: String =_
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
    this.instanceId = instanceId
    this.databaseName = databaseName
    this.tableName = tableName
    this.checkLevel = checkLevel
   }

  override def validate(): Boolean = {
    val loggerTag = "validate"
    edlLogger.logInfo(loggerTag, "Started")
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val startTime = LocalDateTime.now().format(formatter)
    val result = consistencyCheck(sparkSession, sourceDf, databaseName,
      tableName, instanceId, partitionColumn)
    val endTime = LocalDateTime.now().format(formatter)
    var resultMessage: String = ""
    if (result) {
      resultMessage = s"Data consistency Control Completed Successfully"
    }
    else {
      resultMessage = s"Data consistency Control has failed. " + errorStr
    }
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    val controlResult: EdlDataControlsLog = new EdlDataControlsLog(batchPipelineDetails.getBatchId,
      processTypeName, "Consistency", format.parse(startTime), format.parse(endTime),
      resultMessage,
      if (result) EdlDataControlsLog.SUCCESS
      else EdlDataControlsLog.FAILED
    )
    super.registerResult(controlResult)

    if (checkLevel == "Error" && !result) {
      throw new EDLException(errorClass = "EEDLDataQualityException.DATA_CONSISTENCY_EXCEPTION",
        messageParameters = Map("exception" -> resultMessage), cause = null)
    }
    true
  }

  def consistencyCheck(spark: SparkSession, dataFrame: DataFrame, databaseName: String, tableName: String,
                       instanceId: String, partitionColumns: String): Boolean ={
    var result:Boolean = false
    val loggerTag = "consistencyCheck"
    val queryStr: StringBuffer = new StringBuffer()
    val queryStr1: StringBuffer = new StringBuffer()
    if (partitionColumns.isEmpty || partitionColumns =="") {
      queryStr.append(s"select * from $databaseName.$tableName")
    }
    else {
      queryStr.append(s"select * from $databaseName.$tableName where ")
      queryStr1.append(s"select max(")
      val partitionColumnBy: Array[String] = partitionColumns.split(",")
      for (i <- partitionColumnBy.indices) {
        if (partitionColumnBy(i).toLowerCase().contains("business")) {
          queryStr1.append(partitionColumnBy(i))
        }
      }
      queryStr1.append(s") from $databaseName.$tableName where ")

      for (i <- partitionColumnBy.indices) {
        if (i >= 1) {
          queryStr.append("and ")
        }
        if (partitionColumnBy(i).toLowerCase().contains("inst")) {
          queryStr.append(partitionColumnBy(i) + s" = '$instanceId' ")
          queryStr1.append(partitionColumnBy(i) + s" = '$instanceId' ")
        } else if (partitionColumnBy(i).toLowerCase().contains("business")) {
          queryStr.append(partitionColumnBy(i) + s" = (" + queryStr1 + ")")
        }
      }
    }
    if (spark.catalog.tableExists(s"$databaseName.$tableName")) {
      edlLogger.logInfo(loggerTag, s" Table $databaseName.$tableName Exists...")
      val previousDayCount = spark.sql(queryStr.toString).count()

      if (previousDayCount > 0) {
        val curretDayCount = dataFrame.count()
        val variance = (((curretDayCount - previousDayCount) / previousDayCount.toDouble) * 100).round
        val threshold = 10

        if (variance > threshold || variance < -threshold) {
          edlLogger.logInfo(loggerTag, s"The variance between the count of current date and previous date is not within 10% range. " +
            s"Variance is : $variance")
          errorStr.append(s"Variance difference percentage between current and previous day is " + variance)
          result = false
        } else {
          edlLogger.logInfo(loggerTag, s"The variance between the count of current date and previous date is within 10% range. " +
            s"Variance is : $variance")
          result = true
        }
      }
    }
    else {
      result = true
    }
    result
  }
}

