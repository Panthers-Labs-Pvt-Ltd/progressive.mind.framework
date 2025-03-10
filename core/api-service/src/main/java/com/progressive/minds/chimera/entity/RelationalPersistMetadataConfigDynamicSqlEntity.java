package com.progressive.minds.chimera.entity;

import java.sql.JDBCType;
import java.sql.Timestamp;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class RelationalPersistMetadataConfigDynamicSqlEntity {


    /* Child Table */
    public static final RelationalPersistMetadataTable relationalPersistConfig = new RelationalPersistMetadataTable();



    public static final SqlColumn<String> pipelineName = relationalPersistConfig.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = relationalPersistConfig.sequenceNumber;
    public static final SqlColumn<String> sinkType = relationalPersistConfig.sinkType;
    public static final SqlColumn<String> sinkSubType = relationalPersistConfig.sinkSubType;
    public static final SqlColumn<String> predecessorSequences = relationalPersistConfig.predecessorSequences;
    public static final SqlColumn<String> successorSequences = relationalPersistConfig.successorSequences;
    public static final SqlColumn<String> sinkConfiguration = relationalPersistConfig.sinkConfiguration;  
    public static final SqlColumn<String> dataSourceConnectionName = relationalPersistConfig.dataSourceConnectionName;
    public static final SqlColumn<String> partitionKeys = relationalPersistConfig.partitionKeys;
    public static final SqlColumn<String> targetSQL = relationalPersistConfig.targetSQL;
    public static final SqlColumn<String> sortColumns = relationalPersistConfig.sortColumns;
    public static final SqlColumn<String> dedupColumns = relationalPersistConfig.dedupColumns;
    public static final SqlColumn<Timestamp> createdTimestamp = relationalPersistConfig.createdTimestamp;
    public static final SqlColumn<String> createdBy = relationalPersistConfig.createdBy;
    public static final SqlColumn<Timestamp> updatedTimestamp = relationalPersistConfig.updatedTimestamp;
    public static final SqlColumn<String> updatedBy = relationalPersistConfig.updatedBy;
    public static final SqlColumn<String> activeFlag = relationalPersistConfig.activeFlag;
    public static final SqlColumn<String> databaseName = relationalPersistConfig.databaseName;
    public static final SqlColumn<String> schemaName = relationalPersistConfig.schemaName;
    public static final SqlColumn<String> tableName = relationalPersistConfig.tableName;
   

    public static final class RelationalPersistMetadataTable extends SqlTable {
  //      public final SqlTable fileExtractConfig = SqlTable.of("file_extract_metadata_config");
        public final SqlColumn<String> pipelineName = column("PIPELINE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<Integer> sequenceNumber = column("SEQUENCE_NUMBER", JDBCType.INTEGER);
        public final SqlColumn<String> sinkType = column("SINK_TYPE", JDBCType.VARCHAR);
        public final SqlColumn<String> sinkSubType = column("SINK_SUB_TYPE", JDBCType.VARCHAR);
        public final SqlColumn<String> predecessorSequences = column("PREDECESSOR_SEQUENCES", JDBCType.VARCHAR);
        public final SqlColumn<String> successorSequences = column("SUCCESSOR_SEQUENCES", JDBCType.VARCHAR);
        public final SqlColumn<String> sinkConfiguration = column("SINK_CONFIGURATION", JDBCType.CLOB);
        public final SqlColumn<String> dataSourceConnectionName = column("DATA_SOURCE_CONNECTION_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> partitionKeys = column("PARTITION_KEYS", JDBCType.VARCHAR);
        public final SqlColumn<String> targetSQL = column("TARGET_SQL", JDBCType.VARCHAR);
        public final SqlColumn<String> sortColumns = column("SORT_COLUMNS", JDBCType.VARCHAR);
        public final SqlColumn<String> dedupColumns = column("DEDUP_COLUMNS", JDBCType.VARCHAR);
        public final SqlColumn<Timestamp> createdTimestamp = column("CREATED_TIMESTAMP", JDBCType.TIMESTAMP);
        public final SqlColumn<String> createdBy = column("CREATED_BY", JDBCType.VARCHAR);
        public final SqlColumn<Timestamp> updatedTimestamp = column("UPDATED_TIMESTAMP", JDBCType.TIMESTAMP);
        public final SqlColumn<String> updatedBy = column("UPDATED_BY", JDBCType.VARCHAR);
        public final SqlColumn<String> activeFlag = column("ACTIVE_FLAG", JDBCType.VARCHAR);
        public final SqlColumn<String> databaseName = column("DATABASE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> schemaName = column("SCHEMA_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> tableName = column("TABLE_NAME", JDBCType.VARCHAR);
        
        public RelationalPersistMetadataTable() {
            super("relational_persist_metadata_config");
        }
    }
}
