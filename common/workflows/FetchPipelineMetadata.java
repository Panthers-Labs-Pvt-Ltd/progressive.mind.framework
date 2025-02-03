import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;


@WorkflowInterface
public interface FetchPipelineMetadata {

    @WorkflowMethod
    public void getPipelineMetadata(String pipelineName);

}







