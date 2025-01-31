package com.progressive.minds.chimera.dataquality.profiling

import com.amazon.deequ.suggestions.rules.UniqueIfApproximatelyUniqueRule
import com.amazon.deequ.suggestions.{ConstraintSuggestionRunner, Rules}
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory
import org.apache.spark.sql.functions.{col, lit}
import org.apache.spark.sql.{AnalysisException, DataFrame, SparkSession}

import java.util.Locale

object DataProfile {

  val logger = ChimeraLoggerFactory.getLogger(this.getClass)
  val loggerTag = "DataProfile"

  logger.logInfo("Creating Spark Session for Data Profiling")

  val spark = SparkSession.builder()
    .master("local")
    .appName("DeequRunner Test Example")
    .getOrCreate()

  def deequProfiling(df: Option[DataFrame], path: String, databaseNane: String,
                     tableName: String, ediBusinessDay: String): Unit = {
    if (!df.isEmpty) {
      try {
        val suggestionDf = runProfiler(df.getOrElse(null), databaseNane, tableName)
        persistConstraints(suggestionDf)
      }
      catch {
        case e: AnalysisException => e.printStackTrace()
      }
    }
    else if (databaseNane.nonEmpty & tableName.isEmpty) {
      //perform an iterative run on each table in db
    }
    else {
      val sql_str = s"select * from $databaseNane.$tableName where edi_business_day ='$ediBusinessDay'"
      val df = spark.sql(sql_str)
      val suggestionsDf = runProfiler(df, databaseNane, tableName)
      persistConstraints(suggestionsDf)
    }
  }

  def runProfiler(df: DataFrame, databaseName: String, tableName: String): DataFrame = {
    val rec_count = df.limit(1).count()
    if (rec_count > 0) {
      logger.logInfo("count greater than zero")

      val df_curr = df.select(df.columns.map(x => col(x).as(x.toLowerCase(Locale.ROOT))): _*).repartition(200).cache()
      val suggestionResults = {
        ConstraintSuggestionRunner()
          .onData(df_curr)
          .addConstraintRules(Rules.DEFAULT)
          .addConstraintRule(UniqueIfApproximatelyUniqueRule())
          .run()
      }
      logger.logInfo("creating suggestion Dataframe")

      val suggestionDataFrame = suggestionResults.constraintSuggestions.flatMap {
          case (column, suggestions) =>
            suggestions.map { constraint =>
              (column, constraint.description, constraint.codeForConstraint)
            }
        }.toSeq.toDF("ruleColumn", "dqConstarint", "scalaCode")
        .withColumn("databaseName", lit(databaseName))
        .withColumn("sql_Text", lit(tableName))
        .withColumn("processNm", lit("PersistBatchPipelineProcess"))
        .withColumn("id", lit(null).cast("long"))

      suggestionDataFrame.show()
      suggestionDataFrame
    }
    else {
      throw new EDLException(errorClass = "EDLDataQualityException.PROFILE_EXCEPTION",
        messageParameters = null,
        cause = null, summary = "Data Quality Could not run")
    }
  }

  def persistConstraints(df: DataFrame): Unit = {
    EdlDqSuggestionsRepository.addNewEdlDqSuggestions(df)
  }

  def main(args: Array[String]): Unit = {
    val params = collection.mutable.Map[String, String]()

    for (arg <- args) {
      val Array(key, value) = arg.split("==")
      params += (key -> value)
    }

    if (params.contains("profileType")) {
      val profileType = params("profileType")
      if (profileType.equals("path")) {
        val path = params("path")
        val database = params("database")
        val table = params("table")
        DataProfile.deequProfiling(None, path, database, table, "")
      }
      else if (profileType.equals("database")) {
        val database = params("database")
        DataProfile.deequProfiling(None, "", database, "", "")
      }
      else if (profileType.equals("table")) {
        val path = params("path")
        val database = params("database")
        val table = params("table")
        val ediBusinessDay = params("ediBusinessDay")
        DataProfile.deequProfiling(None, path, database, table, ediBusinessDay)
      }
      else {
      }
    }
    else {
      // specify Profile type
    }
  }
}

    