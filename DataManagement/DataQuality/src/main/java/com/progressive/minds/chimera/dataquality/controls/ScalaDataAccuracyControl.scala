package com.progressive.minds.chimera.dataquality.controls

import com.progressive.minds.chimera.dataquality.entities.DataControlsLog
import com.progressive.minds.chimera.foundational.exception.ChimeraException
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.StructType

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util
import java.util.Locale


class ScalaDataAccuracyControl() extends DataControls {
//  final private val edlLogger = ChimeraLoggerFactory.getLogger(this.getClass)
//  val errorStr: StringBuffer = new StringBuffer()
//  private var sourceDf: DataFrame = _
//  private var targetDf: DataFrame = _
//  private var partitionColumn: String = _
//  private var processTypeName: String = _
//  private var batchId: BigInt = _
//  private var checkLevel: String = _
//  private var schemaCheck: Boolean = true
//  private val
//
//  override def apply(sparkSession: SparkSession, sourceDF: DataFrame, targetDf: DataFrame,
//                     processTypeName: String, instanceId: String, partitionColumn: String,
//                     databaseName: String, tableName: String, checkLevel: String, inboundSchema: StructType,
//                     batchId: BigInt): Unit = {
//    this.sourceDf = sourceDF
//    this.targetDf = targetDf
//    this.partitionColumn = partitionColumn
//    this.processTypeName = processTypeName
//    this.batchId = batchId
//    this.checkLevel = checkLevel
//  }
//
//  override def validate(): Boolean = {
//    val loggerTag = "validate"
//    edlLogger.logInfo(loggerTag + "Started")
//    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//    val startTime = LocalDateTime.now().format(formatter)
//    val result = compareDf(sourceDf, targetDf)
//    val endTime = LocalDateTime.now().format(formatter)
//    var resultMessage: String = ""
//    if (result) {
//      resultMessage = s"Data Accuracy Control Completed Successfully"
//    }
//    else {
//      resultMessage = s"Data Accuracy Control has failed. " + errorStr
//    }
//    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//
//    val controlResult: DataControlsLog = new DataControlsLog(batchId,
//      processTypeName, "Accuracy", format.parse(startTime), format.parse(endTime),
//      resultMessage,
//      if (result) EdlDataControlsLog.SUCCESS
//      else EdlDataControlsLog.FAILED
//    )
//    super.registerResult(controlResult)
//
//    if (schemaCheck == false) {
//      val map = new util.HashMap[String, String]()
//      map.put("exception" -> "Schema between Source Dataframe and target DataFrame is not matching")
//
//      throw new ChimeraException("EDLDataQualityException.DATA_ACCURACY_EXCEPTION", map, null)
//    }
//    edlLogger.logInfo(loggerTag + "Completed")
//    true
//  }
//
//  def compareDf(sourceDf: DataFrame, targetDf: DataFrame): Boolean = {
//    var result: Boolean = false
//    var loggerTag = "compareDF"
//    val partitions = partitionColumn.toLowerCase(Locale.ROOT).split(",").map(_.trim)
//    val sourceLimitDf = sourceDf.limit(1000)
//    val targetLimitDf = targetDf.limit(1000)
//
//    val sourceLowerColumns = sourceLimitDf.columns.map(_.toLowerCase(Locale.ROOT))
//    val targetLowerColumns = targetLimitDf.columns.map(_.toLowerCase(Locale.ROOT))
//
//    val sourceRenameDf = sourceLimitDf.toDF(sourceLowerColumns: _*)
//    val targetRenameDf = targetLimitDf.toDF(targetLowerColumns: _*)
//
//    val sourceNonPartitionedColumns = sourceRenameDf.columns.filter(col => !partitions.contains(col))
//    val targetNonPartitionedColumns = targetRenameDf.columns.filter(col => !partitions.contains(col))
//
//    val sourceNewDf = sourceRenameDf.select(sourceNonPartitionedColumns.head, sourceNonPartitionedColumns.tail: _*)
//    val targetNewDf = targetRenameDf.select(targetNonPartitionedColumns.head, targetNonPartitionedColumns.tail: _*)
//
//    val sourceSchema = sourceNewDf.schema
//    val targetSchema = targetNewDf.schema
//    val isCompatible = compareSchemas(sourceSchema, targetSchema)
//    edlLogger.logInfo(loggerTag + "Schema Matched :: " + isCompatible)
//
//    if (sourceSchema.equals(targetSchema)) {
//      val diffDF = compareDataFrameInChunks(sourceNewDf, targetNewDf)
//      val compareCount = diffDF.count()
//      if (compareCount == 0) {
//        result = true
//      } else {
//        edlLogger.logInfo(loggerTag + s"DataFrame has mismatched input from source" +
//          s" to Target: " + diffDF.show(false))
//        errorStr.append(s"Data level comparison between 1000 records is not matching. Number of mismatch from source " +
//          s" to target for sample 1000 record is : "
//          + compareCount)
//      }
//    } else {
//      errorStr.append(s"Schema between Source Dataframe and target DataFrame is not matching")
//      edlLogger.logInfo(loggerTag + "Source_Schema " + sourceSchema)
//      edlLogger.logInfo(loggerTag + "Target_Schema " + targetSchema)
//      result = false
//      schemaCheck = false
//    }
//    result
//  }
//
//  def compareDataFrameInChunks(df1: DataFrame, df2: DataFrame): DataFrame = {
//    val loggerTag = "CompareDataFrameInChunks"
//    val chunkSize = 500
//    edlLogger.logInfo(loggerTag + "DataFrames comaprison in chunks for Accuracy Check")
//    val columnChunks = df1.columns.grouped(chunkSize).toList
//    var diffDF: DataFrame = null
//
//    for (chunk <- columnChunks) {
//      val selectedColumns = chunk.map(col)
//      val df1Chunk = df1.select(selectedColumns: _*)
//      val df2Chunk = df2.select(selectedColumns: _*)
//
//      val chunkDiffDf = df1Chunk.except(df2Chunk)
//
//      if (diffDF == null) {
//        diffDF = chunkDiffDf
//      } else {
//        val commonColumns = diffDF.columns.toSet.intersect(chunkDiffDf.columns.toSet).toSeq
//        val commonDiffDF = diffDF.select(commonColumns.map(col): _*)
//        val commonChunkDiffDF = chunkDiffDf.select(commonColumns.map(col): _*)
//        diffDF = commonDiffDF.union(commonChunkDiffDF)
//      }
//    }
//    if (diffDF == null) {
//      df1.sqlContext.emptyDataFrame
//    } else {
//      diffDF
//    }
//  }
//
//  def compareSchemas(sourceSchema: StructType, targetSchema: StructType): Boolean = {
//
//    val targetSchemaMap = targetSchema.map(f => f.name -> f).toMap
//
//    sourceSchema.forall { sourceField =>
//      targetSchemaMap.get(sourceField.name) match {
//        case Some(targetField) =>
//          sourceField.dataType == targetField.dataType && (!sourceField.nullable || targetField.nullable)
//        case None =>
//          false
//      }
//    }
//  }

  override def validate(): Boolean = true

}
