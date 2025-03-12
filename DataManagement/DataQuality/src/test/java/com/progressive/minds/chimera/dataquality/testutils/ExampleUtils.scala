package com.progressive.minds.chimera.dataquality.testutils

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession

case class Item(id: Long,productName: String,description: String,priority: String,numViews: Long)

case class Manufacturer(id: Long,manufacturerName: String,countryCode: String)

object ExampleUtils {

  def withSpark(func: SparkSession => Unit): Unit = {
    val session = SparkSession.builder()
      .master("local")
      .appName("test")
      .config("spark.ui.enabled", "false")
      .getOrCreate()
    session.sparkContext.setCheckpointDir(System.getProperty("java.io.tmpdir"))

    try {
      func(session)
    } finally {
      session.stop()
      System.clearProperty("spark.driver.port")
    }
  }

  def itemsAsDataframe(session: SparkSession, items: Item*): DataFrame = {
    val rdd = session.sparkContext.parallelize(items)
    session.createDataFrame(rdd)
  }

  def manufacturersAsDataframe(session: SparkSession, manufacturers: Manufacturer*): DataFrame = {
    val rdd = session.sparkContext.parallelize(manufacturers)
    session.createDataFrame(rdd)
  }
}
