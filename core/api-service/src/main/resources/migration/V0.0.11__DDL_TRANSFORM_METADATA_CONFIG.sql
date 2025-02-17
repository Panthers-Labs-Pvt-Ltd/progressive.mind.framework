
CREATE TABLE IF NOT EXISTS transform_metadata_config (
   PIPELINE_NAME              VARCHAR(500),
   SEQUENCE_NUMBER            INTEGER,
 	SQL_TEXT                   TEXT NOT NULL,
   TRANSFORM_DATAFRAME_NAME   VARCHAR(255) NOT NULL,
   CREATED_TIMESTAMP 			TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
	CREATED_BY 					   VARCHAR(255) DEFAULT CURRENT_USER NULL,
	UPDATED_TIMESTAMP 			TIMESTAMP NULL,
	UPDATED_BY 					   VARCHAR(255) NULL,
	ACTIVE_FLAG 				   VARCHAR(1) DEFAULT 'Y'::CHARACTER VARYING NULL,
   CONSTRAINT pk_transform_metadata_config UNIQUE (PIPELINE_NAME, sequence_number),
   CONSTRAINT fk_pipeline_name FOREIGN KEY (PIPELINE_NAME) REFERENCES data_pipelines (PIPELINE_NAME) ON delete CASCADE ON update CASCADE
);
COMMENT ON COLUMN transform_metadata_config.pipeline_name IS 'Name of the pipeline. This needs to be defined in pipeline';
COMMENT ON COLUMN transform_metadata_config.sequence_number IS 'Sequence No. If a pipeline has multiple sources, this field can be used to sequence the sources';
COMMENT ON COLUMN transform_metadata_config.sql_text IS 'The SQL Text to do the transformation';
COMMENT ON COLUMN transform_metadata_config.transform_dataframe_name IS 'Name of the dataframe to be created as output of the transformation';
COMMENT ON COLUMN transform_metadata_config.created_timestamp IS 'Record Creation Timestamp';
COMMENT ON COLUMN transform_metadata_config.created_by IS 'Record Created By';
COMMENT ON COLUMN transform_metadata_config.updated_timestamp IS 'Record updated by';
COMMENT ON COLUMN transform_metadata_config.updated_by IS 'Record updation timestamp';
COMMENT ON COLUMN transform_metadata_config.active_flag IS 'Denotes if the record is active or not. Valid Values: Y, N';


INSERT INTO transform_metadata_config (PIPELINE_NAME, SEQUENCE_NUMBER, SQL_TEXT, TRANSFORM_DATAFRAME_NAME, CREATED_BY, UPDATED_BY, UPDATED_TIMESTAMP) VALUES 
('Test_Pipeline', 1, 'SELECT * FROM data_sources', 'DF_transform', 'PK', null, null);


select * from transform_metadata_config;