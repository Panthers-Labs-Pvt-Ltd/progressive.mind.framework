package com.progressive.minds.chimera.core.data_source.formats.nosql;

import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import static com.progressive.minds.chimera.core.data_source.utility.commonFunctions.*;

public class nosql {

    private static final String loggerTag = "Document DB ";
    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(nosql.class);
    private static final String DEFAULT_COMPRESSION_FORMAT = "zlib,snappy";
    private static final String defaultConf = "[{\"Key\":\"partitioner\",\"value\":\"MongoPaginateBySizePartitioner\"}]";
    private static final String defaultWriteConf = "[{\"Key\":\"maxBatchSize\",\"Value\":\"512\"}]";

    public static Dataset<Row> read(String inSourceType, SparkSession inSparkSession, String inUrl,
                                    String inCollectionNm, String inCustomConf) throws Exception {

        String inSourceTyp = capitalize(inSourceType);
        //Read Other Options -  partitionKey pipeline

        try {
            logger.logInfo(inSourceTyp + " - Read Options: " + inCustomConf);
            String readOptions = isNullOrBlank(inCustomConf) ? defaultConf : inCustomConf;
            Map<String, String> extraOptions = getConfig(readOptions);

            String numPartitions = extraOptions.getOrDefault("numberOfPartitions", "4");
            String fetchSize = extraOptions.getOrDefault("maxBatchSize", "5000");

            return inSparkSession.read()
                    .format("mongodb")
                    .option("database", inUrl)
                    .option("collection", inCollectionNm)
                    .option("schema.sampleSize", fetchSize)
                    .option("numberOfPartitions", numPartitions)
                    .option("spark.mongodb.read.readConcern.level", "majority")
                    .options(extraOptions)
                    .load();

        } catch (Exception e) {
            logger.logError(inSourceTyp + " Data Source - Error executing While reading from MonogDB: " + inCollectionNm, e);
            throw new Exception("DataSourceException.SQLException");
        }
    }

    public static Dataset<Row> write(
            SparkSession inSparkSession,
            String inPipelineName,
            String inDatabaseName,
            String inCollectionNm,
            Dataset<Row> inSourceDataFrame,
            String inCompressionFormat,
            String inSaveMode,
            String inPartitioningKeys,
            String inSortingKeys,
            String inDuplicationKeys,
            String inExtraColumns,
            String inExtraColumnsValues,
            String inCustomConf
    ) throws Exception {

        String COMPRESSION_FORMAT = (inCompressionFormat != null && !inCompressionFormat.isBlank())
                ? inCompressionFormat.toLowerCase(Locale.ROOT)
                : DEFAULT_COMPRESSION_FORMAT;

        logger.logInfo(loggerTag + String.format(
                " - Pipeline Name: %s, Compression format: %s, Write mode: %s",
                inPipelineName, inCompressionFormat, inSaveMode
        ));
        String writeOptions = isNullOrBlank(inCustomConf) ? defaultWriteConf : inCustomConf;
        Map<String, String> extraOptions = getConfig(writeOptions);

        String numPartitions = extraOptions.getOrDefault("numPartitions", "4");
        String batchSize = extraOptions.getOrDefault("batchsize", "5000");

        Dataset<Row> tableDataFrame = inSourceDataFrame;
        boolean status = false;

        try {
            if (inExtraColumns != null && !inExtraColumns.isEmpty()) {
                logger.logInfo("Appending Extra Columns and Values to DataFrame");
                tableDataFrame = mergeColumnsToDataFrame(tableDataFrame, inExtraColumns, inExtraColumnsValues);
                logger.logInfo("Extra Columns and Values have been mapped to DataFrame");
            }

            if (inSortingKeys != null && !inSortingKeys.isBlank()) {
                logger.logInfo("Executing Sorting Process on DataFrame");
                tableDataFrame = sortDataFrame(tableDataFrame, inSortingKeys, true);
                logger.logInfo("Sorting on DataFrame Executed Successfully");
            }

            if (inDuplicationKeys != null && !inDuplicationKeys.isBlank()) {
                logger.logInfo("Executing Deduplication Process on DataFrame");
                logger.logInfo("Source records count (Before Deduplication): " + tableDataFrame.count());
                tableDataFrame = DropDuplicatesOnKey(inDuplicationKeys, tableDataFrame);
                logger.logInfo("Target records count (After Deduplication): " + tableDataFrame.count());
                logger.logInfo("Deduplication on DataFrame Executed Successfully");
            }

            if (inPartitioningKeys == null || inPartitioningKeys.isEmpty()) {
                logger.logInfo("Saving data into Collection " + inCollectionNm);

                tableDataFrame.write()
                        .format("mongodb")
                        .option("database", inDatabaseName)
                        .option("collection", inCollectionNm)
                        .option("batchsize", batchSize)
                        .options(extraOptions)
                        .mode(inSaveMode)
                        .save();
                status = true;
            } else {
                String[] partitioningKeys = inPartitioningKeys.replace("\"", "").split(",");
                tableDataFrame = renamePartitionKeysCase(tableDataFrame, inPartitioningKeys);
                String nonNullBlankColumns = Arrays.toString(isPartitionKeysNull(tableDataFrame, partitioningKeys));

                if (!nonNullBlankColumns.isEmpty()) {
                    throw new Exception("DataQualityException.PARTITION_NULL_CHECK");
                }

                logger.logInfo("Creating Collection " + inCollectionNm + " and writing data");
                tableDataFrame.write()
                        .format("mongodb")
                        .option("database", inDatabaseName)
                        .option("collection", inCollectionNm)
                        .option("numPartitions", numPartitions)
                        .option("batchsize", batchSize)
                        .options(extraOptions)
                        .mode(inSaveMode)
                        .save();

                logger.logInfo("Data Writing Process Completed for " + inCollectionNm + " with Status: " + status);

            }
        } catch (Exception e) {
            logger.logError("NoSQL ::" + e);
            throw new Exception("DataSourceException.DataSourceWrite");
        }

        return tableDataFrame;
    }
}