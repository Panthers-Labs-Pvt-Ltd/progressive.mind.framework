package com.progressive.minds.chimera.DataManagement.datalineage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.progressive.minds.chimera.DataManagement.datalineage.transports.TransportType;
import com.progressive.minds.chimera.DataManagement.datalineage.facets.RunFacets;
import com.progressive.minds.chimera.DataManagement.datalineage.utils.TransformEvents;
import com.progressive.minds.chimera.DataManagement.datalineage.utils.extractsEvents;
import com.progressive.minds.chimera.DataManagement.datalineage.utils.persistEvents;
import com.progressive.minds.chimera.dto.PipelineMetadata;
import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineageClient;
import io.openlineage.client.OpenLineage.RunEvent;
import io.openlineage.client.OpenLineage.InputDataset;
import io.openlineage.client.OpenLineage.JobFacets;
import io.openlineage.client.OpenLineage.OutputDataset;
import io.openlineage.client.OpenLineageClientUtils;
import io.openlineage.client.utils.UUIDUtils;
import org.apache.spark.sql.SparkSession;
import za.co.absa.cobrix.spark.cobol.utils.SparkUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static com.progressive.minds.chimera.DataManagement.datalineage.facets.DatasetFacets.InputDatasetFacet;
import static com.progressive.minds.chimera.DataManagement.datalineage.facets.DatasetFacets.setOutputDataset;
import static com.progressive.minds.chimera.DataManagement.datalineage.facets.JobFacets.JobStartFacet;
import static com.progressive.minds.chimera.DataManagement.datalineage.facets.JobFacets.getJobFacet;

public class ChimeraOpenLineage {

    // Method to merge JSON nodes into an ObjectNode
    private static void mergeJsonNodes(ObjectNode target, JsonNode source) {
        if (source != null && source.isObject()) {
            target.setAll((ObjectNode) source);
        }
    }

    public static String OpenLineageWrapper(RunEvent.EventType eventType,
                                            PipelineMetadata inPipelineMetadata,
                                            SparkSession inSparkSession,
                                            String transportType,
                                            Map<String, String> transportProperties) throws JsonProcessingException {

        String EventJson;
        OpenLineageClient client = new TransportType().set(transportType, transportProperties);
        String PRODUCER_NAME = "https://github.com/OpenLineage/OpenLineage/tree/1.25.0/integration/spark";
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        URI producer = URI.create(PRODUCER_NAME);
        OpenLineage openLineageProducer = new OpenLineage(producer);
        UUID runId = UUIDUtils.generateNewUUID();
        String JobNamespace = inPipelineMetadata.getOrgHierName() + "_" + inPipelineMetadata.getPipelineName();
        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> JobInformation = new HashMap<>();
        JobInformation.put("ProcessingType", Optional.ofNullable(inPipelineMetadata.getProcessMode()).orElse("Batch"));
        JobInformation.put("JobType", "ETL");
        JobInformation.put("PipelineName", inPipelineMetadata.getPipelineName());
        JobInformation.put("Domain", inPipelineMetadata.getOrgHierName());
        JobInformation.put("IntegrationType", "Spark");
        JobInformation.put("JobDocumentation", inPipelineMetadata.getPipelineDescription());

        JobFacets jobFacets = getJobFacet(openLineageProducer, JobInformation);
        JsonNode jobFacetsNode = mapper.readTree(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(jobFacets)));

