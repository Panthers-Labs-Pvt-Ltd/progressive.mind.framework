package com.progressive.minds.chimera.capabilities.DataQuality.controls

import org.nwg.edl.tachyon.common.metadata.dto.PipelineDetails
import org.nwg.edl.tachyon.core.dbmgmt.modal.EdlDataControlsLog
import org.nwg.edl.tachyon.core.dbmgmt.repository.EdlDataControlsLogRepository

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types.StructType

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
