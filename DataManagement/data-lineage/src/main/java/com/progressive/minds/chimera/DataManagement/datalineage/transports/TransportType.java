package com.progressive.minds.chimera.DataManagement.datalineage.transports;

import io.openlineage.client.OpenLineageClient;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

public class TransportType {

    public @NonNull OpenLineageClient set(String inTransportType, Map<String, String> transport) {
        OpenLineageClient client;

        switch (inTransportType.toLowerCase()) {
            case "kafka":
                String topicName = transport.get("topicName");
                String messageKey = transport.get("messageKey");
                List<String> bootstrapServers = List.of(transport.get("server").split(","));
                client = new kafkaTransport().set(topicName, messageKey, bootstrapServers);
                break;
            case "s3":
                String inS3EndPoint = transport.get("S3EndPoint");
                String inBucketName = transport.get("Bucket");
                String inFilePrefix = transport.get("Prefix");
                client = new s3Transport().set(inS3EndPoint, inBucketName, inFilePrefix);
                break;
            case "gcs":
                String inProjectId = transport.get("ProjectId");
                String inLocation = transport.get("Location");
                String CredentialsFile = transport.get("CredentialsFile");
                client = new GCPTransport().set(inProjectId, inLocation, CredentialsFile);
                break;
            case "file":
                String inFileName = transport.get("FileName");
                client = new filesTransport().set(inFileName);
                break;
            /*case "http":
                client = new httpTransport().set();
                break;
            */default:
                client = new consoleTransport().set();
        }
        return client;
    }
}
