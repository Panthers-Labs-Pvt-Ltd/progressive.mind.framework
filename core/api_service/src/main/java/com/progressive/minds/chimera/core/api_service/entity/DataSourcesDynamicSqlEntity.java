package com.progressive.minds.chimera.core.api_service.entity;

import java.sql.JDBCType;
import java.sql.Timestamp;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DataSourcesDynamicSqlEntity {

  /*  Data Sources Table */
  public static DataSourceEntity dataSource = new DataSourceEntity();

  public static final SqlColumn<String> dataSourceType = dataSource.dataSourceType;
  public static final SqlColumn<String> dataSourceSubType = dataSource.dataSourceSubType;
  public static final SqlColumn<String> description = dataSource.description;
  public static final SqlColumn<String> dataSourceTemplate = dataSource.dataSourceTemplate;
  public static final SqlColumn<String> defaultReadConfig = dataSource.defaultReadConfig;
  public static final SqlColumn<String> defaultWriteConfig = dataSource.defaultWriteConfig;
  public static final SqlColumn<Timestamp> createdTimestamp = dataSource.createdTimestamp;
  public static final SqlColumn<String> createdBy = dataSource.createdBy;
  public static final SqlColumn<Timestamp> updatedTimestamp = dataSource.updatedTimestamp;
  public static final SqlColumn<String> updatedBy = dataSource.updatedBy;
  public static final SqlColumn<String> activeFlag = dataSource.activeFlag;

  public static final class DataSourceEntity extends SqlTable {

    /*  Data Sources Table */
    public final SqlColumn<String> dataSourceType = column("DATA_SOURCE_TYPE", JDBCType.VARCHAR);
    public final SqlColumn<String> dataSourceSubType = column("DATA_SOURCE_SUB_TYPE", JDBCType.VARCHAR);
    public final SqlColumn<String> description = column("DESCRIPTION", JDBCType.LONGNVARCHAR);
    public final SqlColumn<String> dataSourceTemplate = column("DATA_SOURCE_TEMPLATE", JDBCType.VARCHAR);
    public final SqlColumn<String> defaultReadConfig = column("DEFAULT_READ_CONFIG", JDBCType.VARCHAR);
    public final SqlColumn<String> defaultWriteConfig = column("DEFAULT_WRITE_CONFIG", JDBCType.VARCHAR);
    public final SqlColumn<Timestamp> createdTimestamp = column("CREATED_TIMESTAMP", JDBCType.TIMESTAMP);
    public final SqlColumn<String> createdBy = column("CREATED_BY", JDBCType.VARCHAR);
    public final SqlColumn<Timestamp> updatedTimestamp = column("UPDATED_TIMESTAMP", JDBCType.TIMESTAMP);
    public final SqlColumn<String> updatedBy = column("UPDATED_BY", JDBCType.VARCHAR);
    public final SqlColumn<String> activeFlag = column("ACTIVE_FLAG", JDBCType.VARCHAR);

    public DataSourceEntity() {
      super("DATA_SOURCES");
    }
  }
}
