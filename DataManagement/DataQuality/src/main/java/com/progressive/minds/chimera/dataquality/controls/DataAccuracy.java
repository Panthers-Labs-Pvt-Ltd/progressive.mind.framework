package com.progressive.minds.chimera.dataquality.controls;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

public class DataAccuracy extends DataControls {
    @Override
    public boolean validate() {
        return false;
    }

//    public DataControls(SparkSession sparkSession, Dataset sourceDF, Dataset targetDf,
//                             String processTypeName, String instanceId, String partitionColumn,
//                             String databaseName, String tableName, String checkLevel,
//                             StructType inboundSchema, Integer batchId) {
//
//        super(sparkSession, sourceDF, targetDf, processTypeName, instanceId,
//                partitionColumn, databaseName, tableName, checkLevel, inboundSchema, batchId);
//    }


}
