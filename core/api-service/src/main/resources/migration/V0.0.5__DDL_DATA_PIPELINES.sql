CREATE TABLE IF NOT EXISTS data_pipelines (
    PIPELINE_NAME           VARCHAR(500),
    PIPELINE_DESCRIPTION    TEXT,
    PROCESS_MODE            VARCHAR(255) NOT NULL CHECK (process_mode IN ('Batch', 'Stream')),
    TAGS                    TEXT,
    ORG_HIER_NAME           VARCHAR(250),
    CREATED_TIMESTAMP       TIMESTAMP default CURRENT_TIMESTAMP,
    CREATED_BY              VARCHAR(255) default CURRENT_USER,
    UPDATED_TIMESTAMP       TIMESTAMP,
    UPDATED_BY              VARCHAR(255),
    ACTIVE_FLAG             VARCHAR(1) default 'Y' :: CHARACTER VARYING,
    CONSTRAINT pk_pipeline PRIMARY KEY (pipeline_name),
    CONSTRAINT check_pipelines_active_flag CHECK (active_flag IN ('Y', 'N')),
    CONSTRAINT fk_owner_org FOREIGN KEY (org_hier_name) REFERENCES organization_hierarchy (ORG_HIER_NAME) ON DELETE CASCADE ON UPDATE CASCADE
);

COMMENT ON COLUMN data_pipelines.pipeline_name IS 'Name of the pipeline';
COMMENT ON COLUMN data_pipelines.pipeline_description IS 'A brief description of the pipeline';
COMMENT ON COLUMN data_pipelines.process_mode IS 'Mode - Batch or Streaming. Valid Values: Batch, Stream';
COMMENT ON COLUMN data_pipelines.tags IS 'Tags associated with the pipeline';
COMMENT ON COLUMN data_pipelines.org_hier_name IS 'Owner of the pipeline';
COMMENT ON COLUMN data_pipelines.created_timestamp IS 'record creation timestamp.';
COMMENT ON COLUMN data_pipelines.created_by IS 'record created by.';
COMMENT ON COLUMN data_pipelines.updated_timestamp IS 'record updation timestamp.';
COMMENT ON COLUMN data_pipelines.updated_by IS 'record updated by.';
COMMENT ON COLUMN data_pipelines.active_flag IS 'Pipeline Active or not. Valid Values: Y, N';

INSERT INTO data_pipelines (pipeline_name, pipeline_description, process_mode, tags, org_hier_name, created_by, updated_timestamp, updated_by) VALUES
('Test_Pipeline', 'test Pipeline', 'Batch', 'ETL, Chimera', 'Chimera', 'PK', NULL, NULL);

select * from data_pipelines;