CREATE TABLE data_management_processes (
  process_id smallint PRIMARY KEY,
  process_name varchar(100) UNIQUE NOT NULL,
  process_short_desc varchar(500) NOT NULL,
  process_long_desc text NOT NULL,
  created_by varchar(50) NOT NULL DEFAULT current_user,
  created_ts timestamp NOT NULL DEFAULT current_timestamp,
  updated_by varchar(50),
  updated_ts timestamp
);

CREATE TABLE data_controls (
  control_id smallint PRIMARY KEY,
  control_name varchar(100) UNIQUE NOT NULL,
  control_short_desc varchar(500) NOT NULL,
  control_long_desc text NOT NULL,
  active_flg varchar(1) NOT NULL DEFAULT 'Y',
  reserved_5 varchar(50),
  reserved_4 varchar(50),
  reserved_3 varchar(50),
  reserved_2 varchar(50),
  reserved_1 varchar(50),
  created_by varchar(50) NOT NULL DEFAULT current_user,
  created_ts timestamp NOT NULL DEFAULT current_timestamp,
  updated_by varchar(50),
  updated_ts timestamp
);

CREATE TABLE data_controls_to_process_maps (
  control_id integer PRIMARY KEY,
  process_id integer NOT NULL,
  tbd_ref_metadata varchar,
  tbd_check_lvl varchar,
  active_flg varchar(1) NOT NULL DEFAULT 'Y',
  reserved_5 varchar(50),
  reserved_4 varchar(50),
  reserved_3 varchar(50),
  reserved_2 varchar(50),
  reserved_1 varchar(50),
  created_by varchar(50) NOT NULL DEFAULT current_user,
  created_ts timestamp NOT NULL DEFAULT current_timestamp,
  updated_by varchar(50),
  updated_ts timestamp
);

CREATE TABLE data_controls_log (
  data_controls_log_id bigserial PRIMARY KEY,
  batch_id VARCHAR(100) NOT NULL,
  process_typ_nm VARCHAR(255),
  control_id smallint NOT NULL,
  start_ts timestamp NOT NULL,
  end_ts timestamp,
  status_desc VARCHAR(1000),
  status VARCHAR(255)
);

CREATE TABLE dq_dimensions (
  dimension_id smallint PRIMARY KEY,
  dimension_name varchar(100) UNIQUE NOT NULL,
  dimension_short_desc varchar(500) NOT NULL,
  dimension_long_desc text NOT NULL,
  calculated_field bool NOT NULL,
  calculation_function varchar(500),
  control_id smallint NOT NULL,
  created_by varchar(50) NOT NULL DEFAULT current_user,
  created_ts timestamp NOT NULL DEFAULT current_timestamp,
  updated_by varchar(50),
  updated_ts timestamp
);

CREATE TABLE dq_rules (
  rule_id serial PRIMARY KEY,
  rule_name varchar(100) UNIQUE NOT NULL,
  rule_desc varchar(500) NOT NULL,
  rule_example text NOT NULL,
  rule_owner varchar(50) NOT NULL,
  dimension_id integer NOT NULL,
  check_level varchar(20) NOT NULL,
  reserved_5 varchar(50),
  reserved_4 varchar(50),
  reserved_3 varchar(50),
  reserved_2 varchar(50),
  reserved_1 varchar(50),
  active_flg varchar(1) NOT NULL DEFAULT 'Y',
  created_by varchar(50) NOT NULL DEFAULT current_user,
  created_ts timestamp NOT NULL DEFAULT current_timestamp,
  updated_by varchar(50),
  updated_ts timestamp
);

CREATE TABLE dq_rules_to_data_asset_map (
  dq_rules_to_data_asset_map_id serial PRIMARY KEY,
  rule_id integer NOT NULL,
  database_name varchar(255) UNIQUE NOT NULL,
  schema_name varchar(255) NOT NULL,
  table_name varchar(255) UNIQUE NOT NULL,
  partition_keys varchar(255) UNIQUE,
  rule_col varchar(255),
  rule_value varchar(255),
  active_flg varchar(1) NOT NULL DEFAULT 'Y',
  reserved_5 varchar(50),
  reserved_4 varchar(50),
  reserved_3 varchar(50),
  reserved_2 varchar(50),
  reserved_1 varchar(50),
  created_by varchar(50) NOT NULL DEFAULT current_user,
  created_ts timestamp NOT NULL DEFAULT current_timestamp,
  updated_ts timestamp,
  updated_by varchar(50)
);

CREATE TABLE data_asset_profiles (
  database_name varchar NOT NULL,
  schema_name varchar,
  table_name varchar NOT NULL,
  partition_key varchar NOT NULL,
  profile_text jsonb NOT NULL,
  batch_id integer NOT NULL,
  reserved_5 varchar(50),
  reserved_4 varchar(50),
  reserved_3 varchar(50),
  reserved_2 varchar(50),
  reserved_1 varchar(50),
  created_by varchar(50) NOT NULL DEFAULT current_user,
  created_ts timestamp NOT NULL DEFAULT current_timestamp
);

CREATE TABLE data_quality_suggestions (
  row_num serial,
  dq_cnstnt_id varchar(255) PRIMARY KEY NOT NULL DEFAULT 'incremental',
  process_typ_nm varchar(255),
  database_name varchar(255),
  schema_name varchar,
  table_name varchar(255),
  partition_keys varchar,
  rule_col varchar(255),
  dq_constraint text,
  scala_code text,
  reserved_5 varchar(50),
  reserved_4 varchar(50),
  reserved_3 varchar(50),
  reserved_2 varchar(50),
  reserved_1 varchar(50),
  suggestion_counter integer NOT NULL,
  ranking integer
);

ALTER TABLE "dq_dimensions" ADD FOREIGN KEY ("control_id") REFERENCES "data_controls" ("control_id");

ALTER TABLE "data_controls_log" ADD FOREIGN KEY ("control_id") REFERENCES "data_controls" ("control_id");

ALTER TABLE "data_controls_to_process_maps" ADD FOREIGN KEY ("control_id") REFERENCES "data_controls" ("control_id");

ALTER TABLE "data_controls_to_process_maps" ADD FOREIGN KEY ("process_id") REFERENCES "data_management_processes" ("process_id");

ALTER TABLE "dq_rules" ADD FOREIGN KEY ("dimension_id") REFERENCES "dq_dimensions" ("dimension_id");

ALTER TABLE "dq_rules_to_data_asset_map" ADD FOREIGN KEY ("rule_id") REFERENCES "dq_rules" ("rule_id");

COMMENT ON COLUMN data_management_processes.process_id IS 'Id';

COMMENT ON COLUMN data_management_processes.process_name IS 'name of the governance process';

COMMENT ON COLUMN data_management_processes.process_short_desc IS 'short description of the process';

COMMENT ON COLUMN data_management_processes.process_long_desc IS 'Long description, can be used by AI or UI to share notes';

COMMENT ON COLUMN data_management_processes.created_by IS 'creator';

COMMENT ON COLUMN data_management_processes.created_ts IS 'create timestamp';

COMMENT ON COLUMN data_management_processes.updated_by IS 'last updated by';

COMMENT ON COLUMN data_management_processes.updated_ts IS 'last updated timestamp';

COMMENT ON COLUMN data_controls.control_id IS 'Id';

COMMENT ON COLUMN data_controls.control_name IS 'meaning name';

COMMENT ON COLUMN data_controls.control_short_desc IS 'data controls description';

COMMENT ON COLUMN data_controls.control_long_desc IS 'Long description, can be used by AI or UI to share notes';

COMMENT ON COLUMN data_controls.active_flg IS 'if this control is still active';

COMMENT ON COLUMN data_controls.reserved_5 IS 'dummy column for future use';

COMMENT ON COLUMN data_controls.reserved_4 IS 'dummy column for future use';

COMMENT ON COLUMN data_controls.reserved_3 IS 'dummy column for future use';

COMMENT ON COLUMN data_controls.reserved_2 IS 'dummy column for future use';

COMMENT ON COLUMN data_controls.reserved_1 IS 'dummy column for future use';

