package com.progressive.minds.chimera.workflow;

import static com.progressive.minds.chimera.core.orchestrator.worker.WorkerCreator.createWorker;

import java.util.List;

public class DecisionEngineWorker {
    public static void main(String[] args) {
      // Create a list of workflow classes
      List<Class<?>> workflows = List.of(
          DecisionEngineWorkflowImpl.class);

      // Create a list of activity implementations
      List<Object> activities = List.of();

      // Call the createWorker method
      createWorker(workflows, activities);
    }
}
