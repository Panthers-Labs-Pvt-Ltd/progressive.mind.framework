package com.progressive.minds.chimera.core.datalineage.models.Transport;

import com.progressive.minds.chimera.core.datalineage.SharedLogger;
import com.progressive.minds.chimera.core.datalineage.models.OpenLineageTransportTypes;
import io.openlineage.client.OpenLineageClient;
import io.openlineage.client.transports.FileConfig;
import io.openlineage.client.transports.FileTransport;

/**
 * the FileTransport emits OpenLineageChimera events to a given file.
 * Configuration
 * type - string, must be "file". Required.
 * location - string specifying the path of the file. Required.
 *
 * Behavior
 * If the target file is absent, it's created.
 * Events are serialized to JSON, and then appended to a file, separated by newlines.
 * Intrinsic newline characters within the event JSON are eliminated to ensure one-line events.
 */
public class Files implements OpenLineageTransportTypes.FilesAsTransport, SharedLogger {
String LoggerTag = "[Open Lineage] - FilesAsTransport";

    public OpenLineageClient set(String inFilePath) {
        LineageLogger.logInfo( "Setting File As Open Lineage Transport Type");
        FileConfig fileConfig = new FileConfig(inFilePath);
        OpenLineageClient client = OpenLineageClient.builder()
                .transport(
                        new FileTransport(fileConfig))
                .build();
        return  client;
    }

}