package com.progressive.minds.chimera.core.api_service.entity;

import java.sql.JDBCType;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;


public class RelationalPersistMetadataDynamicSqlEntity {
    
    /* Child Table */
    public static final RelationalPersistTable relationalPersistTable = new RelationalPersistTable();

    public static final SqlColumn<String> pipelineName = relationalPersistTable.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = relationalPersistTable.sequenceNumber;
    public static final SqlColumn<String> databaseName = relationalPersistTable.databaseName;
    public static final SqlColumn<String> schemaName = relationalPersistTable.schemaName;
    public static final SqlColumn<String> tableName = relationalPersistTable.tableName;

    
    public static final class RelationalPersistTable extends SqlTable {
        public final SqlColumn<String> pipelineName = column("PIPELINE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<Integer> sequenceNumber = column("SEQUENCE_NUMBER", JDBCType.INTEGER);
        public final SqlColumn<String> databaseName = column("DATABASE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> schemaName = column("SCHEMA_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> tableName = column("TABLE_NAME", JDBCType.VARCHAR);
      
        public RelationalPersistTable() {
            super("relational_persist_metadata_config");
        }
    }
}

