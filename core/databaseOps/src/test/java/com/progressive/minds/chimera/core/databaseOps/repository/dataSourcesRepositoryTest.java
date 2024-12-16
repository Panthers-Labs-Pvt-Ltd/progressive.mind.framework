package com.progressive.minds.chimera.core.databaseOps.repository;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataSources;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataSourcesRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.*;

public class dataSourcesRepositoryTest {

    private com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataSourcesRepository dataSourcesRepository;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        System.setProperty("CHIMERA_EXE_ENV", "dev");
        dataSource = DataSourceConfig.getDataSource();
        dataSourcesRepository = new dataSourcesRepository();
    }

    @Test
    void testGetAllDataSources() {
        // Act
        List<dataSources> dataSources = dataSourcesRepository.getAllDataSources();
        dataSources.forEach(
                ds -> System.out.println("dataSourceType : " + ds.getDataSourceType() +
                        ", data_source_sub_type : " + ds.getDataSourceSubType() +
                        ", description : " + ds.getDescription() +
                        ", read_defaults : " + ds.getReadDefaults() +
                        ", write_defaults : " + ds.getWriteDefaults() +
                        ", created_by : " + ds.getCreatedBy() +
                        ", created_timestamp : " + ds.getCreatedTimestamp() +
                        ", updated_by : " + ds.getUpdatedBy() +
                        ", updated_timestamp : " + ds.getUpdatedTimestamp() +
                        ", active_flag : " + ds.getActiveFlag()));
        System.out.println("Size : " + dataSources.size());
        // Assert
        //   assertEquals(13, dataSources.size());
    }

    @Test
    void testPutDataSources() {
        //   List<dataSources> dataSources = new ArrayList<>();
        dataSources ds = new dataSources();
        ds.setDataSourceType("Relational");
        ds.setDataSourceSubType("Postgres");
        ds.setDescription("Postgres Database");
        ds.setReadDefaults(new JSONObject("{\"Key\":\"Value\"}"));
        ds.setWriteDefaults(new JSONObject("{\"Key\":\"Value\"}"));
        ds.setActiveFlag("Y");
        dataSourcesRepository.putDataSources(ds);
    }

    @Test
    void testPutDataSources_multipleInserts() {
        List<dataSources> dataSources = new ArrayList<>();
        dataSources ds = new dataSources();
        ds.setDataSourceType("Files");
        ds.setDataSourceSubType("Parquet");
        ds.setDescription("Parquet Database");
        ds.setReadDefaults(new JSONObject("{\"Key\":\"Value\"}"));
        ds.setWriteDefaults(new JSONObject("{\"Key\":\"Value\"}"));
        ds.setActiveFlag("Y");
        dataSources.add(ds);
        ds = new dataSources();
        ds.setDataSourceType("Files");
        ds.setDataSourceSubType("Avro");
        ds.setDescription("MYSQL Database");
        ds.setReadDefaults(new JSONObject("{\"Key\":\"Value\"}"));
        ds.setWriteDefaults(new JSONObject("{\"Key\":\"Value\"}"));
        ds.setActiveFlag("Y");
        dataSources.add(ds);
        dataSourcesRepository.putDataSources(dataSources);

    }

    @Test
    void testGetFilteredDataSources() {

        // Act
        Map<String, Object> filters = new HashMap<>();
        filters.put("data_source_type", "Relational");
        //     filters.put("data_source_sub_type", "Postgres");

        List<dataSources> dataSources = dataSourcesRepository.getDataSourcesWithFilters(filters);
        dataSources.forEach(
                ds -> System.out.println("dataSourceType : " + ds.getDataSourceType() +
                        ", data_source_sub_type : " + ds.getDataSourceSubType() +
                        ", description : " + ds.getDescription() +
                        ", read_defaults : " + ds.getReadDefaults() +
                        ", write_defaults : " + ds.getWriteDefaults() +
                        ", created_by : " + ds.getCreatedBy() +
                        ", created_timestamp : " + ds.getCreatedTimestamp() +
                        ", updated_by : " + ds.getUpdatedBy() +
                        ", updated_timestamp : " + ds.getUpdatedTimestamp() +
                        ", active_flag : " + ds.getActiveFlag()));
        System.out.println("Size : " + dataSources.size());
        // Assert
        //     assertEquals(13, dataSources.size());
    }

    @Test
    void testUpdateDataSources() {

        // Act
        Map<String, Object> updateFields = new HashMap<>();
        updateFields.put("description", "New Value after multiple column update executing Update ");
        updateFields.put("active_flag", "N");


        Map<String, Object> filters = new HashMap<>();
       // filters.put("data_source_type", "Relational");
       // filters.put("data_source_sub_type", "MySql");

        int executionCode = dataSourcesRepository.updateDataSources(updateFields, filters);
        System.out.println("ExecutionCode - " + executionCode);
    }

    @Test
    void testDeleteFromDataSources() {
        Map<String, Object> filters = new HashMap<>();
        // filters.put("data_source_type", "Relational");
       //  filters.put("data_source_type", "Files");
       // filters.put("data_source_sub_type", "Parquet");

        int executionCode = dataSourcesRepository.deleteFromDataSources(filters);
        System.out.println("ExecutionCode - " + executionCode);
    }
}


