package com.progressive.minds.chimera.core.dataSource.modal;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public interface DataWriter {
    interface Files {
        Dataset<Row> write(String inSourceType, SparkSession inSparkSession, String inPipelineName, String inDatabaseName,
                           String inTableName, Dataset<Row> inSourceDataFrame, String inOutputPath, String inSavingMode,
                           String inPartitioningKeys, String inSortingKeys, String inDuplicationKeys,
                           String inExtraColumns, String inExtraColumnsValues, String inCustomConfig,
                           String inCompressionFormat) throws Exception;
    }
    interface Databases {
        boolean write(String inSourceType, Dataset<Row> inSourceDataFrame,String inDataSourceNm,
                      String inDatabaseName, String inTableName, String inSaveMode, String inCustomConf) throws Exception;
    }
    interface OpenTables {
        void openTableFormatsDatawrite();
    }
    interface NOSQL {
        void noSQLDatabasesDatawrite();
    }



}

