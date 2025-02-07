package com.progressive.minds.chimera.core.temporal.activities;

import java.io.IOException;
import java.util.List;

import com.progressive.minds.chimera.dto.ExtractView;
import org.apache.spark.sql.SparkSession;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface DataIngestionActivities {

    @ActivityMethod
    List<ExtractView> fetchPipelineMetadata(String pipelineName) throws IOException, InterruptedException;

    @ActivityMethod
    SparkSession createSparkSession();

    // @ActivityMethod
    // ExtractedData extractData(PipelineMetadata metadata, SparkSession sparkSession);

    // @ActivityMethod
    // ValidatedData performDataQualityCheck(ExtractedData data);

    // @ActivityMethod
    // void writeData(ValidatedData data, PipelineMetadata metadata);

    // @ActivityMethod
    // void syncWithDataHub(PipelineMetadata metadata);

}

