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
@EqualsAndHashCode
@Setter
@Getter
public class FileExtractMetadataTable {

  private String fileName;

  private String filePath;

  private String schemaPath;

  private Long sizeInByte;

  private String compressionType;
}
