-- Test Case 1.1: Insert a valid record with unique key
INSERT INTO data_sources_connections (data_source_connection_name, data_source_type, data_source_sub_type, authentication_type, user_name, user_password)
VALUES ('conn2', 'Relational', 'Postgres', 'Username&Password', 'sample_user', 'sample_password');

-- Test Case 1.2: Duplicate primary key (should fail)
INSERT INTO data_sources_connections (data_source_connection_name, data_source_type, data_source_sub_type, authentication_type)
VALUES ('conn2', 'Relational', 'Postgres', 'kerberos');

-- Test Case 2.1: Valid foreign key
INSERT INTO data_sources_connections (data_source_connection_name, data_source_type, data_source_sub_type, authentication_type)
VALUES ('conn3', 'NoSql', 'Redis', 'kerberos');

-- Test Case 2.2: Invalid foreign key (should fail)
INSERT INTO data_sources_connections (data_source_connection_name, data_source_type, data_source_sub_type, authentication_type)
VALUES ('conn4', 'InvalidType', 'InvalidSubType', 'token');

-- Test Case 2.3: Delete parent record and check cascading delete
DELETE FROM data_sources WHERE data_source_type = 'Relational' AND data_source_sub_type = 'Postgres';


-- Test Case 3.1: Valid authentication type
INSERT INTO data_sources_connections (data_source_connection_name, data_source_type, data_source_sub_type, authentication_type)
VALUES ('conn4', 'Stream', 'Kafka', 'token');

-- Test Case 3.2: Invalid authentication type (should fail)
INSERT INTO data_sources_connections (data_source_connection_name, data_source_type, data_source_sub_type, authentication_type)
VALUES ('conn5', 'Stream', 'Kafka', 'InvalidAuth');

-- Test Case 3.3: Valid Username&Password authentication
INSERT INTO data_sources_connections (data_source_connection_name, data_source_type, data_source_sub_type, authentication_type, user_name, user_password)
VALUES ('conn6', 'Files', 'Csv', 'Username&Password', 'admin', 'password');

-- Test Case 3.4: Missing user_name or user_password for Username&Password (should fail)
INSERT INTO data_sources_connections (data_source_connection_name, data_source_type, data_source_sub_type, authentication_type)
VALUES ('conn7', 'Files', 'Json', 'Username&Password');

-- Test Case 3.5: Valid active flag
INSERT INTO data_sources_connections (data_source_connection_name, data_source_type, data_source_sub_type, authentication_type, active_flag)
VALUES ('conn8', 'Stream', 'Pulsar', 'token', 'N');

-- Test Case 3.6: Invalid active flag (should fail)
INSERT INTO data_sources_connections (data_source_connection_name, data_source_type, data_source_sub_type, authentication_type, active_flag)
VALUES ('conn9', 'NoSql', 'MongoDb', 'kerberos', 'InvalidFlag');


-- Test Case 4.1: Omit default columns
INSERT INTO data_sources_connections (data_source_connection_name, data_source_type, data_source_sub_type, authentication_type)
VALUES ('conn10', 'Files', 'Parquet', 'kerberos');
SELECT * FROM data_sources_connections WHERE data_source_connection_name = 'conn10';

-- Test Case 4.2: Explicitly set default columns
INSERT INTO data_sources_connections (data_source_connection_name, data_source_type, data_source_sub_type, authentication_type, created_by, active_flag)
VALUES ('conn11', 'Files', 'ORC', 'token', 'TestUser', 'N');

-- Test Case 5.1: Optional fields omitted
INSERT INTO data_sources_connections (data_source_connection_name, data_source_type, data_source_sub_type, authentication_type)
VALUES ('conn12', 'NoSql', 'Cassandra', 'kerberos');

-- Test Case 5.2: All optional fields populated
INSERT INTO data_sources_connections (
    data_source_connection_name, data_source_type, data_source_sub_type, authentication_type, host, port, database_name, schema_name,
    user_name, user_password, sslcert, sslkey, sslrootcert
)
VALUES (
    'conn13', 'Relational', 'MySql', 'Username&Password', 'localhost', 3306, 'test_db', 'public',
    'test_user', 'test_password', 'cert.pem', 'key.pem', 'rootcert.pem'
);

truncate data_sources_connections cascade;




