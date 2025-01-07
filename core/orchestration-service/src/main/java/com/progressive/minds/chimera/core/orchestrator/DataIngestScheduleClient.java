package com.progressive.minds.chimera.core.orchestrator;

import com.progressive.minds.chimera.core.orchestrator.workflow.BaseWorkflow;
import io.temporal.api.enums.v1.ScheduleOverlapPolicy;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.schedules.Schedule;
import io.temporal.client.schedules.ScheduleActionStartWorkflow;
import io.temporal.client.schedules.ScheduleCalendarSpec;
import io.temporal.client.schedules.ScheduleClient;
import io.temporal.client.schedules.ScheduleHandle;
import io.temporal.client.schedules.ScheduleIntervalSpec;
import io.temporal.client.schedules.ScheduleOptions;
import io.temporal.client.schedules.ScheduleRange;
import io.temporal.client.schedules.ScheduleSpec;
import io.temporal.client.schedules.ScheduleState;
import io.temporal.client.schedules.ScheduleUpdate;
import io.temporal.client.schedules.ScheduleUpdateInput;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import java.time.Duration;
import java.util.Collections;

public class DataIngestScheduleClient {

  static final String SCHEDULE_ID = "HelloSchedule";

  public static void main(String[] args) throws InterruptedException {
    // Get a Workflow service stub.
    WorkflowServiceStubs service = WorkflowServiceStubs.newServiceStubs(WorkflowServiceStubsOptions.newBuilder().setTarget("localhost:7233").build());

    /*
     * Get a Schedule client which can be used to interact with schedule.
     */
    ScheduleClient scheduleClient = ScheduleClient.newInstance(service);

    /*
     * Create the workflow options for our schedule.
     * Note: Not all workflow options are supported for schedules.
     */
    WorkflowOptions workflowOptions =
        WorkflowOptions.newBuilder().setWorkflowId("temporal-community-workflow").setTaskQueue("schedules-task-queue").build();

    /*
     * Create the action that will be run when the schedule is triggered.
     */
    ScheduleActionStartWorkflow action =
        ScheduleActionStartWorkflow.newBuilder()
            .setWorkflowType(BaseWorkflow.class)
            .setOptions(workflowOptions)
            .build();

    Schedule schedule =
        Schedule.newBuilder().setAction(action).setSpec(ScheduleSpec.newBuilder().build()).build();

    // Create a schedule on the server
    ScheduleHandle handle =
        scheduleClient.createSchedule(SCHEDULE_ID, schedule, ScheduleOptions.newBuilder().build());

    // Manually trigger the schedule once
    handle.trigger(ScheduleOverlapPolicy.SCHEDULE_OVERLAP_POLICY_ALLOW_ALL);

    // Update the schedule with a spec, so it will run periodically
    handle.update(
        (ScheduleUpdateInput input) -> {
          Schedule.Builder builder = Schedule.newBuilder(input.getDescription().getSchedule());

          builder.setSpec(
              ScheduleSpec.newBuilder()
                  // Run the schedule at 5pm on Friday
                  .setCalendars(
                      Collections.singletonList(
                          ScheduleCalendarSpec.newBuilder()
                              .setHour(Collections.singletonList(new ScheduleRange(17)))
                              .setDayOfWeek(Collections.singletonList(new ScheduleRange(5)))
                              .build()))
                  // Run the schedule every 5s
                  .setIntervals(
                      Collections.singletonList(new ScheduleIntervalSpec(Duration.ofSeconds(5))))
                  .build());
          // Make the schedule paused to demonstrate how to unpause a schedule
          builder.setState(
              ScheduleState.newBuilder()
                  .setPaused(true)
                  .setLimitedAction(true)
                  .setRemainingActions(10)
                  .build());
          return new ScheduleUpdate(builder.build());
        });

    // Unpause schedule
    handle.unpause();

    // Wait for the schedule to run 10 actions
    while (true) {
      ScheduleState state = handle.describe().getSchedule().getState();
      if (state.getRemainingActions() == 0) {
        break;
      }
      Thread.sleep(5000);
    }
    // Delete the schedule once the sample is done
    handle.delete();
    System.exit(0);
  }
}
