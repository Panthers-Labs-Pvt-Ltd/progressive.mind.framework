package com.progressive.minds.chimera.core.databaseOps.repository;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataPipelines;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataSources;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataSourcesConnections;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.extractConfig;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataPipelinesRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataSourcesConnectionRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataSourcesRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.extractConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class testExtractConfigRepository {

    private dataSourcesConnectionRepository dataSourcesConnectionRepository;
    private dataSourcesRepository dataSourcesRepository;
    private extractConfigRepository extractConfigRepository;
    private dataPipelinesRepository dataPipelinesRepository;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        System.setProperty("CHIMERA_EXE_ENV", "dev");
        dataSource = DataSourceConfig.getDataSource();
        dataSourcesConnectionRepository = new dataSourcesConnectionRepository();
        extractConfigRepository = new extractConfigRepository();
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
        extractConfig ec = new extractConfig(1, "TestPipeline", 1, "Relational",
                "Postgres", null, null, null, null, null,
                "extractDF", null, "sampleTable", "public",
                "select * from sampleTable;", null, null, null,
                "Postgres_Local_Test_Connection", new Timestamp(System.currentTimeMillis()),
                "PK", null, null, "Y");
        try {
            dataSourcesRepository.putDataSources(ds);
            System.out.println("Insert Operation Successful on table data_sources.");
            dataSourcesConnectionRepository.putDataSourcesConnections(dsc);
            System.out.println("Insert Operation Successful on table data_sources_connections.");
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            extractConfigRepository.putExtractConfig(ec);
            System.out.println("Insert Operation Successful on extract_config.");
        } catch (Exception e) {
            System.out.println("ErrorMessage: " + e.getMessage());
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains("Foreign Key Violation. Record is missing in Parent Table."));
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            extractConfigRepository.deleteFromExtractConfig(filter);
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
        extractConfig ec = new extractConfig(1, "TestPipeline", 1, "Relational",
                "Postgres", null, null, null, null, null,
                "extractDF", null, "sampleTable", "public",
                "select * from sampleTable;", null, null, null,
                "Postgres_Local_Test_Connection", new Timestamp(System.currentTimeMillis()),
                "PK", null, null, "Y");
        try {
            dataSourcesRepository.putDataSources(ds);
            System.out.println("Insert Operation Successful on table data_sources.");
            dataSourcesConnectionRepository.putDataSourcesConnections(dsc);
            System.out.println("Insert Operation Successful on table data_sources_connections.");
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            extractConfigRepository.putExtractConfig(ec);
            System.out.println("Insert Operation Successful on extract_config.");
        } catch (Exception e) {
            System.out.println("ErrorMessage - " + e.getMessage());
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains("Foreign Key Violation. Record is missing in Parent Table."),
                    "Expected 'Foreign Key Violation. Record is missing in Parent Table.' in the error message.");
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            extractConfigRepository.deleteFromExtractConfig(filter);
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
        extractConfig ec = new extractConfig(1, "TestPipeline", 1, "Relational",
                "Postgres", null, null, null, null, null,
                "extractDF", null, "sampleTable", "public",
                "select * from sampleTable;", null, null, null,
                "Postgres_Local_Test_Connection", new Timestamp(System.currentTimeMillis()),
                "PK", null, null, "Y");
        extractConfig ec1 = new extractConfig(2, "TestPipeline", 1, "Relational",
                "Postgres", null, null, null, null, null,
                "extractDF", null, "sampleTable1", "public",
                "select * from sampleTable;", null, null, null,
                "Postgres_Local_Test_Connection", new Timestamp(System.currentTimeMillis()),
                "PK", null, null, "Y");
        List<extractConfig> ecList = new ArrayList<extractConfig>();
        ecList.add(ec);
        ecList.add(ec1);
        try {
            dataSourcesRepository.putDataSources(ds);
            System.out.println("Insert Operation Successful on table data_sources.");
            dataSourcesConnectionRepository.putDataSourcesConnections(dsc);
            System.out.println("Insert Operation Successful on table data_sources_connections.");
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            extractConfigRepository.putExtractConfig(ecList);
            System.out.println("Insert Operation Successful on extract_config.");
        } catch (Exception e) {
            assertEquals("A Record with the given key already exists. ERROR: duplicate key value violates unique constraint \"pk_chimera_data_sources\"\n" +
                    "  Detail: Key (data_source_type, data_source_sub_type)=(Relational, Postgres) already exists.", e.getMessage());
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            extractConfigRepository.deleteFromExtractConfig(filter);
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
        extractConfig ec = new extractConfig(1, "TestPipeline", 1, "Relational",
                "Postgres", null, null, null, null, null,
                "extractDF", null, "sampleTable", "public",
                "select * from sampleTable;", null, null, null,
                "Postgres_Local_Test_Connection", new Timestamp(System.currentTimeMillis()),
                "PK", null, null, "Y");
        extractConfig ec1 = new extractConfig(2, "TestPipeline", 1, "Relational",
                "Postgres", null, null, null, null, null,
                "extractDF", null, "sampleTable1", "public",
                "select * from sampleTable;", null, null, null,
                "Postgres_Local_Test_Connection", new Timestamp(System.currentTimeMillis()),
                "PK", null, null, "Y");
        List<extractConfig> ecList = new ArrayList<extractConfig>();
        ecList.add(ec);
        ecList.add(ec1);
        try {
            dataSourcesRepository.putDataSources(ds);
            System.out.println("Insert Operation Successful on table data_sources.");
            dataSourcesConnectionRepository.putDataSourcesConnections(dsc);
            System.out.println("Insert Operation Successful on table data_sources_connections.");
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            extractConfigRepository.putExtractConfig(ecList);
            System.out.println("Insert Operation Successful on extract_config.");
        } catch (Exception e) {
            System.out.println("Insert Op not required");
        } finally {
            List<extractConfig> selectList = extractConfigRepository.getAllExtractConfig();
            assertTrue(!selectList.isEmpty());
            assertEquals(2, selectList.size());
            assertEquals(ecList.get(0).getUniqueId(), selectList.get(0).getUniqueId());
            assertEquals(ecList.get(0).getPipelineName(), selectList.get(0).getPipelineName());
            assertEquals(ecList.get(0).getSequenceNumber(), selectList.get(0).getSequenceNumber());
            assertEquals(ecList.get(0).getDataSourceType(), selectList.get(0).getDataSourceType());
            assertEquals(ecList.get(0).getDataSourceSubType(), selectList.get(0).getDataSourceSubType());
            assertEquals(ecList.get(0).getFileName(), selectList.get(0).getFileName());
            assertEquals(ecList.get(0).getFilePath(), selectList.get(0).getFilePath());
            assertEquals(ecList.get(0).getSchemaPath(), selectList.get(0).getSchemaPath());
            assertEquals(ecList.get(0).getRowFilter(), selectList.get(0).getRowFilter());
            assertEquals(ecList.get(0).getColumnFilter(), selectList.get(0).getColumnFilter());
            assertEquals(ecList.get(0).getExtractDataframeName(), selectList.get(0).getExtractDataframeName());
            assertEquals(ecList.get(0).getSourceConfiguration(), selectList.get(0).getSourceConfiguration());
            assertEquals(ecList.get(0).getTableName(), selectList.get(0).getTableName());
            assertEquals(ecList.get(0).getSchemaName(), selectList.get(0).getSchemaName());
            assertEquals(ecList.get(0).getSqlText(), selectList.get(0).getSqlText());
            assertEquals(ecList.get(0).getKafkaConsumerTopic(), selectList.get(0).getKafkaConsumerTopic());
            assertEquals(ecList.get(0).getKafkaConsumerGroup(), selectList.get(0).getKafkaConsumerGroup());
            assertEquals(ecList.get(0).getKafkaStartOffset(), selectList.get(0).getKafkaStartOffset());
            assertEquals(ecList.get(0).getDataSourceConnectionName(), selectList.get(0).getDataSourceConnectionName());
            assertEquals(ecList.get(0).getCreatedBy(), selectList.get(0).getCreatedBy());
            assertEquals(ecList.get(0).getUpdatedBy(), selectList.get(0).getUpdatedBy());
            assertEquals(ecList.get(0).getUpdatedTimestamp(), selectList.get(0).getUpdatedTimestamp());
            assertEquals(ecList.get(0).getActiveFlag(), selectList.get(0).getActiveFlag());

            assertEquals(ecList.get(1).getUniqueId(), selectList.get(1).getUniqueId());
            assertEquals(ecList.get(1).getPipelineName(), selectList.get(1).getPipelineName());
            assertEquals(ecList.get(1).getSequenceNumber(), selectList.get(1).getSequenceNumber());
            assertEquals(ecList.get(1).getDataSourceType(), selectList.get(1).getDataSourceType());
            assertEquals(ecList.get(1).getDataSourceSubType(), selectList.get(1).getDataSourceSubType());
            assertEquals(ecList.get(1).getFileName(), selectList.get(1).getFileName());
            assertEquals(ecList.get(1).getFilePath(), selectList.get(1).getFilePath());
            assertEquals(ecList.get(1).getSchemaPath(), selectList.get(1).getSchemaPath());
            assertEquals(ecList.get(1).getRowFilter(), selectList.get(1).getRowFilter());
            assertEquals(ecList.get(1).getColumnFilter(), selectList.get(1).getColumnFilter());
            assertEquals(ecList.get(1).getExtractDataframeName(), selectList.get(1).getExtractDataframeName());
            assertEquals(ecList.get(1).getSourceConfiguration(), selectList.get(1).getSourceConfiguration());
            assertEquals(ecList.get(1).getTableName(), selectList.get(1).getTableName());
            assertEquals(ecList.get(1).getSchemaName(), selectList.get(1).getSchemaName());
            assertEquals(ecList.get(1).getSqlText(), selectList.get(1).getSqlText());
            assertEquals(ecList.get(1).getKafkaConsumerTopic(), selectList.get(1).getKafkaConsumerTopic());
            assertEquals(ecList.get(1).getKafkaConsumerGroup(), selectList.get(1).getKafkaConsumerGroup());
            assertEquals(ecList.get(1).getKafkaStartOffset(), selectList.get(1).getKafkaStartOffset());
            assertEquals(ecList.get(1).getDataSourceConnectionName(), selectList.get(1).getDataSourceConnectionName());
            assertEquals(ecList.get(1).getCreatedBy(), selectList.get(1).getCreatedBy());
            assertEquals(ecList.get(1).getUpdatedBy(), selectList.get(1).getUpdatedBy());
            assertEquals(ecList.get(1).getUpdatedTimestamp(), selectList.get(1).getUpdatedTimestamp());
            assertEquals(ecList.get(1).getActiveFlag(), selectList.get(1).getActiveFlag());

            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            extractConfigRepository.deleteFromExtractConfig(filter);
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
        extractConfig ec = new extractConfig(1, "TestPipeline", 1, "Relational",
                "Postgres", null, null, null, null, null,
                "extractDF", null, "sampleTable", "public",
                "select * from sampleTable;", null, null, null,
                "Postgres_Local_Test_Connection", new Timestamp(System.currentTimeMillis()),
                "PK", null, null, "Y");
        extractConfig ec1 = new extractConfig(2, "TestPipeline", 1, "Relational",
                "Postgres", null, null, null, null, null,
                "extractDF", null, "sampleTable1", "public",
                "select * from sampleTable;", null, null, null,
                "Postgres_Local_Test_Connection", new Timestamp(System.currentTimeMillis()),
                "PK", null, null, "Y");
        List<extractConfig> ecList = new ArrayList<extractConfig>();
        ecList.add(ec);
        ecList.add(ec1);
        try {
            dataSourcesRepository.putDataSources(ds);
            System.out.println("Insert Operation Successful on table data_sources.");
            dataSourcesConnectionRepository.putDataSourcesConnections(dsc);
            System.out.println("Insert Operation Successful on table data_sources_connections.");
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            extractConfigRepository.putExtractConfig(ecList);
            System.out.println("Insert Operation Successful on extract_config.");
        } catch (Exception e) {
            System.out.println("Insert Op not required");
        } finally {
            Map<String, Object> selFilter = new HashMap<>();
            selFilter.put("table_name", "sampleTable1");
            List<extractConfig> selectList = extractConfigRepository.getExtractConfigWithFilters(selFilter);
            assertTrue(!selectList.isEmpty());
            assertEquals(1, selectList.size());
            assertEquals(ecList.get(1).getUniqueId(), selectList.get(0).getUniqueId());
            assertEquals(ecList.get(1).getPipelineName(), selectList.get(0).getPipelineName());
            assertEquals(ecList.get(1).getSequenceNumber(), selectList.get(0).getSequenceNumber());
            assertEquals(ecList.get(1).getDataSourceType(), selectList.get(0).getDataSourceType());
            assertEquals(ecList.get(1).getDataSourceSubType(), selectList.get(0).getDataSourceSubType());
            assertEquals(ecList.get(1).getFileName(), selectList.get(0).getFileName());
            assertEquals(ecList.get(1).getFilePath(), selectList.get(0).getFilePath());
            assertEquals(ecList.get(1).getSchemaPath(), selectList.get(0).getSchemaPath());
            assertEquals(ecList.get(1).getRowFilter(), selectList.get(0).getRowFilter());
            assertEquals(ecList.get(1).getColumnFilter(), selectList.get(0).getColumnFilter());
            assertEquals(ecList.get(1).getExtractDataframeName(), selectList.get(0).getExtractDataframeName());
            assertEquals(ecList.get(1).getSourceConfiguration(), selectList.get(0).getSourceConfiguration());
            assertEquals(ecList.get(1).getTableName(), selectList.get(0).getTableName());
            assertEquals(ecList.get(1).getSchemaName(), selectList.get(0).getSchemaName());
            assertEquals(ecList.get(1).getSqlText(), selectList.get(0).getSqlText());
            assertEquals(ecList.get(1).getKafkaConsumerTopic(), selectList.get(0).getKafkaConsumerTopic());
            assertEquals(ecList.get(1).getKafkaConsumerGroup(), selectList.get(0).getKafkaConsumerGroup());
            assertEquals(ecList.get(1).getKafkaStartOffset(), selectList.get(0).getKafkaStartOffset());
            assertEquals(ecList.get(1).getDataSourceConnectionName(), selectList.get(0).getDataSourceConnectionName());
            assertEquals(ecList.get(1).getCreatedBy(), selectList.get(0).getCreatedBy());
            assertEquals(ecList.get(1).getUpdatedBy(), selectList.get(0).getUpdatedBy());
            assertEquals(ecList.get(1).getUpdatedTimestamp(), selectList.get(0).getUpdatedTimestamp());
            assertEquals(ecList.get(1).getActiveFlag(), selectList.get(0).getActiveFlag());

            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            extractConfigRepository.deleteFromExtractConfig(filter);
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
        extractConfig ec = new extractConfig(1, "TestPipeline", 1, "Relational",
                "Postgres", null, null, null, null, null,
                "extractDF", null, "sampleTable", "public",
                "select * from sampleTable;", null, null, null,
                "Postgres_Local_Test_Connection", new Timestamp(System.currentTimeMillis()),
                "PK", null, null, "Y");
        extractConfig ec1 = new extractConfig(2, "TestPipeline", 1, "Relational",
                "Postgres", null, null, null, null, null,
                "extractDF", null, "sampleTable1", "public",
                "select * from sampleTable;", null, null, null,
                "Postgres_Local_Test_Connection", new Timestamp(System.currentTimeMillis()),
                "PK", null, null, "Y");
        List<extractConfig> ecList = new ArrayList<extractConfig>();
        ecList.add(ec);
        ecList.add(ec1);
        try {
            dataSourcesRepository.putDataSources(ds);
            System.out.println("Insert Operation Successful on table data_sources.");
            dataSourcesConnectionRepository.putDataSourcesConnections(dsc);
            System.out.println("Insert Operation Successful on table data_sources_connections.");
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            extractConfigRepository.putExtractConfig(ecList);
            System.out.println("Insert Operation Successful on extract_config.");
        } catch (Exception e) {
            System.out.println("Insert Op not required");
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("table_name", "sampleTable1");

            Map<String, Object> updateFields = new HashMap<>();
            updateFields.put("extract_dataframe_name", "New_DF");

            extractConfigRepository.updateExtractConfig(updateFields, filter);
            List<extractConfig> selectList = extractConfigRepository.getExtractConfigWithFilters(filter);
            assertEquals("New_DF", selectList.get(0).getExtractDataframeName());
            Map<String, Object> DelFilter = new HashMap<>();
            DelFilter.put("data_source_type", "Relational");
            DelFilter.put("data_source_sub_type", "Postgres");
            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            extractConfigRepository.deleteFromExtractConfig(DelFilter);
            dataPipelinesRepository.deleteFromDataPipelines(pipelineFilter);
            dataSourcesConnectionRepository.deleteFromDataSourcesConnections(DelFilter);
            dataSourcesRepository.deleteFromDataSources(DelFilter);
        }

    }
}