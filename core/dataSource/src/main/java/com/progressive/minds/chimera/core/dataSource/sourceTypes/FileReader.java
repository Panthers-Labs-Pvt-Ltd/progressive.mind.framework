package com.progressive.minds.chimera.core.dataSource.sourceTypes;
import java.util.Locale;
import com.progressive.minds.chimera.core.dataSource.modal.data.DataReader;
import com.progressive.minds.chimera.core.dataSource.formats.files.*;
import com.progressive.minds.chimera.core.logger.ChimeraLogger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

public class FileReader implements DataReader.Files {

    private final ChimeraLogger logger = new ChimeraLogger(this.getClass());
    private String loggerTagName = "File Reader";

    /**
     * @param inSourceType
     * @param inSparkSession
     * @param inPipelineName
     * @param inSourcePath
     * @param inColumnFilter
     * @param inRowFilter
     * @param inCustomConfig
     * @param inDelimiter
     * @param inQuotes
     * @param Limit
     * @return
     */
    @Override
    public Dataset<Row> read(String inSourceType, SparkSession inSparkSession, String inPipelineName,
                             String inSourcePath, String inColumnFilter, String inRowFilter,
                             StructType inStructSchema, String inCustomConfig, String inDelimiter,
                             String inQuotes, Integer Limit) {

        logger.logInfo(loggerTagName, "Initiating Reading....");
        Dataset<Row> dataFrame = inSparkSession.emptyDataFrame();

        String SourceType = inSourceType.toLowerCase(Locale.ROOT);
        if (SourceType.equals("parquet")) {
            dataFrame = parquet.read(inSparkSession, inPipelineName,inSourcePath, inColumnFilter,  inRowFilter,
                     inCustomConfig,  Limit);
        }
        else if (SourceType.equals("json")) {
            dataFrame = parquet.read(inSparkSession, inPipelineName,inSourcePath, inColumnFilter,  inRowFilter,
                    inCustomConfig,  Limit);
        }
        else if (SourceType.equals("csv")) {
            dataFrame = parquet.read(inSparkSession, inPipelineName,inSourcePath, inColumnFilter,  inRowFilter,
                    inCustomConfig,  Limit);
        }
        else if (SourceType.equals("txt")) {
            dataFrame = parquet.read(inSparkSession, inPipelineName,inSourcePath, inColumnFilter,  inRowFilter,
                    inCustomConfig,  Limit);
        }
        else if (SourceType.equals("avro")) {
            dataFrame = parquet.read(inSparkSession, inPipelineName,inSourcePath, inColumnFilter,  inRowFilter,
                    inCustomConfig,  Limit);
        }
        else if (SourceType.equals("xml")) {
            dataFrame = parquet.read(inSparkSession, inPipelineName,inSourcePath, inColumnFilter,  inRowFilter,
                    inCustomConfig,  Limit);
        }
        else if (SourceType.equals("sequence")) {
            dataFrame = parquet.read(inSparkSession, inPipelineName,inSourcePath, inColumnFilter,  inRowFilter,
                    inCustomConfig,  Limit);
        }
        else if (SourceType.equals("binaryFile")) {
            dataFrame = parquet.read(inSparkSession, inPipelineName,inSourcePath, inColumnFilter,  inRowFilter,
                    inCustomConfig,  Limit);
        }
        else if (SourceType.equals("image")) {
            dataFrame = parquet.read(inSparkSession, inPipelineName,inSourcePath, inColumnFilter,  inRowFilter,
                    inCustomConfig,  Limit);
        }
        else {
            logger.logError(loggerTagName, "Unsupported Format " + inSourceType);
            System.exit(1);
        }
        return dataFrame;
    }
}/*
    *//**
     * @param inSourceType
     * @param inSparkSession
     * @param inPipelineName
     * @param inDatabaseName
     * @param inTableName
     * @param inSourceDataFrame
     * @param inOutputPath
     * @param inSubFormat
     * @param inSavingMode
     * @param inPartitioningKeys
     * @param inSortingKeys
     * @param inDuplicationKeys
     * @param inExtraColumns
     * @param inExtraColumnsValues
     * @param inCustomConfig
     * @param inCompressionFormat
     * @return
     *//*
         public Dataset<Row> read(String inSourceType, SparkSession inSparkSession, String inPipelineName,
                             String inDatabaseNm, String inTableNm, String inColumnFilter, String inRowFilter,
                             String inCustomConfig)
    @Override
    public Dataset<Row> write(String inSourceType, SparkSession inSparkSession, String inPipelineName,
                              String inDatabaseName, String inTableName, Dataset<Row> inSourceDataFrame,
                              String inOutputPath, String inSubFormat, String inSavingMode, String inPartitioningKeys,
                              String inSortingKeys, String inDuplicationKeys, String inExtraColumns,
                              String inExtraColumnsValues, String inCustomConfig, String inCompressionFormat) {
        return null;
    }
}
*/