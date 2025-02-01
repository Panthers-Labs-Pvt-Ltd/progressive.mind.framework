package com.progressive.minds.chimera.core.IngestionWorkflow;

import com.progressive.minds.chimera.core.temporal.controller.implementations.IngestionWorkflowImpl;
import com.progressive.minds.chimera.core.temporal.workflows.implementations.*;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class CustomWorker {
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);
        System.setProperty("jdk.module.addopens", "java.base/sun.nio.ch=ALL-UNNAMED");

        // Create a worker factory and worker
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker("IngestionTaskQueue");

        // Register workflow implementations
        worker.registerWorkflowImplementationTypes(
                PreProcessingImpl.class,
                IngestionWorkflowImpl.class,
                ExtractWorkflowImpl.class,
                TransformWorkflowImpl.class,
                LoadWorkflowImpl.class,
                PostProcessingImpl.class
        );

        // Register custom activity implementation
        worker.registerActivitiesImplementations(new ProcessDataUsingTemporal());

        // Start the worker
        factory.start();
    }
}
