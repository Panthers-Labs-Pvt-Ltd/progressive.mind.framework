package com.progressive.minds.chimera.core.datahub.pipeline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedin.common.*;
import com.linkedin.common.url.Url;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.DataFlowUrn;
import com.linkedin.common.urn.DataJobUrn;
import com.linkedin.common.urn.DatasetUrn;
import com.linkedin.data.template.StringMap;
import com.linkedin.datajob.*;
import com.linkedin.mxe.MetadataChangeProposal;
import com.progressive.minds.chimera.core.datahub.common.genericUtils;
import com.progressive.minds.chimera.core.datahub.dataproduct.ManageDataProduct;
import com.progressive.minds.chimera.core.datahub.datasets.DatasetManager;
import com.progressive.minds.chimera.core.datahub.domain.ManageDomain;
import com.progressive.minds.chimera.core.datahub.modal.Dataset;
import com.progressive.minds.chimera.core.datahub.modal.Owners;
import com.progressive.minds.chimera.core.datahub.modal.Pipeline;
import com.progressive.minds.chimera.core.datahub.modal.jobStages;
import com.progressive.minds.chimera.core.datahub.search.searchAssets;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;


import static com.progressive.minds.chimera.core.datahub.common.ManageOwners.addOwners;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.createProposal;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.emitProposal;
import static com.progressive.minds.chimera.core.datahub.datasets.schema.setGlobalTags;
import static com.progressive.minds.chimera.core.datahub.datasets.schema.setGlossaryTerms;

public class ManagePipeline {
    static String ENTITY_TYPE = "dataFlow";
    static String ACTION_TYPE = "UPSERT";


    public static void createDataPipeline(String pipelineDefinition) throws Exception {


        Pipeline dataPipeline = utils.getPipelineInfo(pipelineDefinition);
        if (dataPipeline != null) {
            String platform = dataPipeline.processingEngine;
            String flowName = dataPipeline.pipelineName;
            String cluster = dataPipeline.FabricType;
            String createdBy = "chimera";
            String domain = dataPipeline.domainName;

            DataFlowUrn dataFlowUrn = new DataFlowUrn(platform, flowName, cluster);
            CorpuserUrn userUrn = new CorpuserUrn(createdBy);

            TimeStamp createdStamp = new TimeStamp()
                    .setActor(new CorpuserUrn(userUrn.getUsernameEntity()))
                    .setTime(Instant.now().toEpochMilli());

            Map<String, String> customProperties = new HashMap<>();
            dataPipeline.properties.forEach(p -> customProperties.put(p.name, p.value));
            StringMap MapCustomProperties = new StringMap();
            MapCustomProperties.putAll(customProperties);
            FabricType env = FabricType.valueOf(dataPipeline.FabricType);

            DataFlowInfo dataFlowInfo = new DataFlowInfo()
                    .setName(flowName)
                    .setCreated(createdStamp)
                    .setDescription(dataPipeline.pipelineDescription)
                    .setCustomProperties(MapCustomProperties)
                    .setEnv(env)
                    .setExternalUrl(new Url(dataPipeline.uri))
                    .setProject(domain);

            MetadataChangeProposal dataFlowInfoProposal = createProposal(String.valueOf(dataFlowUrn), ENTITY_TYPE,
                    "dataFlowInfo", ACTION_TYPE, dataFlowInfo);
            emitProposal(dataFlowInfoProposal, ENTITY_TYPE);


            GlobalTags globalTags = setGlobalTags(dataPipeline.tags);
            MetadataChangeProposal proposal5 = createProposal(String.valueOf(dataFlowUrn), ENTITY_TYPE,
                    "globalTags", "UPSERT", globalTags);
            String retVal5 = emitProposal(proposal5, ENTITY_TYPE);

            ManageDomain manageDomain = new ManageDomain();
            manageDomain.addDomain(dataPipeline.domainName, dataFlowUrn.toString(), ENTITY_TYPE);

            Map<String, String> ownersMap = new HashMap<>();
            for (Owners owner : dataPipeline.owners) {
                ownersMap.put(owner.getName(), owner.getType());
            }

            addOwners(dataFlowUrn, ENTITY_TYPE, "ownership", "UPSERT", ownersMap);

            if (dataPipeline.glossaryTerm != null && !dataPipeline.glossaryTerm.isEmpty()) {
                GlossaryTerms glossaryTerms = setGlossaryTerms(dataPipeline.glossaryTerm, userUrn.getUsernameEntity());
                MetadataChangeProposal glossaryTermsProposal = createProposal(String.valueOf(dataFlowUrn), ENTITY_TYPE,
                        "glossaryTerms", "UPSERT", glossaryTerms);
                String glossaryTermsemit = emitProposal(glossaryTermsProposal, ENTITY_TYPE);


                AuditStamp lastModified = new AuditStamp()
                        .setTime(Instant.now().toEpochMilli())
                        .setActor(new CorpuserUrn(userUrn.getUsernameEntity()));

                InstitutionalMemory institutionalMemory = new InstitutionalMemory().setElements(
                        new InstitutionalMemoryMetadataArray(new InstitutionalMemoryMetadata()
                                .setDescription(dataPipeline.pipelineDescription)
                                .setCreateStamp(lastModified)
                                .setUrl(new Url(dataPipeline.uri))));

                MetadataChangeProposal InstitutionalMemoryProposal = createProposal(String.valueOf(dataFlowUrn), ENTITY_TYPE,
                        "institutionalMemory", "UPSERT", institutionalMemory);
                String InstitutionalMemoryemit = emitProposal(InstitutionalMemoryProposal, ENTITY_TYPE);

                new ManageDataProduct().addAssetToDataProduct(dataPipeline.dataProductName, dataFlowUrn.toString());
                Status isActive = new Status().setRemoved(dataPipeline.inActiveFlag);

                MetadataChangeProposal statusProposal = createProposal(String.valueOf(dataFlowUrn), ENTITY_TYPE,
                        "status", "UPSERT", isActive);
                String statusEmit = emitProposal(statusProposal, ENTITY_TYPE);

                //*******************
                createPipelineStages(dataPipeline, String.valueOf(dataFlowUrn));

            }
        }
    }

