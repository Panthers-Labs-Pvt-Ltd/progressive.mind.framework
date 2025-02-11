package com.progressive.minds.chimera.dataquality.controls

import com.progressive.minds.chimera.dataquality.entities.{DQRulesEntity, DataControlsLogEntity}
import com.progressive.minds.chimera.dataquality.repository.DQRepository
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.mybatis.dynamic.sql.SqlBuilder
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider
import org.mybatis.dynamic.sql.render.RenderingStrategies

trait ScalaDataControls {

  def validate(): Boolean

// def apply(
  // sparkSession: SparkSession, sourceDF: DataFrame,
  // targetDf: DataFrame,processTypeName : String, instanceId: String,
  // partitionColumn: String, databaseName: String, tableName: String,
  // checkLevel: String, inboundSchema: StructType, batchId: BigInt): Unit

  def registerResult(controlResults: DataControlsLogEntity): Boolean = {
//    val insertStatement = SqlBuilder.insert(controlResults)
//      .into(DataControlsLog)
//      .build
//      .render(RenderingStrategies.MYBATIS3)
// dataControlsLogRepository.(controlResults)
true
  }
}
