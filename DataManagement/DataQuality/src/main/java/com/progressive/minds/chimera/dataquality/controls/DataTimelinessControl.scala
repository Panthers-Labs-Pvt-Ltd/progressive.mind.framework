package com.progressive.minds.chimera.dataquality.controls

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, LocalTime}
import java.util
//port java.util.Locale
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.text.SimpleDateFormat
import scala.collection.JavaConversions._


class DataTimelinessControl() extends DataControls {
//  final private val edlLogger = new EDLLogger(this.getClass)
//  val errorStr: StringBuffer = new StringBuffer()
//  private var sourceDf: DataFrame = _
//  private var targetDf: DataFrame = _
//  private var partitionColumn: String = _
//  private var processTypeName: String = _
//  private var batchPipelineDetails: PipelineDetails = _
//  private var sparkSession: SparkSession = _
//  private var databaseName: String = _
//  private var tableName: String = _
//  private var srcSysId: String = _
//  private var checkLevel: String = _
//
//
//  override def apply(sparkSession: SparkSession, sourceDF: DataFrame, targetDf: DataFrame,
//                     processTypeName: String, instanceId: String, partitionColumn: String,
//                     databaseName: String, tableName: String, checkLevel: String, inboundSchema: StructType,
//                     batchPipelineDetails: PipelineDetails): Unit = {
//    this.sourceDf = sourceDF
//    this.targetDf = targetDf
//    this.partitionColumn = partitionColumn
//    this.processTypeName = processTypeName
//    this.batchPipelineDetails = batchPipelineDetails
//    this.sparkSession = sparkSession
//    this.srcSysId = srcSysId
//    this.databaseName = databaseName
//    this.tableName = tableName
//    this.checkLevel = checkLevel
//  }
//
//  override def validate(): Boolean = {
//    val loggerTag = "DataTimelinessControl"
//    edlLogger.logInfo(loggerTag + "Started")
//    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//    val startTime = LocalDateTime.now().format(formatter)
//    val result = timelinessCheck(databaseName, tableName)
//    val endTime = LocalDateTime.now().format(formatter)
//    var resultMessage: String = ""
//    if (result) {
//      resultMessage = s"Data Timeliness Control Completed Successfully"
//    }
//    else {
//      resultMessage = s"Data Timeliness Control has failed. " + errorStr
//    }
//
//    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//
//    val controlResult: EdlDataControlsLog = new EdlDataControlsLog(batchPipelineDetails.getBatchId,
//      processTypeName, "Timeliness", format.parse(startTime), format.parse(endTime),
//      resultMessage,
//      if (result) EdlDataControlsLog.SUCCESS
//      else EdlDataControlsLog.FAILED
//    )
//    super.registerResult(controlResult)
//
//    if (checkLevel == "Error" && !result) {
//      throw new EDLException(errorClass = "EDLDataQualityException.DATA_TIMELINESS_EXCEPTION",
//        messageParameters = Map("exception" -> resultMessage), cause = null)
//    }
//    edlLogger.logInfo(loggerTag + "Completed")
//    true
//  }
//
//  def timelinessCheck(databaseName: String, tableName: String): Boolean = {
//    var result: Boolean = true
//
//    val currentTime = LocalTime.now()
//    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
//
//    val scalaMap = Map("spokeNm" -> batchPipelineDetails.getSpokeName, "databaseNm" -> databaseName,
//      "tableNm" -> tableName, "srcSysNm" -> srcSysId)
//    val sourceSlaTimeliness: util.List[EdlSlaConfig] = EdlSlaConfigRepository
//      .listEdlSlaConfigByColumns(new util.HashMap[String, String](scalaMap))
//    if (sourceSlaTimeliness.size() > 0) {
//      val triggerTimeParsed = LocalTime.parse(sourceSlaTimeliness.get(0).getSlaTime, formatter)
//
//      if (currentTime.isAfter(triggerTimeParsed)) {
//        result = false
//        errorStr.append(s"Delay in Source trigger. Expected Trigger time is " + triggerTimeParsed + " and current " +
//          "time is " + currentTime)
//      }
//    }
//    result
//  }
override def validate(): Boolean = true
}
