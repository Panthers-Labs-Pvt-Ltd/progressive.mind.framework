package com.progressive.minds.chimera.core.api_service.entity;

import java.sql.JDBCType;
import java.sql.Timestamp;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class StreamsExtractMetadataConfigDynamicSqlEntity {

    /* Child Table */
    public static final StreamsExtractMetadataTable streamsExtractConfig = new StreamsExtractMetadataTable();

    public static final SqlColumn<String> pipelineName = streamsExtractConfig.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = streamsExtractConfig.sequenceNumber;
    public static final SqlColumn<String> extractSourceType = streamsExtractConfig.extractSourceType;
    public static final SqlColumn<String> extractSourceSubType = streamsExtractConfig.extractSourceSubType;
    public static final SqlColumn<String> dataSourceConnectionName = streamsExtractConfig.dataSourceConnectionName;
    public static final SqlColumn<String> sourceConfiguration = streamsExtractConfig.sourceConfiguration;
    public static final SqlColumn<String> dataframeName = streamsExtractConfig.dataframeName;
    public static final SqlColumn<String> predecessorSequences = streamsExtractConfig.predecessorSequences;
    public static final SqlColumn<String> successorSequences = streamsExtractConfig.successorSequences;
    public static final SqlColumn<String> rowFilter = streamsExtractConfig.rowFilter;
    public static final SqlColumn<String> columnFilter = streamsExtractConfig.columnFilter;
    public static final SqlColumn<Timestamp> createdTimestamp = streamsExtractConfig.createdTimestamp;
    public static final SqlColumn<String> createdBy = streamsExtractConfig.createdBy;
    public static final SqlColumn<Timestamp> updatedTimestamp = streamsExtractConfig.updatedTimestamp;
    public static final SqlColumn<String> updatedBy = streamsExtractConfig.updatedBy;
    public static final SqlColumn<String> activeFlag = streamsExtractConfig.activeFlag;
    public static final SqlColumn<String> kafkaConsumerTopic = streamsExtractConfig.kafkaConsumerTopic;
    public static final SqlColumn<String> kafkaConsumerGroup = streamsExtractConfig.kafkaConsumerGroup;
    public static final SqlColumn<String> kafkaStartOffset = streamsExtractConfig.kafkaStrtOffset;
    public static final SqlColumn<String> kafkaMaxOffset = streamsExtractConfig.kafkaMaxOffset;
    public static final SqlColumn<Integer> kafkaPollTimeout = streamsExtractConfig.kafkaPollTimeout;
    public static final SqlColumn<String> tranctnlCnsumrFlg = streamsExtractConfig.tranctnlCnsumrFlg;
    public static final SqlColumn<String> watrmrkDuration = streamsExtractConfig.watrmrkDuration;
    public static final SqlColumn<String> stgFormt = streamsExtractConfig.stgFormt;
    public static final SqlColumn<String> stgPath = streamsExtractConfig.stgPath;
    public static final SqlColumn<String> stgPartitions = streamsExtractConfig.stgPartitions;


    public static final class StreamsExtractMetadataTable extends SqlTable {

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

        public StreamsExtractMetadataTable() {
            super("streams_extract_metadata_config");
        }
    }
}
