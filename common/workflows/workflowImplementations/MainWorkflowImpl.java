public class MainWorkflowImpl implements MiainWorkflow {

    @Override
    public void executeMainWorkflow(String pipelineName) {
        ChildWorkflowOptions childOptions = ChildWorkflowOptions.newBuilder()
                .build();

        fet childWorkflow = Workflow.newChildWorkflowStub(DataProcessingWorkflow.class, childOptions);
        childWorkflow.processData(sources, targets);
    
}



public class BatchProcessingWorkflowImpl implements BatchProcessingWorkflow {
    @Override
    public void executeBatchProcessing(List<String> sources, List<String> targets) {
        ChildWorkflowOptions childOptions = ChildWorkflowOptions.newBuilder()
                .build();

        FetchPipelineMetadata childWorkflow = Workflow.newChildWorkflowStub(FetchPipelineMetadata.class, childOptions);
        childWorkflow.getPipelineMetadata(String pipelineName);
    }
}
