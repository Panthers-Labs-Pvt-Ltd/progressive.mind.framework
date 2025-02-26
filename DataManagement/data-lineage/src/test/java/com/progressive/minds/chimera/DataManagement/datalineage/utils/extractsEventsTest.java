package com.progressive.minds.chimera.DataManagement.datalineage.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.progressive.minds.chimera.consumer.DBAPIClient;
import com.progressive.minds.chimera.dto.PipelineMetadata;
import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineageClientUtils;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;
import za.co.absa.cobrix.spark.cobol.utils.SparkUtils;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.progressive.minds.chimera.DataManagement.datalineage.facets.JobFacets.getJobFacet;
import static org.junit.jupiter.api.Assertions.*;

class extractsEventsTest {
    SparkSession spark = SparkSession.builder()
            .appName("Shared Spark Session")
            .master("local[*]")
            .getOrCreate();
    @Test
    void buildExtractEvents() throws IOException, InterruptedException {
        String PRODUCER_NAME = "https://github.com/OpenLineage/OpenLineage/tree/1.16.0/integration/spark";
        URI producer = URI.create(PRODUCER_NAME);
        OpenLineage openLineageProducer = new OpenLineage(producer);
                
        OpenLineage.RunEvent.EventType eventType = OpenLineage.RunEvent.EventType.START;
        System.setProperty("API_CLIENT", "chimera_api_client");
        System.setProperty("API_SECRET", "yhKj2HkNBpyv9ZgV9oqPxHcZOPEb3uBg");
        DBAPIClient dbClient = new DBAPIClient();
        PipelineMetadata inPipelineMetadata = dbClient.get("http://localhost:8888/api/v1/pipelineMetadata/Test_Pipeline",
                new TypeReference<PipelineMetadata>() {});
        Map<String, String> lineageMap = new HashMap<>();
        lineageMap.putIfAbsent("FileName" , "/home/manish/lineage.json");

        Map<String, String> JobInformation = new HashMap<>();
        JobInformation.put("ProcessingType" , Optional.ofNullable(inPipelineMetadata.getProcessMode()).orElse("Batch"));
        JobInformation.put("JobType" , "ETL");
        JobInformation.put("PipelineName" , inPipelineMetadata.getPipelineName());
        JobInformation.put("Domain" , inPipelineMetadata.getOrgHierName());
        JobInformation.put("IntegrationType" , "Spark");
        JobInformation.put("JobDocumentation" , inPipelineMetadata.getPipelineDescription());

        OpenLineage.JobFacets jobFacets = getJobFacet(openLineageProducer, null,JobInformation);


        OpenLineage.RunEvent event = extractsEvents.buildExtractEvents(eventType, openLineageProducer, "Test_Pipeline",
                inPipelineMetadata,spark,jobFacets);
        System.out.println(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(event)));
    }
}
