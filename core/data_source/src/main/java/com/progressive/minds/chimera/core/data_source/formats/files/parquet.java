package com.progressive.minds.chimera.core.data_source.formats.files;

import static com.progressive.minds.chimera.core.data_source.utility.commonFunctions.*;

import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.apache.spark.sql.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;

public class parquet {
    private static final String loggerTag = "Parquet File ";
    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(parquet.class);
    private static final String DEFAULT_COMPRESSION_FORMAT = "snappy";

    /**
     * @param inSparkSession
     * @param inPipelineName
     * @param inSourcePath
     * @param inColumnFilter
     * @param inRowFilter
     * @param inCustomConfig
     * @param Limit
     * @return
     */
    public static Dataset<Row> read(SparkSession inSparkSession, String inPipelineName,
                                    String inSourcePath, String inColumnFilter, String inRowFilter,
                                    String inCustomConfig, Integer Limit) {

        logger.logInfo("Initiated Parquet File Reading For Pipeline :" + inPipelineName);

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
            logger.logError(" Parquet File Reading For Pipeline :" + inPipelineName
                    + " Failed With Error " + e.getMessage());
        }
        return dataFrame;
    }

    /**
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

        String COMPRESSION_FORMAT = getCompressionFormat(inCompressionFormat);
        logger.logInfo(String.format("Pipeline Name: %s, Compression format: %s, Write mode: %s",
                inPipelineName, inCompressionFormat, inSavingMode));

        Dataset<Row> tableDataFrame = inSourceDataFrame;
        boolean status = false;
        String tableAndSchemaName = inDatabaseName + "." + inTableName;

        try {
            tableDataFrame = processExtraColumns(tableDataFrame, inExtraColumns, inExtraColumnsValues);
            tableDataFrame = processSorting(tableDataFrame, inSortingKeys);
            tableDataFrame = processDeduplication(tableDataFrame, inDuplicationKeys);

            if (inPartitioningKeys == null || inPartitioningKeys.isEmpty()) {
                status = saveNonPartitionedTable(inSparkSession, inOutputPath, inSavingMode, tableAndSchemaName, tableDataFrame, COMPRESSION_FORMAT);
            } else {
                tableDataFrame = savePartitionedTable(inSparkSession, inOutputPath, inSavingMode, inPartitioningKeys, tableDataFrame, tableAndSchemaName);
            }
            logger.logInfo("Data Writing Process Completed for " + tableAndSchemaName + " with Status: " + status);

        } catch (Exception e) {
            logger.logError("::" + e);
            throw new Exception("DataSourceException.DataSourceWrite");
        }

        return tableDataFrame;
    }

    private static @NotNull Dataset<Row> savePartitionedTable(SparkSession inSparkSession, String inOutputPath, String inSavingMode, String inPartitioningKeys, Dataset<Row> tableDataFrame, String tableAndSchemaName) throws Exception {
        String[] partitioningKeys = inPartitioningKeys.replace("\"", "").split(",");
        tableDataFrame = renamePartitionKeysCase(tableDataFrame, inPartitioningKeys);
        String nonNullBlankColumns = Arrays.toString(isPartitionKeysNull(tableDataFrame, partitioningKeys));
        if (!nonNullBlankColumns.isEmpty()) {
            throw new Exception("EDLDataQualityException.PARTITION_NULL_CHECK");
        }

        if (!inSparkSession.catalog().tableExists(tableAndSchemaName)) {
            logger.logInfo("Creating table " + tableAndSchemaName + " and writing data");
            tableDataFrame.write()
                    .format("parquet")
                    .option("path", inOutputPath)
                    .mode(SaveMode.Append)
                    .partitionBy(partitioningKeys)
                    .saveAsTable(tableAndSchemaName);
        } else {
            logger.logInfo("Writing data into existing Table " + tableAndSchemaName);
            tableDataFrame.write()
                    .mode(SaveMode.valueOf(inSavingMode))
                    .partitionBy(partitioningKeys)
                    .parquet(inOutputPath);
        }
        return tableDataFrame;
    }

    private static boolean saveNonPartitionedTable(SparkSession inSparkSession, String inOutputPath, String inSavingMode, String tableAndSchemaName, Dataset<Row> tableDataFrame, String COMPRESSION_FORMAT) {
        boolean status;
        logger.logInfo("Saving data into Non-Partitioned Table " + tableAndSchemaName);

        if (!inSparkSession.catalog().tableExists(tableAndSchemaName)) {
            logger.logInfo("Creating table " + tableAndSchemaName + " and writing data");
            tableDataFrame.write()
                    .format("parquet")
                    .option("path", inOutputPath)
                    .mode(SaveMode.Append)
                    .saveAsTable(tableAndSchemaName);
        } else {
            logger.logInfo("Writing data into existing Table " + tableAndSchemaName);
            tableDataFrame.write()
                    .mode(SaveMode.valueOf(inSavingMode))
                    .option("compression", COMPRESSION_FORMAT)
                    .parquet(inOutputPath);
        }
        status = true;
        return status;
    }

    private static Dataset<Row> processDeduplication(Dataset<Row> tableDataFrame, String inDuplicationKeys) {
        if (inDuplicationKeys != null && !inDuplicationKeys.isBlank()) {
            logger.logInfo("Executing Deduplication Process on DataFrame");
            logger.logInfo("Source records count (Before Deduplication): " + tableDataFrame.count());
            tableDataFrame = DropDuplicatesOnKey(inDuplicationKeys, tableDataFrame);
            logger.logInfo("Target records count (After Deduplication): " + tableDataFrame.count());
            logger.logInfo("Deduplication on DataFrame Executed Successfully");
        }
        return tableDataFrame;
    }

    private static Dataset<Row> processSorting(Dataset<Row> tableDataFrame, String inSortingKeys) {
        if (inSortingKeys != null && !inSortingKeys.isBlank()) {
            logger.logInfo("Executing Sorting Process on DataFrame");
            tableDataFrame = sortDataFrame(tableDataFrame, inSortingKeys, true);
            logger.logInfo("Sorting on DataFrame Executed Successfully");
        }
        return tableDataFrame;
    }

    private static Dataset<Row> processExtraColumns(Dataset<Row> tableDataFrame, String inExtraColumns, String inExtraColumnsValues) {
        if (inExtraColumns != null && !inExtraColumns.isEmpty()) {
            logger.logInfo("Appending Extra Columns and Values to DataFrame");
            tableDataFrame = mergeColumnsToDataFrame(tableDataFrame, inExtraColumns, inExtraColumnsValues);
            logger.logInfo("Extra Columns and Values have been mapped to DataFrame");
        }
        return tableDataFrame;
    }

    private static String getCompressionFormat(String inCompressionFormat) {
        return (inCompressionFormat != null && !inCompressionFormat.isBlank())
                ? inCompressionFormat.toLowerCase(Locale.ROOT)
                : DEFAULT_COMPRESSION_FORMAT;
    }

}