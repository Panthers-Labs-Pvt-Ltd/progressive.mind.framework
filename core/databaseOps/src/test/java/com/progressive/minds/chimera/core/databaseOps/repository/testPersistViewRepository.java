package com.progressive.minds.chimera.core.databaseOps.repository;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataPipelines;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataSources;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataSourcesConnections;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.persistConfig;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.persistView;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataPipelinesRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataSourcesConnectionRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataSourcesRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.persistConfigRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.persistViewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class testPersistViewRepository {

    private dataSourcesConnectionRepository dataSourcesConnectionRepository;
    private dataSourcesRepository dataSourcesRepository;
    private persistConfigRepository persistConfigRepository;
    private dataPipelinesRepository dataPipelinesRepository;
    private persistViewRepository persistViewRepository;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        System.setProperty("CHIMERA_EXE_ENV", "dev");
        dataSource = DataSourceConfig.getDataSource();
        dataSourcesConnectionRepository = new dataSourcesConnectionRepository();
        persistConfigRepository = new persistConfigRepository();
        dataSourcesRepository = new dataSourcesRepository();
        dataPipelinesRepository = new dataPipelinesRepository();
        persistViewRepository = new persistViewRepository();
    }


    @Test
    void testGetAllPersistConfigs_1 () {
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
        persistConfig pc = new persistConfig( "TestPipeline", 1, "Relational",
                "Postgres", "targetDB", "targetTable", "targetSchema",
                null, null, null, "Append",
                "Postgres_Local_Test_Connection", null, null, null,
                null, null, null, new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y" );
        persistConfig pc1 = new persistConfig( "TestPipeline", 2, "Relational",
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
            List<persistView> selectList = persistViewRepository.getAllPersistDetails();
            assertTrue(!selectList.isEmpty());
            assertEquals(2, selectList.size());
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
    void testGetPersistConfigWithFilters_5() {
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
        persistConfig pc = new persistConfig( "TestPipeline", 1, "Relational",
                "Postgres", "targetDB", "targetTable", "targetSchema",
                null, null, null, "Append",
                "Postgres_Local_Test_Connection", null, null, null,
                null, null, null, new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y" );
        persistConfig pc1 = new persistConfig( "TestPipeline", 2, "Relational",
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
            List<persistView> selectList = persistViewRepository.getPersistDetailsWithFilters(selFilter);
            assertTrue(!selectList.isEmpty());
            assertEquals(1, selectList.size());
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