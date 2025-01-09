package com.progressive.minds.chimera.core.temporal;

// This is a worker server which can be used to start the worker for the Temporal pipeline.
// Each pipeline creates a new instance of worker.

/*
The `WorkflowWorker` class in the `com.progressive.minds.chimera.core.temporal` package is designed to start a worker for the Temporal pipeline. Temporal is a workflow orchestration engine, and this class sets up the necessary components to run workflows and activities.

First, the class creates instances of `WorkflowServiceStubs` and `WorkflowClient`, which are essential for communicating with the Temporal service:
```java
WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
WorkflowClient client = WorkflowClient.newInstance(service);
```

Next, it sets up a `WorkerFactory` and a `Worker`. The `WorkerFactory` is responsible for creating workers, and the `Worker` is associated with a specific task queue, in this case, "IngestionTaskQueue":
```java
WorkerFactory factory = WorkerFactory.newInstance(client);
Worker worker = factory.newWorker("IngestionTaskQueue");
```

The worker then registers the workflow implementation classes. These classes define the logic for different workflows that the worker will execute. In this example, four workflow implementations are registered:
```java
worker.registerWorkflowImplementationTypes(
    IngestionWorkflowImpl.class,
    ExtractWorkflowImpl.class,
    TransformWorkflowImpl.class,
    LoadWorkflowImpl.class
);
```

Additionally, the worker registers activity implementations. Activities are the building blocks of workflows, and here, an instance of `PipelineActivitiesImpl` is registered:
```java
worker.registerActivitiesImplementations(new PipelineActivitiesImpl());
```

Finally, the worker factory is started, which begins polling the task queue for tasks to execute:
```java
factory.start();
```

This setup allows the worker to handle tasks related to the registered workflows and activities, enabling the Temporal pipeline to function as intended.
 */


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
