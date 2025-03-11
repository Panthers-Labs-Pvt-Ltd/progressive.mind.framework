package com.progressive.minds.chimera.core.api_service.service;

import com.progressive.minds.chimera.core.api_service.dto.DataPipeline;
import com.progressive.minds.chimera.core.api_service.dto.PipelineMetadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PipelineMetadataService {
    private final PipelineService pipelineService;
    private final OrganizationHierarchyService orgHierService;
    private final ExtractMetadataConfigService extractMetadataService;
    private final TransformMetadataConfigService transformMetadataService;
    private final persistMetadataConfigService persistMetadataConfigService;
    
    @Autowired
    public PipelineMetadataService(PipelineService pipelineService,
                                    OrganizationHierarchyService orgHierService,
                                    ExtractMetadataConfigService extractMetadataService,
                                    TransformMetadataConfigService transformMetadataService,
                                    persistMetadataConfigService persistMetadataConfigService
                                    ) {
        this.pipelineService = pipelineService;
        this.orgHierService = orgHierService;
        this.extractMetadataService = extractMetadataService;
        this.transformMetadataService = transformMetadataService;
        this.persistMetadataConfigService = persistMetadataConfigService;
    
    }

    public PipelineMetadata getPipelineMetadata(String pipelineName) {
        DataPipeline dataPipeline = pipelineService.getDataPipeLineByName(pipelineName);
       
        PipelineMetadata pipelineMetadata = new PipelineMetadata();
        pipelineMetadata.setPipelineName(dataPipeline.getPipelineName());
        pipelineMetadata.setPipelineDescription(dataPipeline.getPipelineDescription());
        pipelineMetadata.setProcessMode(dataPipeline.getProcessMode());
        pipelineMetadata.setTags(dataPipeline.getTags());
        pipelineMetadata.setOrgHierName(dataPipeline.getOrgHierName());
        pipelineMetadata.setCreatedBy(dataPipeline.getCreatedBy());
        pipelineMetadata.setCreatedTimestamp(dataPipeline.getCreatedTimestamp());
        pipelineMetadata.setUpdatedBy(dataPipeline.getUpdatedBy());
        pipelineMetadata.setUpdatedTimestamp(dataPipeline.getUpdatedTimestamp());
        pipelineMetadata.setActiveFlag(dataPipeline.getActiveFlag());
        pipelineMetadata.setOrg(orgHierService.getOrgHierarchyByName(dataPipeline.getOrgHierName()).orElse(null));
        pipelineMetadata.setExtractMetadata(extractMetadataService.getExtractMetadata(pipelineName));
        pipelineMetadata.setTransformMetadata(transformMetadataService.getTransformMetadataByPipelineName(pipelineName));
        pipelineMetadata.setPersistMetadata(persistMetadataConfigService.getPersistMetadata(pipelineName));

        
        return pipelineMetadata;


    
    }

}







