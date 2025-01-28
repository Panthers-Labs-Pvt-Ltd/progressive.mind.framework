package com.progressive.minds.chimera.entity;

import java.sql.JDBCType;
import java.sql.Timestamp;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class NoSqlExtractMetadataConfigDynamicSqlEntity {

    /* Child Table */
    public static final NoSqlExtractMetadataTable noSqlExtractConfig = new NoSqlExtractMetadataTable();

    public static final SqlColumn<String> pipelineName = noSqlExtractConfig.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = noSqlExtractConfig.sequenceNumber;
    public static final SqlColumn<String> extractSourceType = noSqlExtractConfig.extractSourceType;
    public static final SqlColumn<String> extractSourceSubType = noSqlExtractConfig.extractSourceSubType;
    public static final SqlColumn<String> dataSourceConnectionName = noSqlExtractConfig.dataSourceConnectionName;
    public static final SqlColumn<String> sourceConfiguration = noSqlExtractConfig.sourceConfiguration;
    public static final SqlColumn<String> dataframeName = noSqlExtractConfig.dataframeName;
    public static final SqlColumn<String> predecessorSequences = noSqlExtractConfig.predecessorSequences;
    public static final SqlColumn<String> successorSequences = noSqlExtractConfig.successorSequences;
    public static final SqlColumn<String> rowFilter = noSqlExtractConfig.rowFilter;
    public static final SqlColumn<String> columnFilter = noSqlExtractConfig.columnFilter;
    public static final SqlColumn<Timestamp> createdTimestamp = noSqlExtractConfig.createdTimestamp;
    public static final SqlColumn<String> createdBy = noSqlExtractConfig.createdBy;
    public static final SqlColumn<Timestamp> updatedTimestamp = noSqlExtractConfig.updatedTimestamp;
    public static final SqlColumn<String> updatedBy = noSqlExtractConfig.updatedBy;
    public static final SqlColumn<String> activeFlag = noSqlExtractConfig.activeFlag;
    public static final SqlColumn<String> collection = noSqlExtractConfig.collection;
    public static final SqlColumn<String> partitioner = noSqlExtractConfig.partitioner;
    
    public static final class NoSqlExtractMetadataTable extends SqlTable {
        public final SqlColumn<String> pipelineName = column("PIPELINE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<Integer> sequenceNumber = column("SEQUENCE_NUMBER", JDBCType.INTEGER);
        public final SqlColumn<String> extractSourceType = column("EXTRACT_SOURCE_TYPE", JDBCType.VARCHAR);
        public final SqlColumn<String> extractSourceSubType = column("EXTRACT_SOURCE_SUB_TYPE", JDBCType.VARCHAR);
        public final SqlColumn<String> dataSourceConnectionName = column("DATA_SOURCE_CONNECTION_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> sourceConfiguration = column("SOURCE_CONFIGURATION", JDBCType.CLOB);
        public final SqlColumn<String> dataframeName = column("DATAFRAME_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> predecessorSequences = column("PREDECESSOR_SEQUENCES", JDBCType.VARCHAR);
        public final SqlColumn<String> successorSequences = column("SUCCESSOR_SEQUENCES", JDBCType.VARCHAR);
        public final SqlColumn<String> rowFilter = column("ROW_FILTER", JDBCType.VARCHAR);
        public final SqlColumn<String> columnFilter = column("COLUMN_FILTER", JDBCType.VARCHAR);
        public final SqlColumn<Timestamp> createdTimestamp = column("CREATED_TIMESTAMP", JDBCType.TIMESTAMP);
        public final SqlColumn<String> createdBy = column("CREATED_BY", JDBCType.VARCHAR);
        public final SqlColumn<Timestamp> updatedTimestamp = column("UPDATED_TIMESTAMP", JDBCType.TIMESTAMP);
        public final SqlColumn<String> updatedBy = column("UPDATED_BY", JDBCType.VARCHAR);
        public final SqlColumn<String> activeFlag = column("ACTIVE_FLAG", JDBCType.VARCHAR);
        public final SqlColumn<String> collection = column("COLLECTION", JDBCType.VARCHAR);
        public final SqlColumn<String> partitioner = column("PARTITIONER", JDBCType.VARCHAR);
      
        public NoSqlExtractMetadataTable() {
            super("nosql_extract_metadata_config");
        }
    }
}
