package com.progressive.minds.chimera.core.parentChild;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import io.temporal.activity.ActivityOptions;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.workflow.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class BatchProcessingApp {
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Create worker factory and workers
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker("BatchProcessingTaskQueue");

        worker.registerWorkflowImplementationTypes(
                BatchProcessingWorkflowImpl.class,
                DataProcessingWorkflowImpl.class
        );

        worker.registerActivitiesImplementations(new DataSourceActivitiesImpl());
        factory.start();

        // Start Workflow Execution
        BatchProcessingWorkflow workflow = client.newWorkflowStub(BatchProcessingWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("BatchProcessingTaskQueue").build()
        );

        workflow.executeBatchProcessing(
                Arrays.asList("oracle", "postgres", "s3"),
                Arrays.asList("target-db1", "target-db2")
        );
    }
}