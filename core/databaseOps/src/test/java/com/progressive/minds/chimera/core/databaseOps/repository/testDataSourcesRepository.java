package com.progressive.minds.chimera.core.databaseOps.repository;

import static org.junit.jupiter.api.Assertions.*;
import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataSources;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataSourcesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class testDataSourcesRepository {

    private dataSourcesRepository dataSourcesRepository;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        System.setProperty("CHIMERA_EXE_ENV", "dev");
        dataSource = DataSourceConfig.getDataSource();
        dataSourcesRepository = new dataSourcesRepository();
    }

    @Test
    void testputDataSources_validSingleRecord_1()  {
        // Arrange
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        try {
            dataSourcesRepository.putDataSources(ds);
            System.out.println("Insert Operation Successful on table data_sources.");
        } catch (Exception e) {
            assertEquals("A Record with the given key already exists. ERROR: duplicate key value violates unique constraint \"pk_chimera_data_sources\"\n" +
                    "  Detail: Key (data_source_type, data_source_sub_type)=(Relational, Postgres) already exists.", e.getMessage());
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            dataSourcesRepository.deleteFromDataSources(filter);
        }
    }
    @Test
    void testputDataSources_invalidSingleRecord_2()  {
        // Arrange
        dataSources ds = new dataSources("Relational", "Avro", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        try {
            dataSourcesRepository.putDataSources(ds);
            System.out.println("Insert Operation Successful on table data_sources.");
        } catch (Exception e) {
            System.out.println("ErrorMessage - " + e.getMessage());
            String actualMessage = e.getMessage();

// Validate key parts of the error message
            assertTrue(actualMessage.contains("Check Constraint is violated"),
                    "Expected 'Check Constraint is violated' in the error message.");
            assertTrue(actualMessage.contains("ERROR: new row for relation \"data_sources\" violates check constraint \"check_data_source_sub_type\""),
                    "Expected 'check_data_source_sub_type' constraint violation in the error message.");
            assertTrue(actualMessage.contains("Detail: Failing row contains"),
                    "Expected 'Detail: Failing row contains' in the error message.");

        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            dataSourcesRepository.deleteFromDataSources(filter);
        }
    }
    @Test
    void testputDataSources_validMultipleRecord_3()  {
        // Arrange
        List<dataSources> dsList = new ArrayList<dataSources>();
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dsList.add(ds);
        ds=new dataSources("Relational", "MySql", "Mysql Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dsList.add(ds);

        try {
            dataSourcesRepository.putDataSources(dsList);
            System.out.println("Insert Operation Successful on table data_sources.");
        } catch (Exception e) {
            assertEquals("A Record with the given key already exists. ERROR: duplicate key value violates unique constraint \"pk_chimera_data_sources\"\n" +
                    "  Detail: Key (data_source_type, data_source_sub_type)=(Relational, Postgres) already exists.", e.getMessage());
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            //filter.put("data_source_sub_type", "Postgres");
            dataSourcesRepository.deleteFromDataSources(filter);
        }
    }
    @Test
    void testGetAllDataSources () {
        List<dataSources> dsList = new ArrayList<dataSources>();
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dsList.add(ds);
        dataSources ds1 =new dataSources("Relational", "MySql", "Mysql Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dsList.add(ds1);
        try {
            dataSourcesRepository.putDataSources(dsList);
        } catch (Exception e) {
            System.out.println("Insert Op not required");
        } finally {
            List<dataSources> selectList = dataSourcesRepository.getAllDataSources();
            assertTrue(!selectList.isEmpty());
            assertEquals(2, selectList.size());
            assertEquals(dsList.get(0).getDataSourceType(), selectList.get(0).getDataSourceType());
            assertEquals(dsList.get(0).getDataSourceSubType(), selectList.get(0).getDataSourceSubType());
            assertEquals(dsList.get(0).getDescription(), selectList.get(0).getDescription());
            assertEquals(dsList.get(0).getReadDefaults(), selectList.get(0).getReadDefaults());
            assertEquals(dsList.get(0).getWriteDefaults(), selectList.get(0).getWriteDefaults());
            assertEquals(dsList.get(0).getCreatedBy(), selectList.get(0).getCreatedBy());
            assertEquals(dsList.get(0).getUpdatedBy(), selectList.get(0).getUpdatedBy());
            assertEquals(dsList.get(0).getUpdatedTimestamp(), selectList.get(0).getUpdatedTimestamp());
            assertEquals(dsList.get(0).getActiveFlag(), selectList.get(0).getActiveFlag());

            assertEquals(dsList.get(1).getDataSourceType(), selectList.get(1).getDataSourceType());
            assertEquals(dsList.get(1).getDataSourceSubType(), selectList.get(1).getDataSourceSubType());
            assertEquals(dsList.get(1).getDescription(), selectList.get(1).getDescription());
            assertEquals(dsList.get(1).getReadDefaults(), selectList.get(1).getReadDefaults());
            assertEquals(dsList.get(1).getWriteDefaults(), selectList.get(1).getWriteDefaults());
            assertEquals(dsList.get(1).getCreatedBy(), selectList.get(1).getCreatedBy());
            assertEquals(dsList.get(1).getUpdatedBy(), selectList.get(1).getUpdatedBy());
            assertEquals(dsList.get(1).getUpdatedTimestamp(), selectList.get(1).getUpdatedTimestamp());
            assertEquals(dsList.get(1).getActiveFlag(), selectList.get(1).getActiveFlag());

            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            //filter.put("data_source_sub_type", "Postgres");
            int rowNum = dataSourcesRepository.deleteFromDataSources(filter);
            System.out.println("No. of Rows successfully Deleted : " + rowNum);
        }




    }

    @Test
    void testGetDataSourcesWithFilters() {
        List<dataSources> dsList = new ArrayList<dataSources>();
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dsList.add(ds);
        dataSources ds1 =new dataSources("Relational", "MySql", "Mysql Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dsList.add(ds1);
        try {
            dataSourcesRepository.putDataSources(dsList);
        } catch (Exception e) {
            System.out.println("Insert Op not required");
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");
            List<dataSources> selectList = dataSourcesRepository.getDataSourcesWithFilters(filter);
            assertTrue(!selectList.isEmpty());
            assertEquals(1, selectList.size());
            assertEquals(dsList.get(0).getDataSourceType(), selectList.get(0).getDataSourceType());
            assertEquals(dsList.get(0).getDataSourceSubType(), selectList.get(0).getDataSourceSubType());
            assertEquals(dsList.get(0).getDescription(), selectList.get(0).getDescription());
            assertEquals(dsList.get(0).getReadDefaults(), selectList.get(0).getReadDefaults());
            assertEquals(dsList.get(0).getWriteDefaults(), selectList.get(0).getWriteDefaults());
            assertEquals(dsList.get(0).getCreatedBy(), selectList.get(0).getCreatedBy());
            assertEquals(dsList.get(0).getUpdatedBy(), selectList.get(0).getUpdatedBy());
            assertEquals(dsList.get(0).getUpdatedTimestamp(), selectList.get(0).getUpdatedTimestamp());
            assertEquals(dsList.get(0).getActiveFlag(), selectList.get(0).getActiveFlag());
            Map<String, Object> deleteFilter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            //filter.put("data_source_sub_type", "Postgres");
            int rowNum = dataSourcesRepository.deleteFromDataSources(deleteFilter);
            System.out.println("No. of Rows successfully Deleted : " + rowNum);
        }


    }

    @Test
    void testUpdateDataSources() {
        List<dataSources> dsList = new ArrayList<dataSources>();
        dataSources ds = new dataSources("Relational", "Postgres", "Postgres Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dsList.add(ds);
        dataSources ds1 =new dataSources("Relational", "MySql", "Mysql Database", "{\"Key\":\"value\"}", "{\"Key\":\"value\"}",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        dsList.add(ds1);
        try {
            dataSourcesRepository.putDataSources(dsList);
        } catch (Exception e) {
            System.out.println("Insert Op not required");
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            filter.put("data_source_sub_type", "Postgres");

            Map<String, Object> updateFields = new HashMap<>();
            updateFields.put("description", "New Description");

            dataSourcesRepository.updateDataSources(updateFields, filter);
            List<dataSources> selectList = dataSourcesRepository.getDataSourcesWithFilters(filter);
            assertEquals("New Description", selectList.get(0).getDescription());
            Map<String, Object> deleteFilter = new HashMap<>();
            filter.put("data_source_type", "Relational");
            //filter.put("data_source_sub_type", "Postgres");
            int rowNum = dataSourcesRepository.deleteFromDataSources(deleteFilter);
            System.out.println("No. of Rows successfully Deleted : " + rowNum);
        }

    }
}