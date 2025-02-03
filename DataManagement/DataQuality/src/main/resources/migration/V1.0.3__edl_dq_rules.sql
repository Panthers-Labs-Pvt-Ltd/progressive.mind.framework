create table if not exists edl_dq_rules
(
row_num 					serial,
rule_id					varchar(255) default('DQ-'::text ||
										lpad(((nextval('edl_persist_dim_seq'::regclass))::CHARACTER VARYING)::text, 5,
										'0'::text)) not null,
rule_name				varchar(255) not null,
control_name 		varchar(255),
rule_desc			varchar(500),
rule_example		varchar(255),
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
CONSTRAINT edl_dq_pkey
	PRIMARY KEY (rule_id),
CONSTRAINT edl_dq_rules_pk
	UNIQUE (rule_name),
CONSTRAINT edl_dq_unq
	UNIQUE (rule_name, control_name),
CONSTRAINT edl_dq_rules_config_edl_dc_rules_control_name_fk
	FOREIGN KEY (control_name) REFERENCES edl_data_controls
);
