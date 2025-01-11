package com.progressive.minds.chimera.core.orchestrator;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class TemporalService {

  private final WorkflowServiceStubs serviceStubs;
  private final WorkerFactory workerFactory;
  private final WorkflowClient workflowClient;

  // with Default namespace
  public TemporalService() {
    this.serviceStubs = WorkflowServiceStubs.newLocalServiceStubs();

    this.workflowClient = WorkflowClient.newInstance(
        serviceStubs,
        WorkflowClientOptions.newBuilder().build()
    );
    this.workerFactory = WorkerFactory.newInstance(workflowClient);
  }

  public TemporalService(WorkflowServiceStubs serviceStubs, WorkflowClientOptions workflowClientOptions) {
    if (serviceStubs == null) {
      throw new IllegalArgumentException("WorkflowServiceStubs must not be null.");
    }

    WorkflowClientOptions options = workflowClientOptions != null
        ? workflowClientOptions
        : WorkflowClientOptions.newBuilder().build();

    this.serviceStubs = serviceStubs;
    this.workflowClient = WorkflowClient.newInstance(serviceStubs, options);
    this.workerFactory = WorkerFactory.newInstance(workflowClient);
  }

  public WorkflowClient createClient(String namespace, WorkflowClientOptions workflowClientOptions) {
    if (namespace == null || namespace.isEmpty()) {
      throw new IllegalArgumentException("Namespace must not be null or empty.");
    }

    WorkflowClientOptions clientOptions = workflowClientOptions != null
        ? workflowClientOptions
        : WorkflowClientOptions.newBuilder().setNamespace(namespace).build();

    return WorkflowClient.newInstance(serviceStubs, clientOptions);
  }

  public void registerWorker(String taskQueue, Class<?> workflowClass, Object... activityImplementations) {
    if (taskQueue == null || taskQueue.isEmpty()) {
      throw new IllegalArgumentException("TaskQueue must not be null or empty.");
    }

    Worker worker = workerFactory.newWorker(taskQueue);

    if (workflowClass != null) {
      worker.registerWorkflowImplementationTypes(workflowClass);
    }

    if (activityImplementations != null) {
      for (Object activity : activityImplementations) {
        worker.registerActivitiesImplementations(activity);
      }
    }
  }

  public void start() {
    workerFactory.start();
  }

  public void shutdown() {
    workerFactory.shutdown();
    serviceStubs.shutdown();
  }
}
