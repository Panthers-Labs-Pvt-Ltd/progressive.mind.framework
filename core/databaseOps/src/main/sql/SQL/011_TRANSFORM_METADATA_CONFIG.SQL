
CREATE TABLE IF NOT EXISTS transform_metadata_config (
    TRANSFORM_ID UUID DEFAULT GEN_RANDOM_UUID() PRIMARY KEY,
    SEQUENCE_NUMBER INTEGER,
 	PIPELINE_ID UUID NOT NULL,
 	sql_text TEXT NOT NULL,
    transform_dataframe_name VARCHAR(255) NOT NULL,
    created_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) DEFAULT CURRENT_USER,
    updated_timestamp TIMESTAMP,
    updated_by VARCHAR(255),
    active_flag VARCHAR(1) default 'Y' :: CHARACTER VARYING,
    CONSTRAINT pk_transform_metadata_config UNIQUE (PIPELINE_ID, sequence_number),
    CONSTRAINT fk_pipeline_name FOREIGN KEY (PIPELINE_ID) REFERENCES data_pipelines (PIPELINE_ID)
     ON delete CASCADE ON update CASCADE
);
COMMENT ON COLUMN transform_metadata_config.sequence_number IS 'Sequence No. If a pipeline has multiple sources, this field can be used to sequence the sources';
COMMENT ON COLUMN transform_metadata_config.sql_text IS 'The SQL Text to do the transformation';
COMMENT ON COLUMN transform_metadata_config.transform_dataframe_name IS 'Name of the dataframe to be created as output of the transformation';
COMMENT ON COLUMN transform_metadata_config.created_timestamp IS 'Record Creation Timestamp';
COMMENT ON COLUMN transform_metadata_config.created_by IS 'Record Created By';
COMMENT ON COLUMN transform_metadata_config.updated_timestamp IS 'Record updated by';
COMMENT ON COLUMN transform_metadata_config.updated_by IS 'Record updation timestamp';
COMMENT ON COLUMN transform_metadata_config.active_flag IS 'Denotes if the record is active or not. Valid Values: Y, N';
