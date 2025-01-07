DROP TABLE IF EXISTS DATA_SOURCES;
CREATE TABLE IF NOT EXISTS DATA_SOURCES (
DATA_SOURCE_TYPE        VARCHAR(255),
DATA_SOURCE_SUB_TYPE    VARCHAR(255),
DESCRIPTION             TEXT,
DATA_SOURCE_TEMPLATE    TEXT,
DEFAULT_READ_CONFIG     TEXT,
DEFAULT_WRITE_CONFIG    TEXT,
CREATED_TIMESTAMP       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
CREATED_BY              VARCHAR(255) DEFAULT CURRENT_USER,
UPDATED_TIMESTAMP       TIMESTAMP,
UPDATED_BY              VARCHAR(255),
ACTIVE_FLAG             VARCHAR(1) DEFAULT 'Y' :: CHARACTER VARYING,
CONSTRAINT pk_data_sources PRIMARY KEY (DATA_SOURCE_TYPE, DATA_SOURCE_SUB_TYPE),
CONSTRAINT check_data_sources_active_flag CHECK (ACTIVE_FLAG in ('Y', 'N'))
);

COMMENT ON COLUMN data_sources.DATA_SOURCE_TYPE IS 'Type of the data source supported';
COMMENT ON COLUMN data_sources.DATA_SOURCE_SUB_TYPE IS 'Type of the database or File format.';
COMMENT ON COLUMN data_sources.DESCRIPTION IS 'Description of the data source';
COMMENT ON COLUMN data_sources.DATA_SOURCE_TEMPLATE IS 'Connection Template for the Data Source';
COMMENT ON COLUMN data_sources.DEFAULT_READ_CONFIG IS 'A Json String - with default options to be used while reading / extracting.';
COMMENT ON COLUMN data_sources.DEFAULT_WRITE_CONFIG IS ' A Json String - with default options to be used while writing/ persisting.';
COMMENT ON COLUMN data_sources.CREATED_TIMESTAMP IS 'record creation timestamp.';
COMMENT ON COLUMN data_sources.CREATED_BY IS 'record created by.';
COMMENT ON COLUMN data_sources.UPDATED_TIMESTAMP IS 'record updation timestamp.';
COMMENT ON COLUMN data_sources.UPDATED_BY IS 'record updated by.';
COMMENT ON COLUMN data_sources.ACTIVE_FLAG IS 'Denotes if the record is active or not.';

INSERT INTO DATA_SOURCES(data_source_type, data_source_sub_type, description, data_source_template, default_read_config, default_write_config, created_by, updated_timestamp, updated_by) values
('Relational', 'Postgres', 'Postgres Database', null, null, null, 'PK', null, null),
('Relational', 'MySql', 'MYSQL Database', null, null, null, 'PK', null, null),
('Files', 'Parquet', 'File Format Parquet', null, null, null, 'PK', null, null),
('Files', 'Avro', 'File Format Avro', null, null, null, 'PK', null, null),
('Files', 'Csv', 'File Format CSV', null, null, null, 'PK', null, null),
('Files', 'Json', 'File Format Json', null, null, null, 'PK', null, null),
('Files', 'ORC', 'File Format ORC', null, null, null, 'PK', null, null),
('Files', 'PDF', 'File Format PDF', null, null, null, 'PK', null, null),
('NoSql', 'Neo4j', 'Neqo4J DB', null, null, null, 'PK', null, null),
('NoSql', 'Redis', 'Redis DB', null, null, null, 'PK', null, null),
('NoSql', 'Cassandra', 'Cassandra DB', null, null, null, 'PK', null, null),
('NoSql', 'Mongodb', 'Mongo DB', null, null, null, 'PK', null, null),
('Stream', 'Kafka', 'Streaming - Kafka', null, null, null, 'PK', null, null),
('Stream', 'Pulsar', 'Streaming - Pulsar', null, null, null, 'PK', null, null);

select * from data_sources;

