package com.progressive.minds.chimera.DataManagement.datalineage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.progressive.minds.chimera.consumer.DBAPIClient;
import com.progressive.minds.chimera.dto.PipelineMetadata;
import io.openlineage.client.OpenLineage;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.progressive.minds.chimera.DataManagement.datalineage.ChimeraOpenLineage.OpenLineageWrapper;
import static org.junit.jupiter.api.Assertions.*;

class ChimeraOpenLineageTest {

    @Test
    void openLineageWrapper() throws IOException, InterruptedException {
        OpenLineage.RunEvent.EventType eventType = OpenLineage.RunEvent.EventType.START;

        DBAPIClient dbClient = new DBAPIClient();
        PipelineMetadata inPipelineMetadata = dbClient.get("http://localhost:8080/api/v1/pipelineMetadata/Test_Pipeline",
                new TypeReference<PipelineMetadata>() {});

        SparkSession  inSparkSession = SparkSession.builder()
                .appName("Shared Spark Session")
                .master("local[*]")
                .getOrCreate();

        OpenLineageWrapper(eventType,inPipelineMetadata,  inSparkSession);
    }
}