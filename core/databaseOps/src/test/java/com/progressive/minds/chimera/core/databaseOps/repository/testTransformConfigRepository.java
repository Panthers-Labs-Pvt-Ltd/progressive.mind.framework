package com.progressive.minds.chimera.core.databaseOps.repository;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.transformConfig;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataPipelines;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.transformConfigRepository;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataPipelinesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class testTransformConfigRepository {
    private transformConfigRepository transformConfigRepository;
    private dataPipelinesRepository dataPipelinesRepository;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        System.setProperty("CHIMERA_EXE_ENV", "dev");
        dataSource = DataSourceConfig.getDataSource();
        transformConfigRepository = new transformConfigRepository();
        dataPipelinesRepository = new dataPipelinesRepository();
    }

    @Test
    void testTransformConfigs_validSingleRecord_1()  {
        // Arrange
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test",
                "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y");
        transformConfig tc = new transformConfig("TestPipeline", 1,
                "select * from table;", "transform_DF",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        try {
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            transformConfigRepository.putTransformConfig(tc);
            System.out.println("Insert Operation Successful on transform_config.");
        } catch (Exception e) {
            System.out.println("ErrorMessage: " + e.getMessage());
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains("Foreign Key Violation. Record is missing in Parent Table."));
        } finally {
            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            transformConfigRepository.deleteFromTransformConfig(pipelineFilter);
            dataPipelinesRepository.deleteFromDataPipelines(pipelineFilter);
        }
    }

    @Test
    void testPutTransformConfig_invalidSingleRecord_2()  {
        // Arrange

        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test",
                "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y");
        transformConfig tc = new transformConfig("TestPipeline1", 1,
                "select * from table;", "transform_DF",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        try {
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            transformConfigRepository.putTransformConfig(tc);
            System.out.println("Insert Operation Successful on transform_config.");
        } catch (Exception e) {
            System.out.println("ErrorMessage - " + e.getMessage());
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains("Foreign Key Violation. Record is missing in Parent Table."),
                    "Expected 'Foreign Key Violation. Record is missing in Parent Table.' in the error message.");
        } finally {
            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            transformConfigRepository.deleteFromTransformConfig(pipelineFilter);
            dataPipelinesRepository.deleteFromDataPipelines(pipelineFilter);
        }
    }

    @Test
    void testTransformPipelines_validMultipleRecord_3()  {
        // Arrange
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test",
                "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y");
        transformConfig tc = new transformConfig( "TestPipeline", 1,
                "select * from table;", "transform_DF",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        transformConfig tc1 = new transformConfig("TestPipeline", 2,
                "select * from table1;", "transform_DF",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        List<transformConfig> tcList = new ArrayList<transformConfig>();
        tcList.add(tc);
        tcList.add(tc1);
        try {
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            transformConfigRepository.putTransformConfig(tcList);
            System.out.println("Insert Operation Successful on transform_config.");
        } catch (Exception e) {
            assertEquals("A Record with the given key already exists. ERROR: duplicate key value violates unique constraint \"pk_chimera_data_sources\"\n" +
                    "  Detail: Key (data_source_type, data_source_sub_type)=(Relational, Postgres) already exists.", e.getMessage());
        } finally {
            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            transformConfigRepository.deleteFromTransformConfig(pipelineFilter);
            dataPipelinesRepository.deleteFromDataPipelines(pipelineFilter);
        }
    }

    @Test
    void testGetAlltransformConfigs_4 () {
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test",
                "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y");
        transformConfig tc = new transformConfig( "TestPipeline", 1,
                "select * from table;", "transform_DF",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        transformConfig tc1 = new transformConfig( "TestPipeline", 2,
                "select * from table1;", "transform_DF",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        List<transformConfig> tcList = new ArrayList<transformConfig>();
        tcList.add(tc);
        tcList.add(tc1);
        try {
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            transformConfigRepository.putTransformConfig(tcList);
            System.out.println("Insert Operation Successful on transform_config.");
        } catch (Exception e) {
            System.out.println("Insert Op not required");
        } finally {
            List<transformConfig> selectList = transformConfigRepository.getAllTransformConfig();
            assertTrue(!selectList.isEmpty());
            assertEquals(2, selectList.size());
            assertEquals(tcList.get(0).getPipelineName(), selectList.get(0).getPipelineName());
            assertEquals(tcList.get(0).getSequenceNumber(), selectList.get(0).getSequenceNumber());
            assertEquals(tcList.get(0).getSqlText(), selectList.get(0).getSqlText());
            assertEquals(tcList.get(0).getTransformDataframeName(), selectList.get(0).getTransformDataframeName());
            assertEquals(tcList.get(0).getCreatedBy(), selectList.get(0).getCreatedBy());
            assertEquals(tcList.get(0).getUpdatedBy(), selectList.get(0).getUpdatedBy());
            assertEquals(tcList.get(0).getUpdatedTimestamp(), selectList.get(0).getUpdatedTimestamp());
            assertEquals(tcList.get(0).getActiveFlag(), selectList.get(0).getActiveFlag());


            assertEquals(tcList.get(1).getPipelineName(), selectList.get(1).getPipelineName());
            assertEquals(tcList.get(1).getSequenceNumber(), selectList.get(1).getSequenceNumber());
            assertEquals(tcList.get(1).getSqlText(), selectList.get(1).getSqlText());
            assertEquals(tcList.get(1).getTransformDataframeName(), selectList.get(1).getTransformDataframeName());
            assertEquals(tcList.get(1).getCreatedBy(), selectList.get(1).getCreatedBy());
            assertEquals(tcList.get(1).getUpdatedBy(), selectList.get(1).getUpdatedBy());
            assertEquals(tcList.get(1).getUpdatedTimestamp(), selectList.get(1).getUpdatedTimestamp());
            assertEquals(tcList.get(1).getActiveFlag(), selectList.get(1).getActiveFlag());

            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            transformConfigRepository.deleteFromTransformConfig(pipelineFilter);
            dataPipelinesRepository.deleteFromDataPipelines(pipelineFilter);
        }
    }

    @Test
    void testGetExtractConfigWithFilters_5() {
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test",
                "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y");
        transformConfig tc = new transformConfig( "TestPipeline", 1,
                "select * from table;", "transform_DF",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        transformConfig tc1 = new transformConfig( "TestPipeline", 2,
                "select * from table1;", "transform_DF",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        List<transformConfig> tcList = new ArrayList<transformConfig>();
        tcList.add(tc);
        tcList.add(tc1);
        try {
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            transformConfigRepository.putTransformConfig(tcList);
            System.out.println("Insert Operation Successful on transform_config.");
        } catch (Exception e) {
            System.out.println("Insert Op not required");
        } finally {
            Map<String, Object> selFilter = new HashMap<>();
            selFilter.put("sequence_number", 2);
            List<transformConfig> selectList = transformConfigRepository.getTransformConfigWithFilters(selFilter);
            assertTrue(!selectList.isEmpty());
            assertEquals(1, selectList.size());
            assertEquals(tcList.get(1).getPipelineName(), selectList.get(0).getPipelineName());
            assertEquals(tcList.get(1).getSequenceNumber(), selectList.get(0).getSequenceNumber());
            assertEquals(tcList.get(1).getSqlText(), selectList.get(0).getSqlText());
            assertEquals(tcList.get(1).getTransformDataframeName(), selectList.get(0).getTransformDataframeName());
            assertEquals(tcList.get(1).getCreatedBy(), selectList.get(0).getCreatedBy());
            assertEquals(tcList.get(1).getUpdatedBy(), selectList.get(0).getUpdatedBy());
            assertEquals(tcList.get(1).getUpdatedTimestamp(), selectList.get(0).getUpdatedTimestamp());
            assertEquals(tcList.get(1).getActiveFlag(), selectList.get(0).getActiveFlag());

            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            transformConfigRepository.deleteFromTransformConfig(pipelineFilter);
            dataPipelinesRepository.deleteFromDataPipelines(pipelineFilter);


        }


    }

    @Test
    void testUpdateDataSourcesConnections_6() {
        dataPipelines dp = new dataPipelines("TestPipeline", "Pipeline to unit test",
                "Batch", "23 * * *",  new Timestamp(System.currentTimeMillis()), "PK",
                null, null, "Y");
        transformConfig tc = new transformConfig( "TestPipeline", 1,
                "select * from table;", "transform_DF",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        transformConfig tc1 = new transformConfig( "TestPipeline", 2,
                "select * from table1;", "transform_DF",
                new Timestamp(System.currentTimeMillis()), "PK", null, null, "Y");
        List<transformConfig> tcList = new ArrayList<transformConfig>();
        tcList.add(tc);
        tcList.add(tc1);
        try {
            dataPipelinesRepository.putDataPipelines(dp);
            System.out.println("Insert Operation Successful on data_pipelines.");
            transformConfigRepository.putTransformConfig(tcList);
            System.out.println("Insert Operation Successful on transform_config.");
        } catch (Exception e) {
            System.out.println("Insert Op not required");
        } finally {
            Map<String, Object> filter = new HashMap<>();
            filter.put("sequence_number", 2);

            Map<String, Object> updateFields = new HashMap<>();
            updateFields.put("transform_dataframe_name", "New_DF");

            transformConfigRepository.updateTransformConfig(updateFields, filter, "PK");
            List<transformConfig> selectList = transformConfigRepository.getTransformConfigWithFilters(filter);
            assertEquals("New_DF", selectList.get(0).getTransformDataframeName());

            Map<String, Object> pipelineFilter = new HashMap<>();
            pipelineFilter.put("pipeline_name", "TestPipeline");

            transformConfigRepository.deleteFromTransformConfig(pipelineFilter);
            dataPipelinesRepository.deleteFromDataPipelines(pipelineFilter);
        }

    }
}