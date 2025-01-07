package com.progressive.minds.chimera.core.orchestrator.worker;

import static com.progressive.minds.chimera.core.orchestrator.worker.WorkerCreator.createWorker;

import com.progressive.minds.chimera.core.orchestrator.activity.impl.ChildIngestionActivityImpl;
import com.progressive.minds.chimera.core.orchestrator.workflow.impl.ChildIngestionWorkflowImpl;
import com.progressive.minds.chimera.core.orchestrator.workflow.impl.ParentIngestionWorkflowImpl;
import java.util.List;

public class ParentIngestionWorker {
  public static void main(String[] args) {
    // Create a list of workflow classes
    List<Class<?>> workflows = List.of(
        ParentIngestionWorkflowImpl.class,
        ChildIngestionWorkflowImpl.class
    );

    // Create a list of activity implementations
    List<Object> activities = List.of(new ChildIngestionActivityImpl());

    // Call the createWorker method
    createWorker(workflows, activities);
  }
}
