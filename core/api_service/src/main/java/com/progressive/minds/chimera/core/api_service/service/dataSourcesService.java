package com.progressive.minds.chimera.core.api_service.service;

import static com.progressive.minds.chimera.core.api_service.entity.DataSourcesDynamicSqlEntity.dataSource;
import static com.progressive.minds.chimera.core.api_service.entity.DataSourcesDynamicSqlEntity.dataSourceType;
import static com.progressive.minds.chimera.core.api_service.entity.DataSourcesDynamicSqlEntity.dataSourceSubType;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

import com.progressive.minds.chimera.core.api_service.dto.DataSources;
import com.progressive.minds.chimera.core.api_service.entity.DataSourcesDynamicSqlEntity;
import com.progressive.minds.chimera.core.api_service.repository.DataSourcesDBMapper;

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
public class dataSourcesService {

    @Autowired
    private DataSourcesDBMapper dataSourcesDBMapper;

    public long getTotalNumberOfDataSources() {
      SelectStatementProvider countStatementProvider =
          SqlBuilder.select(SqlBuilder.count())
              .from(dataSource)
              .build()
              .render(RenderingStrategies.MYBATIS3);

      // Execute the count query
      return dataSourcesDBMapper.count(countStatementProvider);
    }

    @CheckForNull
    public DataSources getDataSourceByTypeAndSubtype(String type, String subType) {
        SelectStatementProvider selectStatement = select(dataSource.allColumns())
        .from(dataSource)
        .where(dataSourceType, isEqualTo(type))
        .and(dataSourceSubType, isEqualTo(subType))
        .build()
        .render(RenderingStrategies.MYBATIS3);
        return  dataSourcesDBMapper.selectOne(selectStatement).orElse(null);
    }

    public List<DataSources> getAllDataSources() {
        SelectStatementProvider selectStatement = select(dataSource.allColumns())
        .from(dataSource)
        .build()
        .render(RenderingStrategies.MYBATIS3);
        return dataSourcesDBMapper.selectMany(selectStatement);
    }

  public int insertDataSource(DataSources data) {
    InsertStatementProvider<DataSources> insertRow =
        SqlBuilder.insert(data)
            .into(dataSource)
            .map(DataSourcesDynamicSqlEntity.dataSourceType).toProperty("dataSourceType")
            .map(DataSourcesDynamicSqlEntity.dataSourceSubType).toProperty("dataSourceSubType")
            .map(DataSourcesDynamicSqlEntity.description).toProperty("description")
            .map(DataSourcesDynamicSqlEntity.dataSourceTemplate).toProperty("dataSourceTemplate")
            .map(DataSourcesDynamicSqlEntity.defaultReadConfig).toProperty("defaultReadConfig")
            .map(DataSourcesDynamicSqlEntity.defaultWriteConfig).toProperty("defaultWriteConfig")
            .map(DataSourcesDynamicSqlEntity.activeFlag).toProperty("activeFlag")
            .map(DataSourcesDynamicSqlEntity.createdTimestamp).toConstant("'" + new Timestamp(System.currentTimeMillis()).toString() + "'")
            .map(DataSourcesDynamicSqlEntity.createdBy).toProperty("createdBy")
            .build()
            .render(RenderingStrategies.MYBATIS3);

    return dataSourcesDBMapper.insert(insertRow);
  }


  public int updateDataSource(DataSources data) {
    // Build the update statement
    UpdateStatementProvider updateStatementProvider = SqlBuilder.update(DataSourcesDynamicSqlEntity.dataSource)
        .set(DataSourcesDynamicSqlEntity.description).equalToWhenPresent(data.getDescription())
        .set(DataSourcesDynamicSqlEntity.dataSourceTemplate).equalToWhenPresent(data.getDataSourceTemplate())
        .set(DataSourcesDynamicSqlEntity.defaultReadConfig).equalToWhenPresent(data.getDefaultReadConfig())
        .set(DataSourcesDynamicSqlEntity.defaultWriteConfig).equalToWhenPresent(data.getDefaultWriteConfig())
        .set(DataSourcesDynamicSqlEntity.activeFlag).equalToWhenPresent(data.getActiveFlag())
        .set(DataSourcesDynamicSqlEntity.updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
        .set(DataSourcesDynamicSqlEntity.updatedBy).equalToWhenPresent(data.getUpdatedBy())
        .where(DataSourcesDynamicSqlEntity.dataSourceType, SqlBuilder.isEqualTo(data.getDataSourceType()))
        .and(DataSourcesDynamicSqlEntity.dataSourceSubType, SqlBuilder.isEqualTo(data.getDataSourceSubType()))
        .build()
        .render(RenderingStrategies.MYBATIS3);
    // Execute the update statement
    return dataSourcesDBMapper.update(updateStatementProvider);
  }

  public int deleteDataSource (String sourceType, String sourceSubType) {
    DeleteStatementProvider deleteStatementProvider =
        SqlBuilder.deleteFrom(dataSource)
            .where(DataSourcesDynamicSqlEntity.dataSourceType, isEqualTo(sourceType))
            .and(DataSourcesDynamicSqlEntity.dataSourceSubType, isEqualTo(sourceSubType))
            .build()
            .render(RenderingStrategies.MYBATIS3);

    // Execute the delete operation and return the number of rows affected
    return dataSourcesDBMapper.delete(deleteStatementProvider);
  }

}

