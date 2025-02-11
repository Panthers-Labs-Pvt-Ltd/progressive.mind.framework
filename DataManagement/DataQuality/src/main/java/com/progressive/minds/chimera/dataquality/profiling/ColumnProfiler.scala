package com.progressive.minds.chimera.dataquality.profiling

import com.amazon.deequ.analyzers.DataTypeInstances
import com.amazon.deequ.profiles.ColumnProfilerRunner
import com.progressive.minds.chimera.dataquality.entities
import com.progressive.minds.chimera.dataquality.entities.StandardDataProfilerEntity
import com.progressive.minds.chimera.dataquality.repository.DQRepository
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.math.BigInteger

class ColumnProfiler {
  //private val stdDataProfilerRepository = DQRepository[StandardDataProfiler]
  def profileData(spark: SparkSession, df: DataFrame, tableName: String): Unit ={
    val result = ColumnProfilerRunner()
      .onData(df)
      .run()
//    result.profiles.foreach { case (colName, profile) =>
//      val cp = new StandardDataProfiler(tableName, colName.toString, "", profile.dataType.toString,
//        profile.completeness.toString, profile.approximateNumDistinctValues.toString)
//
//      stdDataProfilerRepository .addNewEdlStdDataProfiler(cp)
//
//      if (profile.dataType.equals(DataTypeInstances.Fractional)
//        || profile.dataType.equals(DataTypeInstances.Integral)) {
//        val totalNumberProfile = result.profiles(colName).asInstanceOf[com.amazon.deequ.profilesNumericColumnProfile]
//
//        val ncp = new EdlNumDataProfiler(tableName, colName,
//          BigInteger.valueOf(totalNumberProfile.minimum.get.toLong),
//          BigInteger.valueOf(totalNumberProfile.maximum.get.toLong),
//          BigInteger.valueOf(totalNumberProfile.mean.get.toLong),
//          BigInteger.valueOf(totalNumberProfile.stdDev.get.toLong))
//
//        EdlNumDataProfilerRepository.addNewEdlNumDataProfiler(ncp)
//      }
//    }
}
}
