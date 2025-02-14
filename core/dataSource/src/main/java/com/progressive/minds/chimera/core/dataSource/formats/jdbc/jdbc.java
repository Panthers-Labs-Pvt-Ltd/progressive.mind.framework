package com.progressive.minds.chimera.core.dataSource.formats.jdbc;

import java.util.Locale;
import java.util.Map;

import static com.progressive.minds.chimera.core.dataSource.utility.commonFunctions.*;

import com.progressive.minds.chimera.core.dataSource.utility.JdbcDriver;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class jdbc {

    private static final String LOGGER_TAG = "JDBC";
    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(jdbc.class);

    private static final String DEFAULT_READ_CONF = "[{\"Key\":\"pushDownPredicate\",\"Value\":\"true\"}]";
    private static final String DEFAULT_WRITE_CONF = "[{\"Key\":\"queryTimeout\",\"Value\":\"0\"}]";

   public static Dataset<Row> read(String sourceType, SparkSession sparkSession, String jdbcUrl,
                                    String userName, String password, String sqlQuery, String customConf) {

        String sourceTypeCapitalized = capitalize(sourceType);
        validateInputs(sourceType, jdbcUrl, userName, password, sqlQuery);

        try {
            logger.logInfo(LOGGER_TAG + " : " + sourceTypeCapitalized + " - Read Options: " + customConf);

            String readOptions = isNullOrBlank(customConf) ? DEFAULT_READ_CONF : customConf;
            Map<String, String> extraOptions = getConfig(readOptions);

            String numPartitions = extraOptions.getOrDefault("numPartitions", "4");
            String fetchSize = extraOptions.getOrDefault("fetchSize", "5000");

            String driverType = JdbcDriver.getDriver(sourceType.toLowerCase(Locale.ROOT)).orElse("");


            if (isNullOrBlank(driverType)) {
                throw new RuntimeException("Invalid JDBC Driver for " + sourceTypeCapitalized);
            }

            logger.logInfo(LOGGER_TAG + " : " + String.format("Connecting to: %s | Query: %s | User: %s | Driver: %s",
                    jdbcUrl, sqlQuery, userName, driverType));

            Dataset<Row> df = sparkSession.read()
                    .format("jdbc")
                    .option("url", jdbcUrl)
                    .option("query", sqlQuery)
                    .option("user", userName)
                    .option("password", password)
                    .option("numPartitions", numPartitions)
                    .option("fetchSize", fetchSize)
                    .option("driver", driverType)
                    .options(extraOptions)
                    .load();

            if (df.isEmpty()) {
                logger.logWarning(LOGGER_TAG + " : " + "Query returned an empty DataFrame.");
            }

            return df;

        } catch (Exception e) {
            logger.logError(LOGGER_TAG + " : " + sourceTypeCapitalized + " - Error executing SQL Query: " + sqlQuery, e);
            throw new RuntimeException("Database Exception", e);
        }
    }

    public static boolean write(String sourceType, Dataset<Row> dataFrame, String jdbcUrl, String userName,
                                String password, String databaseName, String tableName, String saveMode,
                                String customConf) {

        String sourceTypeCapitalized = capitalize(sourceType);
        validateInputs(sourceType, jdbcUrl, userName, password, tableName);

        try {
            String writeOptions = isNullOrBlank(customConf) ? DEFAULT_WRITE_CONF : customConf;
            Map<String, String> extraOptions = getConfig(writeOptions);

            String numPartitions = extraOptions.getOrDefault("numPartitions", "4");
            String batchSize = extraOptions.getOrDefault("batchsize", "5000");

            logger.logInfo(LOGGER_TAG + " : " + sourceTypeCapitalized + " - Writing to " + databaseName + "." + tableName);

            String driver = JdbcDriver.getDriver(sourceType.toLowerCase(Locale.ROOT)).orElse("");
            if (isNullOrBlank(driver)) {
                throw new RuntimeException("Invalid JDBC Driver for " + sourceTypeCapitalized);
            }

            logger.logInfo(LOGGER_TAG + " : " + String.format("Writing to: %s | table: %s | User: %s | Driver: %s",
                    jdbcUrl, tableName, userName, driver));

            dataFrame.write()
                    .format("jdbc")
                    .option("url", jdbcUrl)
                    .option("dbtable", tableName)
                    .option("user", userName)
                    .option("password", password)
                    .option("driver", driver)
                    .option("numPartitions", numPartitions)
                    .option("batchsize", batchSize)
                    .options(extraOptions)
                    .mode(saveMode)
                    .save();

            logger.logInfo(LOGGER_TAG + " : " + sourceTypeCapitalized + " - Data written successfully to " + databaseName + "." + tableName);
            return true;

        } catch (Exception e) {
            logger.logError(LOGGER_TAG + " : " + sourceTypeCapitalized + " - Error writing to table: " + tableName, e);
            throw new RuntimeException("Database Write Exception", e);
        }
    }

    private static void validateInputs(String sourceType, String jdbcUrl, String userName, String password, String queryOrTable) {
        if (isNullOrBlank(sourceType)) {
            throw new IllegalArgumentException("Source type cannot be null or empty.");
        }
        if (isNullOrBlank(jdbcUrl)) {
            throw new IllegalArgumentException("JDBC URL cannot be null or empty.");
        }
        if (isNullOrBlank(userName)) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (isNullOrBlank(password)) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        if (isNullOrBlank(queryOrTable)) {
            throw new IllegalArgumentException("SQL Query or Table name cannot be null or empty.");
        }
    }
}
