create table if not exists edl_dq_user_config_log
(
row_num 					serial,
dq_log_id				varchar(255) default('DQL-'::text ||
										lpad(((nextval('edl_user_dq_log_seq'::regclass))::CHARACTER VARYING)::text, 5,
										'0'::text)) not null,
batch_id				varchar(255),		
process_typ_nm				varchar(255),	
entity_typ				text 	not null,
rule_name				text	not null,
database_nm					varchar(255),	
table_nm				varchar(255),	
actual_value		text not null,
created_ts					timestamp default CURRENT_TIMESTAMP,
created_by					varchar(255) default CURRENT_USER,
updated_ts					timestamp,
updated_by					varchar(255)
);

comment on column edl_dq_user_config_log.row_num 'Running serial number';
comment on column edl_dq_user_config_log.dq_log_id 'Auto generated sequence';
comment on column edl_dq_user_config_log.batch_id 'Batch ID';
comment on column edl_dq_user_config_log.process_typ_nm 'Process Type Name';
comment on column edl_dq_user_config_log.entity_typ 'Entity Type';
comment on column edl_dq_user_config_log.rule_name 'Rule Name';
comment on column edl_dq_user_config_log.database_nm 'Database Name';
comment on column edl_dq_user_config_log.table_nm 'Table Name';
comment on column edl_dq_user_config_log.actual_value 'Actual Value';
comment on column edl_dq_user_config_log.created_ts 'Creation Timestamp';
comment on column edl_dq_user_config_log.created_by 'Created By';
comment on column edl_dq_user_config_log.updated_ts 'Updated Timestamp';
comment on column edl_dq_user_config_log.updated_by 'Updated By';
