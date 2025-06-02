package com.progressive.minds.chimera.core.data_source.formats.files;

import static com.progressive.minds.chimera.core.data_source.utility.commonFunctions.*;

import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.*;

public class Csv {
    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(Csv.class);
    private static final String DEFAULT_COMPRESSION_FORMAT = null;

    /*
     * @param inSparkSession
     * @param inPipelineName
     * @param inSourcePath
     * @param inColumnFilter
     * @param inRowFilter
     * @param inCustomConfig
     * @param Limit
     * @param schemaPath
     * @return
     */
    public static Dataset<Row> read(SparkSession sparkSession, String pipelineName,
                                    String sourcePath, String columnFilter, String rowFilter,
                                    String customConfig, Integer limit, String schemaPath) {

        logger.logInfo("Initiated CSV File Reading for Pipeline: " + pipelineName);

        Dataset<Row> dataFrame = sparkSession.emptyDataFrame();

        try {
            DataFrameReader reader = sparkSession.read().format("csv");
            applyReaderOptions(customConfig, reader);
            applyOrInferSchema(schemaPath, reader);
            dataFrame = reader.load(sourcePath);
            dataFrame = filterColumns(columnFilter, dataFrame);
            dataFrame = filterRows(rowFilter, dataFrame);
            dataFrame = applyLimit(limit, dataFrame);
        } catch (Exception e) {
            logger.logError("CSV File Reading for Pipeline: " + pipelineName + " failed.", e);
            throw new RuntimeException("Failed to read CSV file", e); // Rethrow or handle as needed
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

        String resolvedCompressionFormat = getCompressionFormat(compressionFormat, DEFAULT_COMPRESSION_FORMAT);
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
                savePartitionedTable(outputPath, "csv", savingMode, partitioningKeys, processedDataFrame, fullTableName, tableExists);
            } else {
                saveNonPartitionedTable(outputPath, "csv", savingMode, processedDataFrame, fullTableName, resolvedCompressionFormat, tableExists);
            }

            logger.logInfo("Data successfully written to " + fullTableName);
            return processedDataFrame;

        } catch (Exception e) {
            logger.logError("Data Write Failure: " + e.getMessage());
            throw new DataSourceWriteException("Data writing failed for table: " + fullTableName, e);
        }
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