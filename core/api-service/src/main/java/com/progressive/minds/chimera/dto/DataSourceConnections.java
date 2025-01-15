package com.progressive.minds.chimera.dto;

import jakarta.validation.constraints.NotBlank;
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
public class DataSourceConnections {

  @NotBlank(message = "Data Source Connection Name cannot be blank")
  private String dataSourceConnectionName;

  private String dataSourceType;

  private String dataSourceSubType;
  
  private String authenticationType;

  private String authenticationData;

  private String connectionMetadata;

  private String userReadConfig;

  private String userWriteConfig;

  private String description;

  private Timestamp createdTimestamp;

  private String createdBy;

  private Timestamp updatedTimestamp;

  private String updatedBy;

  private String activeFlag;
}
