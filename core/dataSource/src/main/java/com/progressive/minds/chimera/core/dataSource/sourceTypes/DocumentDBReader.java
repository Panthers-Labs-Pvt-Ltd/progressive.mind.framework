package com.progressive.minds.chimera.core.dataSource.sourceTypes;

import com.progressive.minds.chimera.core.dataSource.formats.files.parquet;
import com.progressive.minds.chimera.core.dataSource.formats.nosql.nosql;
import com.progressive.minds.chimera.core.dataSource.modal.DataReader;
import com.progressive.minds.chimera.foundational.logger.logger.ChimeraLogger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

import java.util.Locale;

public class DocumentDBReader implements DataReader.NOSQL {

    private final ChimeraLogger logger = new ChimeraLogger(this.getClass());
    private String loggerTagName = "File Reader";

    /**
     *
     * @param inSourceType
     * @param inSparkSession
     * @param inUrl
     * @param inCollectionNm
     * @param inCustomConf
     * @return
     * @throws Exception
     */
    @Override
    public Dataset<Row> read(String inSourceType, SparkSession inSparkSession, String inUrl,
                                          String inCollectionNm, String inCustomConf) throws Exception {

        logger.logInfo(loggerTagName, "Initiating Reading....");
        Dataset<Row> dataFrame = inSparkSession.emptyDataFrame();

        String SourceType = inSourceType.toLowerCase(Locale.ROOT);
        if (SourceType.equals("mongo")) {
            dataFrame = nosql.read( inSourceType,  inSparkSession,  inUrl,
                     inCollectionNm,  inCustomConf);
        }
        else {
            logger.logError(loggerTagName, "Unsupported Format " + inSourceType);
            System.exit(1);
        }
        return dataFrame;
    }
}