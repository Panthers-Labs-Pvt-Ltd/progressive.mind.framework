package com.progressive.minds.chimera.core.orchestrator.worker;

import com.progressive.minds.chimera.core.orchestrator.activity.impl.ChildIngestionActivityImpl;
import com.progressive.minds.chimera.core.orchestrator.workflow.impl.ChildIngestionWorkflowImpl;
import com.progressive.minds.chimera.core.orchestrator.workflow.impl.ParentIngestionWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class ParentIngestionWorker {

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
    worker.registerWorkflowImplementationTypes(ParentIngestionWorkflowImpl.class, ChildIngestionWorkflowImpl.class);

    // Register activities
    worker.registerActivitiesImplementations(new ChildIngestionActivityImpl());

    // Start the worker
    factory.start();
  }
}
