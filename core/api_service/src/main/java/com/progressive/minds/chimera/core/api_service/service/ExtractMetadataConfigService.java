package com.progressive.minds.chimera.core.api_service.service;

import com.progressive.minds.chimera.core.api_service.dto.ExtractMetadata;
import com.progressive.minds.chimera.core.api_service.dto.ExtractMetadataConfig;
import com.progressive.minds.chimera.core.api_service.dto.FileExtractMetadataConfig;
import com.progressive.minds.chimera.core.api_service.dto.NoSqlExtractMetadataConfig;
import com.progressive.minds.chimera.core.api_service.dto.RelationalExtractMetadataConfig;
import com.progressive.minds.chimera.core.api_service.dto.StreamExtractMetadataConfig;

import com.progressive.minds.chimera.core.api_service.dto.RelationalExtractMetadataTable;
import com.progressive.minds.chimera.core.api_service.dto.FileExtractMetadataTable;
import com.progressive.minds.chimera.core.api_service.dto.NoSqlExtractMetadataTable;
import com.progressive.minds.chimera.core.api_service.dto.StreamExtractMetadataTable;

import static com.progressive.minds.chimera.core.api_service.entity.ExtractMetadataConfigDynamicSqlEntity.*;
import static com.progressive.minds.chimera.core.api_service.entity.FileExtractMetadataConfigDynamicSqlEntity.*;
import static com.progressive.minds.chimera.core.api_service.entity.NoSqlExtractMetadataConfigDynamicSqlEntity.*;
import static com.progressive.minds.chimera.core.api_service.entity.RelationalExtractMetadataConfigDynamicSqlEntity.*;
import static com.progressive.minds.chimera.core.api_service.entity.StreamsExtractMetadataConfigDynamicSqlEntity.*;

import static com.progressive.minds.chimera.core.api_service.entity.NoSqlExtractMetadataDynamicSqlEntity.*;
import static com.progressive.minds.chimera.core.api_service.entity.FileExtractMetadataDynamicSqlEntity.*;
import static com.progressive.minds.chimera.core.api_service.entity.RelationalExtractMetadataDynamicSqlEntity.*;
import static com.progressive.minds.chimera.core.api_service.entity.StreamsExtractMetadataDynamicSqlEntity.*;

import com.progressive.minds.chimera.core.api_service.repository.ExtractMetadataConfigDBMapper;
import com.progressive.minds.chimera.core.api_service.repository.ExtractConfigDBMapper;
import com.progressive.minds.chimera.core.api_service.repository.FileExtractMetadataConfigDBMapper;
import com.progressive.minds.chimera.core.api_service.repository.NoSqlExtractMetadataConfigDBMapper;
import com.progressive.minds.chimera.core.api_service.repository.RelationalExtractMetadataConfigDBMapper;
import com.progressive.minds.chimera.core.api_service.repository.StreamsExtractMetadataConfigDBMapper;

import com.progressive.minds.chimera.core.api_service.repository.RelationalExtractMetadataTableDBMapper;
import com.progressive.minds.chimera.core.api_service.repository.FileExtractMetadataTableDBMapper;
import com.progressive.minds.chimera.core.api_service.repository.NoSqlExtractMetadataTableDBMapper;
import com.progressive.minds.chimera.core.api_service.repository.StreamsExtractMetadataTableDBMapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckForNull;

import java.math.BigInteger;

import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;

import static org.mybatis.dynamic.sql.SqlBuilder.select;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.System;
import org.springframework.stereotype.Service;

@Service
public class ExtractMetadataConfigService {

   private static final Logger logger = LoggerFactory.getLogger(ExtractMetadataConfigService.class);

    private final ExtractMetadataConfigDBMapper extractDBMapper;
    private final FileExtractMetadataConfigDBMapper FileDBMapper;
    private final NoSqlExtractMetadataConfigDBMapper NoSqlDBMapper;
    private final RelationalExtractMetadataConfigDBMapper RelationalDBMapper;
    private final StreamsExtractMetadataConfigDBMapper StreamsDBMapper;
    private final ExtractConfigDBMapper extractConfigDBMapper;
    private final RelationalExtractMetadataTableDBMapper relationalExDBMapper;
    private final FileExtractMetadataTableDBMapper fileExDBMapper;
    private final NoSqlExtractMetadataTableDBMapper noSqlExDBMapper;
    private final StreamsExtractMetadataTableDBMapper streamEXDBMapper;
    private final dataSourcesService dataSourcesService;
    private final DataSourceConnectionsService dataSourcesConnService;
    
    public ExtractMetadataConfigService(ExtractMetadataConfigDBMapper extractDBMapper,
                                        FileExtractMetadataConfigDBMapper FileDBMapper,
                                        NoSqlExtractMetadataConfigDBMapper NoSqlDBMapper,
                                        RelationalExtractMetadataConfigDBMapper RelationalDBMapper,
                                        StreamsExtractMetadataConfigDBMapper StreamsDBMapper,
                                        ExtractConfigDBMapper extractConfigDBMapper,
                                        RelationalExtractMetadataTableDBMapper relationalExDBMapper,
                                        FileExtractMetadataTableDBMapper fileExDBMapper,
                                        NoSqlExtractMetadataTableDBMapper noSqlExDBMapper,
                                        StreamsExtractMetadataTableDBMapper streamEXDBMapper,
                                        dataSourcesService dataSourcesService,
                                        DataSourceConnectionsService dataSourcesConnService) {
        this.extractDBMapper = extractDBMapper;
        this.FileDBMapper = FileDBMapper;
        this.NoSqlDBMapper = NoSqlDBMapper;
        this.RelationalDBMapper = RelationalDBMapper;
        this.StreamsDBMapper = StreamsDBMapper;
        this.extractConfigDBMapper = extractConfigDBMapper;
        this.relationalExDBMapper = relationalExDBMapper;
        this.noSqlExDBMapper = noSqlExDBMapper;
        this.streamEXDBMapper = streamEXDBMapper;
        this.fileExDBMapper = fileExDBMapper;
        this.dataSourcesService = dataSourcesService;
        this.dataSourcesConnService = dataSourcesConnService;
    }

