package com.progressive.minds.chimera.core.kaggle;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.http.*;
import java.net.URI;
import java.nio.file.*;
import java.io.IOException;
import java.util.Base64;

public class KaggleDataDownloader {

    private static  String KAGGLE_USERNAME = "your_username";
    private static  String KAGGLE_KEY = "your_api_key";

    public static void main(String[] args) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            // Load the kaggle.json file from resources
            InputStream inputStream = KaggleDataDownloader.class.getResourceAsStream("/kaggle.json");
            if (inputStream == null) {
                throw new RuntimeException("kaggle.json not found in resources.");
            }
            JsonNode rootNode = objectMapper.readTree(inputStream);
             KAGGLE_USERNAME = rootNode.get("username").asText();
             KAGGLE_KEY = rootNode.get("key").asText();

        }
        catch(Exception e){e.printStackTrace();}


        String datasetSlug = "datasets/mllion";
        String fileName = "One_Direction_All_Songs_wr.csv";

        downloadDatasetFile(datasetSlug, fileName);
    }

    public static void downloadDatasetFile(String datasetSlug, String fileName) throws IOException, InterruptedException {
        String kaggleApiUrl = String.format("https://www.kaggle.com/api/v1/datasets/download/%s/%s", datasetSlug, fileName);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(kaggleApiUrl))
                .header("Authorization", "Basic " + encodeCredentials(KAGGLE_USERNAME, KAGGLE_KEY))
                .build();

        HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(Paths.get(fileName)));

        if (response.statusCode() == 200) {
            System.out.println("Download complete: " + response.body().toAbsolutePath());
        } else {
            System.out.println("Failed to download. Status code: " + response.statusCode());
        }
    }

    private static String encodeCredentials(String username, String key) {
        String credentials = username + ":" + key;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
