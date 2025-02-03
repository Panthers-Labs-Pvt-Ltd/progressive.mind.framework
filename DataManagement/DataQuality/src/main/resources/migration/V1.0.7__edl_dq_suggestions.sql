create table if not exists edl_dq_suggestions
(
row_num 					serial,
dq_cnstnt_id					varchar(255) default('DQC-'::text ||
										lpad(((nextval('edl_dq_cnstnt_sugg_seq'::regclass))::CHARACTER VARYING)::text, 5,
										'0'::text)) not null,
process_typ_nm					varchar(255),
database_nm						varchar(255),
table_nm								varchar(255),
rule_col									varchar(255),
dq_constraint						text,
scala_code							text,
reserved_5					varchar(500),
reserved_4					varchar(500),
reserved_3					varchar(500),
reserved_2					varchar(500),
reserved_1					varchar(500),
CONSTRAINT edl_dq_cnstnt_pkey
	PRIMARY KEY (dq_cnstnt_id)
);
