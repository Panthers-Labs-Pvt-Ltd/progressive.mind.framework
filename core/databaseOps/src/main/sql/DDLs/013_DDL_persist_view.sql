create view persist_view as
select pc.pipeline_name pipeline_name, pc.sequence_number sequence_number, pc.data_sink_type data_sink_type, pc.data_sink_sub_type data_sink_sub_type, pc.target_database_name target_database_name,
 pc.target_table_name target_table_name, pc.target_schema_name target_schema_name, pc.partition_keys partition_keys, pc.target_sql_text target_sql_text, pc.target_path target_path,
 pc.write_mode write_mode, pc.data_source_connection_name data_source_connection_name, pc.sink_configuration sink_configuration, pc.sort_columns sort_columns, pc.dedup_columns dedup_columns,
 pc.kafka_topic kafka_topic, pc.kafka_key kafka_key, pc.kafka_message kafka_message, ds.read_defaults read_defaults, ds.write_defaults write_defaults, dsc.host host, dsc.port port,
 dsc.database_name connection_database_name, dsc.schema_name connection_schema_name, dsc.authentication_type authentication_type, dsc.user_name user_name, dsc.user_password user_password,
 dsc.cloud_provider cloud_provider, dsc.secret_name secret_name, dsc.gcp_project_id gcp_project_id, dsc.azure_key_vault_url azure_key_vault_url, dsc.role role, dsc.warehouse warehouse,
 dsc.keytab keytab, dsc.principal principal, dsc.sslcert sslcert, dsc.sslkey sslkey, dsc.sslrootcert sslrootcert, dsc.token token, dsc.kafka_broker kafka_broker, dsc.kafka_key_password kafka_key_password,
 dsc.kafka_keystore_location kafka_keystore_location, dsc.kafka_keystore_password kafka_keystore_password, dsc.kafka_keystore_type kafka_keystore_type, dsc.kafka_truststore_location kafka_truststore_location,
 dsc.kafka_truststore_password kafka_truststore_password, dsc.kafka_truststore_type kafka_truststore_type
  from persist_config pc left join data_sources ds on pc.data_sink_type = ds.data_source_type and pc.data_sink_sub_type = ds.data_source_sub_type
  left join data_sources_connections dsc on pc.data_source_connection_name = dsc.data_source_connection_name;