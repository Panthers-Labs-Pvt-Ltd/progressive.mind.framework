create table if not exists edl_data_controls
(
row_num 					serial,
control_id					varchar(255) default('DC-'::text ||
										lpad(((nextval('edl_data_control_config_seq'::regclass))::CHARACTER VARYING)::text, 5,
										'0'::text)) not null,
control_name				varchar(255) not null,
control_desc				varchar(255),										
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
CONSTRAINT edl_data_control_pkey
	PRIMARY KEY (control_name),
CONSTRAINT edl_data_control_unq
	UNIQUE(control_name)
);		

comment on column edl_data_controls.row_num 'Running serial number'
comment on column edl_data_controls.control_id 'Auto generated sequence';
comment on column edl_data_controls.control_name 'Name of the Control e.g Completness /UNIQUEess';
comment on column edl_data_controls.control_desc 'Description or definition of Control names';
comment on column edl_data_controls.reserved_1 'reserved_5 - Reserved attributes for future use';
comment on column edl_data_controls.created_ts 'creation timestamp';
comment on column edl_data_controls.updated_ts 'Updation timestamp';
comment on column edl_data_controls.created_by	'Created by User';
comment on column edl_data_controls.updated_by  'Updated by User';
comment on column edl_data_controls.active_flg  'Active Inactive Flag';