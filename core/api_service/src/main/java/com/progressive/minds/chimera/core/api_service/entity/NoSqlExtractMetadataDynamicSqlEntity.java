package com.progressive.minds.chimera.core.api_service.entity;

import java.sql.JDBCType;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;


public class NoSqlExtractMetadataDynamicSqlEntity {
    
    /* Child Table */
    public static final NoSqlExtractTable noSqlExtractTable = new NoSqlExtractTable();

    public static final SqlColumn<String> pipelineName = noSqlExtractTable.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = noSqlExtractTable.sequenceNumber;
    public static final SqlColumn<String> collection = noSqlExtractTable.collection;
    public static final SqlColumn<String> partitioner = noSqlExtractTable.partitioner;
    
    public static final class NoSqlExtractTable extends SqlTable {
        public final SqlColumn<String> pipelineName = column("PIPELINE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<Integer> sequenceNumber = column("SEQUENCE_NUMBER", JDBCType.INTEGER);
        public final SqlColumn<String> collection = column("COLLECTION", JDBCType.VARCHAR);
        public final SqlColumn<String> partitioner = column("PARTITIONER", JDBCType.VARCHAR);
      
        public NoSqlExtractTable() {
            super("nosql_extract_metadata_config");
        }
    }
}

