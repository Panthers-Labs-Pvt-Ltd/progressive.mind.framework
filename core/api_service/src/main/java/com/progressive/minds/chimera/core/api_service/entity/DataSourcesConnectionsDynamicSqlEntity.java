package com.progressive.minds.chimera.core.api_service.entity;

import java.sql.JDBCType;
import java.sql.Timestamp;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DataSourcesConnectionsDynamicSqlEntity {

  /*  Data Sources Table */
  public static DataSourceConnectionsEntity dataSourceConnection = new DataSourceConnectionsEntity();

  public static final SqlColumn<String> dataSourceConnectionName = dataSourceConnection.dataSourceConnectionName;
  public static final SqlColumn<String> dataSourceType = dataSourceConnection.dataSourceType;
  public static final SqlColumn<String> dataSourceSubType = dataSourceConnection.dataSourceSubType;
  public static final SqlColumn<String> authenticationType = dataSourceConnection.authenticationType;
  public static final SqlColumn<String> authenticationData = dataSourceConnection.authenticationData;
  public static final SqlColumn<String> connectionMetadata = dataSourceConnection.connectionMetadata;
  public static final SqlColumn<String> userReadConfig = dataSourceConnection.userReadConfig;
  public static final SqlColumn<String> userWriteConfig = dataSourceConnection.userWriteConfig;
  public static final SqlColumn<String> description = dataSourceConnection.description;
  public static final SqlColumn<Timestamp> createdTimestamp = dataSourceConnection.createdTimestamp;
  public static final SqlColumn<String> createdBy = dataSourceConnection.createdBy;
  public static final SqlColumn<Timestamp> updatedTimestamp = dataSourceConnection.updatedTimestamp;
  public static final SqlColumn<String> updatedBy = dataSourceConnection.updatedBy;
  public static final SqlColumn<String> activeFlag = dataSourceConnection.activeFlag;

  public static final class DataSourceConnectionsEntity extends SqlTable {

    /*  Data Sources Table */
    public final SqlColumn<String> dataSourceConnectionName = column("DATA_SOURCE_CONNECTION_NAME", JDBCType.VARCHAR);
    public final SqlColumn<String> dataSourceType = column("DATA_SOURCE_TYPE", JDBCType.VARCHAR);
    public final SqlColumn<String> dataSourceSubType = column("DATA_SOURCE_SUB_TYPE", JDBCType.VARCHAR);
    public final SqlColumn<String> authenticationType = column("AUTHENTICATION_TYPE", JDBCType.LONGNVARCHAR);
    public final SqlColumn<String> authenticationData = column("AUTHENTICATION_DATA", JDBCType.VARCHAR);
    public final SqlColumn<String> connectionMetadata = column("CONNECTION_METADATA", JDBCType.VARCHAR);
    public final SqlColumn<String> userReadConfig = column("USER_READ_CONFIG", JDBCType.VARCHAR);
    public final SqlColumn<String> userWriteConfig = column("USER_WRITE_CONFIG", JDBCType.VARCHAR);
    public final SqlColumn<String> description = column("DESCRIPTION", JDBCType.VARCHAR);
    public final SqlColumn<Timestamp> createdTimestamp = column("CREATED_TIMESTAMP", JDBCType.TIMESTAMP);
    public final SqlColumn<String> createdBy = column("CREATED_BY", JDBCType.VARCHAR);
    public final SqlColumn<Timestamp> updatedTimestamp = column("UPDATED_TIMESTAMP", JDBCType.TIMESTAMP);
    public final SqlColumn<String> updatedBy = column("UPDATED_BY", JDBCType.VARCHAR);
    public final SqlColumn<String> activeFlag = column("ACTIVE_FLAG", JDBCType.VARCHAR);

    public DataSourceConnectionsEntity() {
      super("DATA_SOURCES_CONNECTIONS");
    }
  }
}
