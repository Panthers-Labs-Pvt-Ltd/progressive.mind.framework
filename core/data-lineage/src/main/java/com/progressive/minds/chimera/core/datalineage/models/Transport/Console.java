package com.progressive.minds.chimera.core.datalineage.models.Transport;

import com.progressive.minds.chimera.core.datalineage.SharedLogger;
import com.progressive.minds.chimera.core.datalineage.models.OpenLineageTransportTypes;
import io.openlineage.client.OpenLineageClient;

import io.openlineage.client.transports.ConsoleTransport;


/**
 * This straightforward transport emits OpenLineage events directly to the console through a logger.
 *
 * Configuration
 *No additional configuration is required.
 *
 *Behavior
 * Events are serialized to JSON. Then each event is logged with INFO level to logger with name ConsoleTransport.
 */
public class Console implements OpenLineageTransportTypes.ConsoleAsTransport, SharedLogger {
String LoggerTag = "[Open Lineage] - ConsoleAsTransport";

    public OpenLineageClient set() {
        LineageLogger.logInfo("Setting Console As Open Lineage Transport Type");

        OpenLineageClient client = OpenLineageClient.builder()
                .transport(new ConsoleTransport())
                .build();
        return  client;
    }

}