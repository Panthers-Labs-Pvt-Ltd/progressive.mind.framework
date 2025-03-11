package com.progressive.minds.chimera.core.api_service.dto;

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
public class FileExtractMetadataConfig extends ExtractMetadataConfig {

  private String fileName;

  private String filePath;

  private String schemaPath;

  private Long sizeInByte;

  private String compressionType;
}
