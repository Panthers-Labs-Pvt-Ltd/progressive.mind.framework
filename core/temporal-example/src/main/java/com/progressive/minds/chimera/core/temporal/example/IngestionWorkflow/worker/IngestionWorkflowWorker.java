package com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.worker;

import com.progressive.minds.chimera.core.temporal.example.IngestionWorkflow.InterfacesImplementations.*;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.client.WorkflowClient;

public class IngestionWorkflowWorker {
    public static void main(String[] args) {
        // Connect to Temporal service
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);

        // Register parent worker
        Worker parentWorker = factory.newWorker("IngestionWorkflowQueue");
        parentWorker.registerWorkflowImplementationTypes(IngestionWorkflowImpl.class);

        // Register child workers
        Worker childWorker = factory.newWorker("IngestionStepsWorkflowQueue");
        childWorker.registerWorkflowImplementationTypes(
                PreInitializationWorkflowImpl.class,
                ExtractWorkflowImpl.class,
                TransformWorkflowImpl.class,
                LoadWorkflowImpl.class,
                CleanupWorkflowImpl.class
        );

        // Start workers
        factory.start();
        System.out.println("Workers started.");
    }
}