    public List<ExtractMetadata> getExtractMetadata() {
        SelectStatementProvider selectStatement = select(extractMetadataConfig.allColumns())
        .from(extractMetadataConfig)
        .orderBy(extractMetadataConfig.pipelineName, extractMetadataConfig.sequenceNumber)
        .build()
        .render(RenderingStrategies.MYBATIS3);

        List<ExtractMetadataConfig> extractMetadataList = extractConfigDBMapper.selectMany(selectStatement);
        List<ExtractMetadata> extractMetadata = new ArrayList<ExtractMetadata>();
        extractMetadataList.forEach(config -> {
            ExtractMetadata extract = new ExtractMetadata();
            extract.setPipelineName(config.getPipelineName());
            extract.setSequenceNumber(config.getSequenceNumber());
            extract.setExtractSourceType(config.getExtractSourceType());
            extract.setExtractSourceSubType(config.getExtractSourceSubType());
            extract.setDataframeName(config.getDataframeName());
            extract.setSourceConfiguration(config.getSourceConfiguration());
            extract.setPredecessorSequences(config.getPredecessorSequences());
            extract.setSuccessorSequences(config.getSuccessorSequences());
            extract.setRowFilter(config.getRowFilter());
            extract.setColumnFilter(config.getColumnFilter());
            extract.setDataSourceConnectionName(config.getDataSourceConnectionName());
            extract.setCreatedBy(config.getCreatedBy());
            extract.setCreatedTimestamp(config.getCreatedTimestamp());
            extract.setUpdatedBy(config.getUpdatedBy());
            extract.setUpdatedTimestamp(config.getUpdatedTimestamp());
            extract.setActiveFlag(config.getActiveFlag());
    
            switch (config.getExtractSourceType()) {
                case "Relational" -> {
                    extract.setRelationalMetadata(getRelationalConfigByPipelineName(config.getPipelineName(), config.getSequenceNumber()));
                }
                case "Files" -> {
                    extract.setFileMetadata(getFileConfigByPipelineName(config.getPipelineName(), config.getSequenceNumber()));
                }
                case "Stream" -> {
                    extract.setStreamMetadata(getStreamConfigByPipelineName(config.getPipelineName(),config.getSequenceNumber()));
                }
                case "NoSql" -> {
                    extract.setNoSqlMetadata(getNoSqlConfigByPipelineName(config.getPipelineName(), config.getSequenceNumber()));
                }
            }
            extract.setDataSource(dataSourcesService.getDataSourceByTypeAndSubtype(config.getExtractSourceType(), config.getExtractSourceSubType()));
            extract.setDataSourceConnection(dataSourcesConnService.getConnectionByName(config.getDataSourceConnectionName()).orElse(null));
            extractMetadata.add(extract);
        });
        return extractMetadata; 
  }

  
@CheckForNull
public ExtractMetadata getExtractMetadataByPipelineNameAndSequenceNumber(String name, int sequence) {

    logger.info("Fetching Extract Metadata Config for pipeline : " + name);
    SelectStatementProvider selectStatement = select(extractMetadataConfig.allColumns())
        .from(extractMetadataConfig)
        .where(extractMetadataConfig.pipelineName, isEqualTo(name))
        .and(extractMetadataConfig.sequenceNumber, isEqualTo(sequence))
        .build()
        .render(RenderingStrategies.MYBATIS3);

    ExtractMetadataConfig config = extractConfigDBMapper.selectOne(selectStatement).orElse(null);
    ExtractMetadata extract = new ExtractMetadata();
    extract.setPipelineName(config.getPipelineName());
    extract.setSequenceNumber(config.getSequenceNumber());
    extract.setExtractSourceType(config.getExtractSourceType());
    extract.setExtractSourceSubType(config.getExtractSourceSubType());
    extract.setDataframeName(config.getDataframeName());
    extract.setSourceConfiguration(config.getSourceConfiguration());
    extract.setPredecessorSequences(config.getPredecessorSequences());
    extract.setSuccessorSequences(config.getSuccessorSequences());
    extract.setRowFilter(config.getRowFilter());
    extract.setColumnFilter(config.getColumnFilter());
    extract.setDataSourceConnectionName(config.getDataSourceConnectionName());
    extract.setCreatedBy(config.getCreatedBy());
    extract.setCreatedTimestamp(config.getCreatedTimestamp());
    extract.setUpdatedBy(config.getUpdatedBy());
    extract.setUpdatedTimestamp(config.getUpdatedTimestamp());
    extract.setActiveFlag(config.getActiveFlag());
    
    switch (config.getExtractSourceType()) {
        case "Relational" -> {
            extract.setRelationalMetadata(getRelationalConfigByPipelineName(config.getPipelineName(), config.getSequenceNumber()));
        }
        case "Files" -> {
            extract.setFileMetadata(getFileConfigByPipelineName(config.getPipelineName(), config.getSequenceNumber()));
        }
        case "Stream" -> {
            extract.setStreamMetadata(getStreamConfigByPipelineName(config.getPipelineName(),config.getSequenceNumber()));
        }
        case "NoSql" -> {
            extract.setNoSqlMetadata(getNoSqlConfigByPipelineName(config.getPipelineName(), config.getSequenceNumber()));
        }
    }
    extract.setDataSource(dataSourcesService.getDataSourceByTypeAndSubtype(config.getExtractSourceType(), config.getExtractSourceSubType()));
    extract.setDataSourceConnection(dataSourcesConnService.getConnectionByName(config.getDataSourceConnectionName()).orElse(null));
    return extract; 
}


public int insertExtractMetadata(ExtractMetadata extractMetadata) {
    logger.info("Inserting Extract Metadata for pipeline : " + extractMetadata.getPipelineName());
    return switch (extractMetadata.getExtractSourceType()) {
        case "Files" -> {
            FileExtractMetadataConfig fileExtractConfigData = new FileExtractMetadataConfig();
            mapCommonFields(extractMetadata, fileExtractConfigData);
            fileExtractConfigData.setFileName(extractMetadata.getFileMetadata().getFileName());
            fileExtractConfigData.setFilePath(extractMetadata.getFileMetadata().getFilePath());
            fileExtractConfigData.setSchemaPath(extractMetadata.getFileMetadata().getSchemaPath());
            fileExtractConfigData.setSizeInByte(extractMetadata.getFileMetadata().getSizeInByte());
            fileExtractConfigData.setCompressionType(extractMetadata.getFileMetadata().getCompressionType());
            InsertStatementProvider<FileExtractMetadataConfig> insertStatement = SqlBuilder.insert(fileExtractConfigData)
                .into(fileExtractConfig)
                .map(fileExtractConfig.pipelineName).toProperty("pipelineName")
                .map(fileExtractConfig.sequenceNumber).toProperty("sequenceNumber")
                .map(fileExtractConfig.extractSourceType).toProperty("extractSourceType")
                .map(fileExtractConfig.extractSourceSubType).toProperty("extractSourceSubType")
                .map(fileExtractConfig.dataSourceConnectionName).toProperty("dataSourceConnectionName")
                .map(fileExtractConfig.sourceConfiguration).toProperty("sourceConfiguration")
                .map(fileExtractConfig.dataframeName).toProperty("dataframeName")
                .map(fileExtractConfig.predecessorSequences).toProperty("predecessorSequences")
                .map(fileExtractConfig.successorSequences).toProperty("successorSequences")
                .map(fileExtractConfig.rowFilter).toProperty("rowFilter")
                .map(fileExtractConfig.columnFilter).toProperty("columnFilter")
                .map(fileExtractConfig.createdTimestamp).toConstant("'" + new Timestamp(System.currentTimeMillis()).toString() + "'")
                .map(fileExtractConfig.createdBy).toProperty("createdBy")
                .map(fileExtractConfig.updatedTimestamp).toProperty("updatedTimestamp")
                .map(fileExtractConfig.updatedBy).toProperty("updatedBy")
                .map(fileExtractConfig.activeFlag).toProperty("activeFlag")
                .map(fileExtractConfig.fileName).toProperty("fileName")
                .map(fileExtractConfig.filePath).toProperty("filePath")
                .map(fileExtractConfig.schemaPath).toProperty("schemaPath")
                .map(fileExtractConfig.sizeInByte).toProperty("sizeInByte")
                .map(fileExtractConfig.compressionType).toProperty("compressionType")
                .build()
                .render(RenderingStrategies.MYBATIS3);
            yield FileDBMapper.insert(insertStatement);
        }
        case "Relational" -> {
            RelationalExtractMetadataConfig relationalExtractConfigData = new RelationalExtractMetadataConfig();
            mapCommonFields(extractMetadata, relationalExtractConfigData);
            relationalExtractConfigData.setDatabaseName(extractMetadata.getRelationalMetadata().getDatabaseName());
            relationalExtractConfigData.setTableName(extractMetadata.getRelationalMetadata().getTableName());
            relationalExtractConfigData.setSchemaName(extractMetadata.getRelationalMetadata().getSchemaName());
            relationalExtractConfigData.setSqlText(extractMetadata.getRelationalMetadata().getSqlText());
            
            InsertStatementProvider<RelationalExtractMetadataConfig> insertStatement = SqlBuilder.insert(relationalExtractConfigData)
                .into(relationalExtractConfig)
                .map(relationalExtractConfig.pipelineName).toProperty("pipelineName")
                .map(relationalExtractConfig.sequenceNumber).toProperty("sequenceNumber")
                .map(relationalExtractConfig.extractSourceType).toProperty("extractSourceType")
                .map(relationalExtractConfig.extractSourceSubType).toProperty("extractSourceSubType")
                .map(relationalExtractConfig.dataSourceConnectionName).toProperty("dataSourceConnectionName")
                .map(relationalExtractConfig.sourceConfiguration).toProperty("sourceConfiguration")
                .map(relationalExtractConfig.dataframeName).toProperty("dataframeName")
                .map(relationalExtractConfig.predecessorSequences).toProperty("predecessorSequences")
                .map(relationalExtractConfig.successorSequences).toProperty("successorSequences")
                .map(relationalExtractConfig.rowFilter).toProperty("rowFilter")
                .map(relationalExtractConfig.columnFilter).toProperty("columnFilter")
                .map(relationalExtractConfig.createdTimestamp).toConstant("'" + new Timestamp(System.currentTimeMillis()).toString() + "'")
                .map(relationalExtractConfig.createdBy).toProperty("createdBy")
                .map(relationalExtractConfig.updatedTimestamp).toProperty("updatedTimestamp")
                .map(relationalExtractConfig.updatedBy).toProperty("updatedBy")
                .map(relationalExtractConfig.activeFlag).toProperty("activeFlag")
                .map(relationalExtractConfig.databaseName).toProperty("databaseName")
                .map(relationalExtractConfig.tableName).toProperty("tableName")
                .map(relationalExtractConfig.schemaName).toProperty("schemaName")
                .map(relationalExtractConfig.sqlText).toProperty("sqlText")
                .build()
                .render(RenderingStrategies.MYBATIS3);
            yield RelationalDBMapper.insert(insertStatement);
        }
        case "NoSql" -> {
            NoSqlExtractMetadataConfig noSqlExtractConfigData = new NoSqlExtractMetadataConfig();
            mapCommonFields(extractMetadata, noSqlExtractConfigData);
            noSqlExtractConfigData.setCollection(extractMetadata.getNoSqlMetadata().getCollection());
            noSqlExtractConfigData.setPartitioner(extractMetadata.getNoSqlMetadata().getPartitioner());

            InsertStatementProvider<NoSqlExtractMetadataConfig> insertStatement = SqlBuilder.insert(noSqlExtractConfigData)
                .into(noSqlExtractConfig)
                .map(noSqlExtractConfig.pipelineName).toProperty("pipelineName")
                .map(noSqlExtractConfig.sequenceNumber).toProperty("sequenceNumber")
                .map(noSqlExtractConfig.extractSourceType).toProperty("extractSourceType")
                .map(noSqlExtractConfig.extractSourceSubType).toProperty("extractSourceSubType")
                .map(noSqlExtractConfig.dataSourceConnectionName).toProperty("dataSourceConnectionName")
                .map(noSqlExtractConfig.sourceConfiguration).toProperty("sourceConfiguration")
                .map(noSqlExtractConfig.dataframeName).toProperty("dataframeName")
                .map(noSqlExtractConfig.predecessorSequences).toProperty("predecessorSequences")
                .map(noSqlExtractConfig.successorSequences).toProperty("successorSequences")
                .map(noSqlExtractConfig.rowFilter).toProperty("rowFilter")
                .map(noSqlExtractConfig.columnFilter).toProperty("columnFilter")
                .map(noSqlExtractConfig.createdTimestamp).toConstant("'" + new Timestamp(System.currentTimeMillis()).toString() + "'")
                .map(noSqlExtractConfig.createdBy).toProperty("createdBy")
                .map(noSqlExtractConfig.updatedTimestamp).toProperty("updatedTimestamp")
                .map(noSqlExtractConfig.updatedBy).toProperty("updatedBy")
                .map(noSqlExtractConfig.activeFlag).toProperty("activeFlag")
                .map(noSqlExtractConfig.collection).toProperty("collection")
                .map(noSqlExtractConfig.partitioner).toProperty("partitioner")
                .build()
                .render(RenderingStrategies.MYBATIS3);
            yield NoSqlDBMapper.insert(insertStatement);
           
        }
        case "Stream" -> {
            StreamExtractMetadataConfig streamExtractConfigData = new StreamExtractMetadataConfig();
            mapCommonFields(extractMetadata, streamExtractConfigData);
            streamExtractConfigData.setKafkaConsumerTopic(extractMetadata.getStreamMetadata().getKafkaConsumerTopic());
            streamExtractConfigData.setKafkaConsumerGroup(extractMetadata.getStreamMetadata().getKafkaConsumerGroup());
            streamExtractConfigData.setKafkaMaxOffset(extractMetadata.getStreamMetadata().getKafkaMaxOffset());
            streamExtractConfigData.setKafkaPollTimeout(extractMetadata.getStreamMetadata().getKafkaPollTimeout());
            streamExtractConfigData.setKafkaStrtOffset(extractMetadata.getStreamMetadata().getKafkaStrtOffset());
            streamExtractConfigData.setTranctnlCnsumrFlg(extractMetadata.getStreamMetadata().getTranctnlCnsumrFlg());
            streamExtractConfigData.setWatrmrkDuration(extractMetadata.getStreamMetadata().getWatrmrkDuration());
            streamExtractConfigData.setStgFormt(extractMetadata.getStreamMetadata().getStgFormt());
            streamExtractConfigData.setStgPath(extractMetadata.getStreamMetadata().getStgPath());
            streamExtractConfigData.setStgPartitions(extractMetadata.getStreamMetadata().getStgPartitions());

            InsertStatementProvider<StreamExtractMetadataConfig> insertStatement = SqlBuilder.insert(streamExtractConfigData)
                .into(streamsExtractConfig)
                .map(streamsExtractConfig.pipelineName).toProperty("pipelineName")
                .map(streamsExtractConfig.sequenceNumber).toProperty("sequenceNumber")
                .map(streamsExtractConfig.extractSourceType).toProperty("extractSourceType")
                .map(streamsExtractConfig.extractSourceSubType).toProperty("extractSourceSubType")
                .map(streamsExtractConfig.dataSourceConnectionName).toProperty("dataSourceConnectionName")
                .map(streamsExtractConfig.sourceConfiguration).toProperty("sourceConfiguration")
                .map(streamsExtractConfig.dataframeName).toProperty("dataframeName")
                .map(streamsExtractConfig.predecessorSequences).toProperty("predecessorSequences")
                .map(streamsExtractConfig.successorSequences).toProperty("successorSequences")
                .map(streamsExtractConfig.rowFilter).toProperty("rowFilter")
                .map(streamsExtractConfig.columnFilter).toProperty("columnFilter")
                .map(streamsExtractConfig.createdTimestamp).toConstant("'" + new Timestamp(System.currentTimeMillis()).toString() + "'")
                .map(streamsExtractConfig.createdBy).toProperty("createdBy")
                .map(streamsExtractConfig.updatedTimestamp).toProperty("updatedTimestamp")
                .map(streamsExtractConfig.updatedBy).toProperty("updatedBy")
                .map(streamsExtractConfig.activeFlag).toProperty("activeFlag")
                .map(streamsExtractConfig.kafkaConsumerTopic).toProperty("kafkaConsumerTopic")
                .map(streamsExtractConfig.kafkaConsumerGroup).toProperty("kafkaConsumerGroup")
                .map(streamsExtractConfig.kafkaMaxOffset).toProperty("kafkaMaxOffset")
                .map(streamsExtractConfig.kafkaPollTimeout).toProperty("kafkaPollTimeout")
                .map(streamsExtractConfig.kafkaStrtOffset).toProperty("kafkaStrtOffset")
                .map(streamsExtractConfig.tranctnlCnsumrFlg).toProperty("tranctnlCnsumrFlg")
                .map(streamsExtractConfig.watrmrkDuration).toProperty("watrmrkDuration")
                .map(streamsExtractConfig.stgFormt).toProperty("stgFormt")
                .map(streamsExtractConfig.stgPath).toProperty("stgPath")
                .map(streamsExtractConfig.stgPartitions).toProperty("stgPartitions")
                .build()
                .render(RenderingStrategies.MYBATIS3);
            yield StreamsDBMapper.insert(insertStatement);
        }
        default -> 0;

    };
}

public int deleteExtractMetadata(String pipelineName) {
    logger.info("Deleting Extract Metadata for pipeline : " + pipelineName);
    DeleteStatementProvider deleteStatement = SqlBuilder.deleteFrom(extractMetadataConfig)
        .where(extractMetadataConfig.pipelineName, isEqualTo(pipelineName))
        .build()
        .render(RenderingStrategies.MYBATIS3);
    return extractDBMapper.delete(deleteStatement);
}

public int deleteExtractMetadata(String pipelineName, int sequence) {
    logger.info("Deleting Extract Metadata for pipeline : " + pipelineName);
    DeleteStatementProvider deleteStatement = SqlBuilder.deleteFrom(extractMetadataConfig)
        .where(extractMetadataConfig.pipelineName, isEqualTo(pipelineName))
        .and(extractMetadataConfig.sequenceNumber, isEqualTo(sequence))
        .build()
        .render(RenderingStrategies.MYBATIS3);
    return extractDBMapper.delete(deleteStatement);
}

public int updateExtractMetadata(ExtractMetadata extractMetadata) {
    logger.info("Updating Extract Metadata for pipeline : " + extractMetadata.getPipelineName());
    return switch (extractMetadata.getExtractSourceType()) {
        case "Files" -> {
            UpdateStatementProvider updateStatement = SqlBuilder.update(fileExtractConfig)
                .set(fileExtractConfig.extractSourceType).equalToWhenPresent(extractMetadata.getExtractSourceType())
                .set(fileExtractConfig.extractSourceSubType).equalToWhenPresent(extractMetadata.getExtractSourceSubType())
                .set(fileExtractConfig.dataSourceConnectionName).equalToWhenPresent(extractMetadata.getDataSourceConnectionName())
                .set(fileExtractConfig.sourceConfiguration).equalToWhenPresent(extractMetadata.getSourceConfiguration())
                .set(fileExtractConfig.dataframeName).equalToWhenPresent(extractMetadata.getDataframeName())
                .set(fileExtractConfig.predecessorSequences).equalToWhenPresent(extractMetadata.getPredecessorSequences())
                .set(fileExtractConfig.successorSequences).equalToWhenPresent(extractMetadata.getSuccessorSequences())
                .set(fileExtractConfig.rowFilter).equalToWhenPresent(extractMetadata.getRowFilter())
                .set(fileExtractConfig.columnFilter).equalToWhenPresent(extractMetadata.getColumnFilter())
                .set(fileExtractConfig.updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
                .set(fileExtractConfig.updatedBy).equalToWhenPresent(extractMetadata.getUpdatedBy())
                .set(fileExtractConfig.activeFlag).equalToWhenPresent(extractMetadata.getActiveFlag())
                .set(fileExtractConfig.fileName).equalToWhenPresent(extractMetadata.getFileMetadata().getFileName())
                .set(fileExtractConfig.filePath).equalToWhenPresent(extractMetadata.getFileMetadata().getFilePath())
                .set(fileExtractConfig.schemaPath).equalToWhenPresent(extractMetadata.getFileMetadata().getSchemaPath())
                .set(fileExtractConfig.sizeInByte).equalToWhenPresent(extractMetadata.getFileMetadata().getSizeInByte() != null ? BigInteger.valueOf(extractMetadata.getFileMetadata().getSizeInByte()) : null)
                .set(fileExtractConfig.compressionType).equalToWhenPresent(extractMetadata.getFileMetadata().getCompressionType())
                .where(fileExtractConfig.pipelineName, isEqualTo(extractMetadata.getPipelineName()))
                .and(fileExtractConfig.sequenceNumber, isEqualTo(extractMetadata.getSequenceNumber()))
                .build()
                .render(RenderingStrategies.MYBATIS3);
            yield FileDBMapper.update(updateStatement);
        }
        case "Relational" -> {
            UpdateStatementProvider updateStatement = SqlBuilder.update(relationalExtractConfig)
                .set(relationalExtractConfig.extractSourceType).equalToWhenPresent(extractMetadata.getExtractSourceType())
                .set(relationalExtractConfig.extractSourceSubType).equalToWhenPresent(extractMetadata.getExtractSourceSubType())
                .set(relationalExtractConfig.dataSourceConnectionName).equalToWhenPresent(extractMetadata.getDataSourceConnectionName())
                .set(relationalExtractConfig.sourceConfiguration).equalToWhenPresent(extractMetadata.getSourceConfiguration())
                .set(relationalExtractConfig.dataframeName).equalToWhenPresent(extractMetadata.getDataframeName())
                .set(relationalExtractConfig.predecessorSequences).equalToWhenPresent(extractMetadata.getPredecessorSequences())
                .set(relationalExtractConfig.successorSequences).equalToWhenPresent(extractMetadata.getSuccessorSequences())
                .set(relationalExtractConfig.rowFilter).equalToWhenPresent(extractMetadata.getRowFilter())
                .set(relationalExtractConfig.columnFilter).equalToWhenPresent(extractMetadata.getColumnFilter())
                .set(relationalExtractConfig.updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
                .set(relationalExtractConfig.updatedBy).equalToWhenPresent(extractMetadata.getUpdatedBy())
                .set(relationalExtractConfig.activeFlag).equalToWhenPresent(extractMetadata.getActiveFlag())
                .set(relationalExtractConfig.databaseName).equalToWhenPresent(extractMetadata.getRelationalMetadata().getDatabaseName())
                .set(relationalExtractConfig.tableName).equalToWhenPresent(extractMetadata.getRelationalMetadata().getTableName())
                .set(relationalExtractConfig.schemaName).equalToWhenPresent(extractMetadata.getRelationalMetadata().getSchemaName())
                .set(relationalExtractConfig.sqlText).equalToWhenPresent(extractMetadata.getRelationalMetadata().getSqlText())
                .where(relationalExtractConfig.pipelineName, isEqualTo(extractMetadata.getPipelineName()))
                .and(relationalExtractConfig.sequenceNumber, isEqualTo(extractMetadata.getSequenceNumber()))
                .build()
                .render(RenderingStrategies.MYBATIS3);
            yield RelationalDBMapper.update(updateStatement);
        }
        case "NoSql" -> {
            UpdateStatementProvider updateStatement = SqlBuilder.update(noSqlExtractConfig)
                .set(noSqlExtractConfig.extractSourceType).equalToWhenPresent(extractMetadata.getExtractSourceType())
                .set(noSqlExtractConfig.extractSourceSubType).equalToWhenPresent(extractMetadata.getExtractSourceSubType())
                .set(noSqlExtractConfig.dataSourceConnectionName).equalToWhenPresent(extractMetadata.getDataSourceConnectionName())
                .set(noSqlExtractConfig.sourceConfiguration).equalToWhenPresent(extractMetadata.getSourceConfiguration())
                .set(noSqlExtractConfig.dataframeName).equalToWhenPresent(extractMetadata.getDataframeName())
                .set(noSqlExtractConfig.predecessorSequences).equalToWhenPresent(extractMetadata.getPredecessorSequences())
                .set(noSqlExtractConfig.successorSequences).equalToWhenPresent(extractMetadata.getSuccessorSequences())
                .set(noSqlExtractConfig.rowFilter).equalToWhenPresent(extractMetadata.getRowFilter())
                .set(noSqlExtractConfig.columnFilter).equalToWhenPresent(extractMetadata.getColumnFilter())
                .set(noSqlExtractConfig.updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
                .set(noSqlExtractConfig.updatedBy).equalToWhenPresent(extractMetadata.getUpdatedBy())
                .set(noSqlExtractConfig.activeFlag).equalToWhenPresent(extractMetadata.getActiveFlag())
                .set(noSqlExtractConfig.collection).equalToWhenPresent(extractMetadata.getNoSqlMetadata().getCollection())
                .set(noSqlExtractConfig.partitioner).equalToWhenPresent(extractMetadata.getNoSqlMetadata().getPartitioner())
                .where(noSqlExtractConfig.pipelineName, isEqualTo(extractMetadata.getPipelineName()))
                .and(noSqlExtractConfig.sequenceNumber, isEqualTo(extractMetadata.getSequenceNumber()))
                .build()
                .render(RenderingStrategies.MYBATIS3);
            yield NoSqlDBMapper.update(updateStatement);
        }
        case "Stream" -> {
            UpdateStatementProvider updateStatement = SqlBuilder.update(streamsExtractConfig)
                .set(streamsExtractConfig.extractSourceType).equalToWhenPresent(extractMetadata.getExtractSourceType())
                .set(streamsExtractConfig.extractSourceSubType).equalToWhenPresent(extractMetadata.getExtractSourceSubType())
                .set(streamsExtractConfig.dataSourceConnectionName).equalToWhenPresent(extractMetadata.getDataSourceConnectionName())
                .set(streamsExtractConfig.sourceConfiguration).equalToWhenPresent(extractMetadata.getSourceConfiguration())
                .set(streamsExtractConfig.dataframeName).equalToWhenPresent(extractMetadata.getDataframeName())
                .set(streamsExtractConfig.predecessorSequences).equalToWhenPresent(extractMetadata.getPredecessorSequences())
                .set(streamsExtractConfig.successorSequences).equalToWhenPresent(extractMetadata.getSuccessorSequences())
                .set(streamsExtractConfig.rowFilter).equalToWhenPresent(extractMetadata.getRowFilter())
                .set(streamsExtractConfig.columnFilter).equalToWhenPresent(extractMetadata.getColumnFilter())
                .set(streamsExtractConfig.updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
                .set(streamsExtractConfig.updatedBy).equalToWhenPresent(extractMetadata.getUpdatedBy())
                .set(streamsExtractConfig.activeFlag).equalToWhenPresent(extractMetadata.getActiveFlag())
                .set(streamsExtractConfig.kafkaConsumerTopic).equalToWhenPresent(extractMetadata.getStreamMetadata().getKafkaConsumerTopic())
                .set(streamsExtractConfig.kafkaConsumerGroup).equalToWhenPresent(extractMetadata.getStreamMetadata().getKafkaConsumerGroup())
                .set(streamsExtractConfig.kafkaMaxOffset).equalToWhenPresent(extractMetadata.getStreamMetadata().getKafkaMaxOffset())
                .set(streamsExtractConfig.kafkaPollTimeout).equalToWhenPresent(extractMetadata.getStreamMetadata().getKafkaPollTimeout())
                .set(streamsExtractConfig.kafkaStrtOffset).equalToWhenPresent(extractMetadata.getStreamMetadata().getKafkaStrtOffset())
                .set(streamsExtractConfig.tranctnlCnsumrFlg).equalToWhenPresent(extractMetadata.getStreamMetadata().getTranctnlCnsumrFlg())
                .set(streamsExtractConfig.watrmrkDuration).equalToWhenPresent(extractMetadata.getStreamMetadata().getWatrmrkDuration())
                .set(streamsExtractConfig.stgFormt).equalToWhenPresent(extractMetadata.getStreamMetadata().getStgFormt())
                .set(streamsExtractConfig.stgPath).equalToWhenPresent(extractMetadata.getStreamMetadata().getStgPath())
                .set(streamsExtractConfig.stgPartitions).equalToWhenPresent(extractMetadata.getStreamMetadata().getStgPartitions())
                .where(streamsExtractConfig.pipelineName, isEqualTo(extractMetadata.getPipelineName()))
                .and(streamsExtractConfig.sequenceNumber, isEqualTo(extractMetadata.getSequenceNumber()))
                .build()
                .render(RenderingStrategies.MYBATIS3);
            yield StreamsDBMapper.update(updateStatement);
        }
        default -> 0;
      };
    }
         

private <T extends ExtractMetadataConfig> void mapCommonFields(ExtractMetadata source, T target) {
  target.setPipelineName(source.getPipelineName());
  target.setSequenceNumber(source.getSequenceNumber());
  target.setExtractSourceType(source.getExtractSourceType());
  target.setExtractSourceSubType(source.getExtractSourceSubType());
  target.setDataSourceConnectionName(source.getDataSourceConnectionName());
  target.setSourceConfiguration(source.getSourceConfiguration());
  target.setDataframeName(source.getDataframeName());
  target.setPredecessorSequences(source.getPredecessorSequences());
  target.setSuccessorSequences(source.getSuccessorSequences());
  target.setRowFilter(source.getRowFilter());
  target.setColumnFilter(source.getColumnFilter());
  target.setCreatedTimestamp(source.getCreatedTimestamp());
  target.setCreatedBy(source.getCreatedBy());
  target.setUpdatedTimestamp(source.getUpdatedTimestamp());
  target.setUpdatedBy(source.getUpdatedBy());
  target.setActiveFlag(source.getActiveFlag());
}

public List<ExtractMetadataConfig> getExtractConfigByPipelineName(String pipelineName) {
    logger.info("Fetching Extract Metadata Config for pipeline : " + pipelineName);
    SelectStatementProvider selectStatement = select(extractMetadataConfig.allColumns())
        .from(extractMetadataConfig)
        .where(extractMetadataConfig.pipelineName, isEqualTo(pipelineName))
        .orderBy(extractMetadataConfig.pipelineName, extractMetadataConfig.sequenceNumber)
        .build()
        .render(RenderingStrategies.MYBATIS3);

    return extractConfigDBMapper.selectMany(selectStatement);
}

public FileExtractMetadataTable getFileConfigByPipelineName(String pipelineName, int sequenceNumber) {
    logger.info("Fetching File Metadata Config for pipeline : " + pipelineName);
    SelectStatementProvider selectStatement = select(
        fileExtractTable.fileName,
        fileExtractTable.filePath,
        fileExtractTable.schemaPath,
        fileExtractTable.sizeInByte,
        fileExtractTable.compressionType
    )
        .from(fileExtractTable)
        .where(fileExtractTable.pipelineName, isEqualTo(pipelineName))
        .and(fileExtractTable.sequenceNumber, isEqualTo(sequenceNumber))
        .build()
        .render(RenderingStrategies.MYBATIS3);

    return fileExDBMapper.selectOne(selectStatement).orElse(null);
}

public RelationalExtractMetadataTable getRelationalConfigByPipelineName(String pipelineName, int sequenceNumber) {
    logger.info("Fetching Relational Metadata Config for pipeline : " + pipelineName);
    SelectStatementProvider selectStatement = select(relationalExtractTable.allColumns())
        .from(relationalExtractTable)
        .where(relationalExtractTable.pipelineName, isEqualTo(pipelineName))
        .and(relationalExtractTable.sequenceNumber, isEqualTo(sequenceNumber))
        .build()
        .render(RenderingStrategies.MYBATIS3);

    return relationalExDBMapper.selectOne(selectStatement).orElse(null);
}

public StreamExtractMetadataTable getStreamConfigByPipelineName(String pipelineName, int sequenceNumber) {
    logger.info("Fnulletching Stream Metadata Config for pipeline : " + pipelineName);
    SelectStatementProvider selectStatement = select(streamsExtractTable.allColumns())
        .from(streamsExtractTable)
        .where(streamsExtractTable.pipelineName, isEqualTo(pipelineName))
        .and(streamsExtractTable.sequenceNumber, isEqualTo(sequenceNumber))
        .build()
        .render(RenderingStrategies.MYBATIS3);

    return streamEXDBMapper.selectOne(selectStatement).orElse(null);
}

public NoSqlExtractMetadataTable getNoSqlConfigByPipelineName(String pipelineName, int sequenceNumber) {
    logger.info("Fetching NoSql Metadata Config for pipeline : " + pipelineName);
    SelectStatementProvider selectStatement = select(noSqlExtractTable.allColumns())
        .from(noSqlExtractTable)
        .where(noSqlExtractTable.pipelineName, isEqualTo(pipelineName))
        .and(noSqlExtractTable.sequenceNumber, isEqualTo(sequenceNumber))
        .build()
        .render(RenderingStrategies.MYBATIS3);

    return noSqlExDBMapper.selectOne(selectStatement).orElse(null);
}


public List<ExtractMetadata> getExtractMetadata(String pipelineName) {

    List<ExtractMetadata> extractMetadata = new ArrayList<ExtractMetadata>();
    List<ExtractMetadataConfig> ec = getExtractConfigByPipelineName(pipelineName);
    ec.forEach(config -> {
        ExtractMetadata extract = new ExtractMetadata();
        extract.setPipelineName(config.getPipelineName());
        extract.setSequenceNumber(config.getSequenceNumber());
        extract.setExtractSourceType(config.getExtractSourceType());
        extract.setExtractSourceSubType(config.getExtractSourceSubType());
        extract.setDataframeName(config.getDataframeName());
        extract.setSourceConfiguration(config.getSourceConfiguration());
        extract.setPredecessorSequences(config.getPredecessorSequences());
        extract.setSuccessorSequences(config.getSuccessorSequences());
        extract.setRowFilter(config.getRowFilter());
        extract.setColumnFilter(config.getColumnFilter());
        extract.setDataSourceConnectionName(config.getDataSourceConnectionName());
        extract.setCreatedBy(config.getCreatedBy());
        extract.setCreatedTimestamp(config.getCreatedTimestamp());
        extract.setUpdatedBy(config.getUpdatedBy());
        extract.setUpdatedTimestamp(config.getUpdatedTimestamp());
        extract.setActiveFlag(config.getActiveFlag());

        switch (config.getExtractSourceType()) {
            case "Relational" -> {
                extract.setRelationalMetadata(getRelationalConfigByPipelineName(pipelineName, config.getSequenceNumber()));
            }
            case "Files" -> {
                extract.setFileMetadata(getFileConfigByPipelineName(pipelineName, config.getSequenceNumber()));
            }
            case "Stream" -> {
                extract.setStreamMetadata(getStreamConfigByPipelineName(pipelineName,config.getSequenceNumber()));
            }
            case "NoSql" -> {
                extract.setNoSqlMetadata(getNoSqlConfigByPipelineName(pipelineName, config.getSequenceNumber()));
            }
        }
        extract.setDataSource(dataSourcesService.getDataSourceByTypeAndSubtype(config.getExtractSourceType(), config.getExtractSourceSubType()));
        extract.setDataSourceConnection(dataSourcesConnService.getConnectionByName(config.getDataSourceConnectionName()).orElse(null));
        extractMetadata.add(extract);
    });



    return extractMetadata;
    
}



// public int updateExtractMetadata1(ExtractMetadataResponse extractMetadata) {
//   logger.info("Updating Extract Metadata for pipeline : " + extractMetadata.getPipelineName());
//   System.out.println("Updating Extract Metadata for pipeline : " + extractMetadata.getPipelineName());

//   // Build the update statement dynamically based on the ExtractSourceType
//   UpdateStatementProvider updateStatement = buildUpdateStatement(extractMetadata);

//   if (updateStatement != null) {
//       // Perform the update based on the ExtractSourceType
//       return executeUpdate(extractMetadata.getExtractSourceType(), updateStatement);
//   }

//   return 0;
// }

// private UpdateStatementProvider buildUpdateStatement(ExtractMetadataResponse extractMetadata) {
//   // Build the common update statement for any type
//   UpdateStatementProvider updateBuilder = SqlBuilder.update((SqlTable) getSourceTypeTableEntity(extractMetadata))
//           .set(getSourceTypeTableEntity(extractMetadata).extractSourceType).equalToWhenPresent(extractMetadata.getExtractSourceType())
//           .set(getExtractConfig(extractMetadata).extractSourceSubType).equalToWhenPresent(extractMetadata.getExtractSourceSubType())
//           .set(getExtractConfig(extractMetadata).dataSourceConnectionName).equalToWhenPresent(extractMetadata.getDataSourceConnectionName())
//           .set(getExtractConfig(extractMetadata).sourceConfiguration).equalToWhenPresent(extractMetadata.getSourceConfiguration())
//           .set(getExtractConfig(extractMetadata).dataframeName).equalToWhenPresent(extractMetadata.getDataframeName())
//           .set(getExtractConfig(extractMetadata).predecessorSequences).equalToWhenPresent(extractMetadata.getPredecessorSequences())
//           .set(getExtractConfig(extractMetadata).successorSequences).equalToWhenPresent(extractMetadata.getSuccessorSequences())
//           .set(getExtractConfig(extractMetadata).rowFilter).equalToWhenPresent(extractMetadata.getRowFilter())
//           .set(getExtractConfig(extractMetadata).columnFilter).equalToWhenPresent(extractMetadata.getColumnFilter())
//           .set(getExtractConfig(extractMetadata).updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
//           .set(getExtractConfig(extractMetadata).updatedBy).equalToWhenPresent(extractMetadata.getUpdatedBy())
//           .set(getExtractConfig(extractMetadata).activeFlag).equalToWhenPresent(extractMetadata.getActiveFlag());

//   // Add type-specific fields based on extract source type
//   addTypeSpecificFields(extractMetadata, updateBuilder);

//   // Add where clause
//   updateBuilder.where(getExtractConfig(extractMetadata).pipelineName, isEqualTo(extractMetadata.getPipelineName()))
//           .and(getExtractConfig(extractMetadata).sequenceNumber, isEqualTo(extractMetadata.getSequenceNumber()));

//   return updateBuilder.build().render(RenderingStrategies.MYBATIS3);
// }

// private void addTypeSpecificFields(ExtractMetadataResponse extractMetadata, UpdateStatementProvider.Builder updateBuilder) {
//   switch (extractMetadata.getExtractSourceType()) {
//       case "Files":
//           updateBuilder.set(fileExtractConfig.fileName).equalToWhenPresent(extractMetadata.getFileName())
//                   .set(fileExtractConfig.filePath).equalToWhenPresent(extractMetadata.getFilePath())
//                   .set(fileExtractConfig.schemaPath).equalToWhenPresent(extractMetadata.getSchemaPath())
//                   .set(fileExtractConfig.sizeInByte).equalToWhenPresent(extractMetadata.getSizeInByte() != null ? BigInteger.valueOf(extractMetadata.getSizeInByte()) : null)
//                   .set(fileExtractConfig.compressionType).equalToWhenPresent(extractMetadata.getCompressionType());
//           break;
//       case "Relational":
//           updateBuilder.set(relationalExtractConfig.databaseName).equalToWhenPresent(extractMetadata.getDatabaseName())
//                   .set(relationalExtractConfig.tableName).equalToWhenPresent(extractMetadata.getTableName())
//                   .set(relationalExtractConfig.schemaName).equalToWhenPresent(extractMetadata.getSchemaName())
//                   .set(relationalExtractConfig.sqlText).equalToWhenPresent(extractMetadata.getSqlText());
//           break;
//       case "NoSql":
//           updateBuilder.set(noSqlExtractConfig.collection).equalToWhenPresent(extractMetadata.getCollection())
//                   .set(noSqlExtractConfig.partitioner).equalToWhenPresent(extractMetadata.getPartitioner());
//           break;
//       case "Stream":
//           updateBuilder.set(streamsExtractConfig.kafkaConsumerTopic).equalToWhenPresent(extractMetadata.getKafkaConsumerTopic())
//                   .set(streamsExtractConfig.kafkaConsumerGroup).equalToWhenPresent(extractMetadata.getKafkaConsumerGroup())
//                   .set(streamsExtractConfig.kafkaMaxOffset).equalToWhenPresent(extractMetadata.getKafkaMaxOffset())
//                   .set(streamsExtractConfig.kafkaPollTimeout).equalToWhenPresent(extractMetadata.getKafkaPollTimeout())
//                   .set(streamsExtractConfig.kafkaStrtOffset).equalToWhenPresent(extractMetadata.getKafkaStrtOffset())
//                   .set(streamsExtractConfig.tranctnlCnsumrFlg).equalToWhenPresent(extractMetadata.getTranctnlCnsumrFlg())
//                   .set(streamsExtractConfig.watrmrkDuration).equalToWhenPresent(extractMetadata.getWatrmrkDuration())
//                   .set(streamsExtractConfig.stgFormt).equalToWhenPresent(extractMetadata.getStgFormt())
//                   .set(streamsExtractConfig.stgPath).equalToWhenPresent(extractMetadata.getStgPath())
//                   .set(streamsExtractConfig.stgPartitions).equalToWhenPresent(extractMetadata.getStgPartitions());
//           break;
//       default:
//           break;
//   }
// }

// private Object getSourceTypeTableEntity(ExtractMetadataResponse extractMetadata) {
//   switch (extractMetadata.getExtractSourceType()) {
//       case "Files":
//           return fileExtractConfig;
//       case "Relational":
//           return relationalExtractConfig;
//       case "NoSql":
//           return noSqlExtractConfig;
//       case "Stream":
//           return streamsExtractConfig;
//       default:
//           return null;
//   }
// }

// private int executeUpdate(String extractSourceType, UpdateStatementProvider updateStatement) {
//   switch (extractSourceType) {
//       case "Files":
//           return FileDBMapper.update(updateStatement);
//       case "Relational":
//           return RelationalDBMapper.update(updateStatement);
//       case "NoSql":
//           return NoSqlDBMapper.update(updateStatement);
//       case "Stream":
//           return StreamsDBMapper.update(updateStatement);
//       default:
//           return 0;
//   }
//}


  

}
