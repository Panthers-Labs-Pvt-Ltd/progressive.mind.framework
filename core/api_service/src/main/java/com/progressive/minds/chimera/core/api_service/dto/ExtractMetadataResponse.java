package com.progressive.minds.chimera.core.api_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class ExtractMetadataResponse {

    @NotBlank(message = "Pipeline Name cannot be blank")
    private String pipelineName;
    @NotNull(message = "Sequence Number cannot be null")
    private Integer sequenceNumber;
    private String extractSourceType;
    private String extractSourceSubType;
    private String dataframeName;
    private String sourceConfiguration;
    private String predecessorSequences;
    private String successorSequences;
    private String rowFilter;
    private String columnFilter;
    private String dataSourceConnectionName;
    private Timestamp createdTimestamp;
    private String createdBy;
    private Timestamp updatedTimestamp;
    private String updatedBy;
    private String activeFlag;
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

}
   