    public static void createPipelineStages(Pipeline dataPipeline,String pipelineUrn ) throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        DataFlowUrn pipelineUr = DataFlowUrn.createFromString(pipelineUrn);
        List<jobStages> stages = dataPipeline.stages;
        for (jobStages stageInfo : stages) {
            DataJobInfo dataJobInfo = new DataJobInfo();
            DataJobUrn dataJobUrn = new DataJobUrn(DataFlowUrn.createFromString(pipelineUrn),stageInfo.stageName);

            DataJobInfo.Type JobType =  new DataJobInfo.Type();
            JobType.setString(stageInfo.stageType);

            Map<String, String> customProperties = new HashMap<>();
            stageInfo.properties.forEach(p -> customProperties.put(p.name, p.value));
            StringMap MapCustomProperties = new StringMap();
            MapCustomProperties.putAll(customProperties);

            dataJobInfo
                    .setDescription(stageInfo.stageDescription)
                    .setEnv(FabricType.DEV)
                    .setName(stageInfo.stageName)
                    .setFlowUrn(pipelineUr)
                    .setExternalUrl(new Url(stageInfo.stageUrl))
                    .setType(JobType)
                    .setStatus(JobStatus.UNKNOWN)
                    .setCustomProperties(MapCustomProperties);

            MetadataChangeProposal statusProposal = createProposal(String.valueOf(dataJobUrn), "dataJob",
                    "dataJobInfo", "UPSERT", dataJobInfo);
            emitProposal(statusProposal, "dataJob");

            DataJobInputOutput dataJobInputOutput = new DataJobInputOutput();
            DatasetUrnArray datasetUrnArray = new DatasetUrnArray();
            DatasetUrnArray outputDatasetUrnArray = new DatasetUrnArray();
            ObjectMapper objectMapper = new ObjectMapper();

            List<Dataset> outputDataset = stageInfo.outputDataset;
            List<Dataset> inputDataset = stageInfo.inputDataset;
            String inDatasetJson = "";
            if (inputDataset != null && !inputDataset.isEmpty())
            {
                for (Dataset in : inputDataset) {
                    try {
                        String DatasetPlatform = genericUtils.getOrElse(in.datasetPlatformName, dataPipeline.processingEngine);
                        String DatasetName = genericUtils.getOrElse(in.name, "Invalid");
                        String Fabric = genericUtils.getOrElse(in.FabricType, dataPipeline.FabricType);

                        String datasetURN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                                DatasetPlatform, DatasetName, Fabric);
                        System.out.println("Progressing For " + datasetURN);
                        if (searchAssets.get(datasetURN, "dataset") != true) {
                            inDatasetJson = objectMapper.writeValueAsString(in);
                            DatasetManager.createDataset(inDatasetJson, "system");
                        }
                        datasetUrnArray.add(DatasetUrn.createFromString(datasetURN));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                dataJobInputOutput.setInputDatasets(datasetUrnArray);
            }
            if (outputDataset != null && !outputDataset.isEmpty())
            {
                //outputDataset.forEach(out ->
                for (Dataset out : inputDataset){
                    try {
                        String DatasetPlatform=genericUtils.getOrElse(out.datasetPlatformName,dataPipeline.processingEngine);
                        String DatasetName=genericUtils.getOrElse(out.name,"Invalid");
                        String Fabric=genericUtils.getOrElse(out.FabricType,dataPipeline.FabricType);

                        String datasetURN= String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                                DatasetPlatform,DatasetName, Fabric);
                        outputDatasetUrnArray.add(DatasetUrn.createFromString(datasetURN));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }

                };
                dataJobInputOutput.setOutputDatasets(outputDatasetUrnArray);
            }
            else
            {
                dataJobInputOutput.setOutputDatasets(datasetUrnArray);
            }


            dataJobInputOutput.setInputDatasets(datasetUrnArray); // Provide dataset URNs here
            dataJobInputOutput.setOutputDatasets(outputDatasetUrnArray); // Provide dataset URNs here

            MetadataChangeProposal datasets = createProposal(String.valueOf(dataJobUrn), "dataJob",
                    "dataJobInputOutput", "UPSERT", dataJobInputOutput);
            emitProposal(datasets, "dataJob");


        }
    }
}