COMMENT ON COLUMN data_controls.created_by IS 'creator';

COMMENT ON COLUMN data_controls.created_ts IS 'create timestamp';

COMMENT ON COLUMN data_controls.updated_by IS 'last updated by';

COMMENT ON COLUMN data_controls.updated_ts IS 'last updated timestamp';

COMMENT ON COLUMN data_controls_to_process_maps.control_id IS 'Id';

COMMENT ON COLUMN data_controls_to_process_maps.process_id IS 'Foreign key from data_governance_processes';

COMMENT ON COLUMN data_controls_to_process_maps.tbd_ref_metadata IS 'Ref Metedata which needs to be used for this control based on Process Type name BatchPipelineOrigin/BatchPipelineDeestination';

COMMENT ON COLUMN data_controls_to_process_maps.tbd_check_lvl IS 'Error/Warning/Info';

COMMENT ON COLUMN data_controls_to_process_maps.active_flg IS 'if this control is still active';

COMMENT ON COLUMN data_controls_to_process_maps.reserved_5 IS 'dummy column for future use';

COMMENT ON COLUMN data_controls_to_process_maps.reserved_4 IS 'dummy column for future use';

COMMENT ON COLUMN data_controls_to_process_maps.reserved_3 IS 'dummy column for future use';

COMMENT ON COLUMN data_controls_to_process_maps.reserved_2 IS 'dummy column for future use';

COMMENT ON COLUMN data_controls_to_process_maps.reserved_1 IS 'dummy column for future use';

COMMENT ON COLUMN data_controls_to_process_maps.created_by IS 'creator';

COMMENT ON COLUMN data_controls_to_process_maps.created_ts IS 'create timestamp';

COMMENT ON COLUMN data_controls_to_process_maps.updated_by IS 'last updated by';

COMMENT ON COLUMN data_controls_to_process_maps.updated_ts IS 'last updated timestamp';

COMMENT ON COLUMN data_controls_log.data_controls_log_id IS 'Incremental log';

COMMENT ON COLUMN data_controls_log.control_id IS 'foreign key from data_controls';

COMMENT ON COLUMN data_controls_log.start_ts IS 'Start time of this controls log';

COMMENT ON COLUMN data_controls_log.status_desc IS 'why something happened';

COMMENT ON COLUMN data_controls_log.status IS 'failed/passed/etc';

COMMENT ON COLUMN dq_dimensions.dimension_id IS 'Id';

COMMENT ON COLUMN dq_dimensions.dimension_name IS 'Name of the data quality dimensions';

COMMENT ON COLUMN dq_dimensions.dimension_short_desc IS 'Description of the data quality';

COMMENT ON COLUMN dq_dimensions.dimension_long_desc IS 'Long description, can be used by AI or UI to share notes';

COMMENT ON COLUMN dq_dimensions.calculated_field IS 'Is this dimension a calculated field';

COMMENT ON COLUMN dq_dimensions.calculation_function IS 'Relevant only when calculated_field is true';

COMMENT ON COLUMN dq_dimensions.control_id IS 'foreign key from data_controls';

COMMENT ON COLUMN dq_dimensions.created_by IS 'creator';

COMMENT ON COLUMN dq_dimensions.created_ts IS 'create timestamp';

COMMENT ON COLUMN dq_dimensions.updated_by IS 'last updated by';

COMMENT ON COLUMN dq_dimensions.updated_ts IS 'last updated timestamp';

COMMENT ON COLUMN dq_rules.rule_id IS 'Should this be incremental seq';

COMMENT ON COLUMN dq_rules.rule_name IS 'meaningful name';

COMMENT ON COLUMN dq_rules.rule_desc IS 'short description of the rule';

COMMENT ON COLUMN dq_rules.rule_example IS 'example to help understand the rule and its implementation';

COMMENT ON COLUMN dq_rules.rule_owner IS 'owner of the rule';

COMMENT ON COLUMN dq_rules.dimension_id IS 'foreign key from dq_dimensions';

COMMENT ON COLUMN dq_rules.check_level IS 'Whether this rule is applied at Dataset or Column level?';

