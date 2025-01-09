package com.progressive.minds.chimera.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class DataPipeline {
    @NotNull
    private Long id;
    @Min(value = 5, message = "Name should not be less than 5 characters")
    private String pipelineName;
    private String pipelineDescription;
    private String processMode;
    private String tags;
    private String orgHierName;
    private Timestamp createdTimestamp;
    private String createdBy;
    private Timestamp updatedTimestamp;
    private String updatedBy;
    private String activeFlag;
}
