CREATE TABLE IF NOT EXISTS NOSQL_EXTRACT_METADATA_CONFIG (
  CONNECTION_STRING         TEXT,
  COLLECTION                VARCHAR(4000),
  SOURCE_CONFIGURATION 	    TEXT,
  PARTITIONER               VARCHAR(255)
  ) INHERITS (EXTRACT_METADATA_CONFIG);

  INSERT INTO NOSQL_EXTRACT_METADATA_CONFIG (PIPELINE_NAME, SEQUENCE_NUMBER, EXTRACT_SOURCE_TYPE, EXTRACT_SOURCE_SUB_TYPE, CONNECTION_STRING, COLLECTION, SOURCE_CONFIGURATION, PARTITIONER, dataframe_name, PREDECESSOR_SEQUENCES, SUCCESSOR_SEQUENCES, ROW_FILTER, COLUMN_FILTER, created_by, updated_timestamp, updated_by) VALUES
  ('Test_Pipeline', 1, 'NoSql', 'Mongodb', 'mongodb://localhost:27017', 'myCollection', '{"key": "value"}', 'defaultPartitioner', 'inpur_df', 0, 2, null, null, 'admin', null, null);


  select * from nosql_extract_metadata_config;



