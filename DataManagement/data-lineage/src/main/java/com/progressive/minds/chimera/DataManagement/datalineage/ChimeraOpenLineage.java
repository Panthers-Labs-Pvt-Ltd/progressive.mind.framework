package com.progressive.minds.chimera.DataManagement.datalineage;

import com.progressive.minds.chimera.DataManagement.datalineage.transports.TransportType;
import com.progressive.minds.chimera.DataManagement.datalineage.facets.RunFacets;
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

public class ChimeraOpenLineage  {

    public static RunEvent OpenLineageWrapper(RunEvent.EventType eventType,
                                              PipelineMetadata inPipelineMetadata,
                                              SparkSession inSparkSession,
                                              String transportType,
                                              Map<String,String> transportProperties) {


        OpenLineageClient client = new TransportType().set(transportType, transportProperties);
        RunEvent runEvent =buildEvent(RunEvent.EventType.START, inPipelineMetadata, inSparkSession);
        client.emit(runEvent);

        return runEvent;

 /*       String json = SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(event))
        lineageData.append(json).append(",\n")
        event = extractOperation(RunEvent.EventType.COMPLETE, event.getRun.getRunId, extractList, extractDf, sparkSession)
        json = SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(event))
        lineageData.append(json).append(",\n")*/

    }

    public static RunEvent buildEvent(RunEvent.EventType eventType, PipelineMetadata inPipelineMetadata,
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
