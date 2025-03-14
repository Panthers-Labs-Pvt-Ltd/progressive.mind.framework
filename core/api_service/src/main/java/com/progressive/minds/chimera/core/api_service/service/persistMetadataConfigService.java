package com.progressive.minds.chimera.core.api_service.service;

import static com.progressive.minds.chimera.core.api_service.entity.PersistMetadataConfigDynamicSqlEntity.persistMetadataConfig;
import static com.progressive.minds.chimera.core.api_service.entity.PersistMetadataConfigDynamicSqlEntity.pipelineName;
import static com.progressive.minds.chimera.core.api_service.entity.PersistMetadataConfigDynamicSqlEntity.sequenceNumber;
import static com.progressive.minds.chimera.core.api_service.entity.RelationalPersistMetadataDynamicSqlEntity.relationalPersistTable;
import static com.progressive.minds.chimera.core.api_service.entity.StreamsPersistMetadataDynamicSqlEntity.streamsPersistTable;
import static com.progressive.minds.chimera.core.api_service.entity.FilePersistMetadataDynamicSqlEntity.filePersistTable;
import static com.progressive.minds.chimera.core.api_service.entity.NoSqlPersistMetadataDynamicSqlEntity.noSqlPersistTable;
import static com.progressive.minds.chimera.core.api_service.entity.RelationalPersistMetadataConfigDynamicSqlEntity.relationalPersistConfig;
import static com.progressive.minds.chimera.core.api_service.entity.FilePersistMetadataConfigDynamicSqlEntity.filePersistConfig;
import static com.progressive.minds.chimera.core.api_service.entity.StreamsPersistMetadataConfigDynamicSqlEntity.StreamsPersistConfig;
import static com.progressive.minds.chimera.core.api_service.entity.NoSqlPersistMetadataConfigDynamicSqlEntity.noSqlPersistConfig;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

import com.progressive.minds.chimera.core.api_service.dto.FilePersistMetadataConfig;
import com.progressive.minds.chimera.core.api_service.dto.FilePersistMetadataTable;
import com.progressive.minds.chimera.core.api_service.dto.NoSqlPersistMetadataConfig;
import com.progressive.minds.chimera.core.api_service.dto.NoSqlPersistMetadataTable;
import com.progressive.minds.chimera.core.api_service.dto.PersistMetadata;
import com.progressive.minds.chimera.core.api_service.dto.PersistMetadataConfig;
import com.progressive.minds.chimera.core.api_service.dto.RelationalPersistMetadataConfig;
import com.progressive.minds.chimera.core.api_service.dto.RelationalPersistMetadataTable;
import com.progressive.minds.chimera.core.api_service.dto.StreamPersistMetadataConfig;
import com.progressive.minds.chimera.core.api_service.dto.StreamPersistMetadataTable;
import com.progressive.minds.chimera.core.api_service.entity.PersistMetadataConfigDynamicSqlEntity;
import com.progressive.minds.chimera.core.api_service.repository.FilePersistMetadataConfigDBMapper;
// import com.progressive.minds.chimera.core.api_service.repository.DBMapper;
import com.progressive.minds.chimera.core.api_service.repository.FilePersistMetadataTableDBMapper;
import com.progressive.minds.chimera.core.api_service.repository.NoSqlPersistMetadataTableDBMapper;

