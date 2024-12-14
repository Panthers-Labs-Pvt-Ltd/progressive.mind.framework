package com.progressive.minds.chimera.core.temporal.example.worker;

import com.progressive.minds.chimera.core.temporal.example.workflow.ChildWorkflowImpl;
import com.progressive.minds.chimera.core.temporal.example.workflow.ParentWorkflowImpl;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.client.WorkflowClient;

public class WorkflowWorker {
    public static void main(String[] args) {
        // Step 1: Connect to the Temporal service
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();

        // Step 2: Create a WorkflowClient
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Step 3: Create a WorkerFactory
        WorkerFactory factory = WorkerFactory.newInstance(client);

        // Step 4: Create a worker for the parent workflow's task queue
        Worker parentWorker = factory.newWorker("parentTaskQueue");
        parentWorker.registerWorkflowImplementationTypes(ParentWorkflowImpl.class);

        // Step 5: Create a worker for the child workflow's task queue
        Worker childWorker = factory.newWorker("childTaskQueue");
        childWorker.registerWorkflowImplementationTypes(ChildWorkflowImpl.class);

        // Step 6: Start the worker factory
        factory.start();

        System.out.println("Workers started. Waiting for tasks...");
    }
}
