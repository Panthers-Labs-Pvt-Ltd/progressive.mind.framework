package com.progressive.minds.chimera.DataManagement.datalineage.facets;

import io.openlineage.client.OpenLineage;

import java.util.List;
import java.util.UUID;

import static java.time.ZonedDateTime.now;

public class RunFacets {

    public static OpenLineage.RunFacets getRunFacet(OpenLineage openLineageProducer, UUID runId) {
        return openLineageProducer.newRunFacetsBuilder()
                .nominalTime(openLineageProducer.newNominalTimeRunFacetBuilder()
                        .nominalStartTime(now())
                        .nominalEndTime(now())
                        .build())
                .build();
    }

    public static OpenLineage.Run getRun(OpenLineage openLineageProducer, UUID runId) {
        return openLineageProducer.newRunBuilder()
                .runId(runId)
                .facets(getRunFacet(openLineageProducer, runId))
                .build();
    }

    public static OpenLineage.RunEvent getRunEvent(OpenLineage openLineageProducer, UUID runId, OpenLineage.Job job,
                                                   List<OpenLineage.InputDataset> inputs, List<OpenLineage.OutputDataset> outputs) {
        return openLineageProducer.newRunEventBuilder()
                .eventType(OpenLineage.RunEvent.EventType.START)
                .eventTime(now())
                .run(getRun(openLineageProducer,runId))
                .job(job)
                .inputs(inputs)
                .outputs(outputs)
                .build();
    }
}
