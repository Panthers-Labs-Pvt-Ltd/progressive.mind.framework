package com.progressive.minds.chimera.core.databaseOps.repository;

import static org.junit.jupiter.api.Assertions.*;
import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataPipelines;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataPipelinesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class testDataPipelinesRepository {

    private dataPipelinesRepository dataPipelinesRepository;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        System.setProperty("CHIMERA_EXE_ENV", "dev");
        dataSource = DataSourceConfig.getDataSource();
        dataPipelinesRepository = new dataPipelinesRepository();
    }

    @Test
    void testputDataPipelines_validSingleRecord_1()  {
        // Arrange
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test", "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",null, null, "Y");
        try {
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on table data_pipelines.");
        } catch (Exception e) {
            assertEquals("A Record with the given key already exists. ERROR: duplicate key value violates unique constraint \"pk_chimera_data_sources\"\n" +
                    "  Detail: Key (data_source_type, data_source_sub_type)=(Relational, Postgres) already exists.", e.getMessage());
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("pipeline_name", "TestPipeline");
            dataPipelinesRepository.deleteFromDataPipelines(filter);
        }
    }

    @Test
    void testputDataPipelines_invalidSingleRecord_2()  {
        // Arrange
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test", "Invalid Value", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",null, null, "Y");
        try {
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on table data_pipelines.");
        } catch (Exception e) {
            System.out.println("ErrorMessage - " + e.getMessage());
            String actualMessage = e.getMessage();

// Validate key parts of the error message
            assertTrue(actualMessage.contains("Check Constraint is violated"),
                    "Expected 'Check Constraint is violated' in the error message.");
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("pipeline_name", "TestPipeline");
            dataPipelinesRepository.deleteFromDataPipelines(filter);
        }
    }

    @Test
    void testputDataPipelines_validMultipleRecord_3()  {
        // Arrange
        List<dataPipelines> dpList = new ArrayList<dataPipelines>();
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test", "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",null, null, "Y");
        dpList.add(dp);
        dataPipelines dp1 = new dataPipelines("TestPipeline1", "Pipeline to unit test", "Stream", "24x7",  new Timestamp(System.currentTimeMillis()), "PK",null, null, "Y");
        dpList.add(dp1);
        try {
            dataPipelinesRepository.putDataPipelines(dpList);
            System.out.println("Insert Operation Successful on table data_sources.");
        } catch (Exception e) {
            assertEquals("A Record with the given key already exists. ERROR: duplicate key value violates unique constraint \"pk_chimera_data_sources\"\n" +
                    "  Detail: Key (data_source_type, data_source_sub_type)=(Relational, Postgres) already exists.", e.getMessage());
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("active_flag", "Y");
            dataPipelinesRepository.deleteFromDataPipelines(filter);
        }
    }

    @Test
    void testGetAllDataPipelines_4 () {
        List<dataPipelines> dpList = new ArrayList<dataPipelines>();
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test", "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",null, null, "Y");
        dpList.add(dp);
        dataPipelines dp1 = new dataPipelines("TestPipeline1", "Pipeline to unit test", "Stream", "24x7",  new Timestamp(System.currentTimeMillis()), "PK",null, null, "Y");
        dpList.add(dp1);
        try {
            dataPipelinesRepository.putDataPipelines(dpList);
            System.out.println("Insert Operation Successful on table data_sources.");
        } catch (Exception e) {
            System.out.println("Insert Op not required");
        } finally {
            List<dataPipelines> selectList = dataPipelinesRepository.getAllDataPipelines();
            assertTrue(!selectList.isEmpty());
            assertEquals(2, selectList.size());
            assertEquals(dpList.get(0).getPipelineName(), selectList.get(0).getPipelineName());
            assertEquals(dpList.get(0).getPipelineDescription(), selectList.get(0).getPipelineDescription());
            assertEquals(dpList.get(0).getProcessMode(), selectList.get(0).getProcessMode());
            assertEquals(dpList.get(0).getRunFrequency(), selectList.get(0).getRunFrequency());
            assertEquals(dpList.get(0).getCreatedBy(), selectList.get(0).getCreatedBy());
            assertEquals(dpList.get(0).getUpdatedBy(), selectList.get(0).getUpdatedBy());
            assertEquals(dpList.get(0).getUpdatedTimestamp(), selectList.get(0).getUpdatedTimestamp());
            assertEquals(dpList.get(0).getActiveFlag(), selectList.get(0).getActiveFlag());

            assertEquals(dpList.get(1).getPipelineName(), selectList.get(1).getPipelineName());
            assertEquals(dpList.get(1).getPipelineDescription(), selectList.get(1).getPipelineDescription());
            assertEquals(dpList.get(1).getProcessMode(), selectList.get(1).getProcessMode());
            assertEquals(dpList.get(1).getRunFrequency(), selectList.get(1).getRunFrequency());
            assertEquals(dpList.get(1).getCreatedBy(), selectList.get(1).getCreatedBy());
            assertEquals(dpList.get(1).getUpdatedBy(), selectList.get(1).getUpdatedBy());
            assertEquals(dpList.get(1).getUpdatedTimestamp(), selectList.get(1).getUpdatedTimestamp());
            assertEquals(dpList.get(1).getActiveFlag(), selectList.get(1).getActiveFlag());

            Map<String, Object> filter = new HashMap<>();
            filter.put("active_flag", "Y");
            dataPipelinesRepository.deleteFromDataPipelines(filter);
        }




    }

    @Test
    void testGetDataPipelinesWithFilters_5() {
        List<dataPipelines> dpList = new ArrayList<dataPipelines>();
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test", "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",null, null, "Y");
        dpList.add(dp);
        dataPipelines dp1 = new dataPipelines("TestPipeline1", "Pipeline to unit test", "Stream", "24x7",  new Timestamp(System.currentTimeMillis()), "PK",null, null, "Y");
        dpList.add(dp1);
        try {
            dataPipelinesRepository.putDataPipelines(dpList);
            System.out.println("Insert Operation Successful on table data_sources.");
        } catch (Exception e) {
            System.out.println("Insert Op not required");
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("process_mode", "Batch");
            List<dataPipelines> selectList = dataPipelinesRepository.getDataPipelinesWithFilters(filter);
            assertTrue(!selectList.isEmpty());
            assertEquals(1, selectList.size());
            assertEquals(dpList.get(0).getPipelineName(), selectList.get(0).getPipelineName());
            assertEquals(dpList.get(0).getPipelineDescription(), selectList.get(0).getPipelineDescription());
            assertEquals(dpList.get(0).getProcessMode(), selectList.get(0).getProcessMode());
            assertEquals(dpList.get(0).getRunFrequency(), selectList.get(0).getRunFrequency());
            assertEquals(dpList.get(0).getCreatedBy(), selectList.get(0).getCreatedBy());
            assertEquals(dpList.get(0).getUpdatedBy(), selectList.get(0).getUpdatedBy());
            assertEquals(dpList.get(0).getUpdatedTimestamp(), selectList.get(0).getUpdatedTimestamp());
            assertEquals(dpList.get(0).getActiveFlag(), selectList.get(0).getActiveFlag());
            Map<String, Object> deleteFilter = new HashMap<>();
            filter.put("active_flag", "Y");
            dataPipelinesRepository.deleteFromDataPipelines(deleteFilter);
        }


    }

    @Test
    void testUpdateDataPipelines_6() {
        List<dataPipelines> dpList = new ArrayList<dataPipelines>();
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test", "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",null, null, "Y");
        dpList.add(dp);
        dataPipelines dp1 = new dataPipelines("TestPipeline1", "Pipeline to unit test", "Stream", "24x7",  new Timestamp(System.currentTimeMillis()), "PK",null, null, "Y");
        dpList.add(dp1);
        try {
            dataPipelinesRepository.putDataPipelines(dpList);
            System.out.println("Insert Operation Successful on table data_sources.");
        } catch (Exception e) {
            System.out.println("Insert Op not required");
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("active_flag", "Y");

            Map<String, Object> updateFields = new HashMap<>();
            updateFields.put("active_flag", "N");

            dataPipelinesRepository.updateDataPipelines(updateFields, filter);
            List<dataPipelines> selectList = dataPipelinesRepository.getDataPipelinesWithFilters(updateFields);
            assertEquals("N", selectList.get(0).getActiveFlag());
            Map<String, Object> deleteFilter = new HashMap<>();
           // filter.put("data_source_type", "Relational");
            //filter.put("data_source_sub_type", "Postgres");
            int rowNum = dataPipelinesRepository.deleteFromDataPipelines(deleteFilter);
            System.out.println("No. of Rows successfully Deleted : " + rowNum);
        }

    }
}