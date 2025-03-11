package com.progressive.minds.chimera.pipelineutils;

import com.progressive.minds.chimera.core.api_service.dto.TransformMetadataConfig;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class TransformDataUtils {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(TransformDataUtils.class);

    public static void transformData(TransformMetadataConfig config) {
        SparkSession spark = SharedSparkSession.getSparkSession();
        Dataset<Row> tDf = spark.sql(config.getSqlText());
        if (tDf != null) {
            tDf.createOrReplaceTempView(config.getTransformDataframeName());
        }
    }
}
