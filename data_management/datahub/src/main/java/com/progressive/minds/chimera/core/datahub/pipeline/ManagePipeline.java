package com.progressive.minds.chimera.core.datahub.pipeline;
import com.fasterxml.jackson.databind.MapperFeature;
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
import com.progressive.minds.chimera.core.datahub.datasets.ManageDatasets;
import com.progressive.minds.chimera.core.datahub.domain.ManageDomain;
import com.progressive.minds.chimera.core.datahub.modal.Dataset;
import com.progressive.minds.chimera.core.datahub.modal.JobStages;
import com.progressive.minds.chimera.core.datahub.modal.Owners;
import com.progressive.minds.chimera.core.datahub.modal.Pipeline;
import com.progressive.minds.chimera.core.datahub.search.searchAssets;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


import static com.progressive.minds.chimera.core.datahub.Constants.*;
import static com.progressive.minds.chimera.core.datahub.ownership.ManageOwners.addOwners;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.createProposal;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.emitProposal;
import static com.progressive.minds.chimera.core.datahub.datasets.schema.setGlobalTags;
import static com.progressive.minds.chimera.core.datahub.datasets.schema.setGlossaryTerms;

public class ManagePipeline {
    static ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(ManageDatasets.class);
    static String LoggerTag = "[DataHub- Create Pipeline] -";



    public static void createDataPipeline(String pipelineDefinition) throws Exception {


        Pipeline dataPipeline = utils.getPipelineInfo(pipelineDefinition);
        if (dataPipeline != null) {
            String platform = dataPipeline.getProcessingEngine();
            String flowName = dataPipeline.getPipelineName();
            String cluster = dataPipeline.getFabricType();
            String createdBy = DATAHUB_ACTOR;
            String domain = dataPipeline.getDomainName();

            DataFlowUrn dataFlowUrn = new DataFlowUrn(platform, flowName, cluster);
            CorpuserUrn userUrn = new CorpuserUrn(createdBy);

            TimeStamp createdStamp = new TimeStamp()
                    .setActor(new CorpuserUrn(userUrn.getUsernameEntity()))
                    .setTime(Instant.now().toEpochMilli());

            Map<String, String> customProperties = new HashMap<>();
            dataPipeline.getProperties().forEach(p -> customProperties.put(p.getName(), p.getValue()));
            StringMap MapCustomProperties = new StringMap();
            MapCustomProperties.putAll(customProperties);
            FabricType env = FabricType.valueOf(dataPipeline.getFabricType());

            DataFlowInfo dataFlowInfo = new DataFlowInfo()
                    .setName(flowName)
                    .setCreated(createdStamp)
                    .setDescription(dataPipeline.getPipelineDescription())
                    .setCustomProperties(MapCustomProperties)
                    .setEnv(env)
                    .setExternalUrl(new Url(dataPipeline.getUri()))
                    .setProject(domain);

            MetadataChangeProposal dataFlowInfoProposal = createProposal(String.valueOf(dataFlowUrn), DATA_FLOW_ENTITY_NAME,
                    DATA_FLOW_INFO_ASPECT_NAME, ACTION_TYPE, dataFlowInfo);
            String dataFlowRetVal = emitProposal(dataFlowInfoProposal, DATA_FLOW_ENTITY_NAME);

            DatahubLogger.logInfo(LoggerTag + " Pipeline Data Flow " + dataFlowRetVal);

            GlobalTags globalTags = setGlobalTags(dataPipeline.getTags());
            MetadataChangeProposal proposal5 = createProposal(String.valueOf(dataFlowUrn), DATA_FLOW_ENTITY_NAME,
                    GLOBAL_TAGS_ASPECT_NAME, ACTION_TYPE, globalTags);
            String globalTagsRetVal = emitProposal(proposal5, DATA_FLOW_ENTITY_NAME);
            DatahubLogger.logInfo(LoggerTag + " Pipeline Global Tags " + globalTagsRetVal);

            ManageDomain manageDomain = new ManageDomain();
            manageDomain.addDomain(dataPipeline.getDomainName(), dataFlowUrn.toString(), DATA_FLOW_ENTITY_NAME);

            Map<String, String> ownersMap = new HashMap<>();
            for (Owners owner : dataPipeline.getOwners()) {
                ownersMap.put(owner.getName(), owner.getType());
            }

            addOwners(dataFlowUrn, DATA_FLOW_ENTITY_NAME, OWNERSHIP_ASPECT_NAME, ACTION_TYPE, ownersMap);

            if (dataPipeline.getGlossaryTerm() != null && !dataPipeline.getGlossaryTerm().isEmpty()) {
                GlossaryTerms glossaryTerms = setGlossaryTerms(dataPipeline.getGlossaryTerm(), userUrn.getUsernameEntity());
                MetadataChangeProposal glossaryTermsProposal = createProposal(String.valueOf(dataFlowUrn), DATA_FLOW_ENTITY_NAME,
                        GLOSSARY_TERMS_ASPECT_NAME, ACTION_TYPE, glossaryTerms);
                String glossaryTermsEmit = emitProposal(glossaryTermsProposal, DATA_FLOW_ENTITY_NAME);
                DatahubLogger.logInfo(LoggerTag + " Glossary Term Data Flow " + glossaryTermsEmit);


                AuditStamp lastModified = new AuditStamp()
                        .setTime(Instant.now().toEpochMilli())
                        .setActor(new CorpuserUrn(userUrn.getUsernameEntity()));

                InstitutionalMemory institutionalMemory = new InstitutionalMemory().setElements(
                        new InstitutionalMemoryMetadataArray(new InstitutionalMemoryMetadata()
                                .setDescription(dataPipeline.getPipelineDescription())
                                .setCreateStamp(lastModified)
                                .setUrl(new Url(dataPipeline.getUri()))));

                MetadataChangeProposal InstitutionalMemoryProposal = createProposal(String.valueOf(dataFlowUrn), DATA_FLOW_ENTITY_NAME,
                        "institutionalMemory", ACTION_TYPE, institutionalMemory);
                String InstitutionalMemoryEmit = emitProposal(InstitutionalMemoryProposal, DATA_FLOW_ENTITY_NAME);
                DatahubLogger.logInfo(LoggerTag + " InstitutionalMemory Data Flow " + InstitutionalMemoryEmit);

                new ManageDataProduct().addAssetToDataProduct(dataPipeline.getDataProductName(), dataFlowUrn.toString());
                Status isActive = new Status().setRemoved(dataPipeline.isInActiveFlag());

                MetadataChangeProposal statusProposal = createProposal(String.valueOf(dataFlowUrn), DATA_FLOW_ENTITY_NAME,
                        STATUS_ASPECT_NAME, ACTION_TYPE, isActive);
                String statusEmit = emitProposal(statusProposal, DATA_FLOW_ENTITY_NAME);
                DatahubLogger.logInfo(LoggerTag + " Status Data Flow " + statusEmit);

                createPipelineStages(dataPipeline, String.valueOf(dataFlowUrn), flowName);

            }
        }
    }