        OpenLineage.Job JobStartFacet = JobStartFacet(openLineageProducer, JobNamespace, inPipelineMetadata.getPipelineName(), jobFacets);
        JsonNode JobStartFacetNode = mapper.readTree(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(JobStartFacet)));

        RunEvent extractStartEvent = extractsEvents.buildExtractEvents(RunEvent.EventType.START,
                openLineageProducer, inPipelineMetadata.getPipelineName(), inPipelineMetadata, inSparkSession, jobFacets);
        JsonNode extractStartEventNode = mapper.readTree(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(extractStartEvent)));

        RunEvent extractStopEvent = extractsEvents.buildExtractEvents(RunEvent.EventType.COMPLETE,
                openLineageProducer, inPipelineMetadata.getPipelineName(), inPipelineMetadata, inSparkSession, jobFacets);
        JsonNode extractStopEventNode = mapper.readTree(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(extractStopEvent)));


        RunEvent transformStartEvent = TransformEvents.buildExtractEvents(RunEvent.EventType.START,
                openLineageProducer, inPipelineMetadata.getPipelineName(), inPipelineMetadata, inSparkSession, jobFacets);
        JsonNode transformStartEventNode = mapper.readTree(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(transformStartEvent)));

        RunEvent transformStopEvent = TransformEvents.buildExtractEvents(RunEvent.EventType.COMPLETE,
                openLineageProducer, inPipelineMetadata.getPipelineName(), inPipelineMetadata, inSparkSession, jobFacets);
        JsonNode transformStopEventNode = mapper.readTree(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(transformStopEvent)));

        RunEvent persistStartEvent = persistEvents.buildExtractEvents(RunEvent.EventType.START,
                openLineageProducer, inPipelineMetadata.getPipelineName(), inPipelineMetadata, inSparkSession, jobFacets);
        JsonNode persistStartEventNode = mapper.readTree(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(persistStartEvent)));

        RunEvent persistStopEvent = persistEvents.buildExtractEvents(RunEvent.EventType.COMPLETE,
                openLineageProducer, inPipelineMetadata.getPipelineName(), inPipelineMetadata, inSparkSession, jobFacets);
        JsonNode persistStopEventNode = mapper.readTree(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(persistStopEvent)));

        ObjectNode mergedJson = mapper.createObjectNode();

        // Merge all JSON nodes
        mergeJsonNodes(mergedJson, jobFacetsNode);
        mergeJsonNodes(mergedJson, JobStartFacetNode);
        mergeJsonNodes(mergedJson, extractStartEventNode);
        mergeJsonNodes(mergedJson, extractStopEventNode);
        mergeJsonNodes(mergedJson, transformStartEventNode);
        mergeJsonNodes(mergedJson, transformStopEventNode);
        mergeJsonNodes(mergedJson, persistStartEventNode);
        mergeJsonNodes(mergedJson, persistStopEventNode);
        // Print the final merged JSON
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mergedJson));
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mergedJson);
    }
}

    /*public static RunEvent buildEvent(RunEvent.EventType eventType, PipelineMetadata inPipelineMetadata,
                                      SparkSession inSparkSession) {
        String PRODUCER_NAME = "https://github.com/OpenLineage/OpenLineage/tree/1.25.0/integration/spark";
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        URI producer = URI.create(PRODUCER_NAME);
        OpenLineage openLineageProducer = new OpenLineage(producer);
        UUID runId = UUIDUtils.generateNewUUID();
        String JobNamespace = inPipelineMetadata.getOrgHierName() + "_" + inPipelineMetadata.getPipelineName();

        Map<String, String> JobInformation = new HashMap<>();
        JobInformation.put("ProcessingType" , Optional.ofNullable(inPipelineMetadata.getProcessMode()).orElse("Batch"));
        JobInformation.put("JobType" , "ETL");
        JobInformation.put("PipelineName" , inPipelineMetadata.getPipelineName());
        JobInformation.put("Domain" , inPipelineMetadata.getOrgHierName());
        JobInformation.put("IntegrationType" , "Spark");
        JobInformation.put("JobDocumentation" , inPipelineMetadata.getPipelineDescription());

        JobFacets jobFacets = getJobFacet(openLineageProducer, JobInformation);

        OpenLineage.Job JobStartFacet =JobStartFacet(openLineageProducer, JobNamespace, inPipelineMetadata.getPipelineName(), jobFacets);
        List<InputDataset> inputs = new ArrayList<>();
        inPipelineMetadata.getExtractMetadata().forEach(extractMetadata ->
        {
            try {
                inputs.addAll(
                        InputDatasetFacet(openLineageProducer,extractMetadata,inPipelineMetadata, inSparkSession)
                );
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });


        List<OutputDataset> outputs = new ArrayList<>();
        inPipelineMetadata.getPersistMetadata().forEach(persistMetadata ->
        {
            try {
                outputs.add(setOutputDataset(openLineageProducer, persistMetadata, inPipelineMetadata,
                         inSparkSession));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        RunEvent runStateUpdate = RunFacets.getRunEvent(openLineageProducer,  runId, JobStartFacet,
                inputs,  outputs);
        System.out.println(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(runStateUpdate)));
        return runStateUpdate;
    }
    }
*/
