CREATE TABLE IF NOT EXISTS DATA_SOURCES_TYPES (
DATA_SOURCE_ID UUID DEFAULT GEN_RANDOM_UUID() PRIMARY KEY,
DATA_SOURCE_TYPE VARCHAR(255),
DATA_SOURCE_SUB_TYPE VARCHAR(255),
DESCRIPTION TEXT,
DATA_SOURCE_TEMPLATE TEXT,
READ_CONFIG TEXT,
WRITE_CONFIG TEXT,
CREATED_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
CREATED_BY VARCHAR(255) DEFAULT CURRENT_USER,
UPDATED_TIMESTAMP TIMESTAMP,
UPDATED_BY VARCHAR(255),
ACTIVE_FLAG VARCHAR(1) DEFAULT 'Y' :: CHARACTER VARYING,
CONSTRAINT PK_CHIMERA_DATA_SOURCES UNIQUE (DATA_SOURCE_TYPE, DATA_SOURCE_SUB_TYPE),
CONSTRAINT CHECK_ACTIVE_FLAG CHECK (ACTIVE_FLAG IN ('Y', 'N'))
);

COMMENT ON COLUMN data_sources.data_source_type IS 'Type of the data source supported';
COMMENT ON COLUMN data_sources.data_source_sub_type IS 'Type of the database or File format.';
COMMENT ON COLUMN data_sources.description IS 'Description of the data source';
COMMENT ON COLUMN data_sources.READ_CONFIG IS 'A Json String - with default options to be used while reading / extracting.';
COMMENT ON COLUMN data_sources.WRITE_CONFIG IS ' A Json String - with default options to be used while writing/ persisting.';
COMMENT ON COLUMN data_sources.created_timestamp IS 'record creation timestamp.';
COMMENT ON COLUMN data_sources.created_by IS 'record created by.';
COMMENT ON COLUMN data_sources.updated_timestamp IS 'record updation timestamp.';
COMMENT ON COLUMN data_sources.updated_by IS 'record updated by.';
COMMENT ON COLUMN data_sources.active_flag IS 'Denotes if the record is active or not.';

