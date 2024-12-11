package com.progressive.minds.chimera.core.apacheComet.TemporalSamples;

import com.progressive.minds.chimera.core.orchestrator.worker.WorkerCreator;
import java.util.List;

public class CometWorker {
    public static void main(String[] args) {
      // Create a list of workflow classes
      List<Class<?>> workflows = List.of(
          CometWorkflowImpl.class);

      // Create a list of activity implementations
      List<Object> activities = List.of();

      // Call the createWorker method
      WorkerCreator.createWorker(workflows, activities);
    }
}
