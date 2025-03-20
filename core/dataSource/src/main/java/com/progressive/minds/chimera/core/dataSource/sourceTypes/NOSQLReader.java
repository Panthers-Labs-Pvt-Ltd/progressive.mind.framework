package com.progressive.minds.chimera.core.dataSource.sourceTypes;

import com.progressive.minds.chimera.core.dataSource.formats.jdbc.jdbc;
import com.progressive.minds.chimera.core.dataSource.modal.DataReader;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Map;

public class NOSQLReader implements DataReader.Databases {
    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(NOSQLReader.class);

    private final String loggerTagName = "JDBC Reader";

    @Override
    public Dataset<Row> read(String inSourceType, SparkSession inSparkSession, String inDataSourceNm,
                             String inSQLQuery, String inCustomConf) throws Exception {
        Dataset<Row> dataFrame = inSparkSession.emptyDataFrame();

        // Add Logic to Pull RDS Credentials from Secret Manager or Data Source Connection Table
    /*    Map<String, String> Credentials = CloudCredentials.getCredentials("", "", "");
        String inUserName = Credentials.getOrDefault("username" , "postgres");
        String inPassword = Credentials.getOrDefault("password" , "root");
        String inJDBCUrl  = Credentials.getOrDefault("url" , "url");

        dataFrame =  jdbc.read(inSourceType, inSparkSession,inJDBCUrl,inUserName, inPassword, inSQLQuery, inCustomConf);*/
        return dataFrame;
    }
}