package com.progressive.minds.chimera.entity;

import java.sql.JDBCType;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class RelationalExtractMetadataDynamicSqlEntity {

    /* Child Table */
    public static final RelationalExtractTable relationalExtractTable = new RelationalExtractTable();

    public static final SqlColumn<String> pipelineName = relationalExtractTable.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = relationalExtractTable.sequenceNumber;
    public static final SqlColumn<String> databaseName = relationalExtractTable.databaseName;
    public static final SqlColumn<String> tableName = relationalExtractTable.tableName;
    public static final SqlColumn<String> schemaName = relationalExtractTable.schemaName;
    public static final SqlColumn<String> sqlText = relationalExtractTable.sqlText;

    public static final class RelationalExtractTable extends SqlTable {
        public final SqlColumn<String> pipelineName = column("PIPELINE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<Integer> sequenceNumber = column("SEQUENCE_NUMBER", JDBCType.INTEGER);
        public final SqlColumn<String> databaseName = column("DATABASE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> tableName = column("TABLE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> schemaName = column("SCHEMA_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> sqlText = column("SQL_TEXT", JDBCType.CLOB);

        public RelationalExtractTable() {
            super("relational_extract_metadata_config");
        }
    }
}
