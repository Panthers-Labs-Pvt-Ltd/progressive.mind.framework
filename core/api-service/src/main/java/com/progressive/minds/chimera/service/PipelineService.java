package com.progressive.minds.chimera.service;


import static com.progressive.minds.chimera.entity.DataPipelineDynamicSqlEntity.dataPipeline;
import static com.progressive.minds.chimera.entity.DataPipelineDynamicSqlEntity.pipelineName;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

import com.progressive.minds.chimera.dto.DataPipeline;
import com.progressive.minds.chimera.entity.DataPipelineDynamicSqlEntity;
import com.progressive.minds.chimera.repository.DataPipeLineDBMapper;
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
public class PipelineService {

  @Autowired
  private DataPipeLineDBMapper dataPipeLineDBMapper;

  public long getTotalNumberOfPipeline() {
      SelectStatementProvider countStatementProvider =
          SqlBuilder.select(SqlBuilder.count())
              .from(dataPipeline)
              .build()
              .render(RenderingStrategies.MYBATIS3);

      // Execute the count query
      return dataPipeLineDBMapper.count(countStatementProvider);
  }

  @CheckForNull
  public DataPipeline getDataPipeLineByName(String name) {
    SelectStatementProvider selectStatement = select(dataPipeline.allColumns())
        .from(dataPipeline)
        .where(pipelineName, isEqualTo(name))
        .build()
        .render(RenderingStrategies.MYBATIS3);
    return  dataPipeLineDBMapper.selectOne(selectStatement).orElse(null);
  }

  public List<DataPipeline> getAllPipelines() {
    SelectStatementProvider selectStatement = select(dataPipeline.allColumns()).from(dataPipeline)
        .build()
        .render(RenderingStrategies.MYBATIS3);
    return dataPipeLineDBMapper.selectMany(selectStatement);
  }

  // TODO: Vivek - Why are we trying to map properties again. Should it not be mapped in the Entity already?
  public int insertPipeline(DataPipeline pipeline) {
    InsertStatementProvider<DataPipeline> insertRow =
        SqlBuilder.insert(pipeline)
            .into(dataPipeline)
            .map(DataPipelineDynamicSqlEntity.pipelineName).toProperty("pipelineName")
            .map(DataPipelineDynamicSqlEntity.pipelineDescription).toProperty("pipelineDescription")
            .map(DataPipelineDynamicSqlEntity.processMode).toProperty("processMode")
            .map(DataPipelineDynamicSqlEntity.tags).toProperty("tags")
            .map(DataPipelineDynamicSqlEntity.orgHierName).toProperty("orgHierName")
            .map(DataPipelineDynamicSqlEntity.activeFlag).toProperty("activeFlag")
            .map(DataPipelineDynamicSqlEntity.createdTimestamp).toConstant("'" + new Timestamp(System.currentTimeMillis()) + "'")
            .map(DataPipelineDynamicSqlEntity.createdBy).toProperty("createdBy")
            .build()
            .render(RenderingStrategies.MYBATIS3);

    return dataPipeLineDBMapper.insert(insertRow);
  }


  public int updatePipeline(DataPipeline pipeline) {
    // Build the update statement
    UpdateStatementProvider updateStatementProvider = SqlBuilder.update(DataPipelineDynamicSqlEntity.dataPipeline)
        .set(DataPipelineDynamicSqlEntity.pipelineDescription).equalToWhenPresent(pipeline.getPipelineDescription())
        .set(DataPipelineDynamicSqlEntity.processMode).equalToWhenPresent(pipeline.getProcessMode())
        .set(DataPipelineDynamicSqlEntity.tags).equalToWhenPresent(pipeline.getTags())
        .set(DataPipelineDynamicSqlEntity.orgHierName).equalToWhenPresent(pipeline.getOrgHierName())
        .set(DataPipelineDynamicSqlEntity.activeFlag).equalToWhenPresent(pipeline.getActiveFlag())
        .set(DataPipelineDynamicSqlEntity.updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
        .set(DataPipelineDynamicSqlEntity.updatedBy).equalToWhenPresent(pipeline.getUpdatedBy())
        .where(DataPipelineDynamicSqlEntity.pipelineName, SqlBuilder.isEqualTo(pipeline.getPipelineName()))
        .build()
        .render(RenderingStrategies.MYBATIS3);

    // Execute the update statement
    return dataPipeLineDBMapper.update(updateStatementProvider);
  }

  public int deletePipeline(String pipelineName) {
    DeleteStatementProvider deleteStatementProvider =
        SqlBuilder.deleteFrom(dataPipeline)
            .where(DataPipelineDynamicSqlEntity.pipelineName, isEqualTo(pipelineName))
            .build()
            .render(RenderingStrategies.MYBATIS3);

    // Execute the delete operation and return the number of rows affected
    return dataPipeLineDBMapper.delete(deleteStatementProvider);
  }
}
