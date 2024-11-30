package com.progressive.minds.chimera.core.dataSource.formats.jdbc;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.progressive.minds.chimera.core.logger.ChimeraLogger;
import static com.progressive.minds.chimera.core.dataSource.utility.commonFunctions.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class jdbc {

    private static final String loggerTag = "JDBC ";
    private static final ChimeraLogger logger = new ChimeraLogger(jdbc.class);

    private static final String defaultConf = "[{\"Key\":\"pushDownPredicate\",\"value\":\"true\"}]";
    private static final String defaultWriteConf = "[{\"Key\":\"queryTimeout\",\"Value\":\"0\"}]";

    private static final Map<String, String> driverMap = new HashMap<>() {{
        put("mariadb", "org.mariadb.jdbc.Driver");
        put("mysql", "com.mysql.cj.jdbc.Driver");
        put("oracle", "oracle.jdbc.driver.OracleDriver");
        put("db2", "com.ibm.db2.jcc.DB2Driver");
        put("mssql", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        put("postgres", "org.postgresql.Driver");
        put("teradata", "com.teradata.jdbc.TeraDriver");
        put("redshift", "com.amazon.redshift.jdbc42.Driver");
    }};

    public static Dataset<Row> read(String inSourceType, SparkSession inSparkSession, String inJDBCUrl,
                                 String inUserName, String inPassword,
                                 String inSQLQuery, String inCustomConf) throws Exception {

        String inSourceTyp = capitalize(inSourceType);
        // validateInputs(inSourceType, inJDBCUrl, inUserName, inPassword, inSQLQuery);

        try {
            logger.logInfo(inSourceTyp + " Table", "Read Options: " + inCustomConf);
            String readOptions = isNullOrBlank(inCustomConf) ? defaultConf : inCustomConf;
            Map<String, String> extraOptions = getConfig(readOptions);

            String numPartitions = extraOptions.getOrDefault("numPartitions", "4");
            String fetchSize = extraOptions.getOrDefault("fetchSize", "5000");

            String driverType = driverMap.getOrDefault(inSourceType.toLowerCase(Locale.ROOT),
                    "");

            if (isNullOrBlank(driverType)) {
                logger.logError(inSourceTyp, "Invalid Driver Specified for " + inSourceTyp);
                throw new Exception("DataSourceException.InvalidJDBCDriver");
            }

            return inSparkSession.read()
                    .format("jdbc")
                    .option("url", inJDBCUrl)
                    .option("query", inSQLQuery)
                    .option("user", inUserName)
                    .option("password", inPassword)
                    .option("numPartitions", numPartitions)
                    .option("fetchSize", fetchSize)
                    .option("driver", driverType)
                    .options(extraOptions)
                    .load();

        } catch (Exception e) {
            logger.logError(inSourceTyp + " Data Source", "Error executing SQL Query: " + inSQLQuery, e);
            throw new Exception("DataSourceException.SQLException");
        }
    }

    public static boolean write(String inSourceType, Dataset<Row> inSourceDataFrame, String inJDBCUrl, String inUserName,
                                String inPassword, String inDatabaseName, String inTableName,
                                String inSaveMode, String inCustomConf) throws Exception {

        String inSourceTyp = capitalize(inSourceType);
        validateInputs(inSourceType, inJDBCUrl, inUserName, inPassword, inTableName);

        try {
            String writeOptions = isNullOrBlank(inCustomConf) ? defaultWriteConf : inCustomConf;
            Map<String, String> extraOptions = getConfig(writeOptions);

            String numPartitions = extraOptions.getOrDefault("numPartitions", "4");
            String batchSize = extraOptions.getOrDefault("batchsize", "5000");

            logger.logInfo(inSourceTyp + " Writer", "JDBC URL: " + inJDBCUrl);

            String driver = extraOptions.getOrDefault("driver", driverMap.getOrDefault(inSourceType.toLowerCase(Locale.ROOT), ""));
            if (isNullOrBlank(driver)) {
                throw new Exception("DataSourceException.InvalidJDBCDriver");
            }

            String DatabaseTableName = inDatabaseName + "." + inTableName;

            inSourceDataFrame.write()
                    .format("jdbc")
                    .option("url", inJDBCUrl)
                    .option("dbtable", DatabaseTableName)
                    .option("user", inUserName)
                    .option("password", inPassword)
                    .option("driver", driver)
                    .option("numPartitions", numPartitions)
                    .option("batchsize", batchSize)
                    .options(extraOptions)
                    .mode(inSaveMode)
                    .save();

            logger.logInfo(inSourceTyp + " Writer", "Data Write Process Completed successfully");
            return true;

        } catch (Exception e) {
            logger.logError(inSourceTyp + " Data Source", "Error writing data to table: " + inTableName, e);
            throw new Exception("DataSourceException.SQLException");
        }
    }

    private static void validateInputs(String sourceType, String jdbcUrl, String userName, String password,
                                     String sqlQuery) {
        // Validation logic
    }

}
