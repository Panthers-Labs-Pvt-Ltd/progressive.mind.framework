package com.progressive.minds.chimera.core.datalineage.facets;

import io.openlineage.client.OpenLineage;

import java.util.List;
import java.util.UUID;

import static java.time.ZonedDateTime.now;

public class RunFacets {

    public OpenLineage.RunFacets RunFacet(OpenLineage openLineageProducer, UUID runId) {
        return openLineageProducer.newRunFacetsBuilder()
                .nominalTime(openLineageProducer.newNominalTimeRunFacetBuilder()
                        .nominalStartTime(now())
                        .nominalEndTime(now())
                        .build())
                .build();
    }

    public OpenLineage.Run Run(OpenLineage openLineageProducer, UUID runId) {
        return openLineageProducer.newRunBuilder()
                .runId(runId)
                .facets(RunFacet(openLineageProducer, runId))
                .build();
    }

    public OpenLineage.RunEvent RunEvent(OpenLineage openLineageProducer,  UUID runId, OpenLineage.Job job,
                                         List<OpenLineage.InputDataset> inputs, List<OpenLineage.OutputDataset> outputs) {
        return openLineageProducer.newRunEventBuilder()
                .eventType(OpenLineage.RunEvent.EventType.START)
                .eventTime(now())
                .run(Run(openLineageProducer,runId))
                .job(job)
                .inputs(inputs)
                .outputs(outputs)
                .build();
    }
}
