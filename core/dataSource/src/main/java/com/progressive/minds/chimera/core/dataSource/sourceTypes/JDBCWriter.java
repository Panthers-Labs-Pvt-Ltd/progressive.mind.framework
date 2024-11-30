package com.progressive.minds.chimera.core.dataSource.sourceTypes;

import com.progressive.minds.chimera.core.dataSource.formats.jdbc.jdbc;
import com.progressive.minds.chimera.core.dataSource.modal.DataWriter;
import com.progressive.minds.chimera.core.databaseOps.utility.CloudCredentials;
import com.progressive.minds.chimera.core.logger.ChimeraLogger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Map;

public class JDBCWriter implements DataWriter.Databases {

    private final ChimeraLogger logger = new ChimeraLogger(this.getClass());
    private final String loggerTagName = "JDBC Writer";

    /**
     * @param inSourceType
     * @param inSourceDataFrame
     * @param inDataSourceNm
     * @param inDatabaseName
     * @param inTableName
     * @param inSaveMode
     * @param inCustomConf
     * @return
     * @throws Exception
     */
    @Override
    public boolean write(String inSourceType, Dataset<Row> inSourceDataFrame, String inDataSourceNm,
                         String inDatabaseName, String inTableName, String inSaveMode, String inCustomConf)
            throws Exception {
        logger.logInfo(loggerTagName, "Initiating JDBC Reader....");
        Boolean writeStatus = true;
        // Add Logic to Pull RDS Credentials from Secret Manager or Data Source Connection Table
        Map<String, String> Credentials = CloudCredentials.getCredentials("", "", "");
        String UserName = Credentials.getOrDefault("username" , "postgres");
        String Password = Credentials.getOrDefault("password" , "root");
        String JDBCUrl  = Credentials.getOrDefault("url" , "url");

        writeStatus = jdbc.write(inSourceType, inSourceDataFrame, JDBCUrl,  UserName,
                Password,  inDatabaseName,  inTableName, inSaveMode,inCustomConf);

        return writeStatus;
    }
}