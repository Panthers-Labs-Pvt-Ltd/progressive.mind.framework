package com.progressive.minds.chimera.core.temporal.workflows.implementations;

import java.time.Duration;
import java.util.List;

import org.apache.spark.sql.SparkSession;

import com.progressive.minds.chimera.core.temporal.activities.DataIngestionActivities;
import com.progressive.minds.chimera.core.temporal.workflows.DataIngestionWorkflow;

import com.progressive.minds.chimera.core.api_service.dto.ExtractView;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

public class DataIngestionWorkflowImpl implements DataIngestionWorkflow {
    private final DataIngestionActivities activities = Workflow.newActivityStub(
        DataIngestionActivities.class, 
        ActivityOptions.newBuilder()
        .setStartToCloseTimeout(Duration.ofMinutes(2)) // Set a reasonable timeout for execution
 //       .setScheduleToCloseTimeout(Duration.ofMinutes(5)) // Optional: Set a maximum time for the activity including retries
        .build()
    );

    @Override
    public void runPipeline(String pipelineId) {
        List<ExtractView> metadata = null;
        try {
            metadata = activities.fetchPipelineMetadata(pipelineId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // if (!metadata.isEmpty())
        //     metadata.forEach(config -> System.out.println(config));
        SparkSession sparkSession = activities.createSparkSession();
        System.out.println("Spark Session Created");
        // ExtractedData extractedData = activities.extractData(metadata, sparkSession);
        // ValidatedData validatedData = activities.performDataQualityCheck(extractedData);
        // activities.writeData(validatedData, metadata);
        // activities.syncWithDataHub(metadata);
    }
}
