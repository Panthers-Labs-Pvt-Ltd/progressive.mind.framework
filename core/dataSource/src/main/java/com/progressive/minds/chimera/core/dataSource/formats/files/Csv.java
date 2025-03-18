package com.progressive.minds.chimera.core.dataSource.formats.files;

import static com.progressive.minds.chimera.core.dataSource.utility.commonFunctions.*;

import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class Csv {
    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(Csv.class);
    private static final String DEFAULT_COMPRESSION_FORMAT = "gzip";

    /*
     * @param inSparkSession
     * @param inPipelineName
     * @param inSourcePath
     * @param inColumnFilter
     * @param inRowFilter
     * @param inCustomConfig
     * @param Limit
     * @return
     */
    public static Dataset<Row> read(SparkSession sparkSession, String pipelineName,
                                    String sourcePath, String columnFilter, String rowFilter,
                                    String customConfig, Integer limit) {

        logger.logInfo("Initiated CSV File Reading for Pipeline: " + pipelineName);

        Dataset<Row> dataFrame = sparkSession.emptyDataFrame();

        try {
            DataFrameReader reader = sparkSession.read().format("csv");

            // Handle custom options
            if (StringUtils.isNotEmpty(customConfig)) {
                String[] options = customConfig.split(",");
                for (String option : options) {
                    String[] keyValue = option.split("=");
                    if (keyValue.length == 2) {
                        reader.option(keyValue[0].trim(), keyValue[1].trim());
                    }
                }
            }

            dataFrame = reader.load(sourcePath);

            // Apply column selection
            if (StringUtils.isNotEmpty(columnFilter)) {
                String[] columnArray = columnFilter.split(",");
                if (columnArray.length > 0) {
                    dataFrame = dataFrame.select(columnArray[0], Arrays.copyOfRange(columnArray, 1, columnArray.length));
                }
            }

            // Apply row filter
            if (StringUtils.isNotEmpty(rowFilter)) {
                dataFrame = dataFrame.where(rowFilter);
            }

            // Apply limit
            if (limit != null && limit > 0) {
                dataFrame = dataFrame.limit(limit);
            }

        } catch (Exception e) {
            logger.logError("CSV File Reading for Pipeline: " + pipelineName + " failed.", e);
        }

        return dataFrame;
    }


    /*
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
            SparkSession sparkSession,
            String pipelineName,
            String databaseName,
            String tableName,
            Dataset<Row> sourceDataFrame,
            String outputPath,
            String compressionFormat,
            String savingMode,
            String partitioningKeys,
            String sortingKeys,
            String duplicationKeys,
            String extraColumns,
            String extraColumnsValues,
            String customConfig
    ) throws DataSourceWriteException {

        String resolvedCompressionFormat = getCompressionFormat(compressionFormat);
        String fullTableName = databaseName + "." + tableName;
        boolean isPartitioned = StringUtils.isNotBlank(partitioningKeys);
        boolean tableExists = sparkSession.catalog().tableExists(fullTableName);

        logger.logInfo(String.format("Pipeline: %s, Table: %s, Compression: %s, Write Mode: %s",
                pipelineName, fullTableName, resolvedCompressionFormat, savingMode));

        try {
            Dataset<Row> processedDataFrame = sourceDataFrame;
            processedDataFrame = processExtraColumns(processedDataFrame, extraColumns, extraColumnsValues);
            processedDataFrame = processSorting(processedDataFrame, sortingKeys);
            processedDataFrame = processDeduplication(processedDataFrame, duplicationKeys);

            if (isPartitioned) {
                processedDataFrame = savePartitionedTable(sparkSession, outputPath, savingMode, partitioningKeys, processedDataFrame, fullTableName, tableExists);
            } else {
                saveNonPartitionedTable(sparkSession, outputPath, savingMode, processedDataFrame, fullTableName, resolvedCompressionFormat, tableExists);
            }

            logger.logInfo("Data successfully written to " + fullTableName);
            return processedDataFrame;

        } catch (Exception e) {
            logger.logError("Data Write Failure: " + e.getMessage());
            throw new DataSourceWriteException("Data writing failed for table: " + fullTableName, e);
        }
    }

    private static @NotNull Dataset<Row> savePartitionedTable(SparkSession sparkSession, String outputPath, String savingMode, String partitioningKeys, Dataset<Row> tableDataFrame, String fullTableName, boolean tableExists) throws DataSourceWriteException {
        List<String> partitionKeysList = Arrays.asList(partitioningKeys.replace("\"", "").split(","));
        tableDataFrame = renamePartitionKeysCase(tableDataFrame, partitioningKeys);
        String[] nullOrEmptyColumns = isPartitionKeysNull(tableDataFrame, partitionKeysList.toArray(new String[0]));
        if (nullOrEmptyColumns.length > 0) {
            String nonNullBlankColumns = Arrays.toString(nullOrEmptyColumns);
            throw new DataSourceWriteException("Partition keys contain NULL or empty values: " + nonNullBlankColumns);
        }

        logger.logInfo((tableExists ? "Appending data to" : "Creating and writing data into") + " partitioned table: " + fullTableName);
        saveDataFrame(tableDataFrame, outputPath, savingMode, fullTableName, partitionKeysList, tableExists);
        return tableDataFrame;
    }

    private static void saveNonPartitionedTable(SparkSession sparkSession, String outputPath, String savingMode, Dataset<Row> tableDataFrame, String fullTableName, String compressionFormat, boolean tableExists) {
        logger.logInfo((tableExists ? "Appending data to" : "Creating and writing data into") + " non-partitioned table: " + fullTableName);
        saveDataFrame(tableDataFrame, outputPath, savingMode, fullTableName, null, tableExists, compressionFormat);
    }

    private static void saveDataFrame(Dataset<Row> dataFrame, String outputPath, String savingMode, String tableName, List<String> partitioningKeys, boolean tableExists) {
        saveDataFrame(dataFrame, outputPath, savingMode, tableName, partitioningKeys, tableExists, null);
    }

    private static void saveDataFrame(Dataset<Row> dataFrame, String outputPath, String savingMode, String tableName, List<String> partitioningKeys, boolean tableExists, String compressionFormat) {
        DataFrameWriter<Row> writer = dataFrame.write()
                .mode(SaveMode.valueOf(savingMode))
                .option("path", outputPath);

        if (compressionFormat != null) {
            writer.option("compression", compressionFormat);
        }

        if (partitioningKeys != null) {
            writer.partitionBy(partitioningKeys.toArray(new String[0]));
        }

        if (tableExists) {
            writer.csv(outputPath);
        } else {
            writer.format("csv").saveAsTable(tableName);
        }
    }

    private static Dataset<Row> processDeduplication(Dataset<Row> dataFrame, String duplicationKeys) {
        if (StringUtils.isNotBlank(duplicationKeys)) {
            logger.logInfo("Executing Deduplication");
            long beforeCount = dataFrame.count();
            dataFrame = DropDuplicatesOnKey(duplicationKeys, dataFrame);
            logger.logInfo(String.format("Deduplication completed. Before: %d, After: %d", beforeCount, dataFrame.count()));
        }
        return dataFrame;
    }

    private static Dataset<Row> processSorting(Dataset<Row> dataFrame, String sortingKeys) {
        if (StringUtils.isNotBlank(sortingKeys)) {
            logger.logInfo("Executing Sorting");
            return sortDataFrame(dataFrame, sortingKeys, true);
        }
        return dataFrame;
    }

    private static Dataset<Row> processExtraColumns(Dataset<Row> dataFrame, String extraColumns, String extraColumnsValues) {
        if (StringUtils.isNotBlank(extraColumns)) {
            logger.logInfo("Appending Extra Columns");
            return mergeColumnsToDataFrame(dataFrame, extraColumns, extraColumnsValues);
        }
        return dataFrame;
    }

    private static String getCompressionFormat(String compressionFormat) {
        return Optional.ofNullable(compressionFormat)
                .filter(StringUtils::isNotBlank)
                .map(format -> format.toLowerCase(Locale.ROOT))
                .orElse(DEFAULT_COMPRESSION_FORMAT);
    }

    public static class DataSourceWriteException extends Exception {
        public DataSourceWriteException(String message, Throwable cause) {
            super(message, cause);
        }

        public DataSourceWriteException(String message) {
            super(message);
        }
    }
}