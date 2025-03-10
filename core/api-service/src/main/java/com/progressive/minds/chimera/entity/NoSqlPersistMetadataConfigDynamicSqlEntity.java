package com.progressive.minds.chimera.entity;

import java.sql.JDBCType;
import java.sql.Timestamp;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class NoSqlPersistMetadataConfigDynamicSqlEntity {


    /* Child Table */
    public static final NoSqlPersistMetadataTable noSqlPersistConfig = new NoSqlPersistMetadataTable();



    public static final SqlColumn<String> pipelineName = noSqlPersistConfig.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = noSqlPersistConfig.sequenceNumber;
    public static final SqlColumn<String> sinkType = noSqlPersistConfig.sinkType;
    public static final SqlColumn<String> sinkSubType = noSqlPersistConfig.sinkSubType;
    public static final SqlColumn<String> predecessorSequences = noSqlPersistConfig.predecessorSequences;
    public static final SqlColumn<String> successorSequences = noSqlPersistConfig.successorSequences;
    public static final SqlColumn<String> sinkConfiguration = noSqlPersistConfig.sinkConfiguration;  
    public static final SqlColumn<String> dataSourceConnectionName = noSqlPersistConfig.dataSourceConnectionName;
    public static final SqlColumn<String> partitionKeys = noSqlPersistConfig.partitionKeys;
    public static final SqlColumn<String> targetSQL = noSqlPersistConfig.targetSQL;
    public static final SqlColumn<String> sortColumns = noSqlPersistConfig.sortColumns;
    public static final SqlColumn<String> dedupColumns = noSqlPersistConfig.dedupColumns;
    public static final SqlColumn<Timestamp> createdTimestamp = noSqlPersistConfig.createdTimestamp;
    public static final SqlColumn<String> createdBy = noSqlPersistConfig.createdBy;
    public static final SqlColumn<Timestamp> updatedTimestamp = noSqlPersistConfig.updatedTimestamp;
    public static final SqlColumn<String> updatedBy = noSqlPersistConfig.updatedBy;
    public static final SqlColumn<String> activeFlag = noSqlPersistConfig.activeFlag;
    public static final SqlColumn<String> collection = noSqlPersistConfig.collection;
    public static final SqlColumn<String> partitioner = noSqlPersistConfig.partitioner;
   

    public static final class NoSqlPersistMetadataTable extends SqlTable {
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
        public final SqlColumn<String> collection = column("COLLECTION", JDBCType.VARCHAR);
        public final SqlColumn<String> partitioner = column("PARTITIONER", JDBCType.VARCHAR);
        
        public NoSqlPersistMetadataTable() {
            super("nosql_persist_metadata_config");
        }
    }
}
