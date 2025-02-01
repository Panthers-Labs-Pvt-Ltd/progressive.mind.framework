package com.progressive.minds.chimera.entity;

import java.sql.JDBCType;
import java.sql.Timestamp;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class PersistMetadataConfigDynamicSqlEntity {

    public static PersistMetadataConfigEntity persistMetadataConfig = new PersistMetadataConfigEntity();

    public static final SqlColumn<String> pipelineName = persistMetadataConfig.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = persistMetadataConfig.sequenceNumber;
    public static final SqlColumn<String> sinkType = persistMetadataConfig.sinkType;
    public static final SqlColumn<String> sinkSubType = persistMetadataConfig.sinkSubType;
    public static final SqlColumn<String> dataSourceConnectionName = persistMetadataConfig.dataSourceConnectionName;
    public static final SqlColumn<String> databaseName = persistMetadataConfig.databaseName;
    public static final SqlColumn<String> tableName = persistMetadataConfig.tableName;
    public static final SqlColumn<String> schemaName = persistMetadataConfig.schemaName;
    public static final SqlColumn<String> partitionKeys = persistMetadataConfig.partitionKeys;
    public static final SqlColumn<String> targetSql = persistMetadataConfig.targetSql;
    public static final SqlColumn<String> targetPath = persistMetadataConfig.targetPath;
    public static final SqlColumn<String> writeMode = persistMetadataConfig.writeMode;
    public static final SqlColumn<String> sinkConfiguration = persistMetadataConfig.sinkConfiguration;
    public static final SqlColumn<String> sortColumns = persistMetadataConfig.sortColumns;
    public static final SqlColumn<String> dedupColumns = persistMetadataConfig.dedupColumns;
    public static final SqlColumn<String> kafkaTopic = persistMetadataConfig.kafkaTopic;
    public static final SqlColumn<String> kafkaKey = persistMetadataConfig.kafkaKey;
    public static final SqlColumn<String> kafkaMessage = persistMetadataConfig.kafkaMessage;
    public static final SqlColumn<Timestamp> createdTimestamp = persistMetadataConfig.createdTimestamp;
    public static final SqlColumn<String> createdBy = persistMetadataConfig.createdBy;
    public static final SqlColumn<Timestamp> updatedTimestamp = persistMetadataConfig.updatedTimestamp;
    public static final SqlColumn<String> updatedBy = persistMetadataConfig.updatedBy;
    public static final SqlColumn<String> activeFlag = persistMetadataConfig.activeFlag;

    public static final class PersistMetadataConfigEntity extends SqlTable {

        public final SqlColumn<String> pipelineName = column("PIPELINE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<Integer> sequenceNumber = column("SEQUENCE_NUMBER", JDBCType.INTEGER);
        public final SqlColumn<String> sinkType = column("SINK_TYPE", JDBCType.VARCHAR);
        public final SqlColumn<String> sinkSubType = column("SINK_SUB_TYPE", JDBCType.VARCHAR);
        public final SqlColumn<String> dataSourceConnectionName = column("DATA_SOURCE_CONNECTION_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> databaseName = column("DATABASE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> tableName = column("TABLE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> schemaName = column("SCHEMA_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> partitionKeys = column("PARTITION_KEYS", JDBCType.CLOB);
        public final SqlColumn<String> targetSql = column("TARGET_SQL", JDBCType.CLOB);
        public final SqlColumn<String> targetPath = column("TARGET_PATH", JDBCType.VARCHAR);
        public final SqlColumn<String> writeMode = column("WRITE_MODE", JDBCType.VARCHAR);
        public final SqlColumn<String> sinkConfiguration = column("SINK_CONFIGURATION", JDBCType.CLOB);
        public final SqlColumn<String> sortColumns = column("SORT_COLUMNS", JDBCType.CLOB);
        public final SqlColumn<String> dedupColumns = column("DEDUP_COLUMNS", JDBCType.CLOB);
        public final SqlColumn<String> kafkaTopic = column("KAFKA_TOPIC", JDBCType.VARCHAR);
        public final SqlColumn<String> kafkaKey = column("KAFKA_KEY", JDBCType.CLOB);
        public final SqlColumn<String> kafkaMessage = column("KAFKA_MESSAGE", JDBCType.CLOB);
        public final SqlColumn<Timestamp> createdTimestamp = column("CREATED_TIMESTAMP", JDBCType.TIMESTAMP);
        public final SqlColumn<String> createdBy = column("CREATED_BY", JDBCType.VARCHAR);
        public final SqlColumn<Timestamp> updatedTimestamp = column("UPDATED_TIMESTAMP", JDBCType.TIMESTAMP);
        public final SqlColumn<String> updatedBy = column("UPDATED_BY", JDBCType.VARCHAR);
        public final SqlColumn<String> activeFlag = column("ACTIVE_FLAG", JDBCType.VARCHAR);

        public PersistMetadataConfigEntity() {
            super("PERSIST_METADATA_CONFIG");
        }
    }
}
