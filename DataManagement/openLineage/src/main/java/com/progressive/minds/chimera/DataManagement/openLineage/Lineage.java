package com.progressive.minds.chimera.DataManagement.openLineage;

import com.progressive.minds.chimera.dto.ExtractMetadata;
import com.progressive.minds.chimera.dto.PipelineMetadata;
import io.openlineage.client.OpenLineage;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.spark.sql.SparkSession;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.time.ZonedDateTime.now;


public class Lineage {
    //UUID runId = UUIDUtils.generateNewUUID();
    static String PRODUCER_NAME = "https://github.com/OpenLineage/OpenLineage/tree/1.25.0/integration/spark";
    static ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
    static URI producer = URI.create(PRODUCER_NAME);
    static OpenLineage openLineageProducer = new OpenLineage(producer);

    public String getLineage(PipelineMetadata inPipelineMetadata,SparkSession inSparkSession)
    {
        UUID parentRunId = UUID.randomUUID();
        StringBuilder lineageJson = new StringBuilder("[");

/*        OpenLineage.ParentRunFacet parentRunFacet = openLineageProducer.newParentRunFacetBuilder()
                .run(openLineageProducer.newParentRunFacetRun(parentRunId))
                .job(openLineageProducer.newParentRunFacetJob(inPipelineMetadata.getPipelineName(),
                        inPipelineMetadata.getPipelineName())).put("processingMode", inPipelineMetadata.getProcessMode())
                .put("owningDomain", inPipelineMetadata.getOrgHierName()).put("executionEngine", "spark")
                .put("appName", inSparkSession.sparkContext().appName())
                .put("applicationId", inSparkSession.sparkContext().applicationId())
                .put("deployMode", inSparkSession.sparkContext().deployMode())
                .put("driverHost", inSparkSession.conf().get("spark.driver.host", "Not Available"))
                .put("userName", System.getProperty("user.name"))
                .put("userName", System.getProperty("user.name"))
                .build();

        OpenLineage.ParentRunFacetJobBuilder parentRunFacetJobBuilder = openLineageProducer.newParentRunFacetJobBuilder();
        parentRunFacetJobBuilder.name(inPipelineMetadata.getPipelineName()).namespace(inPipelineMetadata.getPipelineName())
                .build();*/

       // lineageJson.append(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(parentRunFacet))).append(",\n");

//        lineageJson.append(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(parentRunFacetJobBuilder))).append(",\n");


        List<ExtractMetadata> extracts= inPipelineMetadata.getExtractMetadata();

        Map<String, String> JobInformation = new HashMap<>();
        JobInformation.put("processingType", Optional.ofNullable(inPipelineMetadata.getProcessMode()).orElse("Batch"));
        JobInformation.put("jobType", "ETL");
        JobInformation.put("pipelineName", inPipelineMetadata.getPipelineName());
        JobInformation.put("domain", inPipelineMetadata.getOrgHierName());
        JobInformation.put("integrationType", "Spark");
        JobInformation.put("jobDocumentation", inPipelineMetadata.getPipelineDescription());
        AtomicReference<List<OpenLineage.InputDataset>> Inputs = new AtomicReference<>(new ArrayList<>());
        AtomicReference<List<OpenLineage.OutputDataset>> Outputs = new AtomicReference<>(new ArrayList<>());
        Pair<List<OpenLineage.InputDataset>, List<OpenLineage.OutputDataset>> extractLineageMap =
                Pair.of(new ArrayList<>(), new ArrayList<>());

        extracts.forEach(extractMetadata ->
        {
            try {

                Inputs.set(DataLineageExtracts.get(OpenLineage.RunEvent.EventType.START, extractMetadata, JobInformation, inSparkSession, openLineageProducer).getLeft());
                Outputs.set(DataLineageExtracts.get(OpenLineage.RunEvent.EventType.START, extractMetadata, JobInformation, inSparkSession, openLineageProducer).getRight());;

                // lineageJson.append(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(StartEvent))).append(",\n");
/*
                extractMap= DataLineageExtracts.get(OpenLineage.RunEvent.EventType.COMPLETE,
                        runUUID,extractMetadata, JobInformation,inSparkSession, openLineageProducer);
                lineageJson.append(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(CompleteEvent))).append(",\n");*/
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
         });
        System.out.print("Extract " + extracts.size());
//        lineageJson.delete(lineageJson.length() - 2, lineageJson.length());
  //      lineageJson.append("]");
    //    return lineageJson.toString();
        return "";
    }
}
