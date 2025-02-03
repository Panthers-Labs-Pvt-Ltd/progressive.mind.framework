public class FetchPipelineMetadataWorkflowImpl implements fetchPipelineMetadata {
    private final FetchPipelineMetadataActivities activities = Workflow.newActivityStub(
        FetchPipelineMetadataActivities.class,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(2))
            .build()
    );

    @Override
    public void getPipelineMetadata(String pipelineName) {
        PipelineMetadata pipelineMetadata = activities.getpipelineMetadata(pipelineName);
    }

}

