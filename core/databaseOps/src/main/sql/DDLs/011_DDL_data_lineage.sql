create table if not exists data_lineage
(
row_num 				   bigserial,
batch_id			       varchar(500) ,
pipeline_nm		           varchar(255),
database_nm		           varchar(255),
table_nm		           varchar(255),
namespace		           varchar(255),
lineage_json 			   text,
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