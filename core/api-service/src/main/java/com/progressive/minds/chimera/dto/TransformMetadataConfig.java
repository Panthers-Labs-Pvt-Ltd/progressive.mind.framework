package com.progressive.minds.chimera.dto;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TransformMetadataConfig {

  @NotBlank(message = "Pipeline Name cannot be blank")
  private String pipelineName;

  @NotNull(message = "Sequence Number cannot be null")
  private Integer sequenceNumber;

  private String sqlText;

  private String transformDataframeName;

  private Timestamp createdTimestamp;

  private String createdBy;

  private Timestamp updatedTimestamp;

  private String updatedBy;

  private String activeFlag;
}
