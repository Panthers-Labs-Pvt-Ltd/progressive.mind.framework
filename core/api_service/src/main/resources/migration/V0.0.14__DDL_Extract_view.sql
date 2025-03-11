DROP VIEW if exists extract_view;
create or replace view extract_view as
select
    dp.pipeline_name,
    dp.pipeline_description,
    dp.process_mode,
    dp.tags,
    dp.org_hier_name,
    emc.sequence_number,
    emc.extract_source_type,
    emc.extract_source_sub_type,
    emc.data_source_connection_name,
    emc.source_configuration,
    emc.dataframe_name,
    emc.predecessor_sequences,
    emc.successor_sequences,
    emc.row_filter,
    emc.column_filter,
    ds.default_read_config,
    ds.default_write_config,
    femc.file_name,
    femc.file_path,
    femc.schema_path,
    femc.size_in_byte,
    femc.compression_type,
    nemc.COLLECTION,
    nemc.PARTITIONER,
    remc.database_name,
    remc.table_name,
    remc.schema_name,
    remc.sql_text,
    semc.kafka_consumer_topic,
    semc.kafka_consumer_group,
    semc.kafka_strt_offset,
    semc.kafka_max_offset,
    semc.kafka_poll_timeout,
    semc.tranctnl_cnsumr_flg,
    semc.WATRMRK_DURATION,
    semc.stg_formt,
    semc.stg_path,
    semc.stg_partitions,
    dsc.data_source_type,
    dsc.data_source_sub_type,
    dsc.authentication_type,
    dsc.authentication_data,
    dsc.connection_metadata,
    oh.org_type_name,
    oh.parent_org_name,
    oh.coo_owner,
    oh.ops_lead,
    oh.tech_lead,
    oh.bus_owner,
    oh.org_desc,
    oh.org_email,
    oh.org_ci
from data_pipelines dp
join extract_metadata_config emc on emc.pipeline_name = dp.pipeline_name
left join data_sources ds on emc.extract_source_type = ds.data_source_type and emc.extract_source_sub_type = ds.data_source_sub_type
left join file_extract_metadata_config femc on emc.pipeline_name = femc.pipeline_name and emc.sequence_number = femc.sequence_number
left join nosql_extract_metadata_config nemc on emc.pipeline_name = nemc.pipeline_name and emc.sequence_number = nemc.sequence_number
left JOIN relational_extract_metadata_config remc on emc.pipeline_name = remc.pipeline_name and emc.sequence_number = remc.sequence_number
left join streams_extract_metadata_config semc on emc.pipeline_name = semc.pipeline_name and emc.sequence_number = semc.sequence_number
left join data_sources_connections dsc on 
    (nemc.data_source_connection_name = dsc.data_source_connection_name or
 --   femc.data_source_connection_name = dsc.data_source_connection_name or
    remc.data_source_connection_name = dsc.data_source_connection_name or
    semc.data_source_connection_name = dsc.data_source_connection_name)
left join ORGANIZATION_HIERARCHY oh on dp.org_hier_name = oh.org_hier_name
order by sequence_number;

select * from extract_view;
