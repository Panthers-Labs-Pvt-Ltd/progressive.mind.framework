
CREATE TABLE IF NOT EXISTS transform_config (
    unique_id SERIAL PRIMARY KEY,
    pipeline_name VARCHAR(500) NOT NULL,
    sequence_number INTEGER,
    sql_text TEXT NOT NULL,
    transform_dataframe_name VARCHAR(255) NOT NULL,
    created_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) DEFAULT CURRENT_USER,
    updated_timestamp TIMESTAMP,
    updated_by VARCHAR(255),
    active_flag VARCHAR(1) default 'Y' :: CHARACTER VARYING,
    CONSTRAINT fk_pipeline_name FOREIGN KEY (pipeline_name) REFERENCES data_pipelines (pipeline_name)
     ON delete CASCADE ON update CASCADE
);
COMMENT ON COLUMN transform_config.unique_id IS 'Unique ID for each transformation';
COMMENT ON COLUMN transform_config.pipeline_name IS 'Name of the pipeline. This needs to be defined in pipeline';
COMMENT ON COLUMN transform_config.sequence_number IS 'Sequence No. If a pipeline has multiple sources, this field can be used to sequence the sources';
COMMENT ON COLUMN transform_config.sql_text IS 'The SQL Text to do the transformation';
COMMENT ON COLUMN transform_config.transform_dataframe_name IS 'Name of the dataframe to be created as output of the transformation';
COMMENT ON COLUMN transform_config.created_timestamp IS 'Record Creation Timestamp';
COMMENT ON COLUMN transform_config.created_by IS 'Record Created By';
COMMENT ON COLUMN transform_config.updated_timestamp IS 'Record updated by';
COMMENT ON COLUMN transform_config.updated_by IS 'Record updation timestamp';
COMMENT ON COLUMN transform_config.active_flag IS 'Denotes if the record is active or not. Valid Values: Y, N';