COMMENT ON COLUMN dq_rules.reserved_5 IS 'dummy column for future use';

COMMENT ON COLUMN dq_rules.reserved_4 IS 'dummy column for future use';

COMMENT ON COLUMN dq_rules.reserved_3 IS 'dummy column for future use';

COMMENT ON COLUMN dq_rules.reserved_2 IS 'dummy column for future use';

COMMENT ON COLUMN dq_rules.reserved_1 IS 'dummy column for future use';

COMMENT ON COLUMN dq_rules.active_flg IS 'if the rule is still active Y/N';

COMMENT ON COLUMN dq_rules.created_by IS 'creator';

COMMENT ON COLUMN dq_rules.created_ts IS 'create timestamp';

COMMENT ON COLUMN dq_rules.updated_by IS 'last updated by';

COMMENT ON COLUMN dq_rules.updated_ts IS 'last updated timestamp';

COMMENT ON COLUMN dq_rules_to_data_asset_map.dq_rules_to_data_asset_map_id IS 'Should be incremental';

COMMENT ON COLUMN dq_rules_to_data_asset_map.rule_id IS 'Rule Id';

COMMENT ON COLUMN dq_rules_to_data_asset_map.database_name IS 'database_name';

COMMENT ON COLUMN dq_rules_to_data_asset_map.active_flg IS 'whether this record is still valid';

COMMENT ON COLUMN dq_rules_to_data_asset_map.reserved_5 IS 'dummy column for future use';

COMMENT ON COLUMN dq_rules_to_data_asset_map.reserved_4 IS 'dummy column for future use';

COMMENT ON COLUMN dq_rules_to_data_asset_map.reserved_3 IS 'dummy column for future use';

COMMENT ON COLUMN dq_rules_to_data_asset_map.reserved_2 IS 'dummy column for future use';

COMMENT ON COLUMN dq_rules_to_data_asset_map.reserved_1 IS 'dummy column for future use';

COMMENT ON COLUMN dq_rules_to_data_asset_map.updated_ts IS 'last updated by';

COMMENT ON COLUMN dq_rules_to_data_asset_map.updated_by IS 'last updated timestamp';

COMMENT ON COLUMN data_asset_profiles.database_name IS 'Database Name';

COMMENT ON COLUMN data_asset_profiles.schema_name IS 'Schema name';

COMMENT ON COLUMN data_asset_profiles.table_name IS 'table name';

COMMENT ON COLUMN data_asset_profiles.partition_key IS 'specific partition corresponding to this profile';

COMMENT ON COLUMN data_asset_profiles.profile_text IS 'queryable profile binary json';

COMMENT ON COLUMN data_asset_profiles.batch_id IS 'Should it be referenced from batch id';

COMMENT ON COLUMN data_asset_profiles.reserved_5 IS 'dummy column for future use';

COMMENT ON COLUMN data_asset_profiles.reserved_4 IS 'dummy column for future use';

COMMENT ON COLUMN data_asset_profiles.reserved_3 IS 'dummy column for future use';

COMMENT ON COLUMN data_asset_profiles.reserved_2 IS 'dummy column for future use';

COMMENT ON COLUMN data_asset_profiles.reserved_1 IS 'dummy column for future use';

COMMENT ON COLUMN data_asset_profiles.created_by IS 'creator';

COMMENT ON COLUMN data_asset_profiles.created_ts IS 'create timestamp';

COMMENT ON COLUMN data_quality_suggestions.reserved_5 IS 'dummy column for future use';

COMMENT ON COLUMN data_quality_suggestions.reserved_4 IS 'dummy column for future use';

COMMENT ON COLUMN data_quality_suggestions.reserved_3 IS 'dummy column for future use';

COMMENT ON COLUMN data_quality_suggestions.reserved_2 IS 'dummy column for future use';

COMMENT ON COLUMN data_quality_suggestions.reserved_1 IS 'dummy column for future use';

COMMENT ON COLUMN data_quality_suggestions.suggestion_counter IS 'keep counter on how many times the same suggestion has been proposed';

COMMENT ON COLUMN data_quality_suggestions.ranking IS 'Should we rank suggestion based on its purpose';
