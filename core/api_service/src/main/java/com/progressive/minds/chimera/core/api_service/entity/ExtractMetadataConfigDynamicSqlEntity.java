package com.progressive.minds.chimera.core.api_service.entity;

import java.sql.JDBCType;
import java.sql.Timestamp;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public class ExtractMetadataConfigDynamicSqlEntity {

    public static ExtractMetadataConfigEntity extractMetadataConfig = new ExtractMetadataConfigEntity();

    public static final SqlColumn<String> pipelineName = extractMetadataConfig.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = extractMetadataConfig.sequenceNumber;
    public static final SqlColumn<String> extractSourceType = extractMetadataConfig.extractSourceType;
    public static final SqlColumn<String> extractSourceSubType = extractMetadataConfig.extractSourceSubType;
    public static final SqlColumn<String> dataSourceConnectionName = extractMetadataConfig.dataSourceConnectionName;
    public static final SqlColumn<String> sourceConfiguration = extractMetadataConfig.sourceConfiguration;
    public static final SqlColumn<String> dataframeName = extractMetadataConfig.dataframeName;
    public static final SqlColumn<String> predecessorSequences = extractMetadataConfig.predecessorSequences;
    public static final SqlColumn<String> successorSequences = extractMetadataConfig.successorSequences;
    public static final SqlColumn<String> rowFilter = extractMetadataConfig.rowFilter;
    public static final SqlColumn<String> columnFilter = extractMetadataConfig.columnFilter;
    public static final SqlColumn<Timestamp> createdTimestamp = extractMetadataConfig.createdTimestamp;
    public static final SqlColumn<String> createdBy = extractMetadataConfig.createdBy;
    public static final SqlColumn<Timestamp> updatedTimestamp = extractMetadataConfig.updatedTimestamp;
    public static final SqlColumn<String> updatedBy = extractMetadataConfig.updatedBy;
    public static final SqlColumn<String> activeFlag = extractMetadataConfig.activeFlag;

    public static class ExtractMetadataConfigEntity extends SqlTable {

        public final SqlColumn<String> pipelineName = column("PIPELINE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<Integer> sequenceNumber = column("SEQUENCE_NUMBER", JDBCType.INTEGER);
        public final SqlColumn<String> extractSourceType = column("EXTRACT_SOURCE_TYPE", JDBCType.VARCHAR);
        public final SqlColumn<String> extractSourceSubType = column("EXTRACT_SOURCE_SUB_TYPE", JDBCType.VARCHAR);
        public final SqlColumn<String> dataSourceConnectionName = column("DATA_SOURCE_CONNECTION_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> sourceConfiguration = column("SOURCE_CONFIGURATION", JDBCType.CLOB);
        public final SqlColumn<String> dataframeName = column("DATAFRAME_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> predecessorSequences = column("PREDECESSOR_SEQUENCES", JDBCType.LONGNVARCHAR);
        public final SqlColumn<String> successorSequences = column("SUCCESSOR_SEQUENCES", JDBCType.LONGNVARCHAR);
        public final SqlColumn<String> rowFilter = column("ROW_FILTER", JDBCType.LONGNVARCHAR);
        public final SqlColumn<String> columnFilter = column("COLUMN_FILTER", JDBCType.LONGNVARCHAR);
        public final SqlColumn<Timestamp> createdTimestamp = column("CREATED_TIMESTAMP", JDBCType.TIMESTAMP);
        public final SqlColumn<String> createdBy = column("CREATED_BY", JDBCType.VARCHAR);
        public final SqlColumn<Timestamp> updatedTimestamp = column("UPDATED_TIMESTAMP", JDBCType.TIMESTAMP);
        public final SqlColumn<String> updatedBy = column("UPDATED_BY", JDBCType.VARCHAR);
        public final SqlColumn<String> activeFlag = column("ACTIVE_FLAG", JDBCType.VARCHAR);

        public ExtractMetadataConfigEntity() {
            super("EXTRACT_METADATA_CONFIG");
        }
    }
}
