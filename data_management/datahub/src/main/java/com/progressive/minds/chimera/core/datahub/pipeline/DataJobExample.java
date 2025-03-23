package com.progressive.minds.chimera.core.datahub.pipeline;


import com.linkedin.common.*;
import com.linkedin.common.url.Url;
import com.linkedin.common.urn.DataJobUrn;
import com.linkedin.common.urn.DataFlowUrn;
import com.linkedin.common.urn.DatasetUrn;
import com.linkedin.datajob.DataFlowInfo;
import com.linkedin.datajob.DataJobInfo;
import com.linkedin.datajob.DataJobInputOutput;
import com.linkedin.metadata.aspect.DataJobAspect;
import com.linkedin.metadata.aspect.DataJobAspectArray;
import com.linkedin.metadata.snapshot.DataJobSnapshot;
import com.linkedin.mxe.MetadataChangeProposal;
import datahub.event.MetadataChangeProposalWrapper;


import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static com.progressive.minds.chimera.core.datahub.common.genericUtils.createProposal;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.emitProposal;

public class DataJobExample {

    public static void main(String[] args) {
        // Initialize DataHub Rest.li client

        try {
            // Step 1: Create a DataFlowUrn (parent DataFlow for the DataJob)
            String dataFlowName = "sample-data-flow";
            String orchestrator = "Airflow"; // Or "Spark" or other orchestrators
            String cluster = "DEV"; // Cluster where the job runs
            DataFlowUrn dataFlowUrn = new DataFlowUrn(orchestrator, dataFlowName, cluster);

            // Step 2: Create a DataJobUrn
            String dataJobName = "sample-data-job"; // Name of the specific task/job
            DataJobUrn dataJobUrn = new DataJobUrn(dataFlowUrn, dataJobName);

            DataJobInfo.Type JobType =  new DataJobInfo.Type();
            JobType.setString("BATCH");

            // Step 3: Set up DataJobInfo
            DataJobInfo dataJobInfo = new DataJobInfo()
                    .setName("dataJobName")
                    .setType(DataJobInfo.Type.create("COMMAND"))
                    .setDescription("A sample data job to process sales data.")
                    .setExternalUrl(new Url("http://example.com/sample-job-url"))
                    .setEnv(FabricType.DEV).setName("sdfdsfds");

            DataJobInputOutput dataJobInputOutput = new DataJobInputOutput();
            DatasetUrnArray datasetUrnArray = new DatasetUrnArray();
            DatasetUrnArray outputDatasetUrnArray = new DatasetUrnArray();

            datasetUrnArray.add(DatasetUrn.createFromString("urn:li:dataset:(urn:li:dataPlatform:hive,logging_events,PROD)"));
            dataJobInputOutput.setInputDatasets(datasetUrnArray);

            outputDatasetUrnArray.add(DatasetUrn.createFromString("urn:li:dataset:(urn:li:dataPlatform:hive,fct_users_created,PROD)"));
            dataJobInputOutput.setOutputDatasets(outputDatasetUrnArray);

            // Step 4: Set up DataJobInputOutput
            dataJobInputOutput.setInputDatasets(datasetUrnArray); // Provide dataset URNs here
            dataJobInputOutput.setOutputDatasets(outputDatasetUrnArray); // Provide dataset URNs here

            // Step 6: Assemble DataJobSnapshot
            DataJobSnapshot dataJobSnapshot = new DataJobSnapshot()
                    //.setUrn(dataJobUrn)
                    .setAspects(new DataJobAspectArray(
                            DataJobAspect.create(dataJobInfo),
                            DataJobAspect.create(dataJobInputOutput)
                    ));

            MetadataChangeProposal statusProposal = createProposal(String.valueOf(dataJobUrn), "dataJob",
                    "dataJobInfo", "UPSERT", dataJobSnapshot);

            emitProposal(statusProposal, "dataJob");/*
            // Step 7: Ingest DataJob into DataHub
            RestliUriBuilder builder = new RestliUriBuilder(client, "http://localhost:8080");
            AspectUtils.ingestAspects(builder, client, dataJobSnapshot);*/

            System.out.println("Successfully ingested Data Job: " + dataJobName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createDataPipeline() throws IOException, ExecutionException, InterruptedException {
        MetadataChangeProposalWrapper mcpw = MetadataChangeProposalWrapper.builder()
                .entityType("dataflow")
                .entityUrn("urn:li:dataFlow:(kafka,trace-pipeline,PROD)")
                .upsert()
                .aspect(new DataFlowInfo()
                        .setName("Trace pipeline")
                        .setDescription("Pipeline for trace service")
                )
                .build();
    }
}

