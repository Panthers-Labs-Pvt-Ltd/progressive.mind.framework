package com.progressive.minds.chimera.entity;

import java.sql.JDBCType;
import java.sql.Timestamp;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class RelationalExtractMetadataConfigDynamicSqlEntity {

    /* Child Table */
    public static final RelationalExtractMetadataTable relationalExtractConfig = new RelationalExtractMetadataTable();



    public static final SqlColumn<String> pipelineName = relationalExtractConfig.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = relationalExtractConfig.sequenceNumber;
    public static final SqlColumn<String> extractSourceType = relationalExtractConfig.extractSourceType;
    public static final SqlColumn<String> extractSourceSubType = relationalExtractConfig.extractSourceSubType;
    public static final SqlColumn<String> dataSourceConnectionName = relationalExtractConfig.dataSourceConnectionName;
    public static final SqlColumn<String> sourceConfiguration = relationalExtractConfig.sourceConfiguration;
    public static final SqlColumn<String> dataframeName = relationalExtractConfig.dataframeName;
    public static final SqlColumn<String> predecessorSequences = relationalExtractConfig.predecessorSequences;
    public static final SqlColumn<String> successorSequences = relationalExtractConfig.successorSequences;
    public static final SqlColumn<String> rowFilter = relationalExtractConfig.rowFilter;
    public static final SqlColumn<String> columnFilter = relationalExtractConfig.columnFilter;
    public static final SqlColumn<Timestamp> createdTimestamp = relationalExtractConfig.createdTimestamp;
    public static final SqlColumn<String> createdBy = relationalExtractConfig.createdBy;
    public static final SqlColumn<Timestamp> updatedTimestamp = relationalExtractConfig.updatedTimestamp;
    public static final SqlColumn<String> updatedBy = relationalExtractConfig.updatedBy;
    public static final SqlColumn<String> activeFlag = relationalExtractConfig.activeFlag;
    public static final SqlColumn<String> databaseName = relationalExtractConfig.databaseName;
    public static final SqlColumn<String> tableName = relationalExtractConfig.tableName;
    public static final SqlColumn<String> schemaName = relationalExtractConfig.schemaName;
    public static final SqlColumn<String> sqlText = relationalExtractConfig.sqlText;

    public static final class RelationalExtractMetadataTable extends SqlTable {

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
        public final SqlColumn<String> databaseName = column("DATABASE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> tableName = column("TABLE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> schemaName = column("SCHEMA_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> sqlText = column("SQL_TEXT", JDBCType.CLOB);

        public RelationalExtractMetadataTable() {
            super("relational_extract_metadata_config");
        }
    }
}
