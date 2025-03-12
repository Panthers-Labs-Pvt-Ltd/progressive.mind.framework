package com.progressive.minds.chimera.core.api_service.entity;

import java.sql.JDBCType;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;


public class NoSqlPersistMetadataDynamicSqlEntity {
    
    /* Child Table */
    public static final NoSqlPersistTable noSqlPersistTable = new NoSqlPersistTable();

    public static final SqlColumn<String> pipelineName = noSqlPersistTable.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = noSqlPersistTable.sequenceNumber;
    public static final SqlColumn<String> collection = noSqlPersistTable.collection;
    public static final SqlColumn<String> partitioner = noSqlPersistTable.partitioner;
    
    public static final class NoSqlPersistTable extends SqlTable {
        public final SqlColumn<String> pipelineName = column("PIPELINE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<Integer> sequenceNumber = column("SEQUENCE_NUMBER", JDBCType.INTEGER);
        public final SqlColumn<String> collection = column("COLLECTION", JDBCType.VARCHAR);
        public final SqlColumn<String> partitioner = column("PARTITIONER", JDBCType.VARCHAR);
      
        public NoSqlPersistTable() {
            super("nosql_persist_metadata_config");
        }
    }
}

