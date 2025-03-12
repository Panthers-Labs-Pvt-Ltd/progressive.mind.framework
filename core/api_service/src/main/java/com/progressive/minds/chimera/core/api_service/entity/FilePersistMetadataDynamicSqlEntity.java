package com.progressive.minds.chimera.core.api_service.entity;

import java.sql.JDBCType;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public class FilePersistMetadataDynamicSqlEntity {
    
   /* Child Table */
    public static final FilePersistTable filePersistTable = new FilePersistTable();

    public static final SqlColumn<String> pipelineName = filePersistTable.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = filePersistTable.sequenceNumber;
    public static final SqlColumn<String> fileName = filePersistTable.fileName;
    public static final SqlColumn<String> filePath = filePersistTable.filePath;
    public static final SqlColumn<String> writeMode = filePersistTable.writeMode;
    
    public static final class FilePersistTable extends SqlTable {
        public final SqlColumn<String> pipelineName = column("PIPELINE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<Integer> sequenceNumber = column("SEQUENCE_NUMBER", JDBCType.INTEGER);
        public final SqlColumn<String> fileName = column("FILE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> filePath = column("FILE_PATH", JDBCType.VARCHAR);
        public final SqlColumn<String> writeMode = column("WRITE_MODE", JDBCType.VARCHAR);
        
        public FilePersistTable() {
            super("file_persist_metadata_config");
        }
    }
}

