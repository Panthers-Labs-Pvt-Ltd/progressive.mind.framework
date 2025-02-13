package com.progressive.minds.chimera.consumer;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DBAPIClient {
    private static  HttpClient httpClient;
    private ObjectMapper mapper;
 //   private final String accessToken;

 //   public DBAPIClient(String accessToken) {
    public DBAPIClient() {    
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.mapper = new ObjectMapper();

//        this.accessToken = accessToken;
    }

    private HttpRequest.Builder createRequestBuilder(String url) throws IOException, InterruptedException{
        String accessToken = getToken();
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json");
    }

    // public String get(String url) throws IOException, InterruptedException {
    //     HttpRequest request = createRequestBuilder(url)
    //             .GET()
    //             .build();

    //     HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    //     return response.body();
    // }

    public <T> T get(String url, TypeReference<T> typeReference) throws IOException, InterruptedException {
        HttpRequest request = createRequestBuilder(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
         return mapper.readValue(response.body(), typeReference);
    }

    public String post(String url, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = createRequestBuilder(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String put(String url, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = createRequestBuilder(url)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String delete(String url) throws IOException, InterruptedException {
        HttpRequest request = createRequestBuilder(url)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }




    public static String getToken() throws IOException, InterruptedException{
//TODO : Remove hardcoding.
        String tokenUrl = "http://localhost:8800/realms/chimera_api/protocol/openid-connect/token";
//TODO : get client details from vault.
         Map<String, String> formData = Map.of(
            "client_id", System.getProperty("API_CLIENT"), //, "chimera_api_client"),
            "client_secret", System.getProperty("API_SECRET"), //, "yhKj2HkNBpyv9ZgV9oqPxHcZOPEb3uBg"),
            "grant_type", "client_credentials"
        );

         /// chimera_api_service
        //lDNMirvQXuSZz1FMkaQE588qc3Y9w3Pf
        String requestBody = formData.entrySet()
            .stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));

  //      HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(tokenUrl))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
            .build();
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        
            if (response.statusCode() == 200) {
                // Extract token from response (assuming JSON format: {"access_token":"TOKEN_VALUE", ...})
                String responseBody = response.body();
                return extractAccessToken(responseBody);
            } else {
                throw new RuntimeException("Failed to get access token. HTTP Status: " + response.statusCode() + " Response: " + response.body());
            }
        }
    
        private static String extractAccessToken(String jsonResponse) {
            // Extract the access token from JSON response using simple parsing
            int startIndex = jsonResponse.indexOf("\"access_token\":\"") + 16;
            int endIndex = jsonResponse.indexOf("\"", startIndex);
            return jsonResponse.substring(startIndex, endIndex);
        }
}

