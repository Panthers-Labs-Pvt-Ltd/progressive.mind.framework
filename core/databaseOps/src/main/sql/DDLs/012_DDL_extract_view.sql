  create view extract_view as
  select ec.pipeline_name pipeline_name, ec.sequence_number sequence_number, ec.data_source_type data_source_type, ec.data_source_sub_type data_source_sub_type, ec.file_name file_name,
   ec.file_path file_path, ec.schema_path schema_path, ec.row_filter row_filter, ec.column_filter column_filter, ec.extract_dataframe_name extract_dataframe_name,
   ec.source_configuration source_configuration, ec.table_name source_table_name, ec.schema_name source_schema_name, ec.sql_text sql_text, ec.kafka_consumer_topic kafka_consumer_topic,
   ec.kafka_consumer_group kafka_consumer_group, ec.kafka_start_offset kafka_start_offset, ec.data_source_connection_name data_source_connection_name, ds.read_defaults read_defaults,
   ds.write_defaults write_defaults, dsc.host host, dsc.port port, dsc.database_name connection_database_name, dsc.schema_name connection_schema_name, dsc.authentication_type authentication_type,
   dsc.user_name user_name, dsc.user_password user_password, dsc.cloud_provider cloud_provider, dsc.secret_name secret_name, dsc.gcp_project_id gcp_project_id, dsc.azure_key_vault_url azure_key_vault_url,
   dsc.role role, dsc.warehouse warehouse, dsc.keytab keytab, dsc.principal principal, dsc.sslcert sslcert, dsc.sslkey sslkey, dsc.sslrootcert sslrootcert, dsc.token token, dsc.kafka_broker kafka_broker,
   dsc.kafka_key_password kafka_key_password, dsc.kafka_keystore_location kafka_keystore_location, dsc.kafka_keystore_password kafka_keystore_password, dsc.kafka_keystore_type kafka_keystore_type,
   dsc.kafka_truststore_location kafka_truststore_location, dsc.kafka_truststore_password kafka_truststore_password, dsc.kafka_truststore_type kafka_truststore_type
from extract_config ec left join data_sources ds on ec.data_source_type = ds.data_source_type and ec.data_source_sub_type = ds.data_source_sub_type
left join data_sources_connections dsc on ec.data_source_connection_name = dsc.data_source_connection_name;