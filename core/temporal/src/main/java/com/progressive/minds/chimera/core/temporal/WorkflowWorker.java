package com.progressive.minds.chimera.core.temporal;


import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import com.progressive.minds.chimera.core.temporal.activities.PipelineActivitiesImpl;
import com.progressive.minds.chimera.core.temporal.controller.implementations.IngestionWorkflowImpl;
import com.progressive.minds.chimera.core.temporal.workflows.implementations.ExtractWorkflowImpl;
import com.progressive.minds.chimera.core.temporal.workflows.implementations.LoadWorkflowImpl;
import com.progressive.minds.chimera.core.temporal.workflows.implementations.TransformWorkflowImpl;

public class WorkflowWorker {
    public static void main(String[] args) {
        // Create service and client
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Worker factory setup
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker("IngestionTaskQueue");

        // Register parent and child workflows
        worker.registerWorkflowImplementationTypes(
                IngestionWorkflowImpl.class,
                ExtractWorkflowImpl.class,
                TransformWorkflowImpl.class,
                LoadWorkflowImpl.class
        );

        // Register activities
        worker.registerActivitiesImplementations(new PipelineActivitiesImpl());

        // Start the worker
        factory.start();
    }
}
