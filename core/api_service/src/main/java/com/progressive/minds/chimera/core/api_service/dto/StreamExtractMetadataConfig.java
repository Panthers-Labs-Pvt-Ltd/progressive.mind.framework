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
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)  // Include superclass fields in equals/hashCode
public class StreamExtractMetadataConfig extends ExtractMetadataConfig {

  private String kafkaConsumerTopic;

  private String kafkaConsumerGroup;

  private String kafkaStrtOffset;

  private String kafkaMaxOffset;

  private Integer kafkaPollTimeout;

  private String tranctnlCnsumrFlg;
  private String watrmrkDuration;

  private String stgFormt;

  private String stgPath;

  private String stgPartitions;
}
