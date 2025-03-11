package com.progressive.minds.chimera.core.api_service.dto;

import jakarta.validation.constraints.Min;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class DataPipeline {

  @Min(value = 5, message = "Name should not be less than 5 characters")
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
}
