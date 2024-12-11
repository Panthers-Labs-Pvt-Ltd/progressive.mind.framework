package com.progressive.minds.chimera.core.apacheComet.TemporalSamples;

import com.progressive.minds.chimera.core.orchestrator.workflow.BaseWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class readFileClient implements SharedSparkSession {
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
        System.exit(0);
    }
}
