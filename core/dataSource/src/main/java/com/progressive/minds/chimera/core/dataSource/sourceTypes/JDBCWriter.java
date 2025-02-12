package com.progressive.minds.chimera.core.dataSource.sourceTypes;

import com.progressive.minds.chimera.core.dataSource.formats.jdbc.jdbc;
import com.progressive.minds.chimera.core.dataSource.modal.DataWriter;
import com.progressive.minds.chimera.core.databaseOps.utility.CloudCredentials;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.Map;

public class JDBCWriter implements DataWriter.Databases {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(JDBCWriter.class);
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
        logger.logInfo("Initiating JDBC Writer....");
        Boolean writeStatus = true;
        // Add Logic to Pull RDS Credentials from Secret Manager or Data Source Connection Table
      //  Map<String, String> Credentials = CloudCredentials.getCredentials("", "", "");
        Map<String, String> Credentials = new HashMap<>();
        String UserName = Credentials.getOrDefault("username" , "chimera");
        String Password = Credentials.getOrDefault("password" , "chimera123");
        String JDBCUrl  = Credentials.getOrDefault("url" , "jdbc:postgresql://localhost:5432/chimera_test");

        writeStatus = jdbc.write(inSourceType, inSourceDataFrame, JDBCUrl,  UserName,
                Password,  inDatabaseName,  inTableName, inSaveMode,inCustomConf);

        return writeStatus;
    }

    public boolean write(String inSourceType, Dataset<Row> inSourceDataFrame, String JDBCUrl, String UserName,
                         String password, String databaseName, String inTableName, String inSaveMode, String inCustomConf) {
        logger.logInfo("Initiating JDBC Writer....");
        return jdbc.write(inSourceType, inSourceDataFrame, JDBCUrl,  UserName,
                password,  databaseName,  inTableName, inSaveMode,inCustomConf);

    }
}