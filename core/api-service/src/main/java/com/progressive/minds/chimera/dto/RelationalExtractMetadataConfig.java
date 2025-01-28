package com.progressive.minds.chimera.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
public class RelationalExtractMetadataConfig extends ExtractMetadataConfig {

  private String databaseName;

  private String tableName;

  private String schemaName;

  private String sqlText;

}
