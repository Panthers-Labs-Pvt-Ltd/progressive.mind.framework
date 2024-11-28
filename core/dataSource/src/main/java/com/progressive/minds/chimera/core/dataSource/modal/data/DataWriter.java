package com.progressive.minds.chimera.core.dataSource.modal.data;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

public interface DataWriter {
    interface Files {
        Dataset<Row> write(String inSourceType, SparkSession inSparkSession, String inPipelineName, String inDatabaseNm,
                          String inTableNm, String inColumnFilter, String inRowFilter, String inCustomConfig);
    }
    interface Databases {
        Dataset<Row> write(String inSourceType, SparkSession inSparkSession, String inPipelineName, String inSourcePath,
                          String inColumnFilter, String inRowFilter, StructType instructSchema, String inCustomConfig,
                          String inDelim, String inQuotes, int inLimit);

    }
    interface OpenTables {
        void openTableFormatsDatawrite();
    }
    interface NOSQL {
        void noSQLDatabasesDatawrite();
    }



}