import com.progressive.minds.chimera.core.api_service.repository.PersistMetadataConfigDBMapper;
import com.progressive.minds.chimera.core.api_service.repository.RelationalPersistMetadataConfigDBMapper;
import com.progressive.minds.chimera.core.api_service.repository.RelationalPersistMetadataTableDBMapper;
import com.progressive.minds.chimera.core.api_service.repository.StreamsPersistMetadataConfigDBMapper;
import com.progressive.minds.chimera.core.api_service.repository.StreamsPersistMetadataTableDBMapper;
import com.progressive.minds.chimera.core.api_service.repository.NoSqlPersistMetadataConfigDBMapper;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.CheckForNull;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class persistMetadataConfigService {

    @Autowired
    private PersistMetadataConfigDBMapper persistMetadataConfigDBMapper;
    private final dataSourcesService dataSourcesService;
    private final DataSourceConnectionsService dataSourcesConnService;
    private final RelationalPersistMetadataTableDBMapper relationalPersistDBMapper;
    private final RelationalPersistMetadataConfigDBMapper relationalPersistConfigDBMapper;
    private final StreamsPersistMetadataTableDBMapper streamPersistDBMapper;
    private final StreamsPersistMetadataConfigDBMapper streamsPersistConfigDBMapper;
    private final FilePersistMetadataTableDBMapper filePersistDBMapper;
    private final FilePersistMetadataConfigDBMapper filePersistConfigDBMapper;
    private final NoSqlPersistMetadataTableDBMapper noSqlPersistDBMapper;
    private final NoSqlPersistMetadataConfigDBMapper noSqlPersistConfigDBMapper;
  //  private final DBMapper<RelationalPersistMetadataTable> relationalPersistDBMapper;
  //  private final DBMapper<StreamPersistMetadataTable> streamPersistDBMapper;
  //  private final DBMapper<FilePersistMetadataTable> filePersistDBMapper;
   // private final DBMapper<NoSqlPersistMetadataTable> noSqlPersistDBMapper;

    @Autowired
    public persistMetadataConfigService(dataSourcesService dataSourcesService,
                                        DataSourceConnectionsService dataSourcesConnService,
                                        RelationalPersistMetadataTableDBMapper relationalPersistDBMapper,
                                        FilePersistMetadataTableDBMapper filePersistDBMapper,
                                        NoSqlPersistMetadataTableDBMapper noSqlPersistDBMapper,
                                        StreamsPersistMetadataTableDBMapper streamPersistDBMapper,
                                        RelationalPersistMetadataConfigDBMapper relationalPersistConfigDBMapper,
                                        FilePersistMetadataConfigDBMapper filePersistConfigDBMapper,
                                        StreamsPersistMetadataConfigDBMapper streamsPersistConfigDBMapper,
                                        NoSqlPersistMetadataConfigDBMapper noSqlPersistConfigDBMapper) {
     //                                   DBMapper<FilePersistMetadataTable> filePersistDBMapper,
     //                                   DBMapper<NoSqlPersistMetadataTable> noSqlPersistDBMapper,
     //                                   DBMapper<StreamPersistMetadataTable> streamPersistDBMapper) {
        this.dataSourcesService = dataSourcesService;
        this.dataSourcesConnService = dataSourcesConnService;
        this.relationalPersistDBMapper = relationalPersistDBMapper;
        this.streamPersistDBMapper = streamPersistDBMapper;
        this.filePersistDBMapper = filePersistDBMapper;
        this.noSqlPersistDBMapper = noSqlPersistDBMapper;
        this.relationalPersistConfigDBMapper = relationalPersistConfigDBMapper;
        this.filePersistConfigDBMapper = filePersistConfigDBMapper;
        this.streamsPersistConfigDBMapper = streamsPersistConfigDBMapper;
        this.noSqlPersistConfigDBMapper = noSqlPersistConfigDBMapper;
    }

    public long getTotalNumberOfDataSinks() {
      SelectStatementProvider countStatementProvider =
          SqlBuilder.select(SqlBuilder.count())
              .from(persistMetadataConfig)
              .build()
              .render(RenderingStrategies.MYBATIS3);

      // Execute the count query
      return persistMetadataConfigDBMapper.count(countStatementProvider);
    }

    @CheckForNull
    public List<PersistMetadata> getAllPersistMetadataConfig() {
      SelectStatementProvider selectStatement = buildSelectStatement(null, null);
      List<PersistMetadataConfig> pc =  fetchPersistMetadataConfigs(selectStatement);
      return mapPersistMetadataConfigs(pc);
    }

    @CheckForNull
    public List<PersistMetadata> getPersistMetadata(String name) {
      SelectStatementProvider selectStatement = buildSelectStatement(name, null);
      List<PersistMetadataConfig> pc =  fetchPersistMetadataConfigs(selectStatement);
      return mapPersistMetadataConfigs(pc);
    }

    @CheckForNull
    public PersistMetadata getPersistMetadataByPipelineName(String name, int sequence) {
        SelectStatementProvider selectStatement = buildSelectStatement(name, sequence);
        PersistMetadataConfig config =  persistMetadataConfigDBMapper.selectOne(selectStatement).orElse(null);
        return populatePersistMetadata(config);
    }

    public int insertConfig(PersistMetadata data) {
      return switch (data.getSinkType()) {
        case "Relational" -> {
          RelationalPersistMetadataConfig config = new RelationalPersistMetadataConfig();
          mapCommonFields(data, config);
          config.setDatabaseName(data.getRelationalPersistMetadataTable().getDatabaseName());
          config.setSchemaName(data.getRelationalPersistMetadataTable().getSchemaName());
          config.setTableName(data.getRelationalPersistMetadataTable().getTableName());
          InsertStatementProvider<RelationalPersistMetadataConfig> insertStatement = SqlBuilder.insert(config)
              .into(relationalPersistConfig)
              .map(relationalPersistConfig.pipelineName).toProperty("pipelineName")
              .map(relationalPersistConfig.sequenceNumber).toProperty("sequenceNumber")
              .map(relationalPersistConfig.sinkType).toProperty("sinkType")
              .map(relationalPersistConfig.sinkSubType).toProperty("sinkSubType")
              .map(relationalPersistConfig.predecessorSequences).toProperty("predecessorSequences")
              .map(relationalPersistConfig.successorSequences).toProperty("successorSequences")
              .map(relationalPersistConfig.sinkConfiguration).toProperty("sinkConfiguration")
              .map(relationalPersistConfig.dataSourceConnectionName).toProperty("dataSourceConnectionName")
              .map(relationalPersistConfig.partitionKeys).toProperty("partitionKeys")
              .map(relationalPersistConfig.targetSQL).toProperty("targetSql")
              .map(relationalPersistConfig.sortColumns).toProperty("sortColumns")
              .map(relationalPersistConfig.dedupColumns).toProperty("dedupColumns")
              .map(relationalPersistConfig.createdTimestamp).toConstant("'" + new Timestamp(System.currentTimeMillis()).toString() + "'")
              .map(relationalPersistConfig.createdBy).toProperty("createdBy")
              .map(relationalPersistConfig.activeFlag).toProperty("activeFlag")
              .map(relationalPersistConfig.databaseName).toProperty("databaseName")
              .map(relationalPersistConfig.schemaName).toProperty("schemaName")
              .map(relationalPersistConfig.tableName).toProperty("tableName")
              .build()
              .render(RenderingStrategies.MYBATIS3);
          yield relationalPersistConfigDBMapper.insert(insertStatement);
        } 
        case "Files" -> {
          FilePersistMetadataConfig config = new FilePersistMetadataConfig();
          mapCommonFields(data, config);
          config.setFileName(data.getFilePersistMetadataTable().getFileName());
          config.setFilePath(data.getFilePersistMetadataTable().getFilePath());
          config.setWriteMode(data.getFilePersistMetadataTable().getWriteMode());
          
          InsertStatementProvider<FilePersistMetadataConfig> insertStatement = SqlBuilder.insert(config)
              .into(filePersistConfig)
              .map(filePersistConfig.pipelineName).toProperty("pipelineName")
              .map(filePersistConfig.sequenceNumber).toProperty("sequenceNumber")
              .map(filePersistConfig.sinkType).toProperty("sinkType")
              .map(filePersistConfig.sinkSubType).toProperty("sinkSubType")
              .map(filePersistConfig.predecessorSequences).toProperty("predecessorSequences")
              .map(filePersistConfig.successorSequences).toProperty("successorSequences")
              .map(filePersistConfig.sinkConfiguration).toProperty("sinkConfiguration")
              .map(filePersistConfig.dataSourceConnectionName).toProperty("dataSourceConnectionName")
              .map(filePersistConfig.partitionKeys).toProperty("partitionKeys")
              .map(filePersistConfig.targetSQL).toProperty("targetSql")
              .map(filePersistConfig.sortColumns).toProperty("sortColumns")
              .map(filePersistConfig.dedupColumns).toProperty("dedupColumns")
              .map(filePersistConfig.createdTimestamp).toConstant("'" + new Timestamp(System.currentTimeMillis()).toString() + "'")
              .map(filePersistConfig.createdBy).toProperty("createdBy")
              .map(filePersistConfig.activeFlag).toProperty("activeFlag")
              .map(filePersistConfig.fileName).toProperty("fileName")
              .map(filePersistConfig.filePath).toProperty("filePath")
              .map(filePersistConfig.writeMode).toProperty("writeMode")
              .build()
              .render(RenderingStrategies.MYBATIS3);
          yield filePersistConfigDBMapper.insert(insertStatement);
        }
        case "Stream" -> {
          StreamPersistMetadataConfig config = new StreamPersistMetadataConfig();
          mapCommonFields(data, config);
          config.setKafkaTopic(data.getStreamPersistMetadataTable().getKafkaTopic());
          config.setKafkaKey(data.getStreamPersistMetadataTable().getKafkaKey());
          config.setKafkaMessage(data.getStreamPersistMetadataTable().getKafkaMessage());
          
          InsertStatementProvider<StreamPersistMetadataConfig> insertStatement = SqlBuilder.insert(config)
              .into(StreamsPersistConfig)
              .map(StreamsPersistConfig.pipelineName).toProperty("pipelineName")
              .map(StreamsPersistConfig.sequenceNumber).toProperty("sequenceNumber")
              .map(StreamsPersistConfig.sinkType).toProperty("sinkType")
              .map(StreamsPersistConfig.sinkSubType).toProperty("sinkSubType")
              .map(StreamsPersistConfig.predecessorSequences).toProperty("predecessorSequences")
              .map(StreamsPersistConfig.successorSequences).toProperty("successorSequences")
              .map(StreamsPersistConfig.sinkConfiguration).toProperty("sinkConfiguration")
              .map(StreamsPersistConfig.dataSourceConnectionName).toProperty("dataSourceConnectionName")
              .map(StreamsPersistConfig.partitionKeys).toProperty("partitionKeys")
              .map(StreamsPersistConfig.targetSQL).toProperty("targetSql")
              .map(StreamsPersistConfig.sortColumns).toProperty("sortColumns")
              .map(StreamsPersistConfig.dedupColumns).toProperty("dedupColumns")
              .map(StreamsPersistConfig.createdTimestamp).toConstant("'" + new Timestamp(System.currentTimeMillis()).toString() + "'")
              .map(StreamsPersistConfig.createdBy).toProperty("createdBy")
              .map(StreamsPersistConfig.activeFlag).toProperty("activeFlag")
              .map(StreamsPersistConfig.kafkaTopic).toProperty("kafkaTopic")
              .map(StreamsPersistConfig.kafkaKey).toProperty("kafkaKey")
              .map(StreamsPersistConfig.kafkaMessage).toProperty("kafkaMessage")
              .build()
              .render(RenderingStrategies.MYBATIS3);
          yield streamsPersistConfigDBMapper.insert(insertStatement);
        }
        case "NoSql" -> {
          NoSqlPersistMetadataConfig config = new NoSqlPersistMetadataConfig();
          mapCommonFields(data, config);
          config.setCollection(data.getNoSqlPersistMetadataTable().getCollection());
          config.setPartitioner(data.getNoSqlPersistMetadataTable().getPartitioner());
        
          InsertStatementProvider<NoSqlPersistMetadataConfig> insertStatement = SqlBuilder.insert(config)
              .into(noSqlPersistConfig)
              .map(noSqlPersistConfig.pipelineName).toProperty("pipelineName")
              .map(noSqlPersistConfig.sequenceNumber).toProperty("sequenceNumber")
              .map(noSqlPersistConfig.sinkType).toProperty("sinkType")
              .map(noSqlPersistConfig.sinkSubType).toProperty("sinkSubType")
              .map(noSqlPersistConfig.predecessorSequences).toProperty("predecessorSequences")
              .map(noSqlPersistConfig.successorSequences).toProperty("successorSequences")
              .map(noSqlPersistConfig.sinkConfiguration).toProperty("sinkConfiguration")
              .map(noSqlPersistConfig.dataSourceConnectionName).toProperty("dataSourceConnectionName")
              .map(noSqlPersistConfig.partitionKeys).toProperty("partitionKeys")
              .map(noSqlPersistConfig.targetSQL).toProperty("targetSql")
              .map(noSqlPersistConfig.sortColumns).toProperty("sortColumns")
              .map(noSqlPersistConfig.dedupColumns).toProperty("dedupColumns")
              .map(noSqlPersistConfig.createdTimestamp).toConstant("'" + new Timestamp(System.currentTimeMillis()).toString() + "'")
              .map(noSqlPersistConfig.createdBy).toProperty("createdBy")
              .map(noSqlPersistConfig.activeFlag).toProperty("activeFlag")
              .map(noSqlPersistConfig.collection).toProperty("collection")
              .map(noSqlPersistConfig.partitioner).toProperty("partitioner")
              .build()
              .render(RenderingStrategies.MYBATIS3);
          yield noSqlPersistConfigDBMapper.insert(insertStatement);
        }
        default -> 0;
      };
    }

    public int updateConfig(PersistMetadata data) {
      return switch (data.getSinkType()) {
        case "Relational" -> {
          // Build the update statement
          UpdateStatementProvider updateStatement = SqlBuilder.update(relationalPersistConfig)
            .set(relationalPersistConfig.sinkType).equalToWhenPresent(data.getSinkType())
            .set(relationalPersistConfig.sinkSubType).equalToWhenPresent(data.getSinkSubType())
            .set(relationalPersistConfig.dataSourceConnectionName).equalToWhenPresent(data.getDataSourceConnectionName())
            .set(relationalPersistConfig.partitionKeys).equalToWhenPresent(data.getPartitionKeys())
            .set(relationalPersistConfig.targetSQL).equalToWhenPresent(data.getTargetSql())
            .set(relationalPersistConfig.sinkConfiguration).equalToWhenPresent(data.getSinkConfiguration())
            .set(relationalPersistConfig.sortColumns).equalToWhenPresent(data.getSortColumns())
            .set(relationalPersistConfig.dedupColumns).equalToWhenPresent(data.getDedupColumns())
            .set(relationalPersistConfig.updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
            .set(relationalPersistConfig.updatedBy).equalToWhenPresent(data.getUpdatedBy())
            .set(relationalPersistConfig.activeFlag).equalToWhenPresent(data.getActiveFlag())
            .set(relationalPersistConfig.databaseName).equalToWhenPresent(data.getRelationalPersistMetadataTable().getDatabaseName())
            .set(relationalPersistConfig.schemaName).equalToWhenPresent(data.getRelationalPersistMetadataTable().getSchemaName())
            .set(relationalPersistConfig.tableName).equalToWhenPresent(data.getRelationalPersistMetadataTable().getTableName())
            .where(relationalPersistConfig.pipelineName, SqlBuilder.isEqualTo(data.getPipelineName()))
            .and(relationalPersistConfig.sequenceNumber, SqlBuilder.isEqualTo(data.getSequenceNumber()))
            .build()
            .render(RenderingStrategies.MYBATIS3);
          // Execute the update statement
          yield relationalPersistConfigDBMapper.update(updateStatement);
        }
        case "Files" -> {
          // Build the update statement
          UpdateStatementProvider updateStatementProvider = SqlBuilder.update(filePersistConfig)
            .set(filePersistConfig.sinkType).equalToWhenPresent(data.getSinkType())
            .set(filePersistConfig.sinkSubType).equalToWhenPresent(data.getSinkSubType())
            .set(filePersistConfig.dataSourceConnectionName).equalToWhenPresent(data.getDataSourceConnectionName())
            .set(filePersistConfig.partitionKeys).equalToWhenPresent(data.getPartitionKeys())
            .set(filePersistConfig.targetSQL).equalToWhenPresent(data.getTargetSql())
            .set(filePersistConfig.sinkConfiguration).equalToWhenPresent(data.getSinkConfiguration())
            .set(filePersistConfig.sortColumns).equalToWhenPresent(data.getSortColumns())
            .set(filePersistConfig.dedupColumns).equalToWhenPresent(data.getDedupColumns())
            .set(filePersistConfig.updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
            .set(filePersistConfig.updatedBy).equalToWhenPresent(data.getUpdatedBy())
            .set(filePersistConfig.activeFlag).equalToWhenPresent(data.getActiveFlag())
            .set(filePersistConfig.fileName).equalToWhenPresent(data.getFilePersistMetadataTable().getFileName())
            .set(filePersistConfig.filePath).equalToWhenPresent(data.getFilePersistMetadataTable().getFilePath())
            .set(filePersistConfig.writeMode).equalToWhenPresent(data.getFilePersistMetadataTable().getWriteMode())
            .where(filePersistConfig.pipelineName, SqlBuilder.isEqualTo(data.getPipelineName()))
            .and(filePersistConfig.sequenceNumber, SqlBuilder.isEqualTo(data.getSequenceNumber()))
            .build()
            .render(RenderingStrategies.MYBATIS3);
          // Execute the update statement
          yield filePersistConfigDBMapper.update(updateStatementProvider);
        }
        case "NoSql" -> {
          // Build the update statement
          UpdateStatementProvider updateStatementProvider = SqlBuilder.update(noSqlPersistConfig)
            .set(noSqlPersistConfig.sinkType).equalToWhenPresent(data.getSinkType())
            .set(noSqlPersistConfig.sinkSubType).equalToWhenPresent(data.getSinkSubType())
            .set(noSqlPersistConfig.dataSourceConnectionName).equalToWhenPresent(data.getDataSourceConnectionName())
            .set(noSqlPersistConfig.partitionKeys).equalToWhenPresent(data.getPartitionKeys())
            .set(noSqlPersistConfig.targetSQL).equalToWhenPresent(data.getTargetSql())
            .set(noSqlPersistConfig.sinkConfiguration).equalToWhenPresent(data.getSinkConfiguration())
            .set(noSqlPersistConfig.sortColumns).equalToWhenPresent(data.getSortColumns())
            .set(noSqlPersistConfig.dedupColumns).equalToWhenPresent(data.getDedupColumns())
            .set(noSqlPersistConfig.updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
            .set(noSqlPersistConfig.updatedBy).equalToWhenPresent(data.getUpdatedBy())
            .set(noSqlPersistConfig.activeFlag).equalToWhenPresent(data.getActiveFlag())
            .set(noSqlPersistConfig.collection).equalToWhenPresent(data.getNoSqlPersistMetadataTable().getCollection())
            .set(noSqlPersistConfig.partitioner).equalToWhenPresent(data.getNoSqlPersistMetadataTable().getPartitioner())
            .where(noSqlPersistConfig.pipelineName, SqlBuilder.isEqualTo(data.getPipelineName()))
            .and(noSqlPersistConfig.sequenceNumber, SqlBuilder.isEqualTo(data.getSequenceNumber()))
            .build()
            .render(RenderingStrategies.MYBATIS3);
          // Execute the update statement
          yield noSqlPersistConfigDBMapper.update(updateStatementProvider);
        }
        case "Stream" -> {
          // Build the update statement
          UpdateStatementProvider updateStatementProvider = SqlBuilder.update(StreamsPersistConfig)
            .set(StreamsPersistConfig.sinkType).equalToWhenPresent(data.getSinkType())
            .set(StreamsPersistConfig.sinkSubType).equalToWhenPresent(data.getSinkSubType())
            .set(StreamsPersistConfig.dataSourceConnectionName).equalToWhenPresent(data.getDataSourceConnectionName())
            .set(StreamsPersistConfig.partitionKeys).equalToWhenPresent(data.getPartitionKeys())
            .set(StreamsPersistConfig.targetSQL).equalToWhenPresent(data.getTargetSql())
            .set(StreamsPersistConfig.sinkConfiguration).equalToWhenPresent(data.getSinkConfiguration())
            .set(StreamsPersistConfig.sortColumns).equalToWhenPresent(data.getSortColumns())
            .set(StreamsPersistConfig.dedupColumns).equalToWhenPresent(data.getDedupColumns())
            .set(StreamsPersistConfig.updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
            .set(StreamsPersistConfig.updatedBy).equalToWhenPresent(data.getUpdatedBy())
            .set(StreamsPersistConfig.activeFlag).equalToWhenPresent(data.getActiveFlag())
            .set(StreamsPersistConfig.kafkaTopic).equalToWhenPresent(data.getStreamPersistMetadataTable().getKafkaTopic())
            .set(StreamsPersistConfig.kafkaKey).equalToWhenPresent(data.getStreamPersistMetadataTable().getKafkaKey())
            .set(StreamsPersistConfig.kafkaMessage).equalToWhenPresent(data.getStreamPersistMetadataTable().getKafkaMessage())
            .where(StreamsPersistConfig.pipelineName, SqlBuilder.isEqualTo(data.getPipelineName()))
            .and(StreamsPersistConfig.sequenceNumber, SqlBuilder.isEqualTo(data.getSequenceNumber()))
            .build()
            .render(RenderingStrategies.MYBATIS3);
          // Execute the update statement
          yield streamsPersistConfigDBMapper.update(updateStatementProvider);
        }
        default -> 0;
      };
    }

    public int deleteConfig (String pipelineName) {
      DeleteStatementProvider deleteStatementProvider =
          SqlBuilder.deleteFrom(persistMetadataConfig)
              .where(PersistMetadataConfigDynamicSqlEntity.pipelineName, isEqualTo(pipelineName))
              .build()
              .render(RenderingStrategies.MYBATIS3);

      // Execute the delete operation and return the number of rows affected
      return persistMetadataConfigDBMapper.delete(deleteStatementProvider);
    }

    public int deleteConfig (String pipelineName, int sequenceNumber) {
      DeleteStatementProvider deleteStatementProvider =
          SqlBuilder.deleteFrom(persistMetadataConfig)
              .where(PersistMetadataConfigDynamicSqlEntity.pipelineName, isEqualTo(pipelineName))
              .and(PersistMetadataConfigDynamicSqlEntity.sequenceNumber, isEqualTo(sequenceNumber))
              .build()
              .render(RenderingStrategies.MYBATIS3);

      // Execute the delete operation and return the number of rows affected
      return persistMetadataConfigDBMapper.delete(deleteStatementProvider);
    }

    private SelectStatementProvider buildSelectStatement(String name, Integer sequence) {
      QueryExpressionDSL<SelectModel> baseQuery = select(persistMetadataConfig.allColumns())
        .from(persistMetadataConfig);

      if (name != null) {
        QueryExpressionDSL<SelectModel>.QueryExpressionWhereBuilder whereBuilder = baseQuery.where(); 
        whereBuilder = whereBuilder.and(pipelineName, isEqualTo(name));
        if (sequence != null) {
          whereBuilder = whereBuilder.and(sequenceNumber, isEqualTo(sequence));
        }
        return whereBuilder.orderBy(pipelineName, sequenceNumber)
                      .build()
                      .render(RenderingStrategies.MYBATIS3);
      }

      return baseQuery.orderBy(pipelineName, sequenceNumber)
                      .build()
                      .render(RenderingStrategies.MYBATIS3);
    } 

    private List<PersistMetadata> mapPersistMetadataConfigs(List<PersistMetadataConfig> pc) {
      List<PersistMetadata> persistMetadata = new ArrayList<PersistMetadata>();
      pc.forEach(config -> {
        PersistMetadata metadata = populatePersistMetadata(config);
          persistMetadata.add(metadata);
      });
      return persistMetadata;
    }

    private <T extends PersistMetadataConfig> void mapCommonFields(PersistMetadata data, T config) {
      config.setPipelineName(data.getPipelineName());
      config.setSequenceNumber(data.getSequenceNumber());
      config.setSinkType(data.getSinkType());
      config.setSinkSubType(data.getSinkSubType());
      config.setPredecessorSequences(data.getPredecessorSequences());
      config.setSuccessorSequences(data.getSuccessorSequences());
      config.setSinkConfiguration(data.getSinkConfiguration());
      config.setDataSourceConnectionName(data.getDataSourceConnectionName());
      config.setPartitionKeys(data.getPartitionKeys());
      config.setTargetSql(data.getTargetSql());
      config.setSortColumns(data.getSortColumns());
      config.setDedupColumns(data.getDedupColumns());
      config.setCreatedBy(data.getCreatedBy());
      config.setUpdatedBy(data.getUpdatedBy());
      config.setActiveFlag(data.getActiveFlag());
    }

    private PersistMetadata populatePersistMetadata(PersistMetadataConfig config) {
      PersistMetadata metadata = new PersistMetadata();
        metadata.setPipelineName(config.getPipelineName());
        metadata.setSequenceNumber(config.getSequenceNumber());
        metadata.setSinkType(config.getSinkType());
        metadata.setSinkSubType(config.getSinkSubType());
        metadata.setDataSourceConnectionName(config.getDataSourceConnectionName());
        metadata.setPartitionKeys(config.getPartitionKeys());
        metadata.setTargetSql(config.getTargetSql());
        metadata.setSinkConfiguration(config.getSinkConfiguration());
        metadata.setSortColumns(config.getSortColumns());
        metadata.setDedupColumns(config.getDedupColumns());
        metadata.setCreatedBy(config.getCreatedBy());
        metadata.setCreatedTimestamp(config.getCreatedTimestamp());
        metadata.setUpdatedBy(config.getUpdatedBy());
        metadata.setUpdatedTimestamp(config.getUpdatedTimestamp());
        metadata.setActiveFlag(config.getActiveFlag());

        switch (config.getSinkType()) {
          case "Relational" -> {
            metadata.setRelationalPersistMetadataTable(getRelationalConfigByPipelineName(config.getPipelineName(), config.getSequenceNumber()));
          }
          case "Files" -> {
            metadata.setFilePersistMetadataTable(getFileConfigByPipelineName(config.getPipelineName(), config.getSequenceNumber()));
          }
          case "Stream" -> {
            metadata.setStreamPersistMetadataTable(getStreamConfigByPipelineName(config.getPipelineName(),config.getSequenceNumber()));
          }
          case "NoSql" -> {
            metadata.setNoSqlPersistMetadataTable(getNoSqlConfigByPipelineName(config.getPipelineName(), config.getSequenceNumber()));
          }
        }
        metadata.setDataSource(dataSourcesService.getDataSourceByTypeAndSubtype(config.getSinkType(), config.getSinkSubType()));
        metadata.setDataSourceConnection(dataSourcesConnService.getConnectionByName(config.getDataSourceConnectionName()).orElse(null));
      return metadata;
    }

    private List<PersistMetadataConfig> fetchPersistMetadataConfigs(SelectStatementProvider selectStatement) {
      return persistMetadataConfigDBMapper.selectMany(selectStatement);
    }


    private RelationalPersistMetadataTable getRelationalConfigByPipelineName(String pipelineName, int sequenceNumber) {
      //   logger.info("Fetching Relational Metadata Config for pipeline : " + pipelineName);
      SelectStatementProvider selectStatement = select(relationalPersistTable.allColumns())
          .from(relationalPersistTable)
          .where(relationalPersistTable.pipelineName, isEqualTo(pipelineName))
          .and(relationalPersistTable.sequenceNumber, isEqualTo(sequenceNumber))
          .build()
          .render(RenderingStrategies.MYBATIS3);

      RelationalPersistMetadataTable config = relationalPersistDBMapper.selectOne(selectStatement).orElse(null);
      System.out.println("Relational Config : " + config);
      return config;
      // return relationalPersistDBMapper.selectOne(selectStatement).orElse(null);
    }

    private StreamPersistMetadataTable getStreamConfigByPipelineName(String pipelineName, int sequenceNumber) {
      //  logger.info("Fnulletching Stream Metadata Config for pipeline : " + pipelineName);
      SelectStatementProvider selectStatement = select(streamsPersistTable.allColumns())
          .from(streamsPersistTable)
          .where(streamsPersistTable.pipelineName, isEqualTo(pipelineName))
          .and(streamsPersistTable.sequenceNumber, isEqualTo(sequenceNumber))
          .build()
          .render(RenderingStrategies.MYBATIS3);

      return streamPersistDBMapper.selectOne(selectStatement).orElse(null);
    }
    private FilePersistMetadataTable getFileConfigByPipelineName(String pipelineName, int sequenceNumber) {
      //  logger.info("Fetching File Metadata Config for pipeline : " + pipelineName);
      SelectStatementProvider selectStatement = select(filePersistTable.allColumns())
          .from(filePersistTable)
          .where(filePersistTable.pipelineName, isEqualTo(pipelineName))
          .and(filePersistTable.sequenceNumber, isEqualTo(sequenceNumber))
          .build()
          .render(RenderingStrategies.MYBATIS3);

      return filePersistDBMapper.selectOne(selectStatement).orElse(null);
    }

    private NoSqlPersistMetadataTable getNoSqlConfigByPipelineName(String pipelineName, int sequenceNumber) {
      // logger.info("Fetching NoSql Metadata Config for pipeline : " + pipelineName);
      SelectStatementProvider selectStatement = select(noSqlPersistTable.allColumns())
          .from(noSqlPersistTable)
          .where(noSqlPersistTable.pipelineName, isEqualTo(pipelineName))
          .and(noSqlPersistTable.sequenceNumber, isEqualTo(sequenceNumber))
          .build()
          .render(RenderingStrategies.MYBATIS3);

      return noSqlPersistDBMapper.selectOne(selectStatement).orElse(null);
    }
  }