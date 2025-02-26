package com.progressive.minds.chimera.DataManagement.openLineage;


import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineageClient;
import io.openlineage.client.OpenLineage.RunEvent;
import io.openlineage.client.OpenLineage.InputDataset;
import io.openlineage.client.OpenLineage.Job;
import io.openlineage.client.OpenLineage.JobFacets;
import io.openlineage.client.OpenLineage.OutputDataset;
import io.openlineage.client.OpenLineage.Run;
import io.openlineage.client.OpenLineage.RunFacets;
import io.openlineage.client.OpenLineage.RunEvent.EventType;
import io.openlineage.client.transports.ConsoleTransport;
import io.openlineage.client.transports.HttpTransport;
import io.openlineage.client.utils.UUIDUtils;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * My first openlinage client code
 */
public class OpenLineageClientTest
{
    public static void main( String[] args )
    {
        try {
/*            OpenLineageClient client = OpenLineageClient.builder()
                    .transport(new ConsoleTransport()).build();*/

            OpenLineageClient client = OpenLineageClient.builder()
                    .transport(
                            HttpTransport.builder()
                                    .uri("http://localhost:1337")
                                    .build())
                    .build();
            // create one start event for testing
            RunEvent event = buildEvent(EventType.START);

            // emit the event
            client.emit(event);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // sample code to build event
    public static RunEvent buildEvent(EventType eventType) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        URI producer = URI.create("producer");
        OpenLineage ol = new OpenLineage(producer);
        UUID runId = UUIDUtils.generateNewUUID();

        // run facets
        RunFacets runFacets =
                ol.newRunFacetsBuilder()
                        .nominalTime(
                                ol.newNominalTimeRunFacetBuilder()
                                        .nominalStartTime(now)
                                        .nominalEndTime(now)
                                        .build())
                        .build();

        // a run is composed of run id, and run facets
        Run run = ol.newRunBuilder().runId(runId).facets(runFacets).build();

        // job facets
        JobFacets jobFacets = ol.newJobFacetsBuilder().build();

        // job
        String name = "jobName";
        String namespace = "namespace";
        Job job = ol.newJobBuilder().namespace(namespace).name(name).facets(jobFacets).build();

        // input dataset
        List<InputDataset> inputs =
                Arrays.asList(
                        ol.newInputDatasetBuilder()
                                .namespace("ins")
                                .name("input")
                                .facets(
                                        ol.newDatasetFacetsBuilder()
                                                .version(ol.newDatasetVersionDatasetFacet("input-version"))
                                                .build())
                                .inputFacets(
                                        ol.newInputDatasetInputFacetsBuilder()
                                                .dataQualityMetrics(
                                                        ol.newDataQualityMetricsInputDatasetFacetBuilder()
                                                                .rowCount(10L)
                                                                .bytes(20L)
                                                                .columnMetrics(
                                                                        ol.newDataQualityMetricsInputDatasetFacetColumnMetricsBuilder()
                                                                                .put(
                                                                                        "mycol",
                                                                                        ol.newDataQualityMetricsInputDatasetFacetColumnMetricsAdditionalBuilder()
                                                                                                .count(10D)
                                                                                                .distinctCount(10L)
                                                                                                .max(30D)
                                                                                                .min(5D)
                                                                                                .nullCount(1L)
                                                                                                .sum(3000D)
                                                                                                .quantiles(
                                                                                                        ol.newDataQualityMetricsInputDatasetFacetColumnMetricsAdditionalQuantilesBuilder()
                                                                                                                .put("25", 52D)
                                                                                                                .build())
                                                                                                .build())
                                                                                .build())
                                                                .build())
                                                .build())
                                .build());
        // output dataset
        List<OutputDataset> outputs =
                Arrays.asList(
                        ol.newOutputDatasetBuilder()
                                .namespace("ons")
                                .name("output")
                                .facets(
                                        ol.newDatasetFacetsBuilder()
                                                .version(ol.newDatasetVersionDatasetFacet("output-version"))
                                                .build())
                                .outputFacets(
                                        ol.newOutputDatasetOutputFacetsBuilder()
                                                .outputStatistics(ol.newOutputStatisticsOutputDatasetFacet(10L, 20L, 10L))
                                                .build())
                                .build());

        // run state update which encapsulates all - with START event in this case
        RunEvent runStateUpdate =
                ol.newRunEventBuilder()
                        .eventType(OpenLineage.RunEvent.EventType.START)
                        .eventTime(now)
                        .run(run)
                        .job(job)
                        .inputs(inputs)
                        .outputs(outputs)
                        .build();

        return runStateUpdate;
    }
}