import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.List;

public class IngestionWorkflowImpl implements IngestionWorkflow {

    private final IngestionActivities activities = Workflow.newActivityStub(
            IngestionActivities.class,
            ActivityOptions.newBuilder()
                    .setScheduleToCloseTimeout(Duration.ofMinutes(5))
                    .build()
    );

    @Override
    public void runIngestion(IngestionParameters params) {
        List<String> data = activities.readFile(params.getInputPath());
        List<String> transformedData = activities.transformData(data);
        activities.writeFile(params.getOutputPath(), transformedData);
    }
}