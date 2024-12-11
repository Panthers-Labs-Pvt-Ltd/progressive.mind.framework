/*
package com.progressive.minds.chimera.core.databaseOps.apacheComet

import org.apache.spark.sql.SparkSession

// https://datafusion.apache.org/comet/user-guide/overview.html

object apacheComet {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Apache Comet Integration")
      .config("spark.sql.warehouse.dir", "c://temp")
      .config("spark.master", "local[*]")
      .config("spark.plugins", "org.apache.spark.CometPlugin")
      .config("spark.shuffle.manager", "org.apache.spark.sql.comet.execution.shuffle.CometShuffleManager")
      .config("spark.comet.explainFallback.enabled", "true")
      .config("spark.memory.offHeap.enabled", "true")
      .config("spark.memory.offHeap.size", "2g")
      .config("spark.comet.convert.csv.enabled", "true")
      .getOrCreate()

    val fileName = "sample.csv"
 /*   val filePath = Option(getClass.getResource(fileName))
      .map(_.getPath)
      .getOrElse(throw new RuntimeException(s"File $fileName not found in resources!"))
*/
    val df = spark.read.option("header", "true").csv("core/apacheComet/src/main/resources/sample.csv")
    df.show(10)

    import ch.cern.sparkmeasure.

    val stageMetrics = ch.cern.sparkmeasure.StageMetrics(spark)
    stageMetrics.runAndMeasure {
      spark.sql("select count(*) from range(1000) cross join range(1000) cross join range(1000)").show()
    }

    // print report to standard output
    stageMetrics.printReport()

    // get metric values as a Map
    val metrics = stageMetrics.aggregateStageMetrics()
    assert(metrics("numStages") > 1)

    // Introduced in sparkMeasure v0.21, memory metrics report:
    stageMetrics.printMemoryReport()

    //save session metrics data
    val d2f = stageMetrics.createStageMetricsDF("PerfStageMetrics")
    stageMetrics.saveData(d2f.orderBy("jobId", "stageId"), "/tmp/stagemetrics_test1")

    val aggregatedDF = stageMetrics.aggregateStageMetrics("PerfStageMetrics")
    stageMetrics.saveData(aggregatedDF, "/tmp/stagemetrics_report_test2")
  }
}
*/
