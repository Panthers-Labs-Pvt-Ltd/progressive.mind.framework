package com.progressive.minds.chimera.dataquality

import com.amazon.deequ.VerificationResult
import com.progressive.minds.chimera.dataquality.entities.{DQProcessStatus, DQRunnerMetrics}
import com.progressive.minds.chimera.dataquality.profiling.utils.DeequUtils
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory
import com.progressive.minds.chimera.foundational.exception
import com.progressive.minds.chimera.foundational.exception.ChimeraException
import org.apache.spark.sql.functions.lit
import org.apache.spark.sql.types.{DoubleType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

import java.util
import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable.Map

object DeequRunner {
  private val logger = ChimeraLoggerFactory.getLogger(this.getClass)
  private val loggerTag = "EDLDeequRunner"

  def getDqRules(spark: SparkSession, databaseName: String, tableName: String, stageName: String): DataFrame = {
    logger
    logger.logInfo(s"Fetching Data Quality Rules for $databaseName.$tableName")
    var scalaMap = Map.empty[String, String]
    if (stageName.isEmpty) {
      scalaMap = Map("databaseNm" -> databaseName, "tableNm" -> tableName, "activeFlg" -> "Y")
    }
    else {
      scalaMap = Map("databaseNm" -> databaseName, "tableNm" -> tableName, "procecssTypNm" -> stageName, "activeFlg" -> "Y")
    }
    val rules_df = EdlDqUserConfigRepository.listEdlDqUserConfigByColumns(spark, new util.HashMap[String,String](scalaMap))
    rules_df.show()
    rules_df
  }

  def execute(spark: SparkSession, batchId: String, dataFrame: DataFrame, databaseName: String,
              tableName: String, businessDate: String, instance: String, pipelineName: String,
              controlNames: Option[Seq[String]], stageName: Option[String], pipelineVersion: String): DQRunnerMetrics = {
    val audit_time = EDLUtils.convertTimeFormat("yyyy-MM-dd HH:mm::ss.SSS", EDLUtils.currentGMTCalender())
    var sourceRecordDuplicateCount: Double = 0
    var blank_row: Double = 0
    var actual_count: Double = 0
    val metrics = new DQRunnerMetrics
    if (dataFrame.limit(1).count() == 0) {
      logger.logError(loggerTag + " There is no records in Datframe for which DQ check is requested")
      return null
    }
    try {
      val dataFrameTmp = dataFrame.cache
      metrics.runnerStartTime = EDLUtils.currentGMTCalender()
      metrics.getRulesStartTime = EDLUtils.currentGMTCalender()

      val edlCheckResultSchema = StructType(Array(
        StructField("check", StringType, true),
        StructField("check_level", StringType, true),
        StructField("check_status", StringType, true),
        StructField("constraint", StringType, true),
        StructField("constraint_status", StringType, true),
        StructField("constraint_message", StringType, true),
        StructField("table_name", StringType, false),
        StructField("database_name", StringType, false),
        StructField("edi_business_day", StringType, false),
        StructField("src_sys_inst_id", StringType, false)))
      var edlCheckResultsDf = spark.createDataFrame(spark.sparkContext.emptyRDD[Row], edlCheckResultSchema)

      val checkResultsSchema = StructType(Array(
        StructField("dqCheck", StringType, true),
        StructField("checkLevel", StringType, true),
        StructField("checkStatus", StringType, true),
        StructField("dqConstraint ", StringType, true),
        StructField("constraintStatus", StringType, true),
        StructField("constraintMesg", StringType, true),
        StructField("batchId", StringType, false),
        StructField("processTypNm", StringType, false),
        StructField("tableNm", StringType, false),
        StructField("databaseNm", StringType, false),
        StructField("ruleCol", StringType, false),
        StructField("controlNn", StringType, false),
        StructField("ruleNm", StringType, false)))
      var checkResultsDf = spark.createDataFrame(spark.sparkContext.emptyRDD[Row], checkResultsSchema)

      val edlanalysisResultSchema = StructType(Array(
        StructField("entity", StringType, true),
        StructField("instance", StringType, true),
        StructField("name", StringType, true),
        StructField("value", DoubleType, false),
        StructField("table_name", StringType, false),
        StructField("database_name", StringType, false),
        StructField("edi_business_day", StringType, false),
        StructField("src_sys_inst_id", StringType, false)))
      var edlAnalysisResultDf = spark.createDataFrame(spark.sparkContext.emptyRDD[Row], edlanalysisResultSchema)

      val analysisResultSchema = StructType(Array(
        StructField("entityTyp", StringType, true),
        StructField("instance", StringType, true),
        StructField("ruleName", StringType, true),
        StructField("actualValue", DoubleType, false),
        StructField("batchId", StringType, false),
        StructField("processTypNm", StringType, false),
        StructField("tableNm", StringType, false),
        StructField("databaseNm", StringType, false)))
      var analysisResultDf = spark.createDataFrame(spark.sparkContext.emptyRDD[Row], analysisResultSchema)

      val rulesDf = getDqRules(spark, databaseName, tableName, stageName.getOrElse(""))
      metrics.deequRules = rulesDf.count()
      metrics.getRulesEndTime = EDLUtils.currentGMTCalender()
      if (metrics.deequRules == 0) {
        logger.logError(loggerTag + metrics.deequRules + "rules returned for table: %s "
          .format(tableName))
        return null
      } else {
        rulesDf.collect().foreach(row => {
          val ruleControlNm = row.getAs[String](fieldName = "controlNm")
          val ruleNm = row.getAs[String](fieldName = "ruleNm")
          val ruleColNm = row.getAs[String](fieldName = "ruleCol")
          logger.logInfo(loggerTag + s"Executing DQ Rule $ruleNm for $ruleColNm")

          val schema = rulesDf.schema
          val rowRdd = spark.sparkContext.parallelize(Seq(row))
          val rowDf = spark.createDataFrame(rowRdd, schema)
          metrics.executeRulesStartTime = EDLUtils.currentGMTCalender()
          val result = DeequUtils.executeRulesNoRepo(rowDf, dataFrameTmp)
          metrics.executeRulesEndTime = EDLUtils.currentGMTCalender()
          metrics.persistAnalysersStartTime = EDLUtils.currentGMTCalender()

          val edlAnalysisResultTmp = VerificationResult.successMetricsAsDataFrame(spark, result)
            .withColumn("table_name", lit(tableName))
            .withColumn("database_name", lit(databaseName))
            .withColumn("edi_business_day", lit(businessDate))
            .withColumn("src_sys_inst_id", lit(instance))
          edlAnalysisResultDf = edlAnalysisResultDf.union(edlAnalysisResultTmp)

          val analysisResultDfTmp = VerificationResult.successMetricsAsDataFrame(spark, result)
            .withColumnRenamed("entity", "entityTyp")
            .withColumn("batchId", lit(batchId))
            .withColumn("processTypNm", lit(stageName.getOrElse("")))
            .withColumn("tableNm", lit(tableName))
            .withColumn("databaseNm", lit(databaseName))
            .withColumnRenamed("value", "actualValue")
            .withColumnRenamed("name", "ruleName")
          analysisResultDf = analysisResultDf.union(analysisResultDfTmp)

          val edlCheckResultsDfTmp = VerificationResult.checkResultsAsDataFrame(spark, result)
            .withColumnRenamed("entity", "entityTyp")
            .withColumn("table_name", lit(tableName))
            .withColumn("database_name", lit(databaseName))
            .withColumn("edi_business_day", lit(businessDate))
            .withColumn("src_sys_inst_id", lit(instance))
          edlCheckResultsDf = edlCheckResultsDf.union(edlCheckResultsDfTmp)

          val checkResultsDfTmp = VerificationResult.checkResultsAsDataFrame(spark, result)
            .withColumn("batchId", lit(batchId))
            .withColumn("processTypNm", lit(stageName.getOrElse("")))
            .withColumn("tableNm", lit(tableName))
            .withColumn("databaseNm", lit(databaseName))
            .withColumn("ruleCol", lit(ruleColNm))
            .withColumn("controlNm", lit(ruleControlNm))
            .withColumn("ruleNm", lit(ruleNm))
            .withColumnRenamed("constraint_status", "constraintStatus")
            .withColumnRenamed("constraint_message", "constraintMesg")
            .withColumnRenamed("check_status", "checkStatus")
            .withColumnRenamed("check_level", "checkLevel")
            .withColumnRenamed("check", "dqCheck")
            .withColumnRenamed("constraint", "dqConstraint")

          checkResultsDf = checkResultsDf.union(checkResultsDfTmp)
          logger.logInfo(loggerTag + s"DQ Rule $ruleNm for $ruleColNm Executes Successfully")
        })

        edlAnalysisResultDf.createOrReplaceTempView("dqAnalysisResults")
        analysisResultDf.show(false)
        EdlDqUserConfigLogRepository.addNewEdlDqUserConfigLogs(analysisResultDf)

        metrics.persistAnalysersEndTime = EDLUtils.currentGMTCalender()
        logger.logInfo(loggerTag + "Deequ analysis results Added in Tachyon")
        logger.logInfo(loggerTag + "Writing deequ check results")
        metrics.persistChecksStartTime = EDLUtils.currentGMTCalender()
        edlCheckResultsDf.createOrReplaceTempView("dqCheckResults")
        logger.logInfo(loggerTag + "Temporary Deequ Check results created")

        logger.logInfo(loggerTag  + "Check results of created")
        checkResultsDf.show(false)
        EdlDataQualityLogRepository.addNewEdlDataQualityLogs(checkResultsDf)
        logger.logInfo(loggerTag + "check results Dataframe added to DataQualityLogs")
        metrics.persistChecksEndTime = EDLUtils.currentGMTCalender()
        metrics.runnerEndTime = EDLUtils.currentGMTCalender()


        metrics.deequErrors = checkResultsDf
          .where("checkLevel = 'Error' and constraintStatus = 'Failure'")
          .count()

        metrics.deequWarnings = checkResultsDf
          .where("checkLevel = 'Warning' and constraintStatus = 'Success'")
          .count()

        metrics.deequPasses = checkResultsDf.where("constraintStatus = 'Success'")
          .count()

        logger.logInfo(loggerTag + "deequErrors, deequWarnings, deeqPasses :: +" +
          s"${metrics.deequErrors}, ${metrics.deequWarnings}, ${metrics.deequPasses}")

        val uniqueness_df_count = analysisResultDf.where("ruleName = 'Uniqueness'").limit(1).count()
        logger.logInfo(loggerTag + s"uniqueness_df_count is $uniqueness_df_count")
        val size_df_count = analysisResultDf.where("ruleName = 'Size'").limit(1).count()
        logger.logInfo(loggerTag + s"size_df_count is ${size_df_count}")
        val completeness_df_count = analysisResultDf.where("ruleName = 'Completeness'").limit(1).count()
        logger.logInfo(loggerTag + s"completeness_df_count is ${completeness_df_count}")
        var raw_dup_count = ""
        var raw_count = ""
        var raw_blank = ""
        if (uniqueness_df_count > 0) {
          raw_dup_count = analysisResultDf.where("ruleName = 'Uniqueness'")
            .select("actua;Value").first().mkString("")
          logger.logInfo(loggerTag + "raw_dup_count :: " + raw_dup_count)
        }

        if (size_df_count > 0) {
          raw_count = analysisResultDf.where("ruleName = 'Size'")
            .select("actua;Value").first().mkString("")
          logger.logInfo(loggerTag + "raw_count :: " + raw_count)
        }

        if (completeness_df_count > 0) {
          raw_blank = analysisResultDf.where("ruleName = 'Completeness'")
            .select("actua;Value").first().mkString("")
          logger.logInfo(loggerTag + "raw_blank :: " + raw_blank)
        }

        if (!raw_count.isEmpty) {
          actual_count = raw_count.toDouble
          logger.logInfo(loggerTag + "actual_count :: " + actual_count.toString)
        }

        if (!raw_dup_count.isEmpty && actual_count != 0) {
          sourceRecordDuplicateCount = actual_count - (actual_count * raw_dup_count.toDouble)
          logger.logInfo(loggerTag + "sourceDuplicateCount :: " + sourceRecordDuplicateCount.toString)
        }

        if (!raw_blank.isEmpty && actual_count != 0) {
          blank_row = actual_count - (actual_count * raw_blank.toDouble)
          logger.logInfo(loggerTag + "blank_row :: " + blank_row.toString)
        }

        if (metrics.deequWarnings > 0) {
          logger.logWarning(loggerTag + "Dq processing has generated %s warnings".format(metrics.deequWarnings))
        }

        if (metrics.deequErrors > 0) {
          logger.logError(loggerTag + s"Dq processing has generated %s errors".format(metrics.deequErrors))
          throw new ChimeraException(errorClass = "EDLDataQualityExcception.DEEQU_FAILURE",
            messageParameters = scala.collection.immutable.Map("exception" ->
              "Dq processing has generated %s errors".format(metrics.deequErrors)),
            cause = null, summary="")
        }
        metrics.processStatus = DQProcessStatus.Success
      }
      logger.logInfo(loggerTag + s"execute function completed")

    }
    catch {
      case e: Exception =>
        logger.logError(loggerTag + s"An unexpected error has occurres - $e")
        metrics.processStatus = DQProcessStatus.Error
        throw new EDLException(errorClass = "EDLDataQualityException.DEEQU_FAILURE",
          messageParameters = scala.collection.immutable.Map("exception" ->
            "Dq procesing has generated %s errors".format(metrics.deequErrors)),
          cause = e)
    }

    val metricsDf = metrics.toDf(spark).withColumn("job_id", lit(batchId))
      .withColumn("audit_time", lit(audit_time))
      .withColumn("table_name", lit(tableName))
      .withColumn("database_name", lit(databaseName))
      .withColumn("instance", lit(instance))
      .withColumn("business_date", lit(businessDate))
      .withColumn("pipeline_name", lit(pipelineName))
      .withColumn("client_name", lit("OBDEF"))
      .withColumn("process_type_name", lit(stageName.getOrElse("")))
      .withColumn("source_count", lit(actual_count.toLong))
      .withColumn("target_count", lit(actual_count.toLong))
      .withColumn("blank_row", lit(blank_row.toLong))
      .withColumn("source_duplicate_count", lit(sourceRecordDuplicateCount))
      .withColumn("id", lit(null).cast("long"))
      .withColumn("pipeline_version", lit(pipelineVersion))

    metricsDf.createOrReplaceTempView("dqMetrics")
    metricsDf.show(false)
    logger.logInfo(loggerTag + s"Deequ Full Metrics results")
    metrics
  }
}
