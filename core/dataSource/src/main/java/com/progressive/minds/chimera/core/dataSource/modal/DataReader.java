package com.progressive.minds.chimera.core.dataSource.modal;

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

        Dataset<Row> read(String inSourceType, SparkSession inSparkSession, String inDataSourceNm,
                          String inSQLQuery, String inCustomConf) throws Exception;
    }

    interface OpenTables {
        Dataset<Row> read(String inSourceType, SparkSession inSparkSession, String inDatabaseName,
                          String inTableName, String columnFilter, String rowFilter, String inCustomConf) throws Exception;
    }

    interface NOSQL {
         Dataset<Row> read(String inSourceType, SparkSession inSparkSession, String inUrl,
                                 String inCollectionNm, String inCustomConf) throws Exception;
    }
}

