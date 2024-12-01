package com.progressive.minds.chimera.orchestrator;

import com.progressive.minds.chimera.orchestrator.activity.impl.IngestionActivityImpl;
import com.progressive.minds.chimera.orchestrator.activity.impl.LogProcessingActivityImpl;
import com.progressive.minds.chimera.orchestrator.workflow.impl.ChildWorkflowImpl;
import com.progressive.minds.chimera.orchestrator.workflow.impl.IngestionWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class WorkflowWorker {

  public static void main(String[] args) {
    // Create a stub that accesses a Temporal Service on the local development machine
    WorkflowServiceStubs serviceStub = WorkflowServiceStubs.newLocalServiceStubs();

    // The Worker uses the Client to communicate with the Temporal Service
    WorkflowClient client = WorkflowClient.newInstance(serviceStub);

    // A WorkerFactory creates Workers
    WorkerFactory factory = WorkerFactory.newInstance(client);

    // Create a worker for the task queue
    Worker worker = factory.newWorker("IngestionTaskQueue");

    // Register workflows
    worker.registerWorkflowImplementationTypes(IngestionWorkflowImpl.class,
        ChildWorkflowImpl.class);

    // Register activities
    worker.registerActivitiesImplementations(new IngestionActivityImpl(),
        new LogProcessingActivityImpl());

    // Start the worker
    factory.start();
  }
}
