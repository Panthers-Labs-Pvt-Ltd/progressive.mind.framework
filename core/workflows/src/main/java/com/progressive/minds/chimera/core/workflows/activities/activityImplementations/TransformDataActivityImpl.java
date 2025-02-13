package com.progressive.minds.chimera.core.workflows.activities.activityImplementations;

import com.progressive.minds.chimera.core.dataSource.formats.jdbc.jdbc;
import com.progressive.minds.chimera.core.workflows.activities.TransformDataActivity;
import com.progressive.minds.chimera.core.workflows.activities.SharedSparkSession;
import com.progressive.minds.chimera.dto.TransformMetadataConfig;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class TransformDataActivityImpl implements TransformDataActivity {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(jdbc.class);

    @Override
    public void transformData(TransformMetadataConfig config) {
        logger.logInfo("Initiating TransformMetadata Flow");
        SparkSession spark = SharedSparkSession.getSparkSession();
        System.out.println("Spark Session fetched. Printing Catalog : " );
        spark.catalog().listTables().show();
        System.out.println("check if inpuDF exists. : - " + spark.catalog().tableExists("inputDF"));

        Dataset<Row> tDf = spark.sql("select * from inputDF where data_source_sub_type='Postgres'");
        tDf.createOrReplaceTempView(config.getTransformDataframeName());
        System.out.println("Transform Done");
        System.out.println("check if inpuDF exists. : - " + spark.catalog().tableExists("inputDF"));
        spark.sql("select * from " + config.getTransformDataframeName()).show();
    }
}






