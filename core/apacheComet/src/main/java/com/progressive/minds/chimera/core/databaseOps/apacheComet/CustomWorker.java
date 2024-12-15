package com.progressive.minds.chimera.core.databaseOps.apacheComet;

import com.progressive.minds.chimera.core.temporal.controller.implementations.IngestionWorkflowImpl;
import com.progressive.minds.chimera.core.temporal.workflows.implementations.ExtractWorkflowImpl;
import com.progressive.minds.chimera.core.temporal.workflows.implementations.LoadWorkflowImpl;
import com.progressive.minds.chimera.core.temporal.workflows.implementations.TransformWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import com.progressive.minds.chimera.core.databaseOps.apacheComet.ProcessDataUsingTemporal;

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
                IngestionWorkflowImpl.class,
                ExtractWorkflowImpl.class,
                TransformWorkflowImpl.class,
                LoadWorkflowImpl.class
        );

        // Register custom activity implementation
        worker.registerActivitiesImplementations(new ProcessDataUsingTemporal());

        // Start the worker
        factory.start();
    }
}
