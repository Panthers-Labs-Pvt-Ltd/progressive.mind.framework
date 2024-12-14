-- Test Case 1.1: Valid unique key
insert into chimera_data_sources (data_source_type, data_source_sub_type, description, read_defaults, write_defaults) values
('Relational', 'Postgres', 'Postgres Database', null, null),
('Relational', 'MySql', 'MYSQL Database', null, null),
('Files', 'Parquet', 'File Format Parquet', null, null),
('Files', 'Avro', 'File Format Avro', null, null),
('Files', 'Csv', 'File Format CSV', null, null),
('Files', 'Json', 'File Format Json', null, null),
('Files', 'ORC', 'File Format ORC', null, null),
('Files', 'PDF', 'File Format PDF', null, null),
('NoSql', 'Neo4j', 'Neqo4J DB', null, null),
('NoSql', 'Redis', 'Redis DB', null, null),
('NoSql', 'Cassandra', 'Cassandra DB', null, null),
('NoSql', 'Mongodb', 'Mongo DB', null, null),
('Stream', 'Kafka', 'Streaming - Kafka', null, null),
('Stream', 'Pulsar', 'Streaming - Pulsar', null, null)
ON CONFLICT DO NOTHING;

-- Test Case 1.2: Duplicate primary key (should fail)
INSERT INTO chimera_data_sources (data_source_type, data_source_sub_type, description)
VALUES ('Relational', 'Postgres', 'Duplicate entry for primary key');


-- Test Case 2.2: Invalid combination (should fail)
INSERT INTO chimera_data_sources (data_source_type, data_source_sub_type, description)
VALUES ('Relational', 'Kafka', 'Invalid combination of type and sub-type');


-- Test Case 3.2: Explicitly set default columns

insert into chimera_data_sources (data_source_type, data_source_sub_type, description, read_defaults, write_defaults, created_by, active_flag) values
('Relational', 'Postgres', 'Postgres Database', null, null, 'testuser', 'N'),
('Relational', 'MySql', 'Postgres Database', null, null, 'testuser', 'Y');




-- Test Case 4.1: Invalid update (should fail)
UPDATE chimera_data_sources
SET data_source_type = 'InvalidType'
WHERE data_source_type = 'Relational' AND data_source_sub_type = 'Postgres';

-- Test Case 4.2: Update timestamp and user
UPDATE chimera_data_sources
SET updated_timestamp = CURRENT_TIMESTAMP, updated_by = 'UpdatedUser'
WHERE data_source_type = 'Files' AND data_source_sub_type = 'Json';


-- Test Case 5.1: Valid active flag
INSERT INTO chimera_data_sources (data_source_type, data_source_sub_type, description, active_flag)
VALUES ('Stream', 'Pulsar', 'Pulsar streaming platform', 'N');

-- Test Case 5.2: Invalid active flag (should fail)
INSERT INTO chimera_data_sources (data_source_type, data_source_sub_type, description, active_flag)
VALUES ('Stream', 'Kafka', 'Invalid active flag', 'Invalid');


-- Test Case 6.1: Valid JSON
UPDATE chimera_data_sources
SET read_defaults = '{"host": "localhost"}',
write_defaults = '{"write_buffer": 10}' where data_source_type = 'Files' AND data_source_sub_type = 'Json';

-- Test Case 6.2: Invalid JSON (should fail)
UPDATE chimera_data_sources
SET read_defaults = '{"host": "localhost"}',
write_defaults = '{"Invalid Json"}' where data_source_type = 'Files' AND data_source_sub_type = 'Json';

truncate chimera_data_sources;


