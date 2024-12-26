package com.progressive.minds.chimera.core.dataSource.formats.openTable;

import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import static com.progressive.minds.chimera.core.dataSource.utility.commonFunctions.*;

public class iceberg {

    private static final String loggerTag = "openTable";
    private static final String ICEBERG_FORMAT = "iceberg";
    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(iceberg.class);
    private static final String defaultCompressionFormat = "snappy";
    private static final String defaultReadConf = "[ {\"Key\":\"read.split.target-size\",\"value\":\"134217728\"}]";
    private static final String defaultWriteConf = "[ {\"Key\":\"write.target-file-size-bytes\",\"value\":\"536870912\"}]";
    private static final String defaultFileSubFormat = "parquet";
    //  private static final String defaultWriteConf = "[{\"Key\":\"maxBatchSize\",\"Value\":\"512\"}]";

    public static Dataset<Row> read(SparkSession inSparkSession, String inCatalog, String inDatabasename, String inTablename,
                                    String inColumnFilter, String inRowFilter, String inCustomConfig)
            throws Exception {

        logger.logInfo("Iceberg - Read Options: " + inCustomConfig);
        String readOptions = StringUtils.defaultIfBlank(inCustomConfig, defaultReadConf);
        Map<String, String> extraOptions = getConfig(readOptions);

        String[] columnArray = inColumnFilter != null && !inColumnFilter.isEmpty() ?
                inColumnFilter.split(",") : new String[0];
        Dataset<Row> dataFrame = inSparkSession.emptyDataFrame();
        try {
            dataFrame = inSparkSession.read()
                    .format(ICEBERG_FORMAT)
                    .options(extraOptions)
                    .load(inCatalog + "." + inDatabasename + "." + inTablename);

            if (columnArray.length > 0) {
                dataFrame = dataFrame.select(columnArray[0], Arrays.copyOfRange(columnArray, 1, columnArray.length));

            }
            if (!isNullOrBlank(inRowFilter)) {
                dataFrame = dataFrame.where(inRowFilter);
                //  dataFrame = dataFrame.where("middleName=='A'");
            }
        } catch (Exception e) {
            logger.logError(String.format("Iceberg Table Reading Failed: Error=%s",
                    e.getMessage()), e);
        }
        return dataFrame;
    }

    public static Dataset<Row> readWithColumnFilters(SparkSession inSparkSession, String inCatalog, String inDatabasename, String inTablename,
                                                     String inColumnFilter, String inCustomConfig) throws Exception {
        return read(inSparkSession, inCatalog, inDatabasename, inTablename, inColumnFilter, null, inCustomConfig);
    }

    public static Dataset<Row> readWithRowFilters(SparkSession inSparkSession, String inCatalog, String inDatabasename, String inTablename,
                                                  String inRowFilter, String inCustomConfig) throws Exception {
        return read(inSparkSession, inCatalog, inDatabasename, inTablename, null, inRowFilter, inCustomConfig);
    }

    public static Dataset<Row> read(SparkSession inSparkSession, String inCatalog, String inDatabasename, String inTablename,
                                    String inCustomConfig) throws Exception {
        return read(inSparkSession, inCatalog, inDatabasename, inTablename, null, null, inCustomConfig);
    }
    public static Dataset<Row> read(SparkSession inSparkSession, String inCatalog, String inDatabasename, String inTablename) throws Exception {
        return read(inSparkSession, inCatalog, inDatabasename, inTablename, null, null, null);
    }

    public static void write(Dataset<Row> DF, SparkSession inSparkSession, String inCatalogName, String inDatabaseName, String inTableName,
                             String inOutputPath, String inCompressionFormat, String inFileFormat, String inWriteMode, String inPartitionKeys, String inCustomConfig) {
        logger.logInfo("Iceberg - Write Options: " + inCustomConfig);

        // Use default formats if input is blank
        String fileSubFormat = StringUtils.defaultIfBlank(inFileFormat, defaultFileSubFormat).toLowerCase();
        String compressionFormat = StringUtils.defaultIfBlank(inCompressionFormat, defaultCompressionFormat).toLowerCase();

        // Check if the table exists
        boolean tableExists = tableExists(inSparkSession, inCatalogName, inDatabaseName, inTableName);

        // Parse custom configuration options
        String writeOptions = StringUtils.defaultIfBlank(inCustomConfig, defaultWriteConf);
        Map<String, String> extraOptions = getConfig(writeOptions);

        try {
            if (StringUtils.isBlank(inPartitionKeys)) {
                logger.logInfo("Saving data into Non-Partitioned table - " + inCatalogName + "." + inDatabaseName + "." + inTableName);

                if (!tableExists) {
                    // Create table if it doesn't exist
                    logger.logInfo("Table does not exist. Creating Table - " + inCatalogName + "." + inDatabaseName + "." + inTableName);

                    DF.writeTo(inCatalogName + "." + inDatabaseName + "." + inTableName)
                            .options(extraOptions)
                            .tableProperty("write.format.default", fileSubFormat)
                            .tableProperty("write." + fileSubFormat + ".compression-codec", compressionFormat)
                            .tableProperty("location", inOutputPath)
                            .using(ICEBERG_FORMAT)
                            .createOrReplace();
                } else {
                    // Write to an existing table
                    logger.logInfo("Writing data into existing Table - " + inCatalogName + "." + inDatabaseName + "." + inTableName);

                    if ("append".equalsIgnoreCase(inWriteMode)) {
                        DF.writeTo(inCatalogName + "." + inDatabaseName + "." + inTableName)
                                .options(extraOptions)
                                .append();
                    } else {
                        DF.writeTo(inCatalogName + "." + inDatabaseName + "." + inTableName)
                                .options(extraOptions)
                                .overwritePartitions();
                    }
                }
            } else {
                // Handle partitioned table logic if required
                //TODO : Logic to handle partition columns
                logger.logInfo("Partitioned table writing logic is not implemented in this method.");
                throw new UnsupportedOperationException("Partitioned table writing is not implemented yet.");
            }
        } catch (Exception e) {
            logger.logError("Error while writing data into Table - " + inCatalogName + "." + inDatabaseName + "." + inTableName, e);
            throw new RuntimeException("Failed to write data to table", e);
        }
    }

}




