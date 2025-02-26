package com.progressive.minds.chimera.DataManagement.datalineage;

import com.progressive.minds.chimera.DataManagement.datalineage.namespace.UrnUtility;
import com.progressive.minds.chimera.DataManagement.datalineage.transports.TransportType;
import com.progressive.minds.chimera.DataManagement.datalineage.facets.RunFacets;
import com.progressive.minds.chimera.dto.*;
import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineageClient;
import io.openlineage.client.OpenLineage.RunEvent;
import io.openlineage.client.OpenLineage.InputDataset;
import io.openlineage.client.OpenLineage.JobFacets;
import io.openlineage.client.OpenLineage.OutputDataset;
import io.openlineage.client.OpenLineageClientUtils;
import io.openlineage.client.utils.UUIDUtils;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import za.co.absa.cobrix.spark.cobol.utils.SparkUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static com.progressive.minds.chimera.DataManagement.datalineage.facets.DatasetFacets.*;
import static com.progressive.minds.chimera.DataManagement.datalineage.facets.JobFacets.JobStartFacet;
import static com.progressive.minds.chimera.DataManagement.datalineage.facets.JobFacets.getJobFacet;
import static com.progressive.minds.chimera.DataManagement.datalineage.facets.RunFacets.getRun;
import static com.progressive.minds.chimera.DataManagement.datalineage.utils.Utility.getDataFrameSchema;
import static java.time.ZonedDateTime.now;

public class Lineage {

    public static RunEvent OpenLineageWrapper(RunEvent.EventType eventType,
                                              PipelineMetadata inPipelineMetadata,
                                              SparkSession inSparkSession,
                                              String transportType,
                                              Map<String, String> transportProperties) {


        OpenLineageClient client = new TransportType().set(transportType, transportProperties);
        RunEvent runEvent = buildEvent(RunEvent.EventType.START, inPipelineMetadata, inSparkSession);
        client.emit(runEvent);

        return runEvent;


/*       String json = SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(event))
        lineageData.append(json).append(",\n")
        event = extractOperation(RunEvent.EventType.COMPLETE, event.getRun.getRunId, extractList, extractDf, sparkSession)
        json = SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(event))
        lineageData.append(json).append(",\n")

*/
    }

    public static RunEvent buildEvent(RunEvent.EventType eventType, PipelineMetadata inPipelineMetadata,
                                      SparkSession inSparkSession) {
        String PRODUCER_NAME = "https://github.com/OpenLineage/OpenLineage/tree/1.25.0/integration/spark";
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        URI producer = URI.create(PRODUCER_NAME);
        OpenLineage openLineageProducer = new OpenLineage(producer);
        UUID runId = UUIDUtils.generateNewUUID();

        List<ExtractMetadata> extractMetadataList = inPipelineMetadata.getExtractMetadata();
        List<TransformMetadataConfig> transformMetadataList = inPipelineMetadata.getTransformMetadata();
        List<PersistMetadata> persistMetadataList = inPipelineMetadata.getPersistMetadata();
        OrganizationHierarchy orgList = inPipelineMetadata.getOrg();
        String PipelineNamespace = String.format("urn:li:dataFlow:(%s,%s,%s)",
                inPipelineMetadata.getPipelineName(), inPipelineMetadata.getOrgHierName(), inPipelineMetadata.getProcessMode());

        Map<String, String> JobInformation = new HashMap<>();
        JobInformation.put("ProcessingType", Optional.ofNullable(inPipelineMetadata.getProcessMode()).orElse("Batch"));
        JobInformation.put("JobType", "ETL");
        JobInformation.put("PipelineName", inPipelineMetadata.getPipelineName());
        JobInformation.put("Domain", inPipelineMetadata.getOrgHierName());
        JobInformation.put("IntegrationType", "Spark");
        JobInformation.put("JobDocumentation", inPipelineMetadata.getPipelineDescription());
        List<InputDataset> inputs = new ArrayList<>();
        List<OutputDataset> outputs = new ArrayList<>();
        JobFacets jobFacets = getJobFacet(openLineageProducer, null,JobInformation);
        //String pipelineNamespace = String.format("urn:li:dataflow:(%s,extract_%s)", PipelineNamespace, "PROD");
        String pipelineNamespace = UrnUtility.createUrn("pipeline","Spark", inPipelineMetadata.getPipelineName(), "PROD", "PROD" );
        OpenLineage.Job JobStartFacet =JobStartFacet(openLineageProducer, pipelineNamespace, inPipelineMetadata.getPipelineName(), jobFacets);

        extractMetadataList.forEach(extract ->
        {
            String JobNamespace = String.format("urn:li:dataJob:(%s,extract_%s)", PipelineNamespace, extract.getSequenceNumber());
            String datasetUrn = String.format("urn:li:dataset:(urn:li:dataPlatform:s3,project/root/events/logging_events_bckp,PROD)", PipelineNamespace, extract.getSequenceNumber());
            Map<String, String> extractInformation = new HashMap<>();
            extractInformation.put("Source Type", extract.getExtractSourceType());
            extractInformation.put("Sub Source Type", extract.getExtractSourceSubType());
            extractInformation.put("JobType", "Ingestion");
            JobFacets extractFacets = getJobFacet(openLineageProducer, null, extractInformation);
            //OpenLineage.Job JobStartFacet = JobStartFacet(openLineageProducer, JobNamespace, inPipelineMetadata.getPipelineName(), extractFacets);
            try {
                inputs.addAll(InputDatasetFacet(openLineageProducer, extract, inPipelineMetadata, inSparkSession));
                StructType inSchema = getDataFrameSchema(inSparkSession, extract.getDataframeName());

                OpenLineage.DatasetFacets datasetFacets = openLineageProducer.newDatasetFacetsBuilder()
                                        .schema(getDatasourceSchema(openLineageProducer, inSchema))
                                        .dataSource(getDatasourceDatasetFacet(openLineageProducer,extract.getDataSourceConnectionName(), extract.getDataSource().getDataSourceType(),extractInformation))
                                        .documentation(getDocumentationDatasetFacet(openLineageProducer,extract.getPipelineName(), extractInformation))
                                        //.storage(openLineageProducer.newStorageDatasetFacet("storageLayer", extract.getDataSourceConnection().getDataSourceSubType()))
                                        .build();
                OpenLineage.OutputDatasetFacet outputDatasetFacet = openLineageProducer.newOutputDatasetFacet();

                OpenLineage.OutputDatasetOutputFacets outputFacets = openLineageProducer.newOutputDatasetOutputFacetsBuilder()
                        .put("DatasetName", outputDatasetFacet)
                        .build();

                outputs.add(openLineageProducer
                        .newOutputDatasetBuilder()
                        .facets(datasetFacets)
                        .outputFacets(outputFacets)
                        .namespace(datasetUrn)
                        .name(extract.getDataframeName())
                        .outputFacets(outputFacets)
                        .facets(datasetFacets).build());


            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
        RunEvent runStateUpdate = RunFacets.getRunEvent(openLineageProducer,  runId, JobStartFacet,
                inputs,  outputs);

        String rs = SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(runStateUpdate));
        String rn= String.valueOf(com.progressive.minds.chimera.DataManagement.datalineage.facets.JobFacets
                .JobEndFacet(openLineageProducer, "pipeline", pipelineNamespace.toString()));

        System.out.println(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(runStateUpdate)));
        return runStateUpdate;
    }
}
