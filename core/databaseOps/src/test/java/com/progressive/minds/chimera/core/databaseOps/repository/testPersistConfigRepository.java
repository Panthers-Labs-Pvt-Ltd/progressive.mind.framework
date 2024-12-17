package com.progressive.minds.chimera.core.databaseOps.repository;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataPipelines;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataSources;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataSourcesConnections;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.persistConfig;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataPipelinesRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataSourcesConnectionRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataSourcesRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.persistConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class testPersistConfigRepository {

    private dataSourcesConnectionRepository dataSourcesConnectionRepository;
    private dataSourcesRepository dataSourcesRepository;
    private persistConfigRepository persistConfigRepository;
    private dataPipelinesRepository dataPipelinesRepository;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        System.setProperty("CHIMERA_EXE_ENV", "dev");
        dataSource = DataSourceConfig.getDataSource();
        dataSourcesConnectionRepository = new dataSourcesConnectionRepository();
        persistConfigRepository = new persistConfigRepository();
        dataSourcesRepository = new dataSourcesRepository();
        dataPipelinesRepository = new dataPipelinesRepository();
    }

    @Test
    void testPutExtractConfigs_validSingleRecord_1()  {
        // Arrange
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dataSourcesConnections dsc = new dataSourcesConnections("Postgres_Local_Test_Connection",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "chimera", "chimera123",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test",
                "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y");
        persistConfig ec = new persistConfig(1, "TestPipeline", 1, "Relational",
                "Postgres", "targetDB", "targetTable", "targetSchema",
                null, null, null, "Append",
                "Postgres_Local_Test_Connection", null, null, null,
                null, null, null, new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y" );
        try {
            dataSourcesRepository.putDataSources(ds);
            System.out.println("Insert Operation Successful on table data_sources.");
            dataSourcesConnectionRepository.putDataSourcesConnections(dsc);
            System.out.println("Insert Operation Successful on table data_sources_connections.");
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
           persistConfigRepository.putPersistConfig(ec);
            System.out.println("Insert Operation Successful on persist_config.");
        } catch (Exception e) {
            System.out.println("ErrorMessage: " + e.getMessage());
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains("Foreign Key Violation. Record is missing in Parent Table."));
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            Map<String, Object> Pfilter = new HashMap<>();
            Pfilter.put("data_sink_type", "Relational");
            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            persistConfigRepository.deleteFromPersistConfig(Pfilter);
            dataPipelinesRepository.deleteFromDataPipelines(pipelineFilter);
            dataSourcesConnectionRepository.deleteFromDataSourcesConnections(filter);
            dataSourcesRepository.deleteFromDataSources(filter);
        }
    }

    @Test
    void testPutExtractConfig_invalidSingleRecord_2()  {
        // Arrange
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dataSourcesConnections dsc = new dataSourcesConnections("Postgres_Local_Test_Connection",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "chimera", "chimera123",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");
        dataPipelines dp = new dataPipelines("TestPipeline1", "Pipeline to unit test",
                "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y");
        persistConfig ec = new persistConfig(1, "TestPipeline", 1, "Relational",
                "Postgres", "targetDB", "targetTable", "targetSchema",
                null, null, null, "Append",
                "Postgres_Local_Test_Connection", null, null, null,
                null, null, null, new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y" );
        try {
            dataSourcesRepository.putDataSources(ds);
            System.out.println("Insert Operation Successful on table data_sources.");
            dataSourcesConnectionRepository.putDataSourcesConnections(dsc);
            System.out.println("Insert Operation Successful on table data_sources_connections.");
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            persistConfigRepository.putPersistConfig(ec);
            System.out.println("Insert Operation Successful on persist_config.");
        } catch (Exception e) {
            System.out.println("ErrorMessage - " + e.getMessage());
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains("Foreign Key Violation. Record is missing in Parent Table."),
                    "Expected 'Foreign Key Violation. Record is missing in Parent Table.' in the error message.");
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            Map<String, Object> Pfilter = new HashMap<>();
            Pfilter.put("data_sink_type", "Relational");
            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            persistConfigRepository.deleteFromPersistConfig(Pfilter);
            dataPipelinesRepository.deleteFromDataPipelines(pipelineFilter);
            dataSourcesConnectionRepository.deleteFromDataSourcesConnections(filter);
            dataSourcesRepository.deleteFromDataSources(filter);
        }
    }

    @Test
    void testExtractPipelines_validMultipleRecord_3()  {
        // Arrange
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dataSourcesConnections dsc = new dataSourcesConnections("Postgres_Local_Test_Connection",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "chimera", "chimera123",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test",
                "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y");
        persistConfig pc = new persistConfig(1, "TestPipeline", 1, "Relational",
                "Postgres", "targetDB", "targetTable", "targetSchema",
                null, null, null, "Append",
                "Postgres_Local_Test_Connection", null, null, null,
                null, null, null, new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y" );
        persistConfig pc1 = new persistConfig(2, "TestPipeline", 1, "Relational",
                "Postgres", "targetDB1", "targetTable1", "targetSchema1",
                null, null, null, "Append",
                "Postgres_Local_Test_Connection", null, null, null,
                null, null, null, new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y" );
        List<persistConfig> pcList = new ArrayList<persistConfig>();
        pcList.add(pc);
        pcList.add(pc1);
        try {
            dataSourcesRepository.putDataSources(ds);
            System.out.println("Insert Operation Successful on table data_sources.");
            dataSourcesConnectionRepository.putDataSourcesConnections(dsc);
            System.out.println("Insert Operation Successful on table data_sources_connections.");
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            persistConfigRepository.putPersistConfig(pcList);
            System.out.println("Insert Operation Successful on extract_config.");
        } catch (Exception e) {
            assertEquals("A Record with the given key already exists. ERROR: duplicate key value violates unique constraint \"pk_chimera_data_sources\"\n" +
                    "  Detail: Key (data_source_type, data_source_sub_type)=(Relational, Postgres) already exists.", e.getMessage());
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            Map<String, Object> Pfilter = new HashMap<>();
            Pfilter.put("data_sink_type", "Relational");
            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            persistConfigRepository.deleteFromPersistConfig(Pfilter);
            dataPipelinesRepository.deleteFromDataPipelines(pipelineFilter);
            dataSourcesConnectionRepository.deleteFromDataSourcesConnections(filter);
            dataSourcesRepository.deleteFromDataSources(filter);
        }
    }

    @Test
    void testGetAllExtractConfigs_4 () {
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dataSourcesConnections dsc = new dataSourcesConnections("Postgres_Local_Test_Connection",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "chimera", "chimera123",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test",
                "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y");
        persistConfig pc = new persistConfig(1, "TestPipeline", 1, "Relational",
                "Postgres", "targetDB", "targetTable", "targetSchema",
                null, null, null, "Append",
                "Postgres_Local_Test_Connection", null, null, null,
                null, null, null, new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y" );
        persistConfig pc1 = new persistConfig(2, "TestPipeline", 1, "Relational",
                "Postgres", "targetDB1", "targetTable1", "targetSchema1",
                null, null, null, "Append",
                "Postgres_Local_Test_Connection", null, null, null,
                null, null, null, new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y" );
        List<persistConfig> pcList = new ArrayList<persistConfig>();
        pcList.add(pc);
        pcList.add(pc1);
        try {
            dataSourcesRepository.putDataSources(ds);
            System.out.println("Insert Operation Successful on table data_sources.");
            dataSourcesConnectionRepository.putDataSourcesConnections(dsc);
            System.out.println("Insert Operation Successful on table data_sources_connections.");
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            persistConfigRepository.putPersistConfig(pcList);
            System.out.println("Insert Operation Successful on extract_config.");
        } catch (Exception e) {
            assertEquals("A Record with the given key already exists. ERROR: duplicate key value violates unique constraint \"pk_chimera_data_sources\"\n" +
                    "  Detail: Key (data_source_type, data_source_sub_type)=(Relational, Postgres) already exists.", e.getMessage());
        } finally {
            List<persistConfig> selectList = persistConfigRepository.getAllPersistConfig();
            assertTrue(!selectList.isEmpty());
            assertEquals(2, selectList.size());
            assertEquals(pcList.get(0).getUniqueId(), selectList.get(0).getUniqueId());
            assertEquals(pcList.get(0).getPipelineName(), selectList.get(0).getPipelineName());
            assertEquals(pcList.get(0).getSequenceNumber(), selectList.get(0).getSequenceNumber());
            assertEquals(pcList.get(0).getDataSinkType(), selectList.get(0).getDataSinkType());
            assertEquals(pcList.get(0).getDataSinkSubType(), selectList.get(0).getDataSinkSubType());
            assertEquals(pcList.get(0).getTargetDatabaseName(), selectList.get(0).getTargetDatabaseName());
            assertEquals(pcList.get(0).getTargetTableName(), selectList.get(0).getTargetTableName());
            assertEquals(pcList.get(0).getTargetSchemaName(), selectList.get(0).getTargetSchemaName());
            assertEquals(pcList.get(0).getPartitionKeys(), selectList.get(0).getPartitionKeys());
            assertEquals(pcList.get(0).getTargetSqlText(), selectList.get(0).getTargetSqlText());
            assertEquals(pcList.get(0).getTargetPath(), selectList.get(0).getTargetPath());
            assertEquals(pcList.get(0).getWriteMode(), selectList.get(0).getWriteMode());
            assertEquals(pcList.get(0).getDataSourceConnectionName(), selectList.get(0).getDataSourceConnectionName());
            assertEquals(pcList.get(0).getSinkConfiguration(), selectList.get(0).getSinkConfiguration());
            assertEquals(pcList.get(0).getSortColumns(), selectList.get(0).getSortColumns());
            assertEquals(pcList.get(0).getDedupColumns(), selectList.get(0).getDedupColumns());
            assertEquals(pcList.get(0).getKafkaTopic(), selectList.get(0).getKafkaTopic());
            assertEquals(pcList.get(0).getKafkaKey(), selectList.get(0).getKafkaKey());
            assertEquals(pcList.get(0).getKafkaMessage(), selectList.get(0).getKafkaMessage());
            assertEquals(pcList.get(0).getCreatedBy(), selectList.get(0).getCreatedBy());
            assertEquals(pcList.get(0).getUpdatedBy(), selectList.get(0).getUpdatedBy());
            assertEquals(pcList.get(0).getUpdatedTimestamp(), selectList.get(0).getUpdatedTimestamp());
            assertEquals(pcList.get(0).getActiveFlag(), selectList.get(0).getActiveFlag());

            assertEquals(pcList.get(1).getUniqueId(), selectList.get(1).getUniqueId());
            assertEquals(pcList.get(1).getPipelineName(), selectList.get(1).getPipelineName());
            assertEquals(pcList.get(1).getSequenceNumber(), selectList.get(1).getSequenceNumber());
            assertEquals(pcList.get(1).getDataSinkType(), selectList.get(1).getDataSinkType());
            assertEquals(pcList.get(1).getDataSinkSubType(), selectList.get(1).getDataSinkSubType());
            assertEquals(pcList.get(1).getTargetDatabaseName(), selectList.get(1).getTargetDatabaseName());
            assertEquals(pcList.get(1).getTargetTableName(), selectList.get(1).getTargetTableName());
            assertEquals(pcList.get(1).getTargetSchemaName(), selectList.get(1).getTargetSchemaName());
            assertEquals(pcList.get(1).getPartitionKeys(), selectList.get(1).getPartitionKeys());
            assertEquals(pcList.get(1).getTargetSqlText(), selectList.get(1).getTargetSqlText());
            assertEquals(pcList.get(1).getTargetPath(), selectList.get(1).getTargetPath());
            assertEquals(pcList.get(1).getWriteMode(), selectList.get(1).getWriteMode());
            assertEquals(pcList.get(1).getDataSourceConnectionName(), selectList.get(1).getDataSourceConnectionName());
            assertEquals(pcList.get(1).getSinkConfiguration(), selectList.get(1).getSinkConfiguration());
            assertEquals(pcList.get(1).getSortColumns(), selectList.get(1).getSortColumns());
            assertEquals(pcList.get(1).getDedupColumns(), selectList.get(1).getDedupColumns());
            assertEquals(pcList.get(1).getKafkaTopic(), selectList.get(1).getKafkaTopic());
            assertEquals(pcList.get(1).getKafkaKey(), selectList.get(1).getKafkaKey());
            assertEquals(pcList.get(1).getKafkaMessage(), selectList.get(1).getKafkaMessage());
            assertEquals(pcList.get(1).getCreatedBy(), selectList.get(1).getCreatedBy());
            assertEquals(pcList.get(1).getUpdatedBy(), selectList.get(1).getUpdatedBy());
            assertEquals(pcList.get(1).getUpdatedTimestamp(), selectList.get(1).getUpdatedTimestamp());
            assertEquals(pcList.get(1).getActiveFlag(), selectList.get(1).getActiveFlag());

            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            Map<String, Object> Pfilter = new HashMap<>();
            Pfilter.put("data_sink_type", "Relational");
            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            persistConfigRepository.deleteFromPersistConfig(Pfilter);
            dataPipelinesRepository.deleteFromDataPipelines(pipelineFilter);
            dataSourcesConnectionRepository.deleteFromDataSourcesConnections(filter);
            dataSourcesRepository.deleteFromDataSources(filter);
        }
    }

    @Test
    void testGetExtractConfigWithFilters_5() {
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dataSourcesConnections dsc = new dataSourcesConnections("Postgres_Local_Test_Connection",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "chimera", "chimera123",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test",
                "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y");
        persistConfig pc = new persistConfig(1, "TestPipeline", 1, "Relational",
                "Postgres", "targetDB", "targetTable", "targetSchema",
                null, null, null, "Append",
                "Postgres_Local_Test_Connection", null, null, null,
                null, null, null, new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y" );
        persistConfig pc1 = new persistConfig(2, "TestPipeline", 1, "Relational",
                "Postgres", "targetDB1", "targetTable1", "targetSchema1",
                null, null, null, "Append",
                "Postgres_Local_Test_Connection", null, null, null,
                null, null, null, new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y" );
        List<persistConfig> pcList = new ArrayList<persistConfig>();
        pcList.add(pc);
        pcList.add(pc1);
        try {
            dataSourcesRepository.putDataSources(ds);
            System.out.println("Insert Operation Successful on table data_sources.");
            dataSourcesConnectionRepository.putDataSourcesConnections(dsc);
            System.out.println("Insert Operation Successful on table data_sources_connections.");
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            persistConfigRepository.putPersistConfig(pcList);
            System.out.println("Insert Operation Successful on extract_config.");
        } catch (Exception e) {
            assertEquals("A Record with the given key already exists. ERROR: duplicate key value violates unique constraint \"pk_chimera_data_sources\"\n" +
                    "  Detail: Key (data_source_type, data_source_sub_type)=(Relational, Postgres) already exists.", e.getMessage());
        } finally {
            Map<String, Object> selFilter = new HashMap<>();
            selFilter.put("target_table_name", "targetTable1");
            List<persistConfig> selectList = persistConfigRepository.getPersistConfigWithFilters(selFilter);
            assertTrue(!selectList.isEmpty());
            assertEquals(1, selectList.size());
            assertEquals(pcList.get(1).getUniqueId(), selectList.get(0).getUniqueId());
            assertEquals(pcList.get(1).getPipelineName(), selectList.get(0).getPipelineName());
            assertEquals(pcList.get(1).getSequenceNumber(), selectList.get(0).getSequenceNumber());
            assertEquals(pcList.get(1).getDataSinkType(), selectList.get(0).getDataSinkType());
            assertEquals(pcList.get(1).getDataSinkSubType(), selectList.get(0).getDataSinkSubType());
            assertEquals(pcList.get(1).getTargetDatabaseName(), selectList.get(0).getTargetDatabaseName());
            assertEquals(pcList.get(1).getTargetTableName(), selectList.get(0).getTargetTableName());
            assertEquals(pcList.get(1).getTargetSchemaName(), selectList.get(0).getTargetSchemaName());
            assertEquals(pcList.get(1).getPartitionKeys(), selectList.get(0).getPartitionKeys());
            assertEquals(pcList.get(1).getTargetSqlText(), selectList.get(0).getTargetSqlText());
            assertEquals(pcList.get(1).getTargetPath(), selectList.get(0).getTargetPath());
            assertEquals(pcList.get(1).getWriteMode(), selectList.get(0).getWriteMode());
            assertEquals(pcList.get(1).getDataSourceConnectionName(), selectList.get(0).getDataSourceConnectionName());
            assertEquals(pcList.get(1).getSinkConfiguration(), selectList.get(0).getSinkConfiguration());
            assertEquals(pcList.get(1).getSortColumns(), selectList.get(0).getSortColumns());
            assertEquals(pcList.get(1).getDedupColumns(), selectList.get(0).getDedupColumns());
            assertEquals(pcList.get(1).getKafkaTopic(), selectList.get(0).getKafkaTopic());
            assertEquals(pcList.get(1).getKafkaKey(), selectList.get(0).getKafkaKey());
            assertEquals(pcList.get(1).getKafkaMessage(), selectList.get(0).getKafkaMessage());
            assertEquals(pcList.get(1).getCreatedBy(), selectList.get(0).getCreatedBy());
            assertEquals(pcList.get(1).getUpdatedBy(), selectList.get(0).getUpdatedBy());
            assertEquals(pcList.get(1).getUpdatedTimestamp(), selectList.get(0).getUpdatedTimestamp());
            assertEquals(pcList.get(1).getActiveFlag(), selectList.get(0).getActiveFlag());

            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            Map<String, Object> Pfilter = new HashMap<>();
            Pfilter.put("data_sink_type", "Relational");
            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            persistConfigRepository.deleteFromPersistConfig(Pfilter);
            dataPipelinesRepository.deleteFromDataPipelines(pipelineFilter);
            dataSourcesConnectionRepository.deleteFromDataSourcesConnections(filter);
            dataSourcesRepository.deleteFromDataSources(filter);


        }


    }

    @Test
    void testUpdateDataSourcesConnections_6() {
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dataSourcesConnections dsc = new dataSourcesConnections("Postgres_Local_Test_Connection",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "chimera", "chimera123",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test",
                "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y");
        persistConfig pc = new persistConfig(1, "TestPipeline", 1, "Relational",
                "Postgres", "targetDB", "targetTable", "targetSchema",
                null, null, null, "Append",
                "Postgres_Local_Test_Connection", null, null, null,
                null, null, null, new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y" );
        persistConfig pc1 = new persistConfig(2, "TestPipeline", 1, "Relational",
                "Postgres", "targetDB1", "targetTable1", "targetSchema1",
                null, null, null, "Append",
                "Postgres_Local_Test_Connection", null, null, null,
                null, null, null, new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y" );
        List<persistConfig> pcList = new ArrayList<persistConfig>();
        pcList.add(pc);
        pcList.add(pc1);
        try {
            dataSourcesRepository.putDataSources(ds);
            System.out.println("Insert Operation Successful on table data_sources.");
            dataSourcesConnectionRepository.putDataSourcesConnections(dsc);
            System.out.println("Insert Operation Successful on table data_sources_connections.");
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            persistConfigRepository.putPersistConfig(pcList);
            System.out.println("Insert Operation Successful on extract_config.");
        } catch (Exception e) {
            assertEquals("A Record with the given key already exists. ERROR: duplicate key value violates unique constraint \"pk_chimera_data_sources\"\n" +
                    "  Detail: Key (data_source_type, data_source_sub_type)=(Relational, Postgres) already exists.", e.getMessage());
        } finally {
            Map<String, Object> sfilter = new HashMap<>();
            sfilter.put("target_table_name", "targetTable1");

            Map<String, Object> updateFields = new HashMap<>();
            updateFields.put("write_mode", "Overwrite");

            persistConfigRepository.updatePersistConfig(updateFields, sfilter);
            List<persistConfig> selectList = persistConfigRepository.getPersistConfigWithFilters(sfilter);
            assertEquals("Overwrite", selectList.get(0).getWriteMode());
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            Map<String, Object> Pfilter = new HashMap<>();
            Pfilter.put("data_sink_type", "Relational");
            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            persistConfigRepository.deleteFromPersistConfig(Pfilter);
            dataPipelinesRepository.deleteFromDataPipelines(pipelineFilter);
            dataSourcesConnectionRepository.deleteFromDataSourcesConnections(filter);
            dataSourcesRepository.deleteFromDataSources(filter);
        }

    }
}