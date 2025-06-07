package com.progressive.minds.chimera.core.data_source.sourceTypes;

import com.progressive.minds.chimera.core.data_source.formats.nosql.nosql;
import com.progressive.minds.chimera.core.data_source.modal.DataReader;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Locale;

public class DocumentDBReader implements DataReader.NOSQL {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(DocumentDBReader.class);
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

        logger.logInfo("Initiating Reading....");
        Dataset<Row> dataFrame = inSparkSession.emptyDataFrame();

        String SourceType = inSourceType.toLowerCase(Locale.ROOT);
        if (SourceType.equals("mongo")) {
            dataFrame = nosql.read( inSourceType,  inSparkSession,  inUrl,
                     inCollectionNm,  inCustomConf);
        }
        else {
            logger.logError("Unsupported Format " + inSourceType);
            System.exit(1);
        }
        return dataFrame;
    }
}