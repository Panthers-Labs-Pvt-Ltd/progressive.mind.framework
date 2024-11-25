package com.progressive.minds.chimera.core.databaseOps.apacheComet

import org.apache.spark.sql.SparkSession

// https://datafusion.apache.org/comet/user-guide/overview.html

object apacheComet {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Apache Comet Integration")
      .config("spark.sql.warehouse.dir", "c://temp")
      .config("spark.master", "local[*]") // Adjust for your cluster
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
  }
}
