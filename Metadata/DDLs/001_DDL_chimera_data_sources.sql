CREATE TABLE IF NOT EXISTS chimera_data_sources (
data_source_type VARCHAR(255),
data_source_sub_type VARCHAR(255),
description TEXT,
read_defaults JSON,
write_defaults JSON,
created_timestamp TIMESTAMP default CURRENT_TIMESTAMP,
created_by VARCHAR(255) default CURRENT_USER,
updated_timestamp TIMESTAMP,
updated_by VARCHAR(255),
active_flag VARCHAR(1) default 'Y' :: CHARACTER VARYING,
CONSTRAINT pk_chimera_data_sources PRIMARY KEY (data_source_type, data_source_sub_type),
CONSTRAINT check_data_source_type CHECK (data_source_type IN ('Relational', 'NoSql', 'Files', 'Stream')),
CONSTRAINT check_data_source_sub_type CHECK (
(data_source_type = 'Relational' AND data_source_sub_type IN ('Postgres', 'MySql')) or
(data_source_type = 'NoSql' AND data_source_sub_type IN ('Redis', 'Neo4j', 'Cassandra', 'Mongodb')) or
(data_source_type = 'Files' AND data_source_sub_type IN ('Csv', 'Json', 'Parquet', 'Avro', 'ORC', 'PDF')) or
(data_source_type = 'Stream' AND data_source_sub_type IN ('Kafka', 'Pulsar'))),
CONSTRAINT check_active_flag CHECK (active_flag IN ('Y', 'N'))
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