package com.progressive.minds.chimera.common.workflows.activities.activityImplementations;

import com.progressive.minds.chimera.common.workflows.activities.FetchPipelineMetadataActivity;
import com.progressive.minds.chimera.consumer.DBAPIClient;
import com.progressive.minds.chimera.dto.PipelineMetadata;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;

public class FetchPipelineMetadataActivityImpl implements FetchPipelineMetadataActivity {
    @Override
    public PipelineMetadata getPipelineMetadata(String pipelineName) throws IOException, InterruptedException {
        DBAPIClient dbClient = new DBAPIClient();
        return dbClient.get("http://localhost:8080/api/v1/pipelineMetadata/Test_Pipeline", new TypeReference<PipelineMetadata>(){});
    }

}