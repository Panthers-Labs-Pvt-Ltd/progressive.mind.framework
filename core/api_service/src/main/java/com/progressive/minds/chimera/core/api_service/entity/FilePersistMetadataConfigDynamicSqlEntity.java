package com.progressive.minds.chimera.core.api_service.entity;

import java.sql.JDBCType;
import java.sql.Timestamp;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class FilePersistMetadataConfigDynamicSqlEntity {


    /* Child Table */
    public static final FilePersistMetadataTable filePersistConfig = new FilePersistMetadataTable();



    public static final SqlColumn<String> pipelineName = filePersistConfig.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = filePersistConfig.sequenceNumber;
    public static final SqlColumn<String> sinkType = filePersistConfig.sinkType;
    public static final SqlColumn<String> sinkSubType = filePersistConfig.sinkSubType;
    public static final SqlColumn<String> predecessorSequences = filePersistConfig.predecessorSequences;
    public static final SqlColumn<String> successorSequences = filePersistConfig.successorSequences;
    public static final SqlColumn<String> sinkConfiguration = filePersistConfig.sinkConfiguration;  
    public static final SqlColumn<String> dataSourceConnectionName = filePersistConfig.dataSourceConnectionName;
    public static final SqlColumn<String> partitionKeys = filePersistConfig.partitionKeys;
    public static final SqlColumn<String> targetSQL = filePersistConfig.targetSQL;
    public static final SqlColumn<String> sortColumns = filePersistConfig.sortColumns;
    public static final SqlColumn<String> dedupColumns = filePersistConfig.dedupColumns;
    public static final SqlColumn<Timestamp> createdTimestamp = filePersistConfig.createdTimestamp;
    public static final SqlColumn<String> createdBy = filePersistConfig.createdBy;
    public static final SqlColumn<Timestamp> updatedTimestamp = filePersistConfig.updatedTimestamp;
    public static final SqlColumn<String> updatedBy = filePersistConfig.updatedBy;
    public static final SqlColumn<String> activeFlag = filePersistConfig.activeFlag;
    public static final SqlColumn<String> fileName = filePersistConfig.fileName;
    public static final SqlColumn<String> filePath = filePersistConfig.filePath;
    public static final SqlColumn<String> writeMode = filePersistConfig.writeMode;


    public static final class FilePersistMetadataTable extends SqlTable {
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
        public final SqlColumn<String> fileName = column("FILE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> filePath = column("FILE_PATH", JDBCType.VARCHAR);
        public final SqlColumn<String> writeMode = column("WRITE_MODE", JDBCType.VARCHAR);

        public FilePersistMetadataTable() {
            super("file_persist_metadata_config");
        }
    }
}
