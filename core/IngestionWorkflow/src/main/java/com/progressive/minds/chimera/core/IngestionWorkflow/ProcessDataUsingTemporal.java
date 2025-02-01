package com.progressive.minds.chimera.core.IngestionWorkflow;

import com.progressive.minds.chimera.core.dataSource.sourceTypes.FileReader;
import com.progressive.minds.chimera.core.temporal.activities.PipelineActivities;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class ProcessDataUsingTemporal implements PipelineActivities, sparkSession {
    @Override
    public Boolean getPipelineMetadata() {
        System.out.println("Getting Pipeline Metadata ");
        return true;
    }

    @Override
    public Boolean getExecutionEngine() {
        System.out.println("Getting Spark Execution Engine");
        return true;
    }

    /**
     *
     */
    @Override
    public void init() {
        System.out.println("MMM - Initializing Spark Engine for reading Data......");
        System.out.println("Initializing Executing Spark Read......");
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(1000);
                System.out.println("SLEEPTNG " + i);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void execute() {
        try {
            String Folder = "/home/manish/Chimera/core/dataSource/src/test/resources/flight_parquet";
            Dataset<Row> dataFrame = spark.emptyDataFrame();
            dataFrame = new FileReader().read("parquet", spark, "ParquetReaderTest",
                    Folder, "", "",
                    null, "", "",
                    "", 0);
            dataFrame.show(10, false);
            dataFrame.printSchema();
            System.out.print("Total DataSet Read are " + dataFrame.count());
        } catch (Exception e) {
            System.out.print("Exception" + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void complete() {
        System.out.println("GGG Completing Spark Engine for reading Data......");
    }
}
