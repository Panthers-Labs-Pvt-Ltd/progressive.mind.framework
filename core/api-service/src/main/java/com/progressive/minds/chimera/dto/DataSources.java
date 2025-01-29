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
public class DataSources {

  @NotBlank(message = "Data Source Type cannot be blank")
  private String dataSourceType;
  @NotBlank(message = "Data Source Sub-Type cannot be blank")
  private String dataSourceSubType;
  private String description;
  private String dataSourceTemplate;
  private String defaultReadConfig;
  private String defaultWriteConfig;
  private Timestamp createdTimestamp;
  private String createdBy;
  private Timestamp updatedTimestamp;
  private String updatedBy;
  private String activeFlag;
}
