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
