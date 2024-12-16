insert into data_sources (data_source_type, data_source_sub_type, description, read_defaults, write_defaults) values
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