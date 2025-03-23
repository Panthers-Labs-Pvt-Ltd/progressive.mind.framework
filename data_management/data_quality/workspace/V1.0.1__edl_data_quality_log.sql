create table if not exists edl_data_quality_log
(
row_num 					serial,
dq_id				varchar(255) default('DQL-'::text ||
										lpad(((nextval('edl_dq_check_status_dq_id_seq'::regclass))::CHARACTER VARYING)::text, 5,
										'0'::text)) ,
batch_id 			varchar(255),
process_typ_nm				varchar(255),
database_nm				varchar(255),
table_nm					varchar(255),
dq_constraint				varchar(255),
check_level					varchar(255),
check_status				varchar(255),
constraint_mesg				varchar(255),
constraint_status				varchar(255),
dq_check				varchar(255),
control_name				varchar(255),
rule_nm				varchar(255),
rule_column				varchar(255),
CONSTRAINT edl_dq_check_status_pkey
	PRIMARY KEY (dq_id),
CONSTRAINT edl_dq_log_edl_batch_status_batch_id_fk
	FOREIGN KEY (batch_id) REFERENCES edl_batch_log
	ON UPDATE CASCADE ON DELETE CASCADE
);
