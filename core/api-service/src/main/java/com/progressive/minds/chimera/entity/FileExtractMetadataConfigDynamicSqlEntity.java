package com.progressive.minds.chimera.entity;

import java.math.BigInteger;
import java.sql.JDBCType;
import java.sql.Timestamp;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class FileExtractMetadataConfigDynamicSqlEntity {


    /* Child Table */
    public static final FileExtractMetadataTable fileExtractConfig = new FileExtractMetadataTable();



    public static final SqlColumn<String> pipelineName = fileExtractConfig.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = fileExtractConfig.sequenceNumber;
    public static final SqlColumn<String> extractSourceType = fileExtractConfig.extractSourceType;
    public static final SqlColumn<String> extractSourceSubType = fileExtractConfig.extractSourceSubType;
    public static final SqlColumn<String> dataSourceConnectionName = fileExtractConfig.dataSourceConnectionName;
    public static final SqlColumn<String> sourceConfiguration = fileExtractConfig.sourceConfiguration;
    public static final SqlColumn<String> dataframeName = fileExtractConfig.dataframeName;
    public static final SqlColumn<String> predecessorSequences = fileExtractConfig.predecessorSequences;
    public static final SqlColumn<String> successorSequences = fileExtractConfig.successorSequences;
    public static final SqlColumn<String> rowFilter = fileExtractConfig.rowFilter;
    public static final SqlColumn<String> columnFilter = fileExtractConfig.columnFilter;
    public static final SqlColumn<Timestamp> createdTimestamp = fileExtractConfig.createdTimestamp;
    public static final SqlColumn<String> createdBy = fileExtractConfig.createdBy;
    public static final SqlColumn<Timestamp> updatedTimestamp = fileExtractConfig.updatedTimestamp;
    public static final SqlColumn<String> updatedBy = fileExtractConfig.updatedBy;
    public static final SqlColumn<String> activeFlag = fileExtractConfig.activeFlag;
    public static final SqlColumn<String> fileName = fileExtractConfig.fileName;
    public static final SqlColumn<String> filePath = fileExtractConfig.filePath;
    public static final SqlColumn<String> schemaPath = fileExtractConfig.schemaPath;
    public static final SqlColumn<BigInteger> sizeInByte = fileExtractConfig.sizeInByte;
    public static final SqlColumn<String> compressionType = fileExtractConfig.compressionType;

    public static final class FileExtractMetadataTable extends SqlTable {
  //      public final SqlTable fileExtractConfig = SqlTable.of("file_extract_metadata_config");
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
        public final SqlColumn<String> fileName = column("FILE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> filePath = column("FILE_PATH", JDBCType.VARCHAR);
        public final SqlColumn<String> schemaPath = column("SCHEMA_PATH", JDBCType.VARCHAR);
        public final SqlColumn<BigInteger> sizeInByte = column("SIZE_IN_BYTE", JDBCType.BIGINT);
        public final SqlColumn<String> compressionType = column("COMPRESSION_TYPE", JDBCType.VARCHAR);

        public FileExtractMetadataTable() {
            super("file_extract_metadata_config");
        }
    }
}
