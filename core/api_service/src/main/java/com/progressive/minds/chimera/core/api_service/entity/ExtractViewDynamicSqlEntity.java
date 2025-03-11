package com.progressive.minds.chimera.core.api_service.entity;


import java.math.BigInteger;
import java.sql.JDBCType;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public class ExtractViewDynamicSqlEntity {

    public static ExtractViewEntity extractView = new ExtractViewEntity();

    public static final SqlColumn<String> pipelineName = extractView.pipelineName;
    public static final SqlColumn<String> pipelineDescription = extractView.pipelineDescription;
    public static final SqlColumn<String> processMode = extractView.processMode;
    public static final SqlColumn<String> tags = extractView.tags;
    public static final SqlColumn<String> orgHierName = extractView.orgHierName;
    public static final SqlColumn<Integer> sequenceNumber = extractView.sequenceNumber;
    public static final SqlColumn<String> extractSourceType = extractView.extractSourceType;
    public static final SqlColumn<String> extractSourceSubType = extractView.extractSourceSubType;
    public static final SqlColumn<String> dataSourceConnectionName = extractView.dataSourceConnectionName;
    public static final SqlColumn<String> sourceConfiguration = extractView.sourceConfiguration;
    public static final SqlColumn<String> dataframeName = extractView.dataframeName;
    public static final SqlColumn<String> predecessorSequences = extractView.predecessorSequences;
    public static final SqlColumn<String> successorSequences = extractView.successorSequences;
    public static final SqlColumn<String> rowFilter = extractView.rowFilter;
    public static final SqlColumn<String> columnFilter = extractView.columnFilter;
    public static final SqlColumn<String> defaultReadConfig = extractView.defaultReadConfig;
    public static final SqlColumn<String> defaultWriteConfig = extractView.defaultWriteConfig;
    public static final SqlColumn<String> fileName = extractView.fileName;
    public static final SqlColumn<String> filePath = extractView.filePath;
    public static final SqlColumn<String> schemaPath = extractView.schemaPath;
    public static final SqlColumn<BigInteger> sizeInByte = extractView.sizeInByte;
    public static final SqlColumn<String> compressionType = extractView.compressionType;
    public static final SqlColumn<String> collection = extractView.collection;
    public static final SqlColumn<String> partitioner = extractView.partitioner;
    public static final SqlColumn<String> databaseName = extractView.databaseName;
    public static final SqlColumn<String> tableName = extractView.tableName;
    public static final SqlColumn<String> schemaName = extractView.schemaName;
    public static final SqlColumn<String> sqlText = extractView.sqlText;
    public static final SqlColumn<String> kafkaConsumerTopic = extractView.kafkaConsumerTopic;
    public static final SqlColumn<String> kafkaConsumerGroup = extractView.kafkaConsumerGroup;
    public static final SqlColumn<String> kafkaStrtOffset = extractView.kafkaStrtOffset;
    public static final SqlColumn<String> kafkaMaxOffset = extractView.kafkaMaxOffset;
    public static final SqlColumn<Integer> kafkaPollTimeout = extractView.kafkaPollTimeout;
    public static final SqlColumn<String> tranctnlCnsumrFlg = extractView.tranctnlCnsumrFlg;
    public static final SqlColumn<String> watrmrkDuration = extractView.watrmrkDuration;
    public static final SqlColumn<String> stgFormt = extractView.stgFormt;
    public static final SqlColumn<String> stgPath = extractView.stgPath;
    public static final SqlColumn<String> stgPartitions = extractView.stgPartitions;
    public static final SqlColumn<String> dataSourceType = extractView.dataSourceType;
    public static final SqlColumn<String> dataSourceSubType = extractView.dataSourceSubType;
    public static final SqlColumn<String> authenticationType = extractView.authenticationType;
    public static final SqlColumn<String> authenticationData = extractView.authenticationData;
    public static final SqlColumn<String> connectionMetadata = extractView.connectionMetadata;
    public static final SqlColumn<String> orgTypeName = extractView.orgTypeName;
    public static final SqlColumn<String> parentOrgName = extractView.parentOrgName;
    public static final SqlColumn<String> cooOwner = extractView.cooOwner;
    public static final SqlColumn<String> opsLead = extractView.opsLead;
    public static final SqlColumn<String> techLead = extractView.techLead;
    public static final SqlColumn<String> busOwner = extractView.busOwner;
    public static final SqlColumn<String> orgDesc = extractView.orgDesc;
    public static final SqlColumn<String> orgEmail = extractView.orgEmail;
    public static final SqlColumn<String> orgCi = extractView.orgCi;
    
    public static class ExtractViewEntity extends SqlTable {

        public final SqlColumn<String> pipelineName =  column("pipeline_name", JDBCType.VARCHAR);
        public final SqlColumn<String> pipelineDescription =  column("pipeline_description", JDBCType.VARCHAR);
        public final SqlColumn<String> processMode =  column("process_mode", JDBCType.VARCHAR);
        public final SqlColumn<String> tags =  column("tags", JDBCType.VARCHAR);
        public final SqlColumn<String> orgHierName =  column("org_hier_name", JDBCType.VARCHAR);
        public final SqlColumn<Integer>  sequenceNumber =  column("sequence_number", JDBCType.INTEGER);
        public final SqlColumn<String> extractSourceType =  column("extract_source_type", JDBCType.VARCHAR);
        public final SqlColumn<String> extractSourceSubType =  column("extract_source_sub_type", JDBCType.VARCHAR);
        public final SqlColumn<String> dataSourceConnectionName =  column("data_source_connection_name", JDBCType.VARCHAR);
        public final SqlColumn<String> sourceConfiguration =  column("source_configuration", JDBCType.VARCHAR);
        public final SqlColumn<String> dataframeName =  column("dataframe_name", JDBCType.VARCHAR);
        public final SqlColumn<String> predecessorSequences =  column("predecessor_sequences", JDBCType.VARCHAR);
        public final SqlColumn<String> successorSequences =  column("successor_sequences", JDBCType.VARCHAR);
        public final SqlColumn<String> rowFilter =  column("row_filter", JDBCType.VARCHAR);
        public final SqlColumn<String> columnFilter =  column("column_filter", JDBCType.VARCHAR);
        public final SqlColumn<String> defaultReadConfig =  column("default_read_config", JDBCType.VARCHAR);
        public final SqlColumn<String> defaultWriteConfig =  column("default_write_config", JDBCType.VARCHAR);
        public final SqlColumn<String> fileName =  column("file_name", JDBCType.VARCHAR);
        public final SqlColumn<String> filePath =  column("file_path", JDBCType.VARCHAR);
        public final SqlColumn<String> schemaPath =  column("schema_path", JDBCType.VARCHAR);
        public final SqlColumn<BigInteger> sizeInByte =  column("size_in_byte", JDBCType.BIGINT);
        public final SqlColumn<String> compressionType =  column("compression_type", JDBCType.VARCHAR);
        public final SqlColumn<String> collection =  column("COLLECTION", JDBCType.VARCHAR);
        public final SqlColumn<String> partitioner =  column("PARTITIONER", JDBCType.VARCHAR);
        public final SqlColumn<String> databaseName =  column("database_name", JDBCType.VARCHAR);
        public final SqlColumn<String> tableName =  column("table_name", JDBCType.VARCHAR);
        public final SqlColumn<String> schemaName =  column("schema_name", JDBCType.VARCHAR);
        public final SqlColumn<String> sqlText =  column("sql_text", JDBCType.VARCHAR);
        public final SqlColumn<String> kafkaConsumerTopic =  column("kafka_consumer_topic", JDBCType.VARCHAR);
        public final SqlColumn<String> kafkaConsumerGroup =  column("kafka_consumer_group", JDBCType.VARCHAR);
        public final SqlColumn<String> kafkaStrtOffset =  column("kafka_strt_offset", JDBCType.VARCHAR);
        public final SqlColumn<String> kafkaMaxOffset =  column("kafka_max_offset", JDBCType.VARCHAR);
        public final SqlColumn<Integer> kafkaPollTimeout =  column("kafka_poll_timeout", JDBCType.INTEGER);
        public final SqlColumn<String> tranctnlCnsumrFlg =  column("tranctnl_cnsumr_flg", JDBCType.VARCHAR);
        public final SqlColumn<String> watrmrkDuration =  column("WATRMRK_DURATION", JDBCType.VARCHAR);
        public final SqlColumn<String> stgFormt =  column("stg_formt", JDBCType.VARCHAR);
        public final SqlColumn<String> stgPath =  column("stg_path", JDBCType.VARCHAR);
        public final SqlColumn<String> stgPartitions =  column("stg_partitions", JDBCType.VARCHAR);
        public final SqlColumn<String> dataSourceType =  column("data_source_type", JDBCType.VARCHAR);
        public final SqlColumn<String> dataSourceSubType =  column("data_source_sub_type", JDBCType.VARCHAR);
        public final SqlColumn<String> authenticationType =  column("authentication_type", JDBCType.VARCHAR);
        public final SqlColumn<String> authenticationData =  column("authentication_data", JDBCType.VARCHAR);
        public final SqlColumn<String> connectionMetadata =  column("connection_metadata", JDBCType.VARCHAR);
        public final SqlColumn<String> orgTypeName =  column("org_type_name", JDBCType.VARCHAR);
        public final SqlColumn<String> parentOrgName =  column("parent_org_name", JDBCType.VARCHAR);
        public final SqlColumn<String> cooOwner =  column("coo_owner", JDBCType.VARCHAR);
        public final SqlColumn<String> opsLead =  column("ops_lead", JDBCType.VARCHAR);
        public final SqlColumn<String> techLead =  column("tech_lead", JDBCType.VARCHAR);
        public final SqlColumn<String> busOwner =  column("bus_owner", JDBCType.VARCHAR);
        public final SqlColumn<String> orgDesc =  column("org_desc", JDBCType.VARCHAR);
        public final SqlColumn<String> orgEmail =  column("org_email", JDBCType.VARCHAR);
        public final SqlColumn<String> orgCi =  column("org_ci",  JDBCType.VARCHAR);
        



        public ExtractViewEntity() {
            super("EXTRACT_VIEW");
        }
    }
}

