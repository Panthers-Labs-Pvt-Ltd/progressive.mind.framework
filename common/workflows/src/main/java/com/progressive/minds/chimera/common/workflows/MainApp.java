package com.progressive.minds.chimera.common.workflows;

import com.progressive.minds.chimera.common.workflows.activities.activityImplementations.FetchPipelineMetadataActivityImpl;
import com.progressive.minds.chimera.common.workflows.workflowImplementations.FetchPipelineMetadataWorkflowImpl;
import com.progressive.minds.chimera.common.workflows.workflowImplementations.MainWorkflowImpl;
import io.temporal.api.enums.v1.WorkflowIdReusePolicy;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

import java.io.IOException;

public class MainApp {

    public static void main(String[] args) throws IOException, InterruptedException{
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Create worker factory and workers
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker("BatchProcessingTaskQueue");

        worker.registerWorkflowImplementationTypes(
            MainWorkflowImpl.class,
            FetchPipelineMetadataWorkflowImpl.class
    );

        worker.registerActivitiesImplementations(new FetchPipelineMetadataActivityImpl());

        factory.start();
    
     // Start Workflow Execution
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("BatchProcessingTaskQueue")
                .setWorkflowId("DataIngestion")  // 👈 Set a custom workflow ID
       //         .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE)
                .build();
     MainWorkflow workflow = client.newWorkflowStub(MainWorkflow.class, options);

    workflow.executeMainWorkflow("DataIngestion","Test_Pipeline");
    
    }
}


