CREATE TABLE IF NOT EXISTS chimera_pipeline (
    pipeline_name VARCHAR(500) NOT NULL,
    pipeline_description TEXT,
    process_mode VARCHAR (255) NOT NULL CHECK (process_mode IN ('Batch', 'Stream')),
    run_frequency VARCHAR (255),
    created_timestamp timestamp default CURRENT_TIMESTAMP,
    created_by varchar(255) default CURRENT_USER,
    updated_timestamp timestamp,
    updated_by varchar(255),
    active_flag VARCHAR(1) default 'Y' :: CHARACTER VARYING,
    CONSTRAINT pk_chimera_pipeline PRIMARY KEY (pipeline_name),
    CONSTRAINT check_active_flag CHECK (active_flag IN ('Y', 'N'))
);

COMMENT ON COLUMN chimera_pipeline.pipeline_name IS 'Name of the pipeline';
COMMENT ON COLUMN chimera_pipeline.pipeline_description IS 'A brief description of the pipeline';
COMMENT ON COLUMN chimera_pipeline.process_mode IS 'Mode - Batch or Streaming. Valid Values: Batch, Stream';
COMMENT ON COLUMN chimera_pipeline.run_frequency IS 'Trigger Frequency of the pipeline - Time Based / Event Based';
COMMENT ON COLUMN chimera_pipeline.created_timestamp IS 'record creation timestamp.';
COMMENT ON COLUMN chimera_pipeline.created_by IS 'record created by.';
COMMENT ON COLUMN chimera_pipeline.updated_timestamp IS 'record updation timestamp.';
COMMENT ON COLUMN chimera_pipeline.updated_by IS 'record updated by.';
COMMENT ON COLUMN chimera_pipeline.active_flag IS 'Pipeline Active or not. Valid Values: Y, N';