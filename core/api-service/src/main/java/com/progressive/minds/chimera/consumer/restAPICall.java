package com.progressive.minds.chimera.consumer;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;
import java.util.stream.Collectors;


import java.nio.charset.StandardCharsets;

// import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.System;

public class restAPICall {
    public static String consumeAPI(String url) throws IOException, InterruptedException {
        String token = "Bearer " + getToken();
        System.out.println("Token : " + token);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .header("Authorization", token)
        .build();
        try {
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println("response Code : " +  response.statusCode());
        System.out.println("response : " +  response.body());
        return response.body();
    } catch (Exception exception) {
        System.out.println(exception.getStackTrace());
        return null;
    }
    }

    public static String getToken() throws IOException, InterruptedException{

        String tokenUrl = "http://localhost:8800/realms/chimera_api/protocol/openid-connect/token";

         Map<String, String> formData = Map.of(
            "client_id", "chimera_api_service",
            "client_secret", "lDNMirvQXuSZz1FMkaQE588qc3Y9w3Pf",
            "grant_type", "client_credentials"
        );

        String requestBody = formData.entrySet()
            .stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(tokenUrl))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
            .build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        
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

