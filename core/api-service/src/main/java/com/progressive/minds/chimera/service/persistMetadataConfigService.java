package com.progressive.minds.chimera.service;

import static com.progressive.minds.chimera.entity.PersistMetadataConfigDynamicSqlEntity.persistMetadataConfig;
import static com.progressive.minds.chimera.entity.PersistMetadataConfigDynamicSqlEntity.pipelineName;
import static com.progressive.minds.chimera.entity.PersistMetadataConfigDynamicSqlEntity.sequenceNumber;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

import com.progressive.minds.chimera.dto.PersistMetadataConfig;
import com.progressive.minds.chimera.entity.PersistMetadataConfigDynamicSqlEntity;
import com.progressive.minds.chimera.repository.PersistMetadataConfigDBMapper;

import java.sql.Timestamp;
import java.util.List;
import javax.annotation.CheckForNull;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class persistMetadataConfigService {

    @Autowired
    private PersistMetadataConfigDBMapper persistMetadataConfigDBMapper;

    public long getTotalNumberOfDataSources() {
      SelectStatementProvider countStatementProvider =
          SqlBuilder.select(SqlBuilder.count())
              .from(persistMetadataConfig)
              .build()
              .render(RenderingStrategies.MYBATIS3);

      // Execute the count query
      return persistMetadataConfigDBMapper.count(countStatementProvider);
    }

    @CheckForNull
    public List<PersistMetadataConfig> getPersistMetadataByPipelineName(String name) {
        SelectStatementProvider selectStatement = select(persistMetadataConfig.allColumns())
        .from(persistMetadataConfig)
        .where(pipelineName, isEqualTo(name))
        .build()
        .render(RenderingStrategies.MYBATIS3);
        return  persistMetadataConfigDBMapper.selectMany(selectStatement);
    }
    @CheckForNull
    public PersistMetadataConfig getPersistMetadataByPipelineName(String name, int sequence) {
        SelectStatementProvider selectStatement = select(persistMetadataConfig.allColumns())
        .from(persistMetadataConfig)
        .where(pipelineName, isEqualTo(name))
        .and(sequenceNumber, isEqualTo(sequence))
        .build()
        .render(RenderingStrategies.MYBATIS3);
        return  persistMetadataConfigDBMapper.selectOne(selectStatement).orElse(null);
    }

    public List<PersistMetadataConfig> getAllPersistMetadataConfig() {
        SelectStatementProvider selectStatement = select(persistMetadataConfig.allColumns())
        .from(persistMetadataConfig)
        .build()
        .render(RenderingStrategies.MYBATIS3);
        return persistMetadataConfigDBMapper.selectMany(selectStatement);
    }

  public int insertConfig(PersistMetadataConfig data) {
    InsertStatementProvider<PersistMetadataConfig> insertRow =
        SqlBuilder.insert(data)
            .into(persistMetadataConfig)
            .map(PersistMetadataConfigDynamicSqlEntity.pipelineName).toProperty("pipelineName")
            .map(PersistMetadataConfigDynamicSqlEntity.sequenceNumber).toProperty("sequenceNumber")
            .map(PersistMetadataConfigDynamicSqlEntity.sinkType).toProperty("sinkType")
            .map(PersistMetadataConfigDynamicSqlEntity.sinkSubType).toProperty("sinkSubType")
            .map(PersistMetadataConfigDynamicSqlEntity.dataSourceConnectionName).toProperty("dataSourceConnectionName")
            .map(PersistMetadataConfigDynamicSqlEntity.databaseName).toProperty("databaseName")
            .map(PersistMetadataConfigDynamicSqlEntity.tableName).toProperty("tableName")
            .map(PersistMetadataConfigDynamicSqlEntity.schemaName).toProperty("schemaName")
            .map(PersistMetadataConfigDynamicSqlEntity.partitionKeys).toProperty("partitionKeys")
            .map(PersistMetadataConfigDynamicSqlEntity.targetSql).toProperty("targetSql")
            .map(PersistMetadataConfigDynamicSqlEntity.targetPath).toProperty("targetPath")
            .map(PersistMetadataConfigDynamicSqlEntity.writeMode).toProperty("writeMode")
            .map(PersistMetadataConfigDynamicSqlEntity.sinkConfiguration).toProperty("sinkConfiguration")
            .map(PersistMetadataConfigDynamicSqlEntity.sortColumns).toProperty("sortColumns")
            .map(PersistMetadataConfigDynamicSqlEntity.dedupColumns).toProperty("dedupColumns")
            .map(PersistMetadataConfigDynamicSqlEntity.kafkaTopic).toProperty("kafkaTopic")
            .map(PersistMetadataConfigDynamicSqlEntity.kafkaKey).toProperty("kafkaKey")
            .map(PersistMetadataConfigDynamicSqlEntity.kafkaMessage).toProperty("kafkaMessage")
            .map(PersistMetadataConfigDynamicSqlEntity.createdTimestamp).toConstant("'" + new Timestamp(System.currentTimeMillis()).toString() + "'")
            .map(PersistMetadataConfigDynamicSqlEntity.createdBy).toProperty("createdBy")
            .map(PersistMetadataConfigDynamicSqlEntity.activeFlag).toProperty("activeFlag")
            .build()
            .render(RenderingStrategies.MYBATIS3);

    return persistMetadataConfigDBMapper.insert(insertRow);
  }


  public int updateConfig(PersistMetadataConfig data) {
    // Build the update statement
    UpdateStatementProvider updateStatementProvider = SqlBuilder.update(PersistMetadataConfigDynamicSqlEntity.persistMetadataConfig)
        .set(PersistMetadataConfigDynamicSqlEntity.sinkType).equalToWhenPresent(data.getSinkType())
        .set(PersistMetadataConfigDynamicSqlEntity.sinkSubType).equalToWhenPresent(data.getSinkSubType())
        .set(PersistMetadataConfigDynamicSqlEntity.dataSourceConnectionName).equalToWhenPresent(data.getDataSourceConnectionName())
        .set(PersistMetadataConfigDynamicSqlEntity.databaseName).equalToWhenPresent(data.getDatabaseName())
        .set(PersistMetadataConfigDynamicSqlEntity.tableName).equalToWhenPresent(data.getTableName())
        .set(PersistMetadataConfigDynamicSqlEntity.schemaName).equalToWhenPresent(data.getSchemaName())
        .set(PersistMetadataConfigDynamicSqlEntity.partitionKeys).equalToWhenPresent(data.getPartitionKeys())
        .set(PersistMetadataConfigDynamicSqlEntity.targetSql).equalToWhenPresent(data.getTargetSql())
        .set(PersistMetadataConfigDynamicSqlEntity.targetPath).equalToWhenPresent(data.getTargetPath())
        .set(PersistMetadataConfigDynamicSqlEntity.writeMode).equalToWhenPresent(data.getWriteMode())
        .set(PersistMetadataConfigDynamicSqlEntity.sinkConfiguration).equalToWhenPresent(data.getSinkConfiguration())
        .set(PersistMetadataConfigDynamicSqlEntity.sortColumns).equalToWhenPresent(data.getSortColumns())
        .set(PersistMetadataConfigDynamicSqlEntity.dedupColumns).equalToWhenPresent(data.getDedupColumns())
        .set(PersistMetadataConfigDynamicSqlEntity.kafkaTopic).equalToWhenPresent(data.getKafkaTopic())
        .set(PersistMetadataConfigDynamicSqlEntity.kafkaKey).equalToWhenPresent(data.getKafkaKey())
        .set(PersistMetadataConfigDynamicSqlEntity.kafkaMessage).equalToWhenPresent(data.getKafkaMessage())
        .set(PersistMetadataConfigDynamicSqlEntity.updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
        .set(PersistMetadataConfigDynamicSqlEntity.updatedBy).equalToWhenPresent(data.getUpdatedBy())
        .set(PersistMetadataConfigDynamicSqlEntity.activeFlag).equalToWhenPresent(data.getActiveFlag())
        .where(PersistMetadataConfigDynamicSqlEntity.pipelineName, SqlBuilder.isEqualTo(data.getPipelineName()))
        .and(PersistMetadataConfigDynamicSqlEntity.sequenceNumber, SqlBuilder.isEqualTo(data.getSequenceNumber()))
        .build()
        .render(RenderingStrategies.MYBATIS3);
    // Execute the update statement
    return persistMetadataConfigDBMapper.update(updateStatementProvider);
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

}

