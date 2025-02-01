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
public class NoSqlExtractMetadataTable {

  private String collection;

  private String partitioner;
}

  
