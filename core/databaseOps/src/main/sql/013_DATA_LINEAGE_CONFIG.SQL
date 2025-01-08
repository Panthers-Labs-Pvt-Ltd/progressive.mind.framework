create table if not exists DATA_LINEAGE_CONFIG
(
LineageId UUID DEFAULT gen_random_uuid() PRIMARY KEY,
batch_id			       varchar(500) ,
pipeline_name		           varchar(255),
database_name		           varchar(255),
table_name		           varchar(255),
lineage_metadata 			   text,
reserved_5					varchar(500),
reserved_4					varchar(500),
reserved_3					varchar(500),
reserved_2					varchar(500),
reserved_1					varchar(500),
created_ts					timestamp default CURRENT_TIMESTAMP,
created_by					varchar(255) default CURRENT_USER,
updated_ts					timestamp,
updated_by					varchar(255),
active_flg					varchar(1)	default 'Y':: CHARACTER VARYING
);