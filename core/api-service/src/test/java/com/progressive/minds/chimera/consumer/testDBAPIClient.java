package com.progressive.minds.chimera.consumer;

import java.io.IOException;

import org.junit.jupiter.api.Test;


import com.fasterxml.jackson.core.type.TypeReference;
import com.progressive.minds.chimera.dto.PipelineMetadata;

public class testDBAPIClient {
     @Test
    public PipelineMetadata testApiCall() throws IOException, InterruptedException {
        System.out.println("calling the rest API");
        DBAPIClient dbClient = new DBAPIClient();
        System.out.println("calling the rest API");
        PipelineMetadata pipeline = dbClient.get("http://localhost:8080/api/v1/pipelineMetadata/Test_Pipeline", new TypeReference<PipelineMetadata>() {});
        System.out.println(pipeline);
        return pipeline;


    }

}
