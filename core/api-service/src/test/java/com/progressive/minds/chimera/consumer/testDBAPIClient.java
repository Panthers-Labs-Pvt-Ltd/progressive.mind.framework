package com.progressive.minds.chimera.consumer;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progressive.minds.chimera.dto.ExtractMetadataResponse;
import com.progressive.minds.chimera.dto.ExtractView;

public class testDBAPIClient {
     @Test
    public void testApiCall() throws IOException, InterruptedException {
        DBAPIClient dbClient = new DBAPIClient();
    //     TypeReference<List<ExtractMetadataResponse>> typeReference = new TypeReference<>() {};
    //     List<ExtractMetadataResponse> response = dbClient.get("http://localhost:8080/api/v1/extractMetadataConfig", typeReference);
    //   //  System.out.println("extractConfig : " + response);
    //     // ObjectMapper mapper = new ObjectMapper();
    //     // List<ExtractMetadataResponse> extractMetadataConfig = mapper.readValue(response, new TypeReference<>() {});
    //     response.forEach(config -> System.out.println(config));

     //   TypeReference<List<DataSources>> dsTypeReference = new TypeReference<List<DataSources>>() {};
        List<ExtractView> dataSources = dbClient.get("http://localhost:8080/api/v1/extractView/Test_Pipeline", new TypeReference<List<ExtractView>>() {});
        dataSources.forEach(ds -> System.out.println(ds));


    }

}
