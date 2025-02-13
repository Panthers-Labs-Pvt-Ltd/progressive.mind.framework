package com.progressive.minds.chimera.core.workflows;

import com.progressive.minds.chimera.core.workflows.activities.activityImplementations.FetchPipelineMetadataActivityImpl;
import com.progressive.minds.chimera.core.workflows.activities.activityImplementations.ExtractDataActivityImpl;
import com.progressive.minds.chimera.core.workflows.activities.activityImplementations.PersistDataActivityImpl;
import com.progressive.minds.chimera.core.workflows.activities.activityImplementations.TransformDataActivityImpl;
import com.progressive.minds.chimera.core.workflows.workflowImplementations.*;
import com.progressive.minds.chimera.pipelineutils.TransformDataUtils;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

// import io.temporal.api.enums.v1.WorkflowIdReusePolicy;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

import java.io.IOException;

public class MainApp {
    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(TransformDataUtils.class);

    public static void main(String[] args) throws IOException, InterruptedException{
        logger.logInfo("******* Main Workflow Started *******");
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Create worker factory and workers
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker("BatchProcessingTaskQueue");

        worker.registerWorkflowImplementationTypes(
            MainWorkflowImpl.class,
            FetchPipelineMetadataWorkflowImpl.class,
            ExtractDataWorkflowImpl.class,
                TransformDataWorkflowImpl.class,
                PersistDataWorkflowImpl.class
    );

        worker.registerActivitiesImplementations(new FetchPipelineMetadataActivityImpl(),
                                                new ExtractDataActivityImpl(),
                                                new TransformDataActivityImpl(),
                                                new PersistDataActivityImpl());

        factory.start();
    
     // Start Workflow Execution
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("BatchProcessingTaskQueue")
                .setWorkflowId("DataIngestion")  // 👈 Set a custom workflow ID
       //         .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE)
                .build();
     MainWorkflow workflow = client.newWorkflowStub(MainWorkflow.class, options);

    workflow.executeMainWorkflow("DataIngestion","Test_Pipeline_Postgres");
    
    }
}


