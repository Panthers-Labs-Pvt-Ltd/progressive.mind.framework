package com.progressive.minds.chimera.core.apacheComet.TemporalSamples;

import com.progressive.minds.chimera.core.dataSource.sourceTypes.FileReader;
import com.progressive.minds.chimera.core.orchestrator.workflow.BaseWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class readFile implements SharedSparkSession, BaseWorkflow {
    public static void main(String[] args) throws Exception {
        WorkflowServiceStubs serviceStub = WorkflowServiceStubs.newLocalServiceStubs();

        WorkflowClient client = WorkflowClient.newInstance(serviceStub);

        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("IngestionTaskQueue")
                .setWorkflowId("Ingest_" + System.currentTimeMillis())
                .build();
        BaseWorkflow workflow = client.newWorkflowStub(BaseWorkflow.class, options);
        String Folder="/home/manish/Chimera/core/dataSource/src/test/resources/flight_parquet";

        WorkflowClient.start(workflow::execute, Folder);
    }

   @Override
    public void execute(String input) {
        Dataset<Row> dataFrame = spark.emptyDataFrame();
        dataFrame = new FileReader().read("parquet", spark, "ParquetReaderTest",
                input, "", "",
                null, "", "",
                "", 0);
        System.out.println("Total Records " + dataFrame.count());
    }
}
