package com.progressive.minds.chimera.entity;

import java.sql.JDBCType;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class StreamsExtractMetadataDynamicSqlEntity {

    /* Child Table */
    public static final StreamsExtractTable streamsExtractTable = new StreamsExtractTable();

    public static final SqlColumn<String> pipelineName = streamsExtractTable.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = streamsExtractTable.sequenceNumber;
    public static final SqlColumn<String> kafkaConsumerTopic = streamsExtractTable.kafkaConsumerTopic;
    public static final SqlColumn<String> kafkaConsumerGroup = streamsExtractTable.kafkaConsumerGroup;
    public static final SqlColumn<String> kafkaStartOffset = streamsExtractTable.kafkaStrtOffset;
    public static final SqlColumn<String> kafkaMaxOffset = streamsExtractTable.kafkaMaxOffset;
    public static final SqlColumn<Integer> kafkaPollTimeout = streamsExtractTable.kafkaPollTimeout;
    public static final SqlColumn<String> tranctnlCnsumrFlg = streamsExtractTable.tranctnlCnsumrFlg;
    public static final SqlColumn<String> watrmrkDuration = streamsExtractTable.watrmrkDuration;
    public static final SqlColumn<String> stgFormt = streamsExtractTable.stgFormt;
    public static final SqlColumn<String> stgPath = streamsExtractTable.stgPath;
    public static final SqlColumn<String> stgPartitions = streamsExtractTable.stgPartitions;


    public static final class StreamsExtractTable extends SqlTable {

        public final SqlColumn<String> pipelineName = column("PIPELINE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<Integer> sequenceNumber = column("SEQUENCE_NUMBER", JDBCType.INTEGER);
        public final SqlColumn<String> kafkaConsumerTopic = column("KAFKA_CONSUMER_TOPIC", JDBCType.VARCHAR);
        public final SqlColumn<String> kafkaConsumerGroup = column("KAFKA_CONSUMER_GROUP", JDBCType.VARCHAR);
        public final SqlColumn<String> kafkaStrtOffset = column("KAFKA_STRT_OFFSET", JDBCType.VARCHAR);
        public final SqlColumn<String> kafkaMaxOffset = column("KAFKA_MAX_OFFSET", JDBCType.VARCHAR);
        public final SqlColumn<Integer> kafkaPollTimeout = column("KAFKA_POLL_TIMEOUT", JDBCType.INTEGER);
        public final SqlColumn<String> tranctnlCnsumrFlg = column("TRANCTNL_CNSUMR_FLG", JDBCType.VARCHAR);
        public final SqlColumn<String> watrmrkDuration = column("WATRMRK_DURATION", JDBCType.VARCHAR);
        public final SqlColumn<String> stgFormt = column("STG_FORMT", JDBCType.VARCHAR);
        public final SqlColumn<String> stgPath = column("STG_PATH", JDBCType.VARCHAR);
        public final SqlColumn<String> stgPartitions = column("STG_PARTITIONS", JDBCType.VARCHAR);

        public StreamsExtractTable() {
            super("streams_extract_metadata_config");
        }
    }
}