    public static void createPipelineStages(Pipeline dataPipeline,String pipelineUrn, String PipelineName ) throws
            URISyntaxException, IOException, ExecutionException, InterruptedException {
        DataFlowUrn pipelineUr = DataFlowUrn.createFromString(pipelineUrn);
        List<JobStages> stages = dataPipeline.getStages();
        for (JobStages stageInfo : stages) {
            DataJobInfo dataJobInfo = new DataJobInfo();
            DataJobUrn dataJobUrn = new DataJobUrn(DataFlowUrn.createFromString(pipelineUrn),stageInfo.getStageName());

            DataJobInfo.Type JobType =  new DataJobInfo.Type();
            JobType.setString(stageInfo.getStageType());

            Map<String, String> customProperties = new HashMap<>();
            stageInfo.getProperties().forEach(p -> customProperties.put(p.getName(), p.getValue()));
            StringMap MapCustomProperties = new StringMap();
            MapCustomProperties.putAll(customProperties);

            String stageName = PipelineName + "( " + stageInfo.getStageName() + " )";
            dataJobInfo
                    .setDescription(stageInfo.getStageDescription())
                    .setEnv(FabricType.DEV)
                    .setName(stageName)
                    .setFlowUrn(pipelineUr)
                    .setExternalUrl(new Url(stageInfo.getStageUrl()))
                    .setType(JobType)
                    .setStatus(JobStatus.UNKNOWN)
                    .setCustomProperties(MapCustomProperties);

            MetadataChangeProposal statusProposal = createProposal(String.valueOf(dataJobUrn), DATA_JOB_ENTITY_NAME,
                    DATA_JOB_INFO_ASPECT_NAME , ACTION_TYPE, dataJobInfo);
            String dataJobInfoRetVal = emitProposal(statusProposal, DATA_JOB_ENTITY_NAME);
            DatahubLogger.logInfo(LoggerTag + " Data Job Info " + dataJobInfoRetVal);

            DataJobInputOutput dataJobInputOutput = new DataJobInputOutput();
            DatasetUrnArray datasetUrnArray = new DatasetUrnArray();
            DatasetUrnArray outputDatasetUrnArray = new DatasetUrnArray();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            List<Dataset> outputDataset = stageInfo.getOutputDataset();
            List<Dataset> inputDataset = stageInfo.getInputDataset();
            String inDatasetJson;
            if (inputDataset != null && !inputDataset.isEmpty())
            {
                for (Dataset in : inputDataset) {
                    try {
                        String DatasetPlatform = genericUtils.getOrElse(in.getDatasetPlatformName(), dataPipeline.getProcessingEngine());
                        String DatasetName = genericUtils.getOrElse(in.getName(), "Invalid");
                        String Fabric = genericUtils.getOrElse(in.getFabricType(), dataPipeline.getFabricType());

                        String datasetURN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                                DatasetPlatform, DatasetName, Fabric);
                        if (!searchAssets.get(datasetURN, DATASET_KEY_ASPECT_NAME)) {
                            inDatasetJson = objectMapper.writeValueAsString(in);
                            ManageDatasets.createDataset(inDatasetJson, SYSTEM_USER);
                        }
                        datasetUrnArray.add(DatasetUrn.createFromString(datasetURN));
                    } catch (Exception e) {
                        DatahubLogger.logError("Error While adding Input Datasets",e);
                        throw new RuntimeException(e);
                    }
                }
                dataJobInputOutput.setInputDatasets(datasetUrnArray);
            }
            if (outputDataset != null && !outputDataset.isEmpty())
            {
                //outputDataset.forEach(out ->
                for (Dataset out : outputDataset){
                    try {
                        String DatasetPlatform=genericUtils.getOrElse(out.getDatasetPlatformName(),dataPipeline.getProcessingEngine());
                        String DatasetName=genericUtils.getOrElse(out.getName(),"Invalid");
                        String Fabric=genericUtils.getOrElse(out.getFabricType(),dataPipeline.getFabricType());

                        String datasetURN= String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                                DatasetPlatform,DatasetName, Fabric);
                        if (!searchAssets.get(datasetURN, DATASET_KEY_ASPECT_NAME)) {
                            inDatasetJson = objectMapper.writeValueAsString(out);
                            ManageDatasets.createDataset(inDatasetJson, SYSTEM_USER);
                        }
                        outputDatasetUrnArray.add(DatasetUrn.createFromString(datasetURN));
                    } catch (Exception e) {
                        DatahubLogger.logError("Error While adding Output Datasets",e);
                        throw new RuntimeException(e);
                    }
                }
                dataJobInputOutput.setOutputDatasets(outputDatasetUrnArray);
            }
            else
            {
                dataJobInputOutput.setOutputDatasets(datasetUrnArray);
            }


            dataJobInputOutput.setInputDatasets(datasetUrnArray); // Provide dataset URNs here
            dataJobInputOutput.setOutputDatasets(outputDatasetUrnArray); // Provide dataset URNs here

            MetadataChangeProposal datasets = createProposal(String.valueOf(dataJobUrn), DATA_JOB_ENTITY_NAME,
                    DATA_JOB_INPUT_OUTPUT_ASPECT_NAME, ACTION_TYPE, dataJobInputOutput);
            String dataJobInputOutputStatus = emitProposal(datasets, DATA_JOB_ENTITY_NAME);
            DatahubLogger.logInfo("Data Job Return " + dataJobInputOutputStatus);

        }
    }
}
