package com.progressive.minds.chimera.DataManagement.openLineage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.progressive.minds.chimera.DataManagement.datalineage.facets.RunFacets;
import com.progressive.minds.chimera.DataManagement.datalineage.utils.extractsEvents;
import com.progressive.minds.chimera.dto.ExtractMetadata;
import com.progressive.minds.chimera.dto.PipelineMetadata;
import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineageClientUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.spark.sql.SparkSession;
import za.co.absa.cobrix.spark.cobol.utils.SparkUtils;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.progressive.minds.chimera.DataManagement.datalineage.facets.JobFacets.getJobFacet;
import static com.progressive.minds.chimera.DataManagement.datalineage.facets.RunFacets.getRun;
import static java.time.ZonedDateTime.now;


public class Lineage {
    //UUID runId = UUIDUtils.generateNewUUID();
    static String PRODUCER_NAME = "https://github.com/OpenLineage/OpenLineage/tree/1.25.0/integration/spark";
    static ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
    static URI producer = URI.create(PRODUCER_NAME);
    static OpenLineage openLineageProducer = new OpenLineage(producer);

    public String getLineage(PipelineMetadata inPipelineMetadata, SparkSession inSparkSession) throws Exception {
        UUID parentRunId = UUID.randomUUID();
        StringBuilder lineageJson = new StringBuilder("[");

        Map<String, String>  JobMap = new HashMap<>();
        JobMap.put("processingType" , Optional.ofNullable(inPipelineMetadata.getProcessMode()).orElse("Batch"));
        JobMap.put("jobType" , "ETL");
        JobMap.put("pipelineName" , inPipelineMetadata.getPipelineName());
        JobMap.put("domain" , inPipelineMetadata.getOrgHierName());
        JobMap.put("integrationType" , "Spark");
        JobMap.put("jobDocumentation" , inPipelineMetadata.getPipelineDescription());
        JobMap.put("processingMode", inPipelineMetadata.getProcessMode());
        JobMap.put("owningDomain", inPipelineMetadata.getOrgHierName());
        JobMap.put("executionEngine", "spark");
        JobMap.put("appName", inSparkSession.sparkContext().appName());
        JobMap.put("applicationId", inSparkSession.sparkContext().applicationId());
        JobMap.put("deployMode", inSparkSession.sparkContext().deployMode());
        JobMap.put("driverHost", inSparkSession.conf().get("spark.driver.host", "Not Available"));
        JobMap.put("userName", System.getProperty("user.name"));

        List<OpenLineage.InputDataset> Inputs;
        List<OpenLineage.OutputDataset> Outputs;

        Pair<List<OpenLineage.InputDataset>, List<OpenLineage.OutputDataset>> extractLineageMap =
                DataLineageExtracts.get(OpenLineage.RunEvent.EventType.START, inPipelineMetadata,inSparkSession,
                        openLineageProducer);
        Inputs = extractLineageMap.getLeft();
        Outputs = extractLineageMap.getRight();

        OpenLineage.JobFacets jobFacets = getJobFacet(openLineageProducer, null, JobMap);
        OpenLineage.Job job = openLineageProducer.newJob(inPipelineMetadata.getPipelineName(),
                inPipelineMetadata.getPipelineName(), jobFacets);
        OpenLineage.RunEvent runStateUpdate = RunFacets.getRunEvent(openLineageProducer,  parentRunId,job,
                Inputs,  Outputs);
        String json = SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(runStateUpdate));
        System.out.print(json.toString());
        return "";
    }
}


