package com.progressive.minds.chimera.core.dataSource.modal.data;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

public interface DataWriter {
    interface Files {
        Dataset<Row> write(String inSourceType, SparkSession inSparkSession, String inPipelineName, String inDatabaseName,
                           String inTableName, Dataset<Row> inSourceDataFrame, String inOutputPath, String inSavingMode,
                           String inPartitioningKeys, String inSortingKeys, String inDuplicationKeys,
                           String inExtraColumns, String inExtraColumnsValues, String inCustomConfig,
                           String inCompressionFormat) throws Exception;
    }
    interface Databases {
        Dataset<Row> write(String inSourceType, SparkSession inSparkSession, String inPipelineName, String inDatabaseNm,
                           String inTableNm, String inColumnFilter, String inRowFilter, String inCustomConfig);

    }
    interface OpenTables {
        void openTableFormatsDatawrite();
    }
    interface NOSQL {
        void noSQLDatabasesDatawrite();
    }



}

