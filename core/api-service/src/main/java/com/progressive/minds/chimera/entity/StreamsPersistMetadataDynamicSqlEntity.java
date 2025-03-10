package com.progressive.minds.chimera.entity;

import java.sql.JDBCType;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;


public class StreamsPersistMetadataDynamicSqlEntity {
    
    /* Child Table */
    public static final StreamsPersistTable streamsPersistTable = new StreamsPersistTable();

    public static final SqlColumn<String> pipelineName = streamsPersistTable.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = streamsPersistTable.sequenceNumber;
    public static final SqlColumn<String> kafkaTopic = streamsPersistTable.kafkaTopic;
    public static final SqlColumn<String> kafkaKey = streamsPersistTable.kafkaKey;
    public static final SqlColumn<String> kafkaMessage = streamsPersistTable.kafkaMessage;

    
    public static final class StreamsPersistTable extends SqlTable {
        public final SqlColumn<String> pipelineName = column("PIPELINE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<Integer> sequenceNumber = column("SEQUENCE_NUMBER", JDBCType.INTEGER);
        public final SqlColumn<String> kafkaTopic = column("KAFKA_TOPIC", JDBCType.VARCHAR);
        public final SqlColumn<String> kafkaKey = column("KAFKA_KEY", JDBCType.VARCHAR);
        public final SqlColumn<String> kafkaMessage = column("KAFKA_MESSAGE", JDBCType.VARCHAR);
      
        public StreamsPersistTable() {
            super("streams_persist_metadata_config");
        }
    }
}

