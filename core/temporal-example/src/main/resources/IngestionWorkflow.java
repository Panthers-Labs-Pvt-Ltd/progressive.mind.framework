import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface IngestionWorkflow {
    @WorkflowMethod
    void runIngestion(IngestionParameters params);
}