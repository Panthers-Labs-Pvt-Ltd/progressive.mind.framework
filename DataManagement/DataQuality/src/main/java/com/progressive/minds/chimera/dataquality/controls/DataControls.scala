package com.progressive.minds.chimera.dataquality.controls

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}

trait DataControls {

  def validate(): Boolean

  def apply(sparkSession: SparkSession, sourceDF: DataFrame, targetDf: DataFrame,
            processTypeName: String, instanceId: String, partitionColumn: String,
            databaseName: String, tableName: String, checkLevel: String, inboundSchema: StructType,
            batchPipelineDetails: PipelineDetails): Unit

  def registerResult(controlResults: EdlDataControlsLog): Boolean = {
    EdlDataControlsLogRepository.addNewEdlDataControlsLog(controlResults)
  }
}
