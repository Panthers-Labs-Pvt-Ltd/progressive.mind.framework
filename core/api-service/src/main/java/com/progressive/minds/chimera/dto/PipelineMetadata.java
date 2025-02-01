package com.progressive.minds.chimera.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PipelineMetadata {
    OrganizationHierarchy org;
    DataPipeline dataPipeline;
    List<ExtractMetadata> extractMetadata;
    List<TransformMetadataConfig> transformMetadata;
    List<PersistMetadataConfig> persistMetadata;
    List<DataSources> dataSources;
    List<DataSourceConnections> dataSourcesConnections;

}
