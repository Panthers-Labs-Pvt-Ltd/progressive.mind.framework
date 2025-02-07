package com.progressive.minds.chimera.DataManagement.datalineage.models;


import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import io.openlineage.client.OpenLineageClient;
import io.openlineage.client.transports.HttpConfig;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.List;
import java.util.Map;

public interface OpenLineageTransportTypes {

    ChimeraLogger LineageLogger =  ChimeraLoggerFactory.getLogger(OpenLineageTransportTypes.class);

    interface FilesAsTransport {
        OpenLineageClient set(String inFilePath);
    }

    interface KafkaAsTransport {
        OpenLineageClient set(String topicName, String messageKey, List<String> bootstrapServers);
    }

    interface ConsoleAsTransport {
        OpenLineageClient set();
    }

    interface HTTPAsTransport {
        OpenLineageClient set(URI url, @Nullable String endpoint,
                              @Nullable Integer timeoutInMillis, @Nullable String APIKey,
                              @Nullable Map<String, String> urlParams, @Nullable
                              Map<String, String> headers, @Nullable HttpConfig.Compression compression);
    }

    interface GCPAsTransport {
        OpenLineageClient set(String inProjectId, String inLocation, String CredentialsFilePath);
    }

    interface S3Transport {
        OpenLineageClient set(String inS3EndPoint, String inBucketName, String inFilePrefix);
    }
}
