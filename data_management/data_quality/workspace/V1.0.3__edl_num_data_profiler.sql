create table if not exists edl_num_data_profiler
(
profiler_id 					serial,
table_nm					varchar(255),
profile_col_nm					varchar(255),
maximum_val					double PRECISION,
mean_val					double PRECISION,
minimum_val				double PRECISION,
std_dev_val				double PRECISION,
reserved_5					varchar(500),
reserved_4					varchar(500),
reserved_3					varchar(500),
reserved_2					varchar(500),
reserved_1					varchar(500),
PRIMARY KEY (profiler_id)
);