package com.progressive.minds.chimera.core.databaseOps.repository;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataSources;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataSourcesConnections;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataSourcesConnectionRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataSourcesRepository;
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

public class testDataSourcesConnectionsRepository {

    private dataSourcesConnectionRepository dataSourcesConnectionRepository;
    private dataSourcesRepository dataSourcesRepository;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        System.setProperty("CHIMERA_EXE_ENV", "dev");
        dataSource = DataSourceConfig.getDataSource();
        dataSourcesConnectionRepository = new dataSourcesConnectionRepository();
        dataSourcesRepository = new dataSourcesRepository();
    }

    @Test
    void testputDataSourcesConnections_validSingleRecord_1()  {
        // Arrange
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dataSourcesRepository.putDataSources(ds);
        System.out.println("Insert Operation Successful on table data_sources.");

        dataSourcesConnections dsc = new dataSourcesConnections("Postgres_Local_Test_Connection",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "chimera", "chimera123",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");
        try {
            dataSourcesConnectionRepository.putDataSourcesConnections(dsc);
            System.out.println("Insert Operation Successful on table data_sources_connections.");
        } catch (Exception e) {
            System.out.println("ErrorMessage: " + e.getMessage());
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains("Foreign Key Violation. Record is missing in Parent Table."));
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            dataSourcesConnectionRepository.deleteFromDataSourcesConnections(filter);
            dataSourcesRepository.deleteFromDataSources(filter);
        }
    }

    @Test
    void testputDataSourcesConnection_invalidSingleRecord_2()  {
        // Arrange
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dataSourcesRepository.putDataSources(ds);
        System.out.println("Insert Operation Successful on table data_sources.");

        dataSourcesConnections dsc = new dataSourcesConnections("Postgres_Local_Test_Connection",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username123&Password", "chimera", "chimera123",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");

        try {
            dataSourcesConnectionRepository.putDataSourcesConnections(dsc);
            System.out.println("Insert Operation Successful on table data_sources_connections.");
        } catch (Exception e) {
            System.out.println("ErrorMessage - " + e.getMessage());
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains("Check Constraint is violated"),
                    "Expected 'Check Constraint is violated' in the error message.");
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            dataSourcesConnectionRepository.deleteFromDataSourcesConnections(filter);
            dataSourcesRepository.deleteFromDataSources(filter);
        }
    }

    @Test
    void testputDataSourcesConnections_validMultipleRecord_3()  {
        // Arrange
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dataSourcesRepository.putDataSources(ds);
        System.out.println("Insert Operation Successful on table data_sources.");

        List<dataSourcesConnections> dscList = new ArrayList<dataSourcesConnections>();
        dataSourcesConnections dsc = new dataSourcesConnections("Postgres_Local_Test_Connection",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "chimera", "chimera123",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");
        dataSourcesConnections dsc1 = new dataSourcesConnections("Postgres_Local_Test_Connection_new",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "admin", "admin",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");

        dscList.add(dsc);
        dscList.add(dsc1);

        try {
            dataSourcesConnectionRepository.putDataSourcesConnections(dscList);
            System.out.println("Insert Operation Successful on table data_sources_connections.");
        } catch (Exception e) {
            assertEquals("A Record with the given key already exists. ERROR: duplicate key value violates unique constraint \"pk_chimera_data_sources\"\n" +
                    "  Detail: Key (data_source_type, data_source_sub_type)=(Relational, Postgres) already exists.", e.getMessage());
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            //filter.put("data_source_sub_type", "Postgres");
            dataSourcesRepository.deleteFromDataSources(filter);
            dataSourcesConnectionRepository.deleteFromDataSourcesConnections(filter);
        }
    }

    @Test
    void testGetAllDataSources_4 () {
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dataSourcesRepository.putDataSources(ds);
        System.out.println("Insert Operation Successful on table data_sources.");

        List<dataSourcesConnections> dscList = new ArrayList<dataSourcesConnections>();
        dataSourcesConnections dsc = new dataSourcesConnections("Postgres_Local_Test_Connection",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "chimera", "chimera123",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");
        dataSourcesConnections dsc1 = new dataSourcesConnections("Postgres_Local_Test_Connection_new",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "admin", "admin",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");

        dscList.add(dsc);
        dscList.add(dsc1);

        try {
            dataSourcesConnectionRepository.putDataSourcesConnections(dscList);
        } catch (Exception e) {
            System.out.println("Insert Op not required");
        } finally {
            List<dataSourcesConnections> selectList = dataSourcesConnectionRepository.getAllDataSourcesConnections();
            assertTrue(!selectList.isEmpty());
            assertEquals(2, selectList.size());
            assertEquals(dscList.get(0).getDataSourceConnectionName(), selectList.get(0).getDataSourceConnectionName());
            assertEquals(dscList.get(0).getDataSourceType(), selectList.get(0).getDataSourceType());
            assertEquals(dscList.get(0).getDataSourceSubType(), selectList.get(0).getDataSourceSubType());
            assertEquals(dscList.get(0).getHost(), selectList.get(0).getHost());
            assertEquals(dscList.get(0).getPort(), selectList.get(0).getPort());
            assertEquals(dscList.get(0).getDatabaseName(), selectList.get(0).getDatabaseName());
            assertEquals(dscList.get(0).getSchemaName(), selectList.get(0).getSchemaName());
            assertEquals(dscList.get(0).getAuthenticationType(), selectList.get(0).getAuthenticationType());
            assertEquals(dscList.get(0).getUserName(), selectList.get(0).getUserName());
            assertEquals(dscList.get(0).getUserPassword(), selectList.get(0).getUserPassword());
            assertEquals(dscList.get(0).getRole(), selectList.get(0).getRole());
            assertEquals(dscList.get(0).getWarehouse(), selectList.get(0).getWarehouse());
            assertEquals(dscList.get(0).getPrincipal(), selectList.get(0).getPrincipal());
            assertEquals(dscList.get(0).getKeytab(), selectList.get(0).getKeytab());
            assertEquals(dscList.get(0).getSslCert(), selectList.get(0).getSslCert());
            assertEquals(dscList.get(0).getSslKey(), selectList.get(0).getSslKey());
            assertEquals(dscList.get(0).getSslRootCert(), selectList.get(0).getSslRootCert());
            assertEquals(dscList.get(0).getToken(), selectList.get(0).getToken());
            assertEquals(dscList.get(0).getKafkaBroker(), selectList.get(0).getKafkaBroker());
            assertEquals(dscList.get(0).getKafkaKeystoreType(), selectList.get(0).getKafkaKeystoreType());
            assertEquals(dscList.get(0).getKafkaKeystoreLocation(), selectList.get(0).getKafkaKeystoreLocation());
            assertEquals(dscList.get(0).getKafkaKeystorePassword(), selectList.get(0).getKafkaKeystorePassword());
            assertEquals(dscList.get(0).getKafkaTruststoreType(), selectList.get(0).getKafkaTruststoreType());
            assertEquals(dscList.get(0).getKafkaTruststoreLocation(), selectList.get(0).getKafkaTruststoreLocation());
            assertEquals(dscList.get(0).getKafkaTruststorePassword(), selectList.get(0).getKafkaTruststorePassword());
            assertEquals(dscList.get(0).getKafkaKeyPassword(), selectList.get(0).getKafkaKeyPassword());
            assertEquals(dscList.get(0).getCreatedBy(), selectList.get(0).getCreatedBy());
            assertEquals(dscList.get(0).getUpdatedBy(), selectList.get(0).getUpdatedBy());
            assertEquals(dscList.get(0).getUpdatedTimestamp(), selectList.get(0).getUpdatedTimestamp());
            assertEquals(dscList.get(0).getActiveFlag(), selectList.get(0).getActiveFlag());
            assertEquals(dscList.get(1).getDataSourceConnectionName(), selectList.get(1).getDataSourceConnectionName());
            assertEquals(dscList.get(1).getDataSourceType(), selectList.get(1).getDataSourceType());
            assertEquals(dscList.get(1).getDataSourceSubType(), selectList.get(1).getDataSourceSubType());
            assertEquals(dscList.get(1).getHost(), selectList.get(1).getHost());
            assertEquals(dscList.get(1).getPort(), selectList.get(1).getPort());
            assertEquals(dscList.get(1).getDatabaseName(), selectList.get(1).getDatabaseName());
            assertEquals(dscList.get(1).getSchemaName(), selectList.get(1).getSchemaName());
            assertEquals(dscList.get(1).getAuthenticationType(), selectList.get(1).getAuthenticationType());
            assertEquals(dscList.get(1).getUserName(), selectList.get(1).getUserName());
            assertEquals(dscList.get(1).getUserPassword(), selectList.get(1).getUserPassword());
            assertEquals(dscList.get(1).getRole(), selectList.get(1).getRole());
            assertEquals(dscList.get(1).getWarehouse(), selectList.get(1).getWarehouse());
            assertEquals(dscList.get(1).getPrincipal(), selectList.get(1).getPrincipal());
            assertEquals(dscList.get(1).getKeytab(), selectList.get(1).getKeytab());
            assertEquals(dscList.get(1).getSslCert(), selectList.get(1).getSslCert());
            assertEquals(dscList.get(1).getSslKey(), selectList.get(1).getSslKey());
            assertEquals(dscList.get(1).getSslRootCert(), selectList.get(1).getSslRootCert());
            assertEquals(dscList.get(1).getToken(), selectList.get(1).getToken());
            assertEquals(dscList.get(1).getKafkaBroker(), selectList.get(1).getKafkaBroker());
            assertEquals(dscList.get(1).getKafkaKeystoreType(), selectList.get(1).getKafkaKeystoreType());
            assertEquals(dscList.get(1).getKafkaKeystoreLocation(), selectList.get(1).getKafkaKeystoreLocation());
            assertEquals(dscList.get(1).getKafkaKeystorePassword(), selectList.get(1).getKafkaKeystorePassword());
            assertEquals(dscList.get(1).getKafkaTruststoreType(), selectList.get(1).getKafkaTruststoreType());
            assertEquals(dscList.get(1).getKafkaTruststoreLocation(), selectList.get(1).getKafkaTruststoreLocation());
            assertEquals(dscList.get(1).getKafkaTruststorePassword(), selectList.get(1).getKafkaTruststorePassword());
            assertEquals(dscList.get(1).getKafkaKeyPassword(), selectList.get(1).getKafkaKeyPassword());
            assertEquals(dscList.get(1).getCreatedBy(), selectList.get(1).getCreatedBy());
            assertEquals(dscList.get(1).getUpdatedBy(), selectList.get(1).getUpdatedBy());
            assertEquals(dscList.get(1).getUpdatedTimestamp(), selectList.get(1).getUpdatedTimestamp());
            assertEquals(dscList.get(1).getActiveFlag(), selectList.get(1).getActiveFlag());


            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            int dsrowNum = dataSourcesRepository.deleteFromDataSources(filter);
            int dscrowNum = dataSourcesConnectionRepository.deleteFromDataSourcesConnections(filter);
            System.out.println("No. of Rows successfully Deleted from data_sources: " + dsrowNum + "\nNo. of Rows successfully Deleted from data_sources_connections: " + dscrowNum);


        }




    }

    @Test
    void testGetDataSourcesConnectionsWithFilters_5() {
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dataSourcesRepository.putDataSources(ds);
        System.out.println("Insert Operation Successful on table data_sources.");

        List<dataSourcesConnections> dscList = new ArrayList<dataSourcesConnections>();
        dataSourcesConnections dsc = new dataSourcesConnections("Postgres_Local_Test_Connection",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "chimera", "chimera123",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");
        dataSourcesConnections dsc1 = new dataSourcesConnections("Postgres_Local_Test_Connection_new",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "admin", "admin",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");

        dscList.add(dsc);
        dscList.add(dsc1);

        try {
            dataSourcesConnectionRepository.putDataSourcesConnections(dscList);
        } catch (Exception e) {
            System.out.println("Insert Op not required");
        } finally {
            Map<String, Object> selFilter = new HashMap<>();
            selFilter.put("data_source_connection_name", "Postgres_Local_Test_Connection");
            List<dataSourcesConnections> selectList = dataSourcesConnectionRepository.getDataSourcesConnectionsWithFilters(selFilter);
            assertTrue(!selectList.isEmpty());
            assertEquals(1, selectList.size());
            assertEquals(dscList.get(0).getDataSourceConnectionName(), selectList.get(0).getDataSourceConnectionName());
            assertEquals(dscList.get(0).getDataSourceType(), selectList.get(0).getDataSourceType());
            assertEquals(dscList.get(0).getDataSourceSubType(), selectList.get(0).getDataSourceSubType());
            assertEquals(dscList.get(0).getHost(), selectList.get(0).getHost());
            assertEquals(dscList.get(0).getPort(), selectList.get(0).getPort());
            assertEquals(dscList.get(0).getDatabaseName(), selectList.get(0).getDatabaseName());
            assertEquals(dscList.get(0).getSchemaName(), selectList.get(0).getSchemaName());
            assertEquals(dscList.get(0).getAuthenticationType(), selectList.get(0).getAuthenticationType());
            assertEquals(dscList.get(0).getUserName(), selectList.get(0).getUserName());
            assertEquals(dscList.get(0).getUserPassword(), selectList.get(0).getUserPassword());
            assertEquals(dscList.get(0).getRole(), selectList.get(0).getRole());
            assertEquals(dscList.get(0).getWarehouse(), selectList.get(0).getWarehouse());
            assertEquals(dscList.get(0).getPrincipal(), selectList.get(0).getPrincipal());
            assertEquals(dscList.get(0).getKeytab(), selectList.get(0).getKeytab());
            assertEquals(dscList.get(0).getSslCert(), selectList.get(0).getSslCert());
            assertEquals(dscList.get(0).getSslKey(), selectList.get(0).getSslKey());
            assertEquals(dscList.get(0).getSslRootCert(), selectList.get(0).getSslRootCert());
            assertEquals(dscList.get(0).getToken(), selectList.get(0).getToken());
            assertEquals(dscList.get(0).getKafkaBroker(), selectList.get(0).getKafkaBroker());
            assertEquals(dscList.get(0).getKafkaKeystoreType(), selectList.get(0).getKafkaKeystoreType());
            assertEquals(dscList.get(0).getKafkaKeystoreLocation(), selectList.get(0).getKafkaKeystoreLocation());
            assertEquals(dscList.get(0).getKafkaKeystorePassword(), selectList.get(0).getKafkaKeystorePassword());
            assertEquals(dscList.get(0).getKafkaTruststoreType(), selectList.get(0).getKafkaTruststoreType());
            assertEquals(dscList.get(0).getKafkaTruststoreLocation(), selectList.get(0).getKafkaTruststoreLocation());
            assertEquals(dscList.get(0).getKafkaTruststorePassword(), selectList.get(0).getKafkaTruststorePassword());
            assertEquals(dscList.get(0).getKafkaKeyPassword(), selectList.get(0).getKafkaKeyPassword());
            assertEquals(dscList.get(0).getCreatedBy(), selectList.get(0).getCreatedBy());
            assertEquals(dscList.get(0).getUpdatedBy(), selectList.get(0).getUpdatedBy());
            assertEquals(dscList.get(0).getUpdatedTimestamp(), selectList.get(0).getUpdatedTimestamp());
            assertEquals(dscList.get(0).getActiveFlag(), selectList.get(0).getActiveFlag());
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            int dsrowNum = dataSourcesRepository.deleteFromDataSources(filter);
            int dscrowNum = dataSourcesConnectionRepository.deleteFromDataSourcesConnections(filter);
            System.out.println("No. of Rows successfully Deleted from data_sources: " + dsrowNum + "\nNo. of Rows successfully Deleted from data_sources_connections: " + dscrowNum);



        }


    }

    @Test
    void testUpdateDataSourcesConnections_6() {
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dataSourcesRepository.putDataSources(ds);
        System.out.println("Insert Operation Successful on table data_sources.");

        List<dataSourcesConnections> dscList = new ArrayList<dataSourcesConnections>();
        dataSourcesConnections dsc = new dataSourcesConnections("Postgres_Local_Test_Connection",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "chimera", "chimera123",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");
        dataSourcesConnections dsc1 = new dataSourcesConnections("Postgres_Local_Test_Connection_new",
                "Relational", "Postgres", "localhost", 5432, "chimera_db",
                null, "Username&Password", "admin", "admin",
                null, null, null, null, null,null,null,null,
                null,null,null,null,null,
                null,null,null, new Timestamp(System.currentTimeMillis()),"PK",
                null, null, "Y");

        dscList.add(dsc);
        dscList.add(dsc1);

        try {
            dataSourcesConnectionRepository.putDataSourcesConnections(dscList);
        } catch (Exception e) {
            System.out.println("Insert Op not required");
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");

            Map<String, Object> updateFields = new HashMap<>();
            updateFields.put("sslcert", "New Description");

            dataSourcesConnectionRepository.updateDataSourcesConnections(updateFields, filter);
            List<dataSourcesConnections> selectList = dataSourcesConnectionRepository.getDataSourcesConnectionsWithFilters(filter);
            assertEquals("New Description", selectList.get(0).getSslCert());
            Map<String, Object> deleteFilter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            //filter.put("data_source_sub_type", "Postgres");
            int rowNum = dataSourcesRepository.deleteFromDataSources(deleteFilter);
            System.out.println("No. of Rows successfully Deleted : " + rowNum);
        }

    }
}