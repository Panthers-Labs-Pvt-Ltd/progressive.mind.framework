package com.progressive.minds.chimera.core.api_service.entity;

import java.math.BigInteger;
import java.sql.JDBCType;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public class FileExtractMetadataDynamicSqlEntity {
    
   /* Child Table */
    public static final FileExtractTable fileExtractTable = new FileExtractTable();

    public static final SqlColumn<String> pipelineName = fileExtractTable.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = fileExtractTable.sequenceNumber;
    public static final SqlColumn<String> fileName = fileExtractTable.fileName;
    public static final SqlColumn<String> filePath = fileExtractTable.filePath;
    public static final SqlColumn<String> schemaPath = fileExtractTable.schemaPath;
    public static final SqlColumn<BigInteger> sizeInByte = fileExtractTable.sizeInByte;
    public static final SqlColumn<String> compressionType = fileExtractTable.compressionType;

    public static final class FileExtractTable extends SqlTable {
        public final SqlColumn<String> pipelineName = column("PIPELINE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<Integer> sequenceNumber = column("SEQUENCE_NUMBER", JDBCType.INTEGER);
        public final SqlColumn<String> fileName = column("FILE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> filePath = column("FILE_PATH", JDBCType.VARCHAR);
        public final SqlColumn<String> schemaPath = column("SCHEMA_PATH", JDBCType.VARCHAR);
        public final SqlColumn<BigInteger> sizeInByte = column("SIZE_IN_BYTE", JDBCType.BIGINT);
        public final SqlColumn<String> compressionType = column("COMPRESSION_TYPE", JDBCType.VARCHAR);

        public FileExtractTable() {
            super("file_extract_metadata_config");
        }
    }
}

