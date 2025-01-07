CREATE TABLE IF NOT EXISTS data_sources_connections (
    data_source_connection_name VARCHAR(255) ,
    data_source_type VARCHAR (255) NOT NULL,
    data_source_sub_type VARCHAR(255) NOT NULL,
    host VARCHAR(500),
    port INTEGER,
    database_name VARCHAR(255),
    schema_name VARCHAR(255),
    authentication_type VARCHAR(255) NOT NULL,
    user_name VARCHAR(255),
    user_password VARCHAR(255),
    cloud_provider VARCHAR(255),
    secret_name VARCHAR(500),
    gcp_project_id VARCHAR(500),
    azure_key_vault_url VARCHAR(500),
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
    CONSTRAINT check_authentication_type CHECK (authentication_type IN ('Username&Password', 'Kerberos', 'Token', 'SecretManager')),
    CONSTRAINT auth_user_pass_check CHECK ((authentication_type = 'Username&Password' AND user_name IS NOT NULL AND user_password IS NOT NULL) OR authentication_type <> 'Username&Password'),
    CONSTRAINT check_active_flag CHECK (active_flag IN ('Y', 'N')),
    CONSTRAINT fk_data_source_type FOREIGN KEY (data_source_type, data_source_sub_type) REFERENCES data_sources (data_source_type, data_source_sub_type)
    ON delete CASCADE ON update CASCADE
    -- Add more checks on authentication
);


COMMENT ON COLUMN data_sources_connections.data_source_connection_name IS 'Name of the data source connection';
COMMENT ON COLUMN data_sources_connections.data_source_type IS 'Type of the data source supported. Referenced from data_sources table.';
COMMENT ON COLUMN data_sources_connections.data_source_sub_type IS 'Type of the database or File format. Referenced from data_sources table.';
COMMENT ON COLUMN data_sources_connections.host IS 'Host name of the source to establish the connection';
COMMENT ON COLUMN data_sources_connections.port IS 'Port of the source to establish the connection';
COMMENT ON COLUMN data_sources_connections.database_name IS 'Name of the database to establish the connection';
COMMENT ON COLUMN data_sources_connections.schema_name IS 'Name of the schema to establish the connection';
COMMENT ON COLUMN data_sources_connections.authentication_type IS 'Type of Authentication to be used for establishing connectivity. Valid Values: Username & Password, SSL, Kerberos';
COMMENT ON COLUMN data_sources_connections.user_name IS 'Username to be used in authentication while connecting to the source';
COMMENT ON COLUMN data_sources_connections.user_password IS 'Password to be used in authentication while connecting to the source';
COMMENT ON COLUMN data_sources_connections.cloud_provider IS 'Cloud SecretManager Service is hosted. Mandatory if authentication_type is SecretManager. Eg. AWS / GCP / Azure.';
COMMENT ON COLUMN data_sources_connections.secret_name IS 'SecretName on the secretManager. Mandatory if authentication_type is SecretManager.';
COMMENT ON COLUMN data_sources_connections.gcp_project_id IS 'GCP Project ID. Mandatory if cloud_provider is GCP.';
COMMENT ON COLUMN data_sources_connections.azure_key_vault_url IS 'URL of Azure Key Vault. Mandatory if cloud_provider id Azure.';
COMMENT ON COLUMN data_sources_connections.role IS 'Role to be used while establishing the connectivity';
COMMENT ON COLUMN data_sources_connections.warehouse IS 'Warehouse with which connectivity needs to be established';
COMMENT ON COLUMN data_sources_connections.principal IS 'Used for Kerberos Authentication.';
COMMENT ON COLUMN data_sources_connections.keytab IS 'Used for Kerberos Authentication.';
COMMENT ON COLUMN data_sources_connections.sslcert IS 'Used for SSL Authentication.';
COMMENT ON COLUMN data_sources_connections.sslkey IS 'Used for SSL Authentication.';
COMMENT ON COLUMN data_sources_connections.sslrootcert IS 'Used for SSL Authentication.';
COMMENT ON COLUMN data_sources_connections.token IS 'Used for token Authentication.';
COMMENT ON COLUMN data_sources_connections.kafka_broker IS 'Kafka Broker details. Used if the data_source_type is Stream and data_source_sub_type is kafka.';
COMMENT ON COLUMN data_sources_connections.kafka_keystore_type IS 'Type of the keystore used to establish connectivity with Kafka';
COMMENT ON COLUMN data_sources_connections.kafka_keystore_location IS 'Location / Path of the keystore';
COMMENT ON COLUMN data_sources_connections.kafka_keystore_password IS 'Password for the keystore if any';
COMMENT ON COLUMN data_sources_connections.kafka_truststore_type IS 'Type of the truststore used to establish connectivity with Kafka';
COMMENT ON COLUMN data_sources_connections.kafka_truststore_location IS 'Location / Path of the truststore';
COMMENT ON COLUMN data_sources_connections.kafka_truststore_password IS 'Password for the truststore if any';
COMMENT ON COLUMN data_sources_connections.kafka_key_password IS 'Password if any';
COMMENT ON COLUMN data_sources_connections.created_timestamp IS 'record creation timestamp.';
COMMENT ON COLUMN data_sources_connections.created_by IS 'record created by.';
COMMENT ON COLUMN data_sources_connections.updated_timestamp IS 'record updation timestamp.';
COMMENT ON COLUMN data_sources_connections.updated_by IS 'record updated by.';
COMMENT ON COLUMN data_sources_connections.active_flag IS 'Denotes if the record is active or not.';
