package com.progressive.minds.chimera.core.databaseOps.repository;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataPipelines;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataSources;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataSourcesConnections;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.extractConfig;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.extractView;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataPipelinesRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataSourcesConnectionRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataSourcesRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.extractConfigRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.extractViewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class testExtractViewRepository {

    private dataSourcesConnectionRepository dataSourcesConnectionRepository;
    private dataSourcesRepository dataSourcesRepository;
    private extractConfigRepository extractConfigRepository;
    private dataPipelinesRepository dataPipelinesRepository;
    private extractViewRepository extractViewRepository;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        System.setProperty("CHIMERA_EXE_ENV", "dev");
        dataSource = DataSourceConfig.getDataSource();
        dataSourcesConnectionRepository = new dataSourcesConnectionRepository();
        extractConfigRepository = new extractConfigRepository();
        dataSourcesRepository = new dataSourcesRepository();
        dataPipelinesRepository = new dataPipelinesRepository();
        extractViewRepository = new extractViewRepository();
    }




    @Test
    void testGetAllExtractView_1 () {
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dataSourcesConnections dsc = new dataSourcesConnections("Postgres_Local_Test_Connection",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "chimera", "chimera123",
                null, null, null, null, null,null,null,null, null, null, null, null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test",
                "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y");
        extractConfig ec = new extractConfig("TestPipeline", 1, "Relational",
                "Postgres", null, null, null, null, null,
                "extractDF", null, "sampleTable", "public",
                "select * from sampleTable;", null, null, null,
                "Postgres_Local_Test_Connection", new Timestamp(System.currentTimeMillis()),
                "PK", null, null, "Y");
        extractConfig ec1 = new extractConfig( "TestPipeline", 2, "Relational",
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
            List<extractView> selectList = extractViewRepository.getAllExtractDetails();
            assertTrue(!selectList.isEmpty());
            assertEquals(2, selectList.size());
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
            assertEquals(dsc.getHost(), selectList.get(0).getHost());
            assertEquals(dsc.getPort(), selectList.get(0).getPort());
            assertEquals(dsc.getDatabaseName(), selectList.get(0).getConnectionDatabaseName());
            assertEquals(dsc.getSchemaName(), selectList.get(0).getConnectionSchemaName());
            assertEquals(dsc.getAuthenticationType(), selectList.get(0).getAuthenticationType());
            assertEquals(dsc.getUserName(), selectList.get(0).getUserName());
            assertEquals(dsc.getUserPassword(), selectList.get(0).getUserPassword());
            assertEquals(dsc.getRole(), selectList.get(0).getRole());
            assertEquals(dsc.getWarehouse(), selectList.get(0).getWarehouse());
            assertEquals(dsc.getPrincipal(), selectList.get(0).getPrincipal());
            assertEquals(dsc.getKeytab(), selectList.get(0).getKeytab());
            assertEquals(dsc.getSslCert(), selectList.get(0).getSslCert());
            assertEquals(dsc.getSslKey(), selectList.get(0).getSslKey());
            assertEquals(dsc.getSslRootCert(), selectList.get(0).getSslRootCert());
            assertEquals(dsc.getToken(), selectList.get(0).getToken());
            assertEquals(dsc.getKafkaBroker(), selectList.get(0).getKafkaBroker());
            assertEquals(dsc.getKafkaKeystoreType(), selectList.get(0).getKafkaKeystoreType());
            assertEquals(dsc.getKafkaKeystoreLocation(), selectList.get(0).getKafkaKeystoreLocation());
            assertEquals(dsc.getKafkaKeystorePassword(), selectList.get(0).getKafkaKeystorePassword());
            assertEquals(dsc.getKafkaTruststoreType(), selectList.get(0).getKafkaTruststoreType());
            assertEquals(dsc.getKafkaTruststoreLocation(), selectList.get(0).getKafkaTruststoreLocation());
            assertEquals(dsc.getKafkaTruststorePassword(), selectList.get(0).getKafkaTruststorePassword());
            assertEquals(dsc.getKafkaKeyPassword(), selectList.get(0).getKafkaKeyPassword());


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
            assertEquals(dsc.getHost(), selectList.get(1).getHost());
            assertEquals(dsc.getPort(), selectList.get(1).getPort());
            assertEquals(dsc.getDatabaseName(), selectList.get(1).getConnectionDatabaseName());
            assertEquals(dsc.getSchemaName(), selectList.get(1).getConnectionSchemaName());
            assertEquals(dsc.getAuthenticationType(), selectList.get(1).getAuthenticationType());
            assertEquals(dsc.getUserName(), selectList.get(1).getUserName());
            assertEquals(dsc.getUserPassword(), selectList.get(1).getUserPassword());
            assertEquals(dsc.getRole(), selectList.get(1).getRole());
            assertEquals(dsc.getWarehouse(), selectList.get(1).getWarehouse());
            assertEquals(dsc.getPrincipal(), selectList.get(1).getPrincipal());
            assertEquals(dsc.getKeytab(), selectList.get(1).getKeytab());
            assertEquals(dsc.getSslCert(), selectList.get(1).getSslCert());
            assertEquals(dsc.getSslKey(), selectList.get(1).getSslKey());
            assertEquals(dsc.getSslRootCert(), selectList.get(1).getSslRootCert());
            assertEquals(dsc.getToken(), selectList.get(1).getToken());
            assertEquals(dsc.getKafkaBroker(), selectList.get(1).getKafkaBroker());
            assertEquals(dsc.getKafkaKeystoreType(), selectList.get(1).getKafkaKeystoreType());
            assertEquals(dsc.getKafkaKeystoreLocation(), selectList.get(1).getKafkaKeystoreLocation());
            assertEquals(dsc.getKafkaKeystorePassword(), selectList.get(1).getKafkaKeystorePassword());
            assertEquals(dsc.getKafkaTruststoreType(), selectList.get(1).getKafkaTruststoreType());
            assertEquals(dsc.getKafkaTruststoreLocation(), selectList.get(1).getKafkaTruststoreLocation());
            assertEquals(dsc.getKafkaTruststorePassword(), selectList.get(1).getKafkaTruststorePassword());
            assertEquals(dsc.getKafkaKeyPassword(), selectList.get(1).getKafkaKeyPassword());


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
                null, null, null, null, null,null,null,null, null, null, null, null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test",
                "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y");
        extractConfig ec = new extractConfig( "TestPipeline", 1, "Relational",
                "Postgres", null, null, null, null, null,
                "extractDF", null, "sampleTable", "public",
                "select * from sampleTable;", null, null, null,
                "Postgres_Local_Test_Connection", new Timestamp(System.currentTimeMillis()),
                "PK", null, null, "Y");
        extractConfig ec1 = new extractConfig( "TestPipeline", 2, "Relational",
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
            selFilter.put("source_table_name", "sampleTable1");
            List<extractView> selectList = extractViewRepository.getExtractDetailsWithFilters(selFilter);
            assertTrue(!selectList.isEmpty());
            assertEquals(1, selectList.size());
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
            assertEquals(dsc.getHost(), selectList.get(0).getHost());
            assertEquals(dsc.getPort(), selectList.get(0).getPort());
            assertEquals(dsc.getDatabaseName(), selectList.get(0).getConnectionDatabaseName());
            assertEquals(dsc.getSchemaName(), selectList.get(0).getConnectionSchemaName());
            assertEquals(dsc.getAuthenticationType(), selectList.get(0).getAuthenticationType());
            assertEquals(dsc.getUserName(), selectList.get(0).getUserName());
            assertEquals(dsc.getUserPassword(), selectList.get(0).getUserPassword());
            assertEquals(dsc.getRole(), selectList.get(0).getRole());
            assertEquals(dsc.getWarehouse(), selectList.get(0).getWarehouse());
            assertEquals(dsc.getPrincipal(), selectList.get(0).getPrincipal());
            assertEquals(dsc.getKeytab(), selectList.get(0).getKeytab());
            assertEquals(dsc.getSslCert(), selectList.get(0).getSslCert());
            assertEquals(dsc.getSslKey(), selectList.get(0).getSslKey());
            assertEquals(dsc.getSslRootCert(), selectList.get(0).getSslRootCert());
            assertEquals(dsc.getToken(), selectList.get(0).getToken());
            assertEquals(dsc.getKafkaBroker(), selectList.get(0).getKafkaBroker());
            assertEquals(dsc.getKafkaKeystoreType(), selectList.get(0).getKafkaKeystoreType());
            assertEquals(dsc.getKafkaKeystoreLocation(), selectList.get(0).getKafkaKeystoreLocation());
            assertEquals(dsc.getKafkaKeystorePassword(), selectList.get(0).getKafkaKeystorePassword());
            assertEquals(dsc.getKafkaTruststoreType(), selectList.get(0).getKafkaTruststoreType());
            assertEquals(dsc.getKafkaTruststoreLocation(), selectList.get(0).getKafkaTruststoreLocation());
            assertEquals(dsc.getKafkaTruststorePassword(), selectList.get(0).getKafkaTruststorePassword());
            assertEquals(dsc.getKafkaKeyPassword(), selectList.get(0).getKafkaKeyPassword());

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


    }