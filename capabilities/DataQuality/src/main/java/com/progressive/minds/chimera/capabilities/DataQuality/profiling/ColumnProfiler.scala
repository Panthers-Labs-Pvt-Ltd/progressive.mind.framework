package com.progressive.minds.chimera.capabilities.DataQuality.profiling

import scala.None.getOrElse
import com.amazon.deequ.analyzers.DataTypeInstances
import com.amazon.deequ.profiles.ColumnProfilerRunner
import org.apache.spark
import org.nwg.edl.tachyon.core.dbmgmt.modal.{EdlNumDataProfiler, EdlStdDataProfiler}
import org.nwg.edl.tachyon.core.dbmgmt.repository.{EdlNumDataProfilerRepository, EdlStdDataProfilerRepository}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.functions.lit
import org.apache.spark.sql.types.{DoubleType, LongType, StringType, StructField, StructType}

import java.math.BigInteger

class ColumnProfiler {

  def profileData(spark: SparkSession, df: DataFrame, tableName: String): Unit ={
    val result = ColumnProfilerRunner()
      .onData(df)
      .run()

    result.profiles.foreach { case (colName, profile) =>
      val cp = new EdlStdDataProfiler(tableName, colName.toString, "", profile.dataType.toString,
        profile.completeness.toString, profile.approximateNumDistinctValues.toString)

      EdlStdDataProfilerRepository.addNewEdlStdDataProfiler(cp)

      if (profile.dataType.equals(DataTypeInstances.Fractional)
        || profile.dataType.equals(DataTypeInstances.Integral)) {
        val totalNumberProfile = result.profiles(colName).asInstanceOf[com.amazon.deequ.profiles.NumericColumnProfile]

        val ncp = new EdlNumDataProfiler(tableName, colName,
          BigInteger.valueOf(totalNumberProfile.minimum.get.toLong),
          BigInteger.valueOf(totalNumberProfile.maximum.get.toLong),
          BigInteger.valueOf(totalNumberProfile.mean.get.toLong),
          BigInteger.valueOf(totalNumberProfile.stdDev.get.toLong))

        EdlNumDataProfilerRepository.addNewEdlNumDataProfiler(ncp)
      }
    }

    }


}
