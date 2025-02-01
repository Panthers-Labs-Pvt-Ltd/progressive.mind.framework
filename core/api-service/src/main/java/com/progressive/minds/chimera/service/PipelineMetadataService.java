package com.progressive.minds.chimera.service;


import com.progressive.minds.chimera.dto.DataPipeline;
import com.progressive.minds.chimera.dto.DataSourceConnections;
import com.progressive.minds.chimera.dto.OrganizationHierarchy;
import com.progressive.minds.chimera.dto.PersistMetadataConfig;
import com.progressive.minds.chimera.dto.PipelineMetadata;
import com.progressive.minds.chimera.dto.TransformMetadataConfig;
import com.progressive.minds.chimera.dto.DataSources;
import com.progressive.minds.chimera.dto.ExtractMetadata;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PipelineMetadataService {
    private final PipelineService pipelineService;
    private final OrganizationHierarchyService orgHierService;
    private final ExtractMetadataConfigService extractMetadataService;
    private final TransformMetadataConfigService transformMetadataService;
    private final persistMetadataConfigService persistMetadataConfigService;
    private final DataSourceConnectionsService dataSourceConnectionsService;
    private final dataSourcesService dataSourcesService;

    @Autowired
    public PipelineMetadataService(PipelineService pipelineService,
                                    OrganizationHierarchyService orgHierService,
                                    ExtractMetadataConfigService extractMetadataService,
                                    TransformMetadataConfigService transformMetadataService,
                                    persistMetadataConfigService persistMetadataConfigService,
                                    DataSourceConnectionsService dataSourceConnectionsService,
                                    dataSourcesService dataSourcesService) {
        this.pipelineService = pipelineService;
        this.orgHierService = orgHierService;
        this.extractMetadataService = extractMetadataService;
        this.transformMetadataService = transformMetadataService;
        this.persistMetadataConfigService = persistMetadataConfigService;
        this.dataSourceConnectionsService = dataSourceConnectionsService;
        this.dataSourcesService = dataSourcesService;
    }

    public PipelineMetadata getPipelineMetadata(String pipelineName) {
        DataPipeline dataPipeline = pipelineService.getDataPipeLineByName(pipelineName);
        OrganizationHierarchy org = orgHierService.getOrgHierarchyByName(dataPipeline.getOrgHierName()).orElse(null);
        List<ExtractMetadata> extractMetadata = extractMetadataService.getPipelineMetadata(pipelineName);
        List<TransformMetadataConfig> transformMetadata = transformMetadataService.getTransformMetadataByPipelineName(pipelineName);
        List<PersistMetadataConfig> persistMetadata = persistMetadataConfigService.getPersistMetadataByPipelineName(pipelineName);
        List<DataSources> dataSources = new ArrayList<DataSources>();
        List<DataSourceConnections> connection = new ArrayList<DataSourceConnections>();
        extractMetadata.forEach(config -> {
            DataSources ds = dataSourcesService.getDataSourceByTypeAndSubtype(config.getExtractSourceType(), config.getExtractSourceSubType());
            if (ds != null) {
                dataSources.add(ds); 
            }
            DataSourceConnections dsc = dataSourceConnectionsService.getConnectionByName(config.getDataSourceConnectionName()).orElse(null);
            if(dsc != null) {
                connection.add(dsc);
            }
        });
        persistMetadata.forEach(config -> {
            DataSources ds = dataSourcesService.getDataSourceByTypeAndSubtype(config.getSinkType(), config.getSinkSubType());
            if (ds != null) {
                dataSources.add(ds); 
            }
            DataSourceConnections dsc = dataSourceConnectionsService.getConnectionByName(config.getDataSourceConnectionName()).orElse(null);
            if(dsc != null) {
                connection.add(dsc);
            }
        });

        PipelineMetadata pipelineMetadata = new PipelineMetadata(org, dataPipeline, extractMetadata, transformMetadata, persistMetadata, dataSources, connection);
        
        return pipelineMetadata;


    
    }

}







