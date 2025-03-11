package com.progressive.minds.chimera.core.api_service.service;

import static com.progressive.minds.chimera.core.api_service.entity.TransformMetadataConfigDynamicSqlEntity.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

import com.progressive.minds.chimera.core.api_service.dto.TransformMetadataConfig;
import com.progressive.minds.chimera.core.api_service.repository.TransformMetadataConfigDBMapper;

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
public class TransformMetadataConfigService {

    @Autowired
    private TransformMetadataConfigDBMapper transformMetadataConfigDBMapper;

    public long getTotalNumberOfTransformMetadataConfig() {
      SelectStatementProvider countStatementProvider =
          SqlBuilder.select(SqlBuilder.count())
              .from(transformMetadataConfig)
              .build()
              .render(RenderingStrategies.MYBATIS3);

      // Execute the count query
      return transformMetadataConfigDBMapper.count(countStatementProvider);
    }

    @CheckForNull
    public List<TransformMetadataConfig> getTransformMetadataByPipelineName(String name) {
        SelectStatementProvider selectStatement = select(transformMetadataConfig.allColumns())
        .from(transformMetadataConfig)
        .where(pipelineName, isEqualTo(name))
        .build()
        .render(RenderingStrategies.MYBATIS3);
        return  transformMetadataConfigDBMapper.selectMany(selectStatement);
    }
    @CheckForNull
    public TransformMetadataConfig getTransformMetadataByPipelineName(String name, int sequence) {
        SelectStatementProvider selectStatement = select(transformMetadataConfig.allColumns())
        .from(transformMetadataConfig)
        .where(pipelineName, isEqualTo(name))
        .and(sequenceNumber, isEqualTo(sequence))
        .build()
        .render(RenderingStrategies.MYBATIS3);
        return  transformMetadataConfigDBMapper.selectOne(selectStatement).orElse(null);
    }

    public List<TransformMetadataConfig> getAllTransformMetadataConfig() {
        SelectStatementProvider selectStatement = select(transformMetadataConfig.allColumns())
        .from(transformMetadataConfig)
        .build()
        .render(RenderingStrategies.MYBATIS3);
        return transformMetadataConfigDBMapper.selectMany(selectStatement);
    }

  public int insertConfig(TransformMetadataConfig data) {
    InsertStatementProvider<TransformMetadataConfig> insertRow =
        SqlBuilder.insert(data)
            .into(transformMetadataConfig)
            .map(pipelineName).toProperty("pipelineName")
            .map(sequenceNumber).toProperty("sequenceNumber")
            .map(sqlText).toProperty("sqlText")
            .map(transformDataframeName).toProperty("transformDataframeName")
            .map(createdTimestamp).toConstant("'" + new Timestamp(System.currentTimeMillis()).toString() + "'")
            .map(createdBy).toProperty("createdBy")
            .map(activeFlag).toProperty("activeFlag")
            .build()
            .render(RenderingStrategies.MYBATIS3);
      return transformMetadataConfigDBMapper.insert(insertRow);
  }

  public int updateConfig(TransformMetadataConfig data) {
    // Build the update statement
    UpdateStatementProvider updateStatementProvider = SqlBuilder.update(transformMetadataConfig)
        .set(sqlText).equalToWhenPresent(data.getSqlText())
        .set(transformDataframeName).equalToWhenPresent(data.getTransformDataframeName())
        .set(updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
        .set(updatedBy).equalToWhenPresent(data.getUpdatedBy())
        .set(activeFlag).equalToWhenPresent(data.getActiveFlag())
        .where(pipelineName, SqlBuilder.isEqualTo(data.getPipelineName()))
        .and(sequenceNumber, SqlBuilder.isEqualTo(data.getSequenceNumber()))
        .build()
        .render(RenderingStrategies.MYBATIS3);
    // Execute the update statement
    return transformMetadataConfigDBMapper.update(updateStatementProvider);
  }
       

  public int deleteConfig (String name) {
    DeleteStatementProvider deleteStatementProvider =
        SqlBuilder.deleteFrom(transformMetadataConfig)
            .where(pipelineName, isEqualTo(name))
            .build()
            .render(RenderingStrategies.MYBATIS3);

    // Execute the delete operation and return the number of rows affected
    return transformMetadataConfigDBMapper.delete(deleteStatementProvider);
  }

  public int deleteConfig (String name, int sequence) {
    DeleteStatementProvider deleteStatementProvider =
        SqlBuilder.deleteFrom(transformMetadataConfig)
            .where(pipelineName, isEqualTo(name))
            .and(sequenceNumber, isEqualTo(sequence))
            .build()
            .render(RenderingStrategies.MYBATIS3);

    // Execute the delete operation and return the number of rows affected
    return transformMetadataConfigDBMapper.delete(deleteStatementProvider);
  }

}

