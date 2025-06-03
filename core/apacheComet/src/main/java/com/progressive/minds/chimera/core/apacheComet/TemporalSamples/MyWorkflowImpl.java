/*
package com.progressive.minds.chimera.core.apacheComet.TemporalSamples;



import com.progressive.minds.chimera.core.data_source.sourceTypes.FileReader;
// import com.progressive.minds.chimera.core.temporal.example.workflows.MyWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import static com.progressive.minds.chimera.core.apacheComet.TemporalSamples.SharedSparkSession.spark;

public class MyWorkflowImpl implements MyWorkflow {
    @Override
    public String myFunction(String input) {
        System.out.print("Started Executing myFUnction with INput " + input);
        Dataset<Row> dataFrame = spark.emptyDataFrame();
        dataFrame = new FileReader().read("parquet", spark, "ParquetReaderTest",
                input, "", "",
                null, "", "",
                "", 0);
        System.out.print("Completed Executing myFUnction with INput " + input);
        return String.valueOf(dataFrame.count());
    }
 }*/
