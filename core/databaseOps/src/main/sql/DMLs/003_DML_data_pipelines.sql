INSERT INTO data_pipelines (pipeline_name, pipeline_description, process_mode, run_frequency, created_by, updated_by, updated_timestamp, active_flag) values (
'TestPipeline', 'Pipeline for unit testing', 'Batch', '12 * * * ', 'PK', null, null, 'Y')
ON CONFLICT DO NOTHING
);
