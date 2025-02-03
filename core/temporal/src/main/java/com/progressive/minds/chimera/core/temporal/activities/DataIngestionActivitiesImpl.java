package com.progressive.minds.chimera.core.temporal.activities;

import java.io.IOException;
import java.util.List;

import org.apache.spark.sql.SparkSession;

import com.fasterxml.jackson.core.type.TypeReference;
import com.progressive.minds.chimera.consumer.DBAPIClient;
import com.progressive.minds.chimera.dto.ExtractView;

public class DataIngestionActivitiesImpl implements DataIngestionActivities{

    @Override
    public List<ExtractView> fetchPipelineMetadata(String pipelineId) throws IOException, InterruptedException {
        // Fetch metadata from PostgreSQL via REST API
        DBAPIClient dbClient = new DBAPIClient();
        List<ExtractView> pipelineMetadata = dbClient.get("http://localhost:8080/api/v1/extractView/Test_Pipeline", new TypeReference<List<ExtractView>>() {});
        pipelineMetadata.forEach(ds -> System.out.println(ds));
        return pipelineMetadata;
    }

    @Override
    public SparkSession createSparkSession() {
        return SparkSession.builder().appName("DataPipeline").master("local").getOrCreate();
    }

}
