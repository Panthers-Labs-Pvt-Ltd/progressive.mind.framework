create table if not exists edl_data_controls_map
(
row_num 					serial,
map_id					varchar(255) default('DM-'::text ||
										lpad(((nextval('edl_data_control_map_dim_seq'::regclass))::CHARACTER VARYING)::text, 5,
										'0'::text)) not null,
control_name				varchar(255),
control_desc				varchar(500),
process_typ_nm			varchar(255),
ref_metadata				varchar(255),
check_lvl						varchar(255),
reserved_5					varchar(500),
reserved_4					varchar(500),
reserved_3					varchar(500),
reserved_2					varchar(500),
reserved_1					varchar(500),
created_ts					timestamp default CURRENT_TIMESTAMP,
created_by					varchar(255) default CURRENT_USER,
updated_ts					timestamp,
updated_by					varchar(255),
active_flg					varchar(1)	default 'Y':: CHARACTER VARYING,
CONSTRAINT edl_data_control_map_dim_pkey
	PRIMARY KEY (map_id),
CONSTRAINT edl_dc_metrics_unq
	UNIQUE(control_name, process_typ_nm),
CONSTRAINT edl_data_controls_map_edl_data_controls_name_fk
FOREIGN KEY (control_name) REFERENCES edl_data_controls
);		

comment on column edl_data_controls_map.row_num 'Running serial number'
comment on column edl_data_controls_map.map_id 'Auto generated sequence';
comment on column edl_data_controls_map.control_name 'Name of the Control e.g Completness /UNIQUEess';
comment on column edl_data_controls_map.control_desc 'Description or definition of Control names';
comment on column edl_data_controls_map.reserved_1 'reserved_5 - Reserved attributes for future use';
comment on column edl_data_controls_map.created_ts 'creation timestamp';
comment on column edl_data_controls_map.updated_ts 'Updation timestamp';
comment on column edl_data_controls_map.created_by	'Created by User';
comment on column edl_data_controls_map.updated_by  'Updated by User';
comment on column edl_data_controls_map.active_flg  'Active Inactive Flag';
comment on column edl_data_controls_map.process_typ_nm	'Process Type Name from which this control needs to be mapped eg transformBatchPipeline/PersistBatchPipeline';
comment on column edl_data_controls_map.ref_metadata  'Ref Metedata which needs to be used for this control based on Process Type name BatchPipelineOrigin/BatchPipelineDeestination';
comment on column edl_data_controls_map.check_lvl  'ERROR/WARNING OR INFO';





