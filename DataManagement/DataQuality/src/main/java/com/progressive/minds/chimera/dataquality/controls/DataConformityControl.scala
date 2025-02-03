package com.progressive.minds.chimera.dataquality.controls

import org.apache.spark.sql.types.{StructField, StructType}
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DataConformityControl() extends DataControls {
  final private val edlLogger = new EDLLogger(this.getClass)
  val errorStr: StringBuffer = new StringBuffer()
  private var sourceDf: DataFrame = _
  private var targetDf: DataFrame = _
  private var partitionColumn: String = _
  private var processTypeName: String = _
  private var batchPipelineDetails: PipelineDetails = _
  private var sparkSession: SparkSession = _
  private var checkLevel: String = _
  private  var inboundSchema: StructType = _
  private var schemaCheck:Boolean = true


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
    this.inboundSchema = inboundSchema
  }

  override def validate(): Boolean = {
    val loggerTag = "validate"
    edlLogger.logInfo(loggerTag + "Started")
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val startTime = LocalDateTime.now().format(formatter)
    val result = conformityCheck(sourceDf, inboundSchema)
    val endTime = LocalDateTime.now().format(formatter)
    var resultMessage: String = ""
    if (result) {
      resultMessage = s"Data conformity Control Completed Successfully"
    }
    else {
      resultMessage = s"Data Conformity Control has failed. " + errorStr
    }
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    val controlResult: EdlDataControlsLog = new EdlDataControlsLog(batchPipelineDetails.getBatchId,
      processTypeName, "Conformity", format.parse(startTime), format.parse(endTime),
      resultMessage,
      if (result) EdlDataControlsLog.SUCCESS
      else EdlDataControlsLog.FAILED
    )
    super.registerResult(controlResult)

    if(schemaCheck == false && batchPipelineDetails.getDeployMode != "test" ) {
      resultMessage += " For Schema Check."
      throw new EDLException(errorClass = "EDLDataQualityException.DATA_CONFORMITY_EXCEPTION",
        messageParameters = Map("exception" ->
        resultMessage), cause = null)
    }

    if (checkLevel == "Error" && !result) {
      throw new EDLException(errorClass = "EEDLDataQualityException.DATA_CONFORMITY_EXCEPTION",
        messageParameters = Map("exception" -> resultMessage), cause = null)
    }
    true
  }

  def conformityCheck(sourceDf: DataFrame, targetSchema: StructType):Boolean ={
    var result:Boolean = false
    val loggerTag = "conformityCheck"
    var sourceFields: Array[StructField] = null
    var targetFields: Array[StructField] = null
    edlLogger.logInfo(loggerTag + "SourceSchema")

    sourceDf.printSchema().toString
    edlLogger.logInfo(loggerTag + "Target Schema Struct " + targetSchema.toString())
    if(targetSchema.isEmpty) {
      return true
    }
    if (partitionColumn.isEmpty || partitionColumn == "") {
      sourceFields = sourceDf.schema.fields
      targetFields = targetSchema.fields
    }
    else {
      val partitions = partitionColumn.toLowerCase(Locale.ROOT).split(",").map(_.trim)
      val sourceLowerColumns = sourceDf.columns.map(_.toLowerCase(Locale.ROOT))
      val targetLowerColumns = targetDf.columns.map(_.toLowerCase(Locale.ROOT))

      val sourceRenamedDf = sourceDf.toDF(sourceLowerColumns:_*)
      val targetRenamedDf = targetDf.toDF(targetLowerColumns:_*)

      val sourceNonPartitionedColumns = sourceRenamedDf.columns.filter(col => !partitions.contains(col))
      val targetNonPartitionedColumns = targetRenamedDf.columns.filter(col => !partitions.contains(col))

      val sourceNewDf = sourceRenamedDf.select(sourceNonPartitionedColumns.head, sourceNonPartitionedColumns.tail:_*)
      val targetNewDf = targetRenamedDf.select(targetNonPartitionedColumns.head, targetNonPartitionedColumns.tail:_*)

      sourceFields = sourceNewDf.schema.fields
      targetFields = targetNewDf.schema.fields

    }
    val sourceTypeMap = sourceFields.map(f=>f.name.toLowerCase(Locale.ROOT) -> f.dataType).toMap
    val targetTypeMap = targetFields.map(f=>f.name.toLowerCase(Locale.ROOT) -> f.dataType).toMap

    val sourceColumnDiff = sourceFields.filter(f=>sourceTypeMap.get(f.name.trim) !=
    targetTypeMap.get(f.name.trim)).toList
    val targetColumnDiff = targetFields.filter(f=>sourceTypeMap.get(f.name.trim) !=
      targetTypeMap.get(f.name.trim)).toList

    val sourceSchemaDiff = sourceFields.filter(f=>sourceTypeMap.get(f.dataType.toString.toLowerCase(Locale.ROOT)) !=
      targetTypeMap.get(f.dataType.toString.toLowerCase(Locale.ROOT))).toList
    val targetSchemaDiff = targetFields.filter(f=>sourceTypeMap.get(f.dataType.toString.toLowerCase(Locale.ROOT)) !=
      targetTypeMap.get(f.dataType.toString.toLowerCase(Locale.ROOT))).toList

    if (sourceSchemaDiff.nonEmpty || sourceColumnDiff.nonEmpty) {
      errorStr.append(s"Difference in Schema between Source & Target. Source Schema: " + sourceSchemaDiff +
      "Target Schema : " + targetSchemaDiff)
      result = false
      schemaCheck = false
    }
    else {
      result = true
    }
    result
  }
}

