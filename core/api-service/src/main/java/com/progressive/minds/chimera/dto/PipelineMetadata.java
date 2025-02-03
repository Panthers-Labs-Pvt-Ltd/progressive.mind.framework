package com.progressive.minds.chimera.dto;

import java.sql.Timestamp;
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
    private String pipelineName;
    private String pipelineDescription;
    private String processMode;
    private String tags;
    private String orgHierName;
    private String activeFlag;
    private Timestamp createdTimestamp;
    private String createdBy;
    private Timestamp updatedTimestamp;
    private String updatedBy;
    OrganizationHierarchy org;
//    DataPipeline dataPipeline;
    List<ExtractMetadata> extractMetadata;
    List<TransformMetadataConfig> transformMetadata;
    List<PersistMetadata> persistMetadata;

}
