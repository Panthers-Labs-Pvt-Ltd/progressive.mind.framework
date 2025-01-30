create table if not exists edl_data_controls_log
(
row_num 					serial,
dc_log_id					integer default NEXTVAL('edl_data_control_log_dc_log_id_seq'::regclass) not null,
batch_id					VARCHAR(255),
process_typ_nm		VARCHAR(255),
control_nm				VARCHAR(255),
start_ts					timestamp,
end_ts						timestamp,
status_desc				VARCHAR(1000),
status						VARCHAR(255),
CONSTRAINT edl_data_control_log_pkey
	PRIMARY KEY (dc_log_id),
CONSTRAINT edl_dc_log_edl_batch_status_batch_id_fk
	FOREIGN KEY (batch_id) REFERENCES edl_batch_log
	ON UPDATE CASCADE on DELETE CASCADE,
CONSTRAINT edl_dc_metrics_edl_dc_log_control_name_process_type_name_fk
FOREIGN KEY (control_nm, process_typ_nm) REFERENCES edl_data_controls_map ( control_name, process_typ_nm)
);
