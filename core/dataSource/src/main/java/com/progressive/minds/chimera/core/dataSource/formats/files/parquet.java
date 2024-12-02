package com.progressive.minds.chimera.core.dataSource.formats.files;

import static com.progressive.minds.chimera.core.dataSource.utility.commonFunctions.*;
import com.progressive.minds.chimera.foundational.logger.logger.ChimeraLogger;
import org.apache.spark.sql.*;
import java.util.Arrays;
import java.util.Locale;

public class parquet {
    private static final String loggerTag = "Parquet File ";
    private static final ChimeraLogger logger = new ChimeraLogger(parquet.class);
    private static final String DEFAULT_COMPRESSION_FORMAT = "snappy";

    /**
     *
     * @param inSparkSession
     * @param inPipelineName
     * @param inSourcePath
     * @param inColumnFilter
     * @param inRowFilter
     * @param inCustomConfig
     * @param Limit
     * @return
     */
    public  static Dataset<Row> read(SparkSession inSparkSession, String inPipelineName,
                                    String inSourcePath, String inColumnFilter, String inRowFilter,
                                    String inCustomConfig, Integer Limit) {

        logger.logInfo(loggerTag, "Initiated Parquet File Reading For Pipeline :" + inPipelineName);

        String[] columnArray = inColumnFilter != null && !inColumnFilter.isEmpty() ?
                inColumnFilter.split(",") : new String[0];

        Dataset<Row> dataFrame = inSparkSession.emptyDataFrame();

        try {
            if (columnArray.length == 0 && inRowFilter.isEmpty() && Limit == 0) {
                dataFrame = inSparkSession.read().parquet(inSourcePath);
            } else if (inRowFilter.isEmpty() && Limit == 0) {
                dataFrame = inSparkSession.read()
                        .parquet(inSourcePath)
                        .select(columnArray[0], Arrays.copyOfRange(columnArray, 1, columnArray.length));

            } else if (!inRowFilter.isEmpty() && Limit == 0) {
                dataFrame = inSparkSession.read()
                        .parquet(inSourcePath)
                        .select(columnArray[0], Arrays.copyOfRange(columnArray, 1, columnArray.length))
                        .where(inRowFilter);
            } else {
                dataFrame = inSparkSession.read().parquet(inSourcePath)
                        .select(columnArray[0], Arrays.copyOfRange(columnArray, 1, columnArray.length))
                        .where(inRowFilter).limit(Limit);
            }
        } catch (Exception e) {
            logger.logError(loggerTag, " Parquet File Reading For Pipeline :" + inPipelineName
                    + " Failed With Error " + e.getMessage());
        }
        return dataFrame;
    }

