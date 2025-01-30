package com.progressive.minds.chimera.consumer;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progressive.minds.chimera.dto.ExtractMetadataResponse;

public class testDBAPIClient {
     @Test
    public void testApiCall() throws IOException, InterruptedException {
        DBAPIClient dbClient = new DBAPIClient();
        String response = dbClient.get("http://localhost:8080/api/v1/extractMetadataConfig");
        System.out.println("extractConfig : " + response);
        ObjectMapper mapper = new ObjectMapper();
        List<ExtractMetadataResponse> extractMetadataConfig = mapper.readValue(response, new TypeReference<>() {});
        extractMetadataConfig.forEach(config -> System.out.println(config));


    }

}
