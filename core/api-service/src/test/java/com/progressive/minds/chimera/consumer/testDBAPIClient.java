package com.progressive.minds.chimera.consumer;

import java.io.IOException;

import org.junit.jupiter.api.Test;


import com.fasterxml.jackson.core.type.TypeReference;
import com.progressive.minds.chimera.dto.PipelineMetadata;

public class testDBAPIClient {
     @Test
    public void testApiCall() throws IOException, InterruptedException {
        System.out.println("calling the rest API");
        DBAPIClient dbClient = new DBAPIClient();
        System.out.println("calling the rest API");
        PipelineMetadata pipeline = dbClient.get("http://localhost:8080/api/v1/pipelineMetadata/Test_Pipeline", new TypeReference<PipelineMetadata>() {});
        pipeline.getExtractMetadata().forEach(config -> {
            if(config.getDataSourceConnection() != null) {
                System.out.println(config.getDataSourceConnection().getConnectionMetadata());
            }
        });
        
       


    }

}
