create table if not exists edl_dq_user_config
(
row_num 					serial,
dq_config_id				varchar(255) default('DQM-'::text ||
										lpad(((nextval('edl_dq_metrics_seq'::regclass))::CHARACTER VARYING)::text, 5,
										'0'::text)) not null,
control_nm			varchar(255),
rule_nm				varchar(255),
process_typ_nm	varchar(255),
check_level			varchar(255),
database_nm		varchar(255),
table_nm				varchar(255),
rule_col				varchar(255),
rule_value			varchar(255),
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
CONSTRAINT edl_dq_metrics_seq_pkey
	PRIMARY KEY (dq_config_id),
CONSTRAINT edl_dq_metrics_unq
	UNIQUE (control_nm, process_typ_nm, database_nm, table_nm, rule_nm, rule_col),
CONSTRAINT edl_dq_metrics_edl_dq_rules_control_name_fk
	FOREIGN KEY (rule_nm, control_nm) REFERENCES edl_dq_rules(rule_name, control_name)
);
