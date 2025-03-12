package com.progressive.minds.chimera.dataquality.controls

import com.progressive.minds.chimera.dataquality.entities.DataControlsLogEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StructType

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
