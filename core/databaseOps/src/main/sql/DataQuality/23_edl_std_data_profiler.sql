create table if not exists edl_std_data_profiler
(
profiler_id 					serial,
table_nm					varchar(255),
column_nm			varchar(255),
approx_distinct_values	varchar(255),
completeness	varchar(255),
datatype	varchar(255),
description	varchar(255),
reserved_5					varchar(500),
reserved_4					varchar(500),
reserved_3					varchar(500),
reserved_2					varchar(500),
reserved_1					varchar(500),
PRIMARY KEY (profiler_id)
);