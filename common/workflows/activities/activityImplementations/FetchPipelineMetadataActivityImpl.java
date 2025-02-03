public class FetchPipelineMetadataActivityImpl {

    PipelineMetadata getPipelineMetadata(String pipelineName) {
        DBAPIClient dbClient = new DBAPIClient();
        PipelineMetadata pipelineMetadata = dbClient.get("http://localhost:8080/api/v1/pipelineMetadata/Test_Pipeline", new TypeReference<PipelineMetadata>);
        return pipelineMetadata;
    }

}