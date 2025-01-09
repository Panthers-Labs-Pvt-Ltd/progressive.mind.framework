package com.progressive.minds.chimera.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataPipeline {
    private Long id;
    private String pipelineName;
    private String pipelineDescription;
    private String processMode;
    private String runFrequency;
    private Timestamp createdTimestamp;
    private String createdBy;
    private Timestamp updatedTimestamp;
    private String updatedBy;
    private String activeFlag;

}
