CREATE TABLE IF NOT EXISTS extract_config (
    pipeline_name VARCHAR(500) NOT NULL,
    sequence_number INTEGER,
    data_source_type VARCHAR(255) NOT NULL,
    data_source_sub_type VARCHAR(255) NOT NULL,
    file_name VARCHAR(255),
    file_path TEXT,
    schema_path TEXT,
    row_filter TEXT,
    column_filter TEXT,
    extract_dataframe_name VARCHAR(255),
    source_configuration TEXT,
    table_name VARCHAR(255),
    schema_name VARCHAR(255),
    sql_text TEXT,
    kafka_consumer_topic VARCHAR(255),
    kafka_consumer_group VARCHAR(255),
    kafka_start_offset VARCHAR(255),
    data_source_connection_name VARCHAR(255),
    created_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) DEFAULT CURRENT_USER,
    updated_timestamp TIMESTAMP,
    updated_by VARCHAR(255),
    active_flag VARCHAR(1) default 'Y' :: CHARACTER VARYING,
    CONSTRAINT pk_extract_config PRIMARY KEY (pipeline_name, sequence_number),
    CONSTRAINT fk_pipeline_name FOREIGN KEY (pipeline_name) REFERENCES data_pipelines (pipeline_name)
     ON delete CASCADE ON update CASCADE,
    CONSTRAINT fk_data_source_type FOREIGN KEY (data_source_type, data_source_sub_type) REFERENCES data_sources (data_source_type, data_source_sub_type)
     ON delete CASCADE ON update CASCADE,
    CONSTRAINT fk_data_source_connection FOREIGN KEY (data_source_connection_name) REFERENCES data_sources_connections (data_source_connection_name)
     ON delete CASCADE ON update CASCADE
);

COMMENT ON COLUMN extract_config.pipeline_name IS 'Name of the pipeline. This needs to be defined in pipeline';
COMMENT ON COLUMN extract_config.sequence_number IS 'Sequence No. If a pipeline has multiple sources, this field can be used to sequence the sources';
COMMENT ON COLUMN extract_config.data_source_type IS 'Type of the data source supported. Referenced from data_sources table';
COMMENT ON COLUMN extract_config.data_source_sub_type IS 'Type of the database or File format. Referenced from data_sources table';
COMMENT ON COLUMN extract_config.file_name IS 'Name of the File for data_source_type - File';
COMMENT ON COLUMN extract_config.file_path IS 'Path where the file is present for data_source_type - File';
COMMENT ON COLUMN extract_config.schema_path IS 'Path of the input schema';
COMMENT ON COLUMN extract_config.row_filter IS 'Condition to be applied to select the rows';
COMMENT ON COLUMN extract_config.column_filter IS 'Condition to be applied to select the columns';
COMMENT ON COLUMN extract_config.extract_dataframe_name IS 'Name of the dataframe which holds the input data';
COMMENT ON COLUMN extract_config.source_configuration IS 'A JSON String - with config values. It will override the read_defaults in data_sources table';
COMMENT ON COLUMN extract_config.table_name IS 'The input table name in case of data_source_type - Database';
COMMENT ON COLUMN extract_config.schema_name IS 'The schema name in case of data_source_type - Database';
COMMENT ON COLUMN extract_config.sql_text IS 'The SQL to extract the data in case of data_source_type - Database';
COMMENT ON COLUMN extract_config.kafka_consumer_topic IS 'Kafka topic in case of data_source_sub_type - Kafka';
COMMENT ON COLUMN extract_config.kafka_consumer_group IS 'Kafka Consumer group in case of data_source_sub_type - Kafka';
COMMENT ON COLUMN extract_config.kafka_start_offset IS 'Starting offset for the consumer in case of data_source_sub_type - Kafka';
COMMENT ON COLUMN extract_config.data_source_connection_name IS 'The data source connection defined in data_sources_connection';
COMMENT ON COLUMN extract_config.created_timestamp IS 'Record Creation Timestamp';
COMMENT ON COLUMN extract_config.created_by IS 'Record Created By';
COMMENT ON COLUMN extract_config.updated_timestamp IS 'Record updated by';
COMMENT ON COLUMN extract_config.updated_by IS 'Record updation timestamp';
COMMENT ON COLUMN extract_config.active_flag IS 'Denotes if the record is active or not. Valid Values: Y, N';
