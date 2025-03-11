package com.progressive.minds.chimera.core.api_service.dto;

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
public class ExtractView {

  private String pipelineName;

  private String pipelineDescription;

  private String processMode;

  private String tags;

  private String orgHierName;

  private Integer sequenceNumber;

  private String extractSourceType;

  private String extractSourceSubType;

  private String dataSourceConnectionName;

  private String sourceConfiguration;

  private String dataframeName;

  private String predecessorSequences;

  private String successorSequences;

  private String rowFilter;

  private String columnFilter;

  private String defaultReadConfig;

  private String defaultWriteConfig;

  private String fileName;

  private String filePath;

  private String schemaPath;

  private Long sizeInByte;

  private String compressionType;

  private String collection;

  private String partitioner;

  private String databaseName;

  private String tableName;

  private String schemaName;

  private String sqlText;

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

  private String dataSourceType;

  private String dataSourceSubType;

  private String authenticationType;

  private String authenticationData;

  private String connectionMetadata;

  private String orgTypeName;

  private String parentOrgName;

  private String cooOwner;

  private String opsLead;

  private String techLead;

  private String busOwner;

  private String orgDesc;

  private String orgEmail;

  private String orgCi;

  private Timestamp createdTimestamp;

  private String createdBy;

  private Timestamp updatedTimestamp;

  private String updatedBy;

  private String activeFlag;
}
