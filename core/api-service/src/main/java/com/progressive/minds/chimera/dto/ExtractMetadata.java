package com.progressive.minds.chimera.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter

public class ExtractMetadata {

  @NotBlank(message = "Pipeline Name cannot be blank")
  private String pipelineName;

  @NotNull(message = "Sequence Number cannot be null")
  private Integer sequenceNumber;

  private String extractSourceType;

  private String extractSourceSubType;

  private String dataframeName;

  private String sourceConfiguration;

  private String predecessorSequences;

  private String successorSequences;

  private String rowFilter;

  private String columnFilter;

  private String dataSourceConnectionName;

  private Timestamp createdTimestamp;

  private String createdBy;

  private Timestamp updatedTimestamp;

  private String updatedBy;

  private String activeFlag;

  private FileExtractMetadataTable fileMetadata;

  private RelationalExtractMetadataTable relationalMetadata;

  private NoSqlExtractMetadataTable noSqlMetadata;

  private StreamExtractMetadataTable streamMetadata;
}