    /**
     *
     * @param inSparkSession
     * @param inPipelineName
     * @param inDatabaseName
     * @param inTableName
     * @param inSourceDataFrame
     * @param inOutputPath
     * @param inCompressionFormat
     * @param inSavingMode
     * @param inPartitioningKeys
     * @param inSortingKeys
     * @param inDuplicationKeys
     * @param inExtraColumns
     * @param inExtraColumnsValues
     * @param inCustomConfig
     * @return
     * @throws Exception
     */
    public static Dataset<Row> write(
            SparkSession inSparkSession,
            String inPipelineName,
            String inDatabaseName,
            String inTableName,
            Dataset<Row> inSourceDataFrame,
            String inOutputPath,
            String inCompressionFormat,
            String inSavingMode,
            String inPartitioningKeys,
            String inSortingKeys,
            String inDuplicationKeys,
            String inExtraColumns,
            String inExtraColumnsValues,
            String inCustomConfig
    ) throws Exception {

        String COMPRESSION_FORMAT = (inCompressionFormat != null && !inCompressionFormat.isBlank())
                ? inCompressionFormat.toLowerCase(Locale.ROOT)
                : DEFAULT_COMPRESSION_FORMAT;

        logger.logInfo(loggerTag, String.format(
                "Pipeline Name: %s, Compression format: %s, Write mode: %s",
                inPipelineName, inCompressionFormat, inSavingMode
        ));

        Dataset<Row> tableDataFrame = inSourceDataFrame;
        boolean status = false;
        String tableAndSchemaName = inDatabaseName + "." + inTableName;

        try {
            if (inExtraColumns != null && !inExtraColumns.isEmpty()) {
                logger.logInfo(loggerTag, "Appending Extra Columns and Values to DataFrame");
                tableDataFrame = mergeColumnsToDataFrame(tableDataFrame, inExtraColumns, inExtraColumnsValues);
                logger.logInfo(loggerTag, "Extra Columns and Values have been mapped to DataFrame");
            }

            if (inSortingKeys != null && !inSortingKeys.isBlank()) {
                logger.logInfo(loggerTag, "Executing Sorting Process on DataFrame");
                tableDataFrame = sortDataFrame(tableDataFrame, inSortingKeys,true);
                logger.logInfo(loggerTag, "Sorting on DataFrame Executed Successfully");
            }

            if (inDuplicationKeys != null && !inDuplicationKeys.isBlank()) {
                logger.logInfo(loggerTag, "Executing Deduplication Process on DataFrame");
                logger.logInfo(loggerTag, "Source records count (Before Deduplication): " + tableDataFrame.count());
                tableDataFrame = DropDuplicatesOnKey(inDuplicationKeys, tableDataFrame);
                logger.logInfo(loggerTag, "Target records count (After Deduplication): " + tableDataFrame.count());
                logger.logInfo(loggerTag, "Deduplication on DataFrame Executed Successfully");
            }

            if (inPartitioningKeys == null || inPartitioningKeys.isEmpty()) {
                logger.logInfo(loggerTag, "Saving data into Non-Partitioned Table " + tableAndSchemaName);

                if (!inSparkSession.catalog().tableExists(tableAndSchemaName)) {
                    logger.logInfo(loggerTag, "Creating table " + tableAndSchemaName + " and writing data");
                    tableDataFrame.write()
                            .format("parquet")
                            .option("path", inOutputPath)
                            .mode(SaveMode.Append)
                            .saveAsTable(tableAndSchemaName);
                } else {
                    logger.logInfo(loggerTag, "Writing data into existing Table " + tableAndSchemaName);
                    tableDataFrame.write()
                            .mode(SaveMode.valueOf(inSavingMode))
                            .option("compression", COMPRESSION_FORMAT)
                            .parquet(inOutputPath);
                }
                status = true;
            } else {
                String[] partitioningKeys = inPartitioningKeys.replace("\"", "").split(",");
                tableDataFrame = renamePartitionKeysCase(tableDataFrame, inPartitioningKeys);
                String nonNullBlankColumns = Arrays.toString(isPartitionKeysNull(tableDataFrame, partitioningKeys));
                 if (!nonNullBlankColumns.isEmpty()) {
                    throw new Exception("EDLDataQualityException.PARTITION_NULL_CHECK");
                }

                if (!inSparkSession.catalog().tableExists(tableAndSchemaName)) {
                    logger.logInfo(loggerTag, "Creating table " + tableAndSchemaName + " and writing data");
                    tableDataFrame.write()
                            .format("parquet")
                            .option("path", inOutputPath)
                            .mode(SaveMode.Append)
                            .partitionBy(partitioningKeys)
                            .saveAsTable(tableAndSchemaName);
                } else {
                    logger.logInfo(loggerTag, "Writing data into existing Table " + tableAndSchemaName);
                    tableDataFrame.write()
                            .mode(SaveMode.valueOf(inSavingMode))
                            .partitionBy(partitioningKeys)
                            .parquet(inOutputPath);
                }
            }

            logger.logInfo(loggerTag, "Data Writing Process Completed for " + tableAndSchemaName + " with Status: " + status);

        } catch (Exception e) {
            logger.logError(loggerTag, "::" + e);
            throw new Exception("DataSourceException.DataSourceWrite");
        }

        return tableDataFrame;
    }

    }