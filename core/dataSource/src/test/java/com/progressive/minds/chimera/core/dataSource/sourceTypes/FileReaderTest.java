package com.progressive.minds.chimera.core.dataSource.sourceTypes;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class FileReaderTest implements SharedSparkSession{

    void TestRead()
        {
        Dataset<Row> dataFrame = spark.emptyDataFrame();
        dataFrame = new FileReader().read("parquet", spark, "ParquetReaderTest",
                getClass().getResource("/" + "flight_parquet").getPath(), "", "",
                null, "", "",
                "", 0);
        System.out.println("Total Records " + dataFrame.count());
    }
}
