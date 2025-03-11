package com.progressive.minds.chimera.core.api_service.dto;

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

public class PersistMetadata {

  @NotBlank(message = "Pipeline Name cannot be blank")
  private String pipelineName;

  @NotNull(message = "Sequence Number cannot be null")
  private Integer sequenceNumber;

  private String sinkType;

  private String sinkSubType;
  
  private String dataSourceConnectionName;

  private String databaseName;

  private String tableName;

  private String schemaName;

  private String partitionKeys;

  private String targetSql;

  private String targetPath;

  private String writeMode;

  private String sinkConfiguration;

  private String sortColumns;

  private String dedupColumns;

  private String kafkaTopic;

  private String kafkaKey;

  private String kafkaMessage;

  private Timestamp createdTimestamp;

  private String createdBy;

  private Timestamp updatedTimestamp;

  private String updatedBy;

  private String activeFlag; // Default value 'Y'

  DataSources dataSource;

  DataSourceConnections dataSourceConnection;
}
