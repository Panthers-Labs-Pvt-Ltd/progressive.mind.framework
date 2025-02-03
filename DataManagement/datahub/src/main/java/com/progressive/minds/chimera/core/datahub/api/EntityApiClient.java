package com.progressive.minds.chimera.core.datahub.api;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class EntityApiClient {
    public static void main(String[] args) {
        String serverUrl = "http://localhost:9002/openapi/v3/entity/"; // Replace with your DataHub server URL
        String authToken = "eyJhbGciOiJIUzI1NiJ9.eyJhY3RvclR5cGUiOiJVU0VSIiwiYWN0b3JJZCI6ImRhdGFodWIiLCJ0eXBlIjoiUEVSU09OQUwiLCJ2ZXJzaW9uIjoiMiIsImp0aSI6IjY5ZDRlY2QyLTM2NDItNDk3YS05OTIwLThiZjQyM2I3Yjg3OSIsInN1YiI6ImRhdGFodWIiLCJleHAiOjE3Mzk4OTE4NzgsImlzcyI6ImRhdGFodWItbWV0YWRhdGEtc2VydmljZSJ9.lWCSLgZ096DdlI6lsqTgicd-uKYi4RNf1pOHaCCM-WI"; // Optional: Add your token if authentication is enabled
        String entityUrn = "urn:li:dataProduct:chimeradataproduct4"; // Replace with your entity URN
        String entityType = "dataproduct/";

        try {
            // Create RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

             String url = serverUrl + entityType + entityUrn;

             HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + authToken);


            // Perform GET request
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class
            );
            System.out.println(response);
            // Parse the response
            ObjectMapper objectMapper = new ObjectMapper();
            DataProduct entityResponse = objectMapper.readValue(response.getBody(), DataProduct.class);

            // Print details
            System.out.println("Entity URN: " + entityResponse.getUrn());
            System.out.println("Entity Type: " + entityResponse.getDataProductKey());
            System.out.println("Metadata: " + entityResponse.getOwnership());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

