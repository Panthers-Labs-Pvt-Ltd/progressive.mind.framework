package com.progressive.minds.chimera.dto;

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

  private String pipelineName;
  private String pipelineDescription;
}
