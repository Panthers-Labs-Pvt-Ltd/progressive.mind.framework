package com.progressive.minds.chimera.core.datahub;

import com.progressive.minds.chimera.core.dataSource.sourceTypes.FileReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class readUsingSpark {
    public static void main(String[] args) {

        SparkSession spark = SparkSession.builder()
                .appName("Shared Spark Session for Data Read")
                .master("local[*]")
                .config("spark.executor.extraJavaOptions", "--add-opens java.base/sun.nio.ch=ALL-UNNAMED" )
                .config("spark.driver.extraJavaOptions", "--add-opens java.base/sun.nio.ch=ALL-UNNAMED" )
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
    }
}
