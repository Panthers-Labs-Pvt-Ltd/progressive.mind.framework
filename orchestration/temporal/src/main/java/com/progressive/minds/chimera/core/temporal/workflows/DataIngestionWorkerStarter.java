package com.progressive.minds.chimera.core.temporal.workflows;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import com.progressive.minds.chimera.core.temporal.activities.DataIngestionActivitiesImpl;
import com.progressive.minds.chimera.core.temporal.workflows.implementations.DataIngestionWorkflowImpl;

public class DataIngestionWorkerStarter {
    public static void main(String[] args) {
        // Create service and client
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Worker factory setup
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker("IngestionTaskQueue");

        // Register parent and child workflows
        worker.registerWorkflowImplementationTypes(DataIngestionWorkflowImpl.class);

        // Register activities
        worker.registerActivitiesImplementations(new DataIngestionActivitiesImpl());

        // Start the worker
        factory.start();
    }
}

