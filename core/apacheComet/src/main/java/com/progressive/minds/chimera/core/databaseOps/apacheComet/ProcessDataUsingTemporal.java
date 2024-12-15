package com.progressive.minds.chimera.core.databaseOps.apacheComet;

import com.progressive.minds.chimera.core.dataSource.sourceTypes.FileReader;
import com.progressive.minds.chimera.core.temporal.activities.PipelineActivities;
import com.progressive.minds.chimera.core.temporal.startWorkflow;
import com.progressive.minds.chimera.core.temporal.workflows.ExtractWorkflow;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.SparkSession;

public class ProcessDataUsingTemporal implements PipelineActivities {
    /**
     *
     */
    @Override
    public void init() {
        System.out.println("MMM - Initializing Spark Engine for reading Data......");
        System.out.println("Initializing Executing Spark Read......");
        try {
            for (int i = 0; i < 5; i++) {

                // it will sleep the main thread for 1 sec
                // ,each time the for loop runs
                Thread.sleep(1000);

                // printing the value of the variable
                System.out.println("SLEEPTNG " + i);
            }
        }
        catch (Exception e) {

            // catching the exception
            System.out.println(e);
        }
        /*try {
            SparkSession spark = SparkSession.builder()
                    .appName("Shared Spark Session for Data Read")
                    .master("local[*]")
                    .config("spark.driver.extraJavaOptions", "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED")
                    .config("spark.executor.extraJavaOptions", "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED")
                    .getOrCreate();
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
        }*/
    }

    /**
     *
     */
    @Override
    public void execute() {
        System.setProperty("jdk.module.addopens", "java.base/sun.nio.ch=ALL-UNNAMED");

        System.out.println("KKKK Executing Spark Read......");
        try {
            SparkSession spark = SparkSession.builder()
                    .appName("Shared Spark Session for Data Read")
                    .master("local[*]")
                    .config("spark.driver.extraJavaOptions", "add-opens=java.base/sun.nio.ch=ALL-UNNAMED")
                    .config("spark.executor.extraJavaOptions", "add-opens=java.base/sun.nio.ch=ALL-UNNAMED")
                    .config ("jdk.module.addopens", "java.base/sun.nio.ch=ALL-UNNAMED")
                    .getOrCreate();
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

    /**
     *
     */
    @Override
    public void complete() {
        System.out.println("GGG Completing Spark Engine for reading Data......");

    }

    }
