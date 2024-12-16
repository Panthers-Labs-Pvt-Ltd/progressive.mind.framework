CREATE TABLE IF NOT EXISTS persist_config (
    unique_id SERIAL PRIMARY KEY,
    pipeline_name VARCHAR(500) NOT NULL,
    sequence_number INTEGER,
    data_sink_type VARCHAR(255) NOT NULL,
    data_sink_sub_type VARCHAR(255) NOT NULL,
    target_database_name VARCHAR(255),
    target_table_name VARCHAR(255),
    target_schema_name VARCHAR(255),
    partition_keys TEXT,
    target_sql_text TEXT,
    target_path TEXT,
    write_mode VARCHAR(255) CHECK (write_mode IN ('Overwrite', 'Append')),
    data_source_connection_name VARCHAR(255),
    sink_configuration TEXT,
    sort_columns TEXT,
    dedup_columns TEXT,
    kafka_topic VARCHAR(255),
    kafka_key TEXT,
    kafka_message TEXT,
    created_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) DEFAULT CURRENT_USER,
    updated_timestamp TIMESTAMP,
    updated_by VARCHAR(255),
    active_flag VARCHAR(1) default 'Y' :: CHARACTER VARYING,
    CONSTRAINT fk_pipeline_name FOREIGN KEY (pipeline_name) REFERENCES data_pipelines (pipeline_name) ON delete CASCADE ON update CASCADE,
    CONSTRAINT fk_data_sink_type FOREIGN KEY (data_sink_type, data_sink_sub_type) REFERENCES data_sources (data_source_type, data_source_sub_type) ON delete CASCADE ON update CASCADE,
    CONSTRAINT fk_data_source_connection FOREIGN KEY (data_source_connection_name) REFERENCES data_sources_connections (data_source_connection_name) ON delete CASCADE ON update CASCADE
);

COMMENT ON COLUMN persist_config.unique_id IS 'Unique ID for each sink';
COMMENT ON COLUMN persist_config.pipeline_name IS 'Name of the pipeline. This needs to be defined in pipeline';
COMMENT ON COLUMN persist_config.sequence_number IS 'Sequence No. If a pipeline has multiple sinks, this field can be used to sequence the sinks';
COMMENT ON COLUMN persist_config.data_sink_type IS 'Type of the data sink. Referenced from data_sources';
COMMENT ON COLUMN persist_config.data_sink_sub_type IS 'Type of the database or File format. Referenced from data_sources table';
COMMENT ON COLUMN persist_config.target_database_name IS 'Name of the target Database';
COMMENT ON COLUMN persist_config.target_table_name IS 'Name of the target Table';
COMMENT ON COLUMN persist_config.target_schema_name IS 'Name of the target Schema';
COMMENT ON COLUMN persist_config.partition_keys IS 'Target Partition Names';
COMMENT ON COLUMN persist_config.target_sql_text IS 'SQL text to generate the final df which would be persisted';
COMMENT ON COLUMN persist_config.target_path IS 'Target Path where the files would be persisted, in case, the data_source_type is File';
COMMENT ON COLUMN persist_config.write_mode IS 'Mode in which the data should be loaded. Valid Values: Overwrite, Append';
COMMENT ON COLUMN persist_config.data_source_connection_name IS 'The data source connection defined in data_sources_connection';
COMMENT ON COLUMN persist_config.sink_configuration IS 'A JSON String - with config options. It will override the write_defaults in data_sources table';
COMMENT ON COLUMN persist_config.sort_columns IS 'Column names to be sorted before persisting';
COMMENT ON COLUMN persist_config.dedup_columns IS 'Column names on the basis of which deduplication will happen';
COMMENT ON COLUMN persist_config.kafka_topic IS 'Name of the kafka topic on which the event is to be produced. This field is used in case data_source_type is Stream and sub-type is Kafka';
COMMENT ON COLUMN persist_config.kafka_key IS 'JSON string to be published as the key on the kafka topic. This field is used in case data_source_type is Stream and sub-type is Kafka';
COMMENT ON COLUMN persist_config.kafka_message IS 'JSON string to be published as the message on the kafka topic. This field is used in case data_source_type is Stream and sub-type is Kafka';
COMMENT ON COLUMN persist_config.created_timestamp IS 'Record Creation Timestamp';
COMMENT ON COLUMN persist_config.created_by IS 'Record Created By';
COMMENT ON COLUMN persist_config.updated_timestamp IS 'Record updated by';
COMMENT ON COLUMN persist_config.updated_by IS 'Record updation timestamp';
COMMENT ON COLUMN persist_config.active_flag IS 'Denotes if the record is active or not. Valid Values: Y, N';