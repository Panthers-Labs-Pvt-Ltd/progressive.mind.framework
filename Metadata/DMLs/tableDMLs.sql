insert into chimera_data_sources (data_source_type, data_source_sub_type, description, read_defaults, write_defaults) values
('Relational', 'Postgres', 'Postgres Database', null, null),
('Relational', 'MYSQL', 'MYSQL Database', null, null),
('File', 'Parquet', 'File Format Parquet', null, null),
('File', 'Avro', 'File Format Avro', null, null),
('File', 'CSV', 'File Format CSV', null, null),
('File', 'Json', 'File Format Json', null, null),
('File', 'ORC', 'File Format ORC', null, null),
('File', 'PDF', 'File Format PDF', null, null),
('NoSql', 'Neo4j', 'Neqo4J DB', null, null),
('NoSql', 'Redis', 'Redis DB', null, null),
('NoSql', 'Cassandra', 'Cassandra DB', null, null),
('NoSql', 'Mongodb', 'Mongo DB', null, null),
('Stream', 'Kafka', 'Streaming - Kafka', null, null),
('Stream', 'Pulsar', 'Streaming - Pulsar', null, null)
ON CONFLICT DO NOTHING;


insert into chimera_data_sources_connections (data_source_connection_name, data_source_type, data_source_sub_type, database_name, schema_name, authentication_type, host, port, user_name, password, role, warehouse, keytab, principal, sslcert, sslkey, sslrootcert, token,
kafka_broker, kafka_key_password, kafka_keystore_location, kafka_keystore_password, kafka_keystore_type, kafka_truststore_location, kafka_truststore_password, kafka_truststore_type) values
('Local Postgres connection - chimera', 'Relational', 'Postgres', 'chimera_db', null, 'Username & Password', 'localhost', 5432, 'chimera', 'chimera123', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
ON CONFLICT DO NOTHING;
