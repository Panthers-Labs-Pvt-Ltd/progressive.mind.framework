package com.progressive.minds.chimera.core.databaseOps.utility;

//import com.google.cloud.secretmanager.v1.AccessSecretVersionRequest;
//import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
//import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
//import com.azure.identity.DefaultAzureCredentialBuilder;
//import com.azure.security.keyvault.secrets.SecretClient;
//import com.azure.security.keyvault.secrets.SecretClientBuilder;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class CloudCredentials {
    private static Map<String, String> getAWSCredentials(String secretName) {
        // Create Secrets Manager client
        SecretsManagerClient secretsClient = SecretsManagerClient.create();

        // Build request to fetch secret
        GetSecretValueRequest secretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
        GetSecretValueResponse secretValueResponse = secretsClient.getSecretValue(secretValueRequest);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(secretValueResponse.secretString(), Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse secret from AWS Secrets Manager", e);
        }
    }

//    private static Map<String, String> getGCPCredentials(String secretName, String inProjectId) throws Exception {
//        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
//            // Access the secret version
//            String projectId = inProjectId; // Replace with your GCP project ID
//            String secretPath = String.format("projects/%s/secrets/%s/versions/latest", projectId, secretName);
//            AccessSecretVersionRequest request = AccessSecretVersionRequest.newBuilder()
//                    .setName(secretPath)
//                    .build();
//
//            // Fetch secret
//            AccessSecretVersionResponse response = client.accessSecretVersion(request);
//            String secretJson = response.getPayload().getData().toStringUtf8();
//
//            // Parse JSON into a Map
//            ObjectMapper objectMapper = new ObjectMapper();
//            return objectMapper.readValue(secretJson, Map.class);
//        }
//    }
//
//    private static Map<String, String> getAZURECredentials(String secretName, String keyVaultUrl) throws Exception {
//        // Create a Secret Client
//        // String keyVaultUrl = "https://<your-key-vault-name>.vault.azure.net/"; // Replace with your Key Vault URL
//        SecretClient secretClient = new SecretClientBuilder()
//                .vaultUrl(keyVaultUrl)
//                .credential(new DefaultAzureCredentialBuilder().build())
//                .buildClient();
//
//        // Fetch the secret value
//        String secretValue = secretClient.getSecret(secretName).getValue();
//
//        // Parse the JSON secret into a Map
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readValue(secretValue, Map.class);
//    }

    public static Map<String, String> getCredentials(String CloudType, String secretName, String ProjectKey) throws Exception {
        if (CloudType.equalsIgnoreCase("aws")) {
            return getAWSCredentials(secretName);
        } else
            throw new RuntimeException("Unsupported Cloud Type");
//        else if (CloudType.equalsIgnoreCase("azure")) {
//            return getAZURECredentials(secretName, ProjectKey);
//        } else if (CloudType.equalsIgnoreCase("gcp")) {
//            return getGCPCredentials(secretName, ProjectKey);
//        } else {
//            return null;
//        }
    }
}
