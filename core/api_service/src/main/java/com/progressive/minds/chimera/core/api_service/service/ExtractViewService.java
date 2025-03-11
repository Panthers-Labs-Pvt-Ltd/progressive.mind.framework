package com.progressive.minds.chimera.core.api_service.service;

import static com.progressive.minds.chimera.core.api_service.entity.ExtractViewDynamicSqlEntity.extractView;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

import com.progressive.minds.chimera.core.api_service.dto.ExtractView;
import com.progressive.minds.chimera.core.api_service.repository.ExtractViewDBMapper;

import java.util.List;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExtractViewService {

    private static final Logger logger = LoggerFactory.getLogger(ExtractMetadataConfigService.class);
    
    private final ExtractViewDBMapper extractViewDBMapper;

    @Autowired
    public ExtractViewService(ExtractViewDBMapper extractViewDBMapper) {
        this.extractViewDBMapper = extractViewDBMapper;
    }

    public List<ExtractView> getExtractView() {
      logger.info("Fetching All Extract Metadata Config.");
      SelectStatementProvider selectStatement = select(
        extractView.pipelineName,
        extractView.pipelineDescription,
        extractView.pipelineName,
        extractView.pipelineDescription,
        extractView.processMode,
        extractView.tags,
        extractView.orgHierName,
        extractView.sequenceNumber,
        extractView.extractSourceType,
        extractView.extractSourceSubType,
        extractView.dataSourceConnectionName,
        extractView.sourceConfiguration,
        extractView.dataframeName,
        extractView.predecessorSequences,
        extractView.successorSequences,
        extractView.rowFilter,
        extractView.columnFilter,
        extractView.defaultReadConfig,
        extractView.defaultWriteConfig,
        extractView.fileName,
        extractView.filePath,
        extractView.schemaPath,
        extractView.sizeInByte,
        extractView.compressionType,
        extractView.collection,
        extractView.partitioner,
        extractView.databaseName,
        extractView.tableName,
        extractView.schemaName,
        extractView.sqlText,
        extractView.kafkaConsumerTopic,
        extractView.kafkaConsumerGroup,
        extractView.kafkaStrtOffset,
        extractView.kafkaMaxOffset,
        extractView.kafkaPollTimeout,
        extractView.tranctnlCnsumrFlg,
        extractView.watrmrkDuration,
        extractView.stgFormt,
        extractView.stgPath,
        extractView.stgPartitions,
        extractView.dataSourceType,
        extractView.dataSourceSubType,
        extractView.authenticationType,
        extractView.authenticationData,
        extractView.connectionMetadata,
        extractView.orgTypeName,
        extractView.parentOrgName,
        extractView.cooOwner,
        extractView.opsLead,
        extractView.techLead,
        extractView.busOwner,
        extractView.orgDesc,
        extractView.orgEmail,
        extractView.orgCi
      ).from(extractView)
      .build()
      .render(RenderingStrategies.MYBATIS3);

      return extractViewDBMapper.selectMany(selectStatement);

}

public List<ExtractView> getExtractViewByPipelineName(String pipelineName) {
    logger.info("Fetching All Extract Metadata Config.");
    SelectStatementProvider selectStatement = select(
      extractView.pipelineName,
      extractView.pipelineDescription,
      extractView.pipelineName,
      extractView.pipelineDescription,
      extractView.processMode,
      extractView.tags,
      extractView.orgHierName,
      extractView.sequenceNumber,
      extractView.extractSourceType,
      extractView.extractSourceSubType,
      extractView.dataSourceConnectionName,
      extractView.sourceConfiguration,
      extractView.dataframeName,
      extractView.predecessorSequences,
      extractView.successorSequences,
      extractView.rowFilter,
      extractView.columnFilter,
      extractView.defaultReadConfig,
      extractView.defaultWriteConfig,
      extractView.fileName,
      extractView.filePath,
      extractView.schemaPath,
      extractView.sizeInByte,
      extractView.compressionType,
      extractView.collection,
      extractView.partitioner,
      extractView.databaseName,
      extractView.tableName,
      extractView.schemaName,
      extractView.sqlText,
      extractView.kafkaConsumerTopic,
      extractView.kafkaConsumerGroup,
      extractView.kafkaStrtOffset,
      extractView.kafkaMaxOffset,
      extractView.kafkaPollTimeout,
      extractView.tranctnlCnsumrFlg,
      extractView.watrmrkDuration,
      extractView.stgFormt,
      extractView.stgPath,
      extractView.stgPartitions,
      extractView.dataSourceType,
      extractView.dataSourceSubType,
      extractView.authenticationType,
      extractView.authenticationData,
      extractView.connectionMetadata,
      extractView.orgTypeName,
      extractView.parentOrgName,
      extractView.cooOwner,
      extractView.opsLead,
      extractView.techLead,
      extractView.busOwner,
      extractView.orgDesc,
      extractView.orgEmail,
      extractView.orgCi
    ).from(extractView)
    .where(extractView.pipelineName, isEqualTo(pipelineName))
    .build()
    .render(RenderingStrategies.MYBATIS3);

    return extractViewDBMapper.selectMany(selectStatement);

}
}
