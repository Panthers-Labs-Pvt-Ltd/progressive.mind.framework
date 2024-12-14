import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;

public class IngestionWorkflowStarter {

    public static void startWorkflow(IngestionParameters params) {
        WorkflowClient client = WorkflowClient.newInstance("temporal-service-address");
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("IngestionTaskQueue")
                .build();

        IngestionWorkflow workflow = client.newWorkflowStub(IngestionWorkflow.class, options);
        workflow.runIngestion(params);
    }
}
