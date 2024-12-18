package com.progressive.minds.chimera.core.datalineage.models.Transport;

import com.progressive.minds.chimera.core.datalineage.SharedLogger;

import com.progressive.minds.chimera.core.datalineage.models.OpenLineageTransportTypes;
import io.openlineage.client.OpenLineageClient;
import io.openlineage.client.transports.gcplineage.GcpLineageTransport;
import io.openlineage.client.transports.gcplineage.GcpLineageTransportConfig;


import java.io.IOException;


/**
 * the GCP Transport emits OpenLineage events to a GCP.
 * Configuration
 * type - string, must be "gcplineage". Required.
 * endpoint - string, specifies the endpoint to which events are sent, default value is datalineage.googleapis.com:443. Optional.
 * projectId - string, the project quota identifier. If not provided, it is determined based on user credentials. Optional.
 * location - string, Dataplex location. Optional, default: "us".
 * credentialsFile - string, path to the Service Account credentials JSON file. Optional, if not provided Application Default Credentials are used
 * mode - enum that specifies the type of client used for publishing OpenLineage events to GCP Lineage service.
 * Possible values: sync (synchronous) or async (asynchronous). Optional, default: async.
 * <p>
 * Behavior
 * If the target file is absent, it's created.
 * Events are serialized to JSON, and then appended to a file, separated by newlines.
 * Intrinsic newline characters within the event JSON are eliminated to ensure one-line events.
 */
public class GCP implements OpenLineageTransportTypes.GCPAsTransport, SharedLogger {
    String LoggerTag = "[Open Lineage] - GCPAsTransport";

    public OpenLineageClient set(String inProjectId, String inLocation, String CredentialsFilePath) {
        LineageLogger.logInfo( "Setting GCP As Open Lineage Transport Type");
        GcpLineageTransportConfig gcplineageConfig = new GcpLineageTransportConfig();

        gcplineageConfig.setProjectId(inProjectId);
        gcplineageConfig.setLocation(inLocation);
        gcplineageConfig.setCredentialsFile(CredentialsFilePath);

        OpenLineageClient client = null;
        try {
            client = OpenLineageClient.builder()
                    .transport(
                            new GcpLineageTransport(gcplineageConfig))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return client;
    }

}