CREATE TABLE IF NOT EXISTS chimera_data_sources (
data_source_type varchar(255),
data_source_sub_type varchar(255),
description text,
read_defaults JSON,
write_defaults JSON,
created_timestamp timestamp default CURRENT_TIMESTAMP,
created_by varchar(255) default CURRENT_USER,
updated_timestamp timestamp,
updated_by varchar(255),
active_flag varchar(1) default 'Y' :: CHARACTER VARYING,
CONSTRAINT chimera_data_sources_pk PRIMARY KEY (data_source_type, data_source_sub_type)
);

COMMENT ON COLUMN chimera_data_sources.data_source_type IS 'Type of the data source supported';
COMMENT ON COLUMN chimera_data_sources.data_source_sub_type IS 'Type of the database or File format.';
COMMENT ON COLUMN chimera_data_sources.description IS 'Description of the data source';
COMMENT ON COLUMN chimera_data_sources.read_defaults IS 'A Json String - with default options to be used while reading / extracting.';
COMMENT ON COLUMN chimera_data_sources.write_defaults IS ' A Json String - with default options to be used while writing/ persisting.';
COMMENT ON COLUMN chimera_data_sources.created_timestamp IS 'record creation timestamp.';
COMMENT ON COLUMN chimera_data_sources.created_by IS 'record created by.';
COMMENT ON COLUMN chimera_data_sources.updated_timestamp IS 'record updation timestamp.';
COMMENT ON COLUMN chimera_data_sources.updated_by IS 'record updated by.';
COMMENT ON COLUMN chimera_data_sources.active_flag IS 'Denotes if the record is active or not.';

CREATE TABLE IF NOT EXISTS chimera_data_sources_connections (
    data_source_connection_name VARCHAR(255) NOT NULL,
    data_source_type VARCHAR (255) NOT NULL,
    data_source_sub_type VARCHAR(255) NOT NULL,
    host VARCHAR(500),
    port INTEGER,
    database_name VARCHAR(255),
    schema_name VARCHAR(255),
    authentication_type VARCHAR(255) NOT NULL,
    user_name VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255),
    warehouse VARCHAR(255),
    principal TEXT,
    keytab TEXT,
    sslcert TEXT,
    sslkey TEXT,
    sslrootcert TEXT,
    token TEXT,
    kafka_broker TEXT,
    kafka_keystore_type VARCHAR(255),
    kafka_keystore_location TEXT,
    kafka_keystore_password TEXT,
    kafka_truststore_type VARCHAR(255),
    kafka_truststore_location TEXT,
    kafka_truststore_password TEXT,
    kafka_key_password TEXT,
    created_timestamp timestamp default CURRENT_TIMESTAMP,
    created_by varchar(255) default CURRENT_USER,
    updated_timestamp timestamp,
    updated_by varchar(255),
    active_flag VARCHAR(1) default 'Y' :: CHARACTER VARYING,
    CONSTRAINT pk_data_source_connections PRIMARY KEY (data_source_connection_name),
    CONSTRAINT fk_data_source_type FOREIGN KEY (data_source_type, data_source_sub_type) REFERENCES chimera_data_sources (data_source_type, data_source_sub_type)
    ON delete CASCADE ON update CASCADE
);

COMMENT ON COLUMN chimera_data_sources_connections.data_source_connection_name IS 'Name of the data source connection';
COMMENT ON COLUMN chimera_data_sources_connections.data_source_type IS 'Type of the data source supported. Referenced from chimera_data_sources table.';
COMMENT ON COLUMN chimera_data_sources_connections.data_source_sub_type IS 'Type of the database or File format. Referenced from chimera_data_sources table.';
COMMENT ON COLUMN chimera_data_sources_connections.host IS 'Host name of the source to establish the connection';
COMMENT ON COLUMN chimera_data_sources_connections.port IS 'Port of the source to establish the connection';
COMMENT ON COLUMN chimera_data_sources_connections.database_name IS 'Name of the database to establish the connection';
COMMENT ON COLUMN chimera_data_sources_connections.schema_name IS 'Name of the schema to establish the connection';
COMMENT ON COLUMN chimera_data_sources_connections.authentication_type IS 'Type of Authentication to be used for establishing connectivity. Valid Values: Username & Password, SSL, Kerberos';
COMMENT ON COLUMN chimera_data_sources_connections.user_name IS 'Username to be used in authentication while connecting to the source';
COMMENT ON COLUMN chimera_data_sources_connections.password IS 'Password to be used in authentication while connecting to the source';
COMMENT ON COLUMN chimera_data_sources_connections.role IS 'Role to be used while establishing the connectivity';
COMMENT ON COLUMN chimera_data_sources_connections.warehouse IS 'Warehouse with which connectivity needs to be established';
COMMENT ON COLUMN chimera_data_sources_connections.principal IS 'Used for Kerberos Authentication.';
COMMENT ON COLUMN chimera_data_sources_connections.keytab IS 'Used for Kerberos Authentication.';
COMMENT ON COLUMN chimera_data_sources_connections.sslcert IS 'Used for SSL Authentication.';
COMMENT ON COLUMN chimera_data_sources_connections.sslkey IS 'Used for SSL Authentication.';
COMMENT ON COLUMN chimera_data_sources_connections.sslrootcert IS 'Used for SSL Authentication.';
COMMENT ON COLUMN chimera_data_sources_connections.token IS 'Used for token Authentication.';
COMMENT ON COLUMN chimera_data_sources_connections.kafka_broker IS 'Kafka Broker details. Used if the data_source_type is Stream and data_source_sub_type is kafka.';
COMMENT ON COLUMN chimera_data_sources_connections.kafka_keystore_type IS 'Type of the keystore used to establish connectivity with Kafka';
COMMENT ON COLUMN chimera_data_sources_connections.kafka_keystore_location IS 'Location / Path of the keystore';
COMMENT ON COLUMN chimera_data_sources_connections.kafka_keystore_password IS 'Password for the keystore if any';
COMMENT ON COLUMN chimera_data_sources_connections.kafka_truststore_type IS 'Type of the truststore used to establish connectivity with Kafka';
COMMENT ON COLUMN chimera_data_sources_connections.kafka_truststore_location IS 'Location / Path of the truststore';
COMMENT ON COLUMN chimera_data_sources_connections.kafka_truststore_password IS 'Password for the truststore if any';
COMMENT ON COLUMN chimera_data_sources_connections.kafka_key_password IS 'Password if any';
COMMENT ON COLUMN chimera_data_sources_connections.created_timestamp IS 'record creation timestamp.';
COMMENT ON COLUMN chimera_data_sources_connections.created_by IS 'record created by.';
COMMENT ON COLUMN chimera_data_sources_connections.updated_timestamp IS 'record updation timestamp.';
COMMENT ON COLUMN chimera_data_sources_connections.updated_by IS 'record updated by.';
COMMENT ON COLUMN chimera_data_sources_connections.active_flag IS 'Denotes if the record is active or not.';


CREATE TABLE IF NOT EXISTS chimera_pipeline (
    pipeline_name VARCHAR(500) NOT NULL,
    pipeline_description TEXT,
    process_mode VARCHAR (255) NOT NULL CHECK (process_mode IN ('Batch', 'Stream')),
    run_frequency VARCHAR (255),
    created_timestamp timestamp default CURRENT_TIMESTAMP,
    created_by varchar(255) default CURRENT_USER, -- record created by.
    updated_timestamp timestamp, -- record updation timestamp.
    updated_by varchar(255), -- record updated by.
    active_flag VARCHAR(1) default 'Y' :: CHARACTER VARYING,
    CONSTRAINT pk_chimera_data_sources PRIMARY KEY (pipeline_name)
);

COMMENT ON COLUMN chimera_pipeline.pipeline_name IS 'Name of the pipeline';
COMMENT ON COLUMN chimera_pipeline.pipeline_description IS 'A brief description of the pipeline';
COMMENT ON COLUMN chimera_pipeline.process_mode IS 'Mode - Batch or Streaming. Valid Values: Batch, Stream';
COMMENT ON COLUMN chimera_pipeline.run_frequency IS 'Trigger Frequency of the pipeline - Time Based / Event Based';
COMMENT ON COLUMN chimera_pipeline.created_timestamp IS 'record creation timestamp.';
COMMENT ON COLUMN chimera_pipeline.created_by IS 'record created by.';
COMMENT ON COLUMN chimera_pipeline.updated_timestamp IS 'record updation timestamp.';
COMMENT ON COLUMN chimera_pipeline.updated_by IS 'record updated by.';
COMMENT ON COLUMN chimera_pipeline.active_flag IS 'Pipeline Active or not. Valid Values: Y, N';

CREATE TABLE IF NOT EXISTS chimera_extract_config (
    unique_id SERIAL PRIMARY KEY,
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
    source_configuration JSON,
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
    CONSTRAINT fk_pipeline_name FOREIGN KEY (pipeline_name) REFERENCES chimera_pipeline (pipeline_name)
     ON delete CASCADE ON update CASCADE,
    CONSTRAINT fk_data_source_type FOREIGN KEY (data_source_type, data_source_sub_type) REFERENCES chimera_data_sources (data_source_type, data_source_sub_type)
     ON delete CASCADE ON update CASCADE,
    CONSTRAINT fk_data_source_connection FOREIGN KEY (data_source_connection_name) REFERENCES chimera_data_sources_connections (data_source_connection_name)
     ON delete CASCADE ON update CASCADE
);

COMMENT ON COLUMN chimera_extract_config.unique_id IS 'Unique ID for each source';
COMMENT ON COLUMN chimera_extract_config.pipeline_name IS 'Name of the pipeline. This needs to be defined in chimera_pipeline';
COMMENT ON COLUMN chimera_extract_config.sequence_number IS 'Sequence No. If a pipeline has multiple sources, this field can be used to sequence the sources';
COMMENT ON COLUMN chimera_extract_config.data_source_type IS 'Type of the data source supported. Referenced from chimera_data_sources table';
COMMENT ON COLUMN chimera_extract_config.data_source_sub_type IS 'Type of the database or File format. Referenced from chimera_data_sources table';
COMMENT ON COLUMN chimera_extract_config.file_name IS 'Name of the File for data_source_type - File';
COMMENT ON COLUMN chimera_extract_config.file_path IS 'Path where the file is present for data_source_type - File';
COMMENT ON COLUMN chimera_extract_config.schema_path IS 'Path of the input schema';
COMMENT ON COLUMN chimera_extract_config.row_filter IS 'Condition to be applied to select the rows';
COMMENT ON COLUMN chimera_extract_config.column_filter IS 'Condition to be applied to select the columns';
COMMENT ON COLUMN chimera_extract_config.extract_dataframe_name IS 'Name of the dataframe which holds the input data';
COMMENT ON COLUMN chimera_extract_config.source_configuration IS 'A JSON String - with config values. It will override the read_defaults in chimera_data_sources table';
COMMENT ON COLUMN chimera_extract_config.table_name IS 'The input table name in case of data_source_type - Database';
COMMENT ON COLUMN chimera_extract_config.schema_name IS 'The schema name in case of data_source_type - Database';
COMMENT ON COLUMN chimera_extract_config.sql_text IS 'The SQL to extract the data in case of data_source_type - Database';
COMMENT ON COLUMN chimera_extract_config.kafka_consumer_topic IS 'Kafka topic in case of data_source_sub_type - Kafka';
COMMENT ON COLUMN chimera_extract_config.kafka_consumer_group IS 'Kafka Consumer group in case of data_source_sub_type - Kafka';
COMMENT ON COLUMN chimera_extract_config.kafka_start_offset IS 'Starting offset for the consumer in case of data_source_sub_type - Kafka';
COMMENT ON COLUMN chimera_extract_config.data_source_connection_name IS 'The data source connection defined in chimera_data_sources_connection';
COMMENT ON COLUMN chimera_extract_config.created_timestamp IS 'Record Creation Timestamp';
COMMENT ON COLUMN chimera_extract_config.created_by IS 'Record Created By';
COMMENT ON COLUMN chimera_extract_config.updated_timestamp IS 'Record updated by';
COMMENT ON COLUMN chimera_extract_config.updated_by IS 'Record updation timestamp';
COMMENT ON COLUMN chimera_extract_config.active_flag IS 'Denotes if the record is active or not. Valid Values: Y, N';

CREATE TABLE IF NOT EXISTS chimera_transform_config (
    unique_id SERIAL PRIMARY KEY,
    pipeline_name VARCHAR(500) NOT NULL,
    sequence_number INTEGER,
    sql_text TEXT NOT NULL,
    transform_dataframe_name VARCHAR(255) NOT NULL,
    created_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) DEFAULT CURRENT_USER,
    updated_timestamp TIMESTAMP,
    updated_by VARCHAR(255),
    active_flag VARCHAR(1) default 'Y' :: CHARACTER VARYING,
    CONSTRAINT fk_pipeline_name FOREIGN KEY (pipeline_name) REFERENCES chimera_pipeline (pipeline_name)
     ON delete CASCADE ON update CASCADE
);

COMMENT ON COLUMN chimera_transform_config.unique_id IS 'Unique ID for each transformation';
COMMENT ON COLUMN chimera_transform_config.pipeline_name IS 'Name of the pipeline. This needs to be defined in chimera_pipeline';
COMMENT ON COLUMN chimera_transform_config.sequence_number IS 'Sequence No. If a pipeline has multiple sources, this field can be used to sequence the sources';
COMMENT ON COLUMN chimera_transform_config.sql_text IS 'The SQL Text to do the transformation';
COMMENT ON COLUMN chimera_transform_config.transform_dataframe_name IS 'Name of the dataframe to be created as output of the transformation';
COMMENT ON COLUMN chimera_transform_config.created_timestamp IS 'Record Creation Timestamp';
COMMENT ON COLUMN chimera_transform_config.created_by IS 'Record Created By';
COMMENT ON COLUMN chimera_transform_config.updated_timestamp IS 'Record updated by';
COMMENT ON COLUMN chimera_transform_config.updated_by IS 'Record updation timestamp';
COMMENT ON COLUMN chimera_transform_config.active_flag IS 'Denotes if the record is active or not. Valid Values: Y, N';

CREATE TABLE IF NOT EXISTS chimera_persist_config (
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
    sink_configuration JSON,
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
    CONSTRAINT fk_pipeline_name FOREIGN KEY (pipeline_name) REFERENCES chimera_pipeline (pipeline_name)
     ON delete CASCADE ON update CASCADE,
    CONSTRAINT fk_data_sink_type FOREIGN KEY (data_sink_type, data_sink_sub_type) REFERENCES chimera_data_sources (data_source_type, data_source_sub_type)
     ON delete CASCADE ON update CASCADE,
    CONSTRAINT fk_data_source_connection FOREIGN KEY (data_source_connection_name) REFERENCES chimera_data_sources_connections (data_source_connection_name)
     ON delete CASCADE ON update CASCADE
);

COMMENT ON COLUMN chimera_persist_config.unique_id IS 'Unique ID for each sink';
COMMENT ON COLUMN chimera_persist_config.pipeline_name IS 'Name of the pipeline. This needs to be defined in chimera_pipeline';
COMMENT ON COLUMN chimera_persist_config.sequence_number IS 'Sequence No. If a pipeline has multiple sinks, this field can be used to sequence the sinks';
COMMENT ON COLUMN chimera_persist_config.data_sink_type IS 'Type of the data sink. Referenced from chimera_data_sources';
COMMENT ON COLUMN chimera_persist_config.data_sink_sub_type IS 'Type of the database or File format. Referenced from chimera_data_sources table';
COMMENT ON COLUMN chimera_persist_config.target_database_name IS 'Name of the target Database';
COMMENT ON COLUMN chimera_persist_config.target_table_name IS 'Name of the target Table';
COMMENT ON COLUMN chimera_persist_config.target_schema_name IS 'Name of the target Schema';
COMMENT ON COLUMN chimera_persist_config.partition_keys IS 'Target Partition Names';
COMMENT ON COLUMN chimera_persist_config.target_sql_text IS 'SQL text to generate the final df which would be persisted';
COMMENT ON COLUMN chimera_persist_config.target_path IS 'Target Path where the files would be persisted, in case, the data_source_type is File';
COMMENT ON COLUMN chimera_persist_config.write_mode IS 'Mode in which the data should be loaded. Valid Values: Overwrite, Append';
COMMENT ON COLUMN chimera_persist_config.data_source_connection_name IS 'The data source connection defined in chimera_data_sources_connection';
COMMENT ON COLUMN chimera_persist_config.sink_configuration IS 'A JSON String - with config options. It will override the write_defaults in chimera_data_sources table';
COMMENT ON COLUMN chimera_persist_config.sort_columns IS 'Column names to be sorted before persisting';
COMMENT ON COLUMN chimera_persist_config.dedup_columns IS 'Column names on the basis of which deduplication will happen';
COMMENT ON COLUMN chimera_persist_config.kafka_topic IS 'Name of the kafka topic on which the event is to be produced. This field is used in case data_source_type is Stream and sub-type is Kafka';
COMMENT ON COLUMN chimera_persist_config.kafka_key IS 'JSON string to be published as the key on the kafka topic. This field is used in case data_source_type is Stream and sub-type is Kafka';
COMMENT ON COLUMN chimera_persist_config.kafka_message IS 'JSON string to be published as the message on the kafka topic. This field is used in case data_source_type is Stream and sub-type is Kafka';
COMMENT ON COLUMN chimera_persist_config.created_timestamp IS 'Record Creation Timestamp';
COMMENT ON COLUMN chimera_persist_config.created_by IS 'Record Created By';
COMMENT ON COLUMN chimera_persist_config.updated_timestamp IS 'Record updated by';
COMMENT ON COLUMN chimera_persist_config.updated_by IS 'Record updation timestamp';
COMMENT ON COLUMN chimera_persist_config.active_flag IS 'Denotes if the record is active or not. Valid Values: Y, N';