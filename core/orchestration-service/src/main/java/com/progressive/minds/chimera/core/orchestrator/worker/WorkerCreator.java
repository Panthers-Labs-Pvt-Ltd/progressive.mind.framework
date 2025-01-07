package com.progressive.minds.chimera.core.orchestrator.worker;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class WorkerCreator {

  public static void createWorker(List<Class<?>> workflowClasses, List<Object> activityImplementations) {
    // Create a stub that accesses a Temporal Service on the local development machine
    WorkflowServiceStubs serviceStub = WorkflowServiceStubs.newLocalServiceStubs();

    // The Worker uses the Client to communicate with the Temporal Service
    WorkflowClient client = WorkflowClient.newInstance(serviceStub);

    // A WorkerFactory creates Workers
    WorkerFactory factory = WorkerFactory.newInstance(client);

    // Create a worker for the task queue
    Worker worker = factory.newWorker("IngestionTaskQueue");

    // Register workflows dynamically
    for (Class<?> workflowClass : workflowClasses) {
      worker.registerWorkflowImplementationTypes(workflowClass);
    }

    // Register activities dynamically
    if(CollectionUtils.isNotEmpty(activityImplementations)) {
      for (Object activityImplementation : activityImplementations) {
        worker.registerActivitiesImplementations(activityImplementation);
      }
    }

    // Start the worker
    factory.start();
  }
}

