package com.progressive.minds.chimera.core.dataSource.modal.data;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface DataReader {

    interface Files {
        Dataset<Row> read(String inSourceType, SparkSession inSparkSession, String inPipelineName,
                          String inSourcePath, String inColumnFilter, String inRowFilter,
                          StructType inStructSchema, String inCustomConfig, String inDelimiter, String inQuotes
                , Integer Limit);
    }

    interface Databases {
        Dataset<Row> read(String inSourceType, SparkSession inSparkSession, String inPipelineName, String inDatabaseNm,
                          String inTableNm, String inColumnFilter, String inRowFilter, String inCustomConfig);
    }

    interface OpenTables {
        void openTableFormatsData();
    }

    interface NOSQL {
        void noSQLDatabasesData();
    }
}

