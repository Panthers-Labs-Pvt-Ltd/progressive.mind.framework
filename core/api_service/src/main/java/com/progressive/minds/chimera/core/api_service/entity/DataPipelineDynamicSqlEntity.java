package com.progressive.minds.chimera.core.api_service.entity;

import java.sql.JDBCType;
import java.sql.Timestamp;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DataPipelineDynamicSqlEntity {

  /*  Data pipeline Table */
  public static DataPipelineEntity dataPipeline = new DataPipelineEntity();

  public static final SqlColumn<String> pipelineName = dataPipeline.pipelineName;
  public static final SqlColumn<String> pipelineDescription = dataPipeline.pipelineDescription;
  public static final SqlColumn<String> processMode = dataPipeline.processMode;

  public static final SqlColumn<String> tags = dataPipeline.tags;
  public static final SqlColumn<String> orgHierName = dataPipeline.orgHierName;
    public static final SqlColumn<String> activeFlag = dataPipeline.activeFlag;

  public static final SqlColumn<Timestamp> createdTimestamp = dataPipeline.createdTimestamp;
  public static final SqlColumn<String> createdBy = dataPipeline.createdBy;

  public static final SqlColumn<Timestamp> updatedTimestamp = dataPipeline.updatedTimestamp;
  public static final SqlColumn<String> updatedBy = dataPipeline.updatedBy;


  public static final class DataPipelineEntity extends SqlTable {

    /*  Data pipeline Table */
    public final SqlColumn<String> pipelineName = column("PIPELINE_NAME", JDBCType.VARCHAR);
    public final SqlColumn<String> pipelineDescription = column("PIPELINE_DESCRIPTION",
        JDBCType.LONGNVARCHAR);
    public final SqlColumn<String> processMode = column("PROCESS_MODE", JDBCType.VARCHAR);
    public final SqlColumn<String> tags = column("TAGS", JDBCType.VARCHAR);
    public final SqlColumn<String> orgHierName = column("ORG_HIER_NAME", JDBCType.VARCHAR);
    public final SqlColumn<Timestamp> createdTimestamp = column("CREATED_TIMESTAMP",
        JDBCType.TIMESTAMP);
    public final SqlColumn<String> createdBy = column("CREATED_BY", JDBCType.VARCHAR);
    public final SqlColumn<Timestamp> updatedTimestamp = column("UPDATED_TIMESTAMP",
        JDBCType.TIMESTAMP);
    public final SqlColumn<String> updatedBy = column("UPDATED_BY", JDBCType.VARCHAR);
    public final SqlColumn<String> activeFlag = column("ACTIVE_FLAG",
        JDBCType.VARCHAR);

    public DataPipelineEntity() {
      super("data_pipelines");
    }
  }
}
