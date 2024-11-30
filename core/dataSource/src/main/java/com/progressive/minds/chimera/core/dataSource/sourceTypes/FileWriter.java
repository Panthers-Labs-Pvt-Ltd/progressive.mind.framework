package com.progressive.minds.chimera.core.dataSource.sourceTypes;

import com.progressive.minds.chimera.core.dataSource.formats.files.parquet;
import com.progressive.minds.chimera.core.dataSource.modal.DataWriter;
import com.progressive.minds.chimera.core.logger.ChimeraLogger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Locale;

public class FileWriter  implements DataWriter.Files{

    private final ChimeraLogger logger = new ChimeraLogger(this.getClass());
    private String loggerTagName = "File Writer";
    /**
     * @param inSourceType
     * @param inSparkSession
     * @param inPipelineName
     * @param inDatabaseName
     * @param inTableName
     * @param inSourceDataFrame
     * @param inOutputPath
     * @param inSavingMode
     * @param inPartitioningKeys
     * @param inSortingKeys
     * @param inDuplicationKeys
     * @param inExtraColumns
     * @param inExtraColumnsValues
     * @param inCustomConfig
     * @param inCompressionFormat
     * @return
     */
    @Override
    public Dataset<Row> write(String inSourceType, SparkSession inSparkSession, String inPipelineName,
                              String inDatabaseName, String inTableName, Dataset<Row> inSourceDataFrame,
                              String inOutputPath, String inSavingMode, String inPartitioningKeys,
                              String inSortingKeys, String inDuplicationKeys, String inExtraColumns,
                              String inExtraColumnsValues, String inCustomConfig, String inCompressionFormat) throws Exception {
        logger.logInfo(loggerTagName, "Initiating Writing....");
        Dataset<Row> dataFrame = inSparkSession.emptyDataFrame();
        String SourceType = inSourceType.toLowerCase(Locale.ROOT);
        if (SourceType.equals("parquet")) {
            dataFrame = parquet.write(inSparkSession, inPipelineName,inDatabaseName,inTableName,inSourceDataFrame,
                    inOutputPath,inCompressionFormat,inSavingMode,inPartitioningKeys,inSortingKeys,inDuplicationKeys,
                    inExtraColumns,inExtraColumnsValues,inCustomConfig) ;
        }
        else if (SourceType.equals("json")) {
            dataFrame = parquet.write(inSparkSession, inPipelineName,inDatabaseName,inTableName,inSourceDataFrame,
                    inOutputPath,inCompressionFormat,inSavingMode,inPartitioningKeys,inSortingKeys,inDuplicationKeys,
                    inExtraColumns,inExtraColumnsValues,inCustomConfig) ;
        }
        else if (SourceType.equals("csv")) {
            dataFrame = parquet.write(inSparkSession, inPipelineName,inDatabaseName,inTableName,inSourceDataFrame,
                    inOutputPath,inCompressionFormat,inSavingMode,inPartitioningKeys,inSortingKeys,inDuplicationKeys,
                    inExtraColumns,inExtraColumnsValues,inCustomConfig) ;
        }
        else if (SourceType.equals("txt")) {
            dataFrame = parquet.write(inSparkSession, inPipelineName,inDatabaseName,inTableName,inSourceDataFrame,
                    inOutputPath,inCompressionFormat,inSavingMode,inPartitioningKeys,inSortingKeys,inDuplicationKeys,
                    inExtraColumns,inExtraColumnsValues,inCustomConfig) ;
        }
        else if (SourceType.equals("avro")) {
            dataFrame = parquet.write(inSparkSession, inPipelineName,inDatabaseName,inTableName,inSourceDataFrame,
                    inOutputPath,inCompressionFormat,inSavingMode,inPartitioningKeys,inSortingKeys,inDuplicationKeys,
                    inExtraColumns,inExtraColumnsValues,inCustomConfig) ;
        }
        else if (SourceType.equals("xml")) {
            dataFrame = parquet.write(inSparkSession, inPipelineName,inDatabaseName,inTableName,inSourceDataFrame,
                    inOutputPath,inCompressionFormat,inSavingMode,inPartitioningKeys,inSortingKeys,inDuplicationKeys,
                    inExtraColumns,inExtraColumnsValues,inCustomConfig) ;
        }
        else if (SourceType.equals("sequence")) {
            dataFrame = parquet.write(inSparkSession, inPipelineName,inDatabaseName,inTableName,inSourceDataFrame,
                    inOutputPath,inCompressionFormat,inSavingMode,inPartitioningKeys,inSortingKeys,inDuplicationKeys,
                    inExtraColumns,inExtraColumnsValues,inCustomConfig) ;
        }
        else if (SourceType.equals("binaryFile")) {
            dataFrame = parquet.write(inSparkSession, inPipelineName,inDatabaseName,inTableName,inSourceDataFrame,
                    inOutputPath,inCompressionFormat,inSavingMode,inPartitioningKeys,inSortingKeys,inDuplicationKeys,
                    inExtraColumns,inExtraColumnsValues,inCustomConfig) ;
        }
        else if (SourceType.equals("image")) {
            dataFrame = parquet.write(inSparkSession, inPipelineName,inDatabaseName,inTableName,inSourceDataFrame,
                    inOutputPath,inCompressionFormat,inSavingMode,inPartitioningKeys,inSortingKeys,inDuplicationKeys,
                    inExtraColumns,inExtraColumnsValues,inCustomConfig) ;
        }
        else {
            logger.logError(loggerTagName, "Unsupported Format " + inSourceType);
            System.exit(1);
        }
    return dataFrame;
    }
}
