package com.progressive.minds.chimera.core.api_service.entity;

import java.sql.JDBCType;
import java.sql.Timestamp;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class StreamsPersistMetadataConfigDynamicSqlEntity {


    /* Child Table */
    public static final StreamsPersistMetadataTable StreamsPersistConfig = new StreamsPersistMetadataTable();



    public static final SqlColumn<String> pipelineName = StreamsPersistConfig.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = StreamsPersistConfig.sequenceNumber;
    public static final SqlColumn<String> sinkType = StreamsPersistConfig.sinkType;
    public static final SqlColumn<String> sinkSubType = StreamsPersistConfig.sinkSubType;
    public static final SqlColumn<String> predecessorSequences = StreamsPersistConfig.predecessorSequences;
    public static final SqlColumn<String> successorSequences = StreamsPersistConfig.successorSequences;
    public static final SqlColumn<String> sinkConfiguration = StreamsPersistConfig.sinkConfiguration;  
    public static final SqlColumn<String> dataSourceConnectionName = StreamsPersistConfig.dataSourceConnectionName;
    public static final SqlColumn<String> partitionKeys = StreamsPersistConfig.partitionKeys;
    public static final SqlColumn<String> targetSQL = StreamsPersistConfig.targetSQL;
    public static final SqlColumn<String> sortColumns = StreamsPersistConfig.sortColumns;
    public static final SqlColumn<String> dedupColumns = StreamsPersistConfig.dedupColumns;
    public static final SqlColumn<Timestamp> createdTimestamp = StreamsPersistConfig.createdTimestamp;
    public static final SqlColumn<String> createdBy = StreamsPersistConfig.createdBy;
    public static final SqlColumn<Timestamp> updatedTimestamp = StreamsPersistConfig.updatedTimestamp;
    public static final SqlColumn<String> updatedBy = StreamsPersistConfig.updatedBy;
    public static final SqlColumn<String> activeFlag = StreamsPersistConfig.activeFlag;
    public static final SqlColumn<String> kafkaTopic = StreamsPersistConfig.kafkaTopic;
    public static final SqlColumn<String> kafkaKey = StreamsPersistConfig.kafkaKey;
    public static final SqlColumn<String> kafkaMessage = StreamsPersistConfig.kafkaMessage;
   

    public static final class StreamsPersistMetadataTable extends SqlTable {
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
        public final SqlColumn<String> kafkaTopic = column("KAFKA_TOPIC", JDBCType.VARCHAR);
        public final SqlColumn<String> kafkaKey = column("KAFKA_KEY", JDBCType.VARCHAR);
        public final SqlColumn<String> kafkaMessage = column("KAFKA_MESSAGE", JDBCType.VARCHAR);
        
        public StreamsPersistMetadataTable() {
            super("streams_persist_metadata_config");
        }
    }
}
