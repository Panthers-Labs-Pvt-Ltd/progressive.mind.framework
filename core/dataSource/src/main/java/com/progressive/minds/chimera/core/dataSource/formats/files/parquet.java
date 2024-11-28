package com.progressive.minds.chimera.core.dataSource.formats.files;

import com.progressive.minds.chimera.core.logger.ChimeraLogger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;

public class parquet {
    private static final String loggerTagName = "Parquet File ";
    private static final ChimeraLogger logger = new ChimeraLogger(parquet.class);

    public static Dataset<Row> read(SparkSession inSparkSession, String inPipelineName,
                                    String inSourcePath, String inColumnFilter, String inRowFilter,
                                    String inCustomConfig, Integer Limit) {

        logger.logInfo(loggerTagName, "Initiated Parquet File Reading For Pipeline :" + inPipelineName);

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
            logger.logError(loggerTagName, " Parquet File Reading For Pipeline :" + inPipelineName
                    + " Failed With Error " + e.getMessage());
        }
        return dataFrame;
    }
}