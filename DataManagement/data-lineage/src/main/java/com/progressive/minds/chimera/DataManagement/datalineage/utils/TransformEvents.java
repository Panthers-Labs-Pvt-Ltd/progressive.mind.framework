package com.progressive.minds.chimera.DataManagement.datalineage.utils;

import com.progressive.minds.chimera.DataManagement.datalineage.facets.RunFacets;
import com.progressive.minds.chimera.dto.PipelineMetadata;
import com.progressive.minds.chimera.dto.TransformMetadataConfig;
import io.openlineage.client.OpenLineage;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

import java.util.*;

import static com.progressive.minds.chimera.DataManagement.datalineage.facets.DatasetFacets.*;
import static com.progressive.minds.chimera.DataManagement.datalineage.facets.DatasetFacets.getDocumentationDatasetFacet;
import static com.progressive.minds.chimera.DataManagement.datalineage.facets.JobFacets.*;
import static com.progressive.minds.chimera.DataManagement.datalineage.utils.Utility.getDataFrameSchema;

public class TransformEvents {
    private static final String STRING_DEFAULTS = "-";
    private static String DATASET_URN ;

    public static OpenLineage.RunEvent buildExtractEvents(OpenLineage.RunEvent.EventType eventType,
                                                          OpenLineage openLineage,
                                                          String inPipelineUrn,
                                                          PipelineMetadata inPipelineMetadata,
                                                          SparkSession inSparkSession,
                                                          OpenLineage.JobFacets jobFacets )
    {
        List<TransformMetadataConfig> extractMetadataList = inPipelineMetadata.getTransformMetadata();
        List<OpenLineage.InputDataset> inputs = new ArrayList<>();
        List<OpenLineage.OutputDataset> outputs = new ArrayList<>();

        switch (eventType) {
            case START -> {
                OpenLineage.Job JobStartFacet =JobStartFacet(openLineage, inPipelineUrn, inPipelineMetadata.getPipelineName(), jobFacets);
                extractMetadataList.forEach(transform ->
                {
                    String datasetUrn = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,PROD)",
                            "InMemory", transform.getSequenceNumber());
                    Map<String, String> transformInformation = new HashMap<>();
                    transformInformation.put("Source Type", "DataFrames");
                    transformInformation.put("JobType", "Transformation");
                    Dataset<Row> dataframe = inSparkSession.sql("SELECT * from " + transform.getTransformDataframeName()).limit(1);
                   /* try {
                        dataframe.createTempView(transform.getTransformDataframeName());
                    } catch (AnalysisException e) {
                        throw new RuntimeException(e);
                    }*/
                    try {
                        OpenLineage.DatasetFacetsBuilder datasetFacets = openLineage.newDatasetFacetsBuilder();
                        // Adding Documentations
                        datasetFacets.documentation(getDocumentationDatasetFacet(openLineage,transform.getPipelineName(),
                                transformInformation));
                        // Adding Storage layer Information
                            datasetFacets.storage(openLineage.newStorageDatasetFacet("Storage Layer",
                                    "In Memory"));
                        inputs.add(openLineage
                                .newInputDatasetBuilder()
                                .namespace(datasetUrn)
                                .name(transform.getTransformDataframeName())
                                .facets(datasetFacets.build())
                                .build());
                        // Mapping Output Datasets =============================
                        OpenLineage.DatasetFacetsBuilder outputDataset = datasetFacets;
                        // Life Cycle Change
                        OpenLineage.LifecycleStateChangeDatasetFacet lifecycleStateChange = getDataSetStateChange(openLineage,
                                datasetUrn, datasetUrn, "CREATE");
                        outputDataset.lifecycleStateChange(lifecycleStateChange);

                        // Storage
                        OpenLineage.StorageDatasetFacet storage = openLineage
                                .newStorageDatasetFacet("Storage Layer", "Temporary");
                        outputDataset.storage(storage);

                        //TODO Add versioning Information's
                        OpenLineage.DatasetVersionDatasetFacet version = openLineage.newDatasetVersionDatasetFacet("1");
                        outputDataset.version(version);

                        OpenLineage.ColumnLineageDatasetFacet columnLineage =ColumnLevelLineage.get( "SELECT * from " + transform.getTransformDataframeName(), dataframe);
                        outputDataset.columnLineage(columnLineage);

                        StructType inSchema = getDataFrameSchema(inSparkSession, transform.getTransformDataframeName());
                        OpenLineage.SchemaDatasetFacet schema = getDatasourceSchema(openLineage, inSchema);
                        outputDataset.schema(schema);

                        outputs.add(openLineage
                                .newOutputDatasetBuilder()
                                .facets(outputDataset.build())
                                .namespace(datasetUrn)
                                .name(transform.getTransformDataframeName())
                                .facets(datasetFacets.build()).build());


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                return RunFacets.getRunEvent(openLineage,  UUID.randomUUID(), JobStartFacet,inputs,  outputs);
            }
            case COMPLETE -> {
                OpenLineage.Job jobEnd = JobEndFacet(openLineage,inPipelineUrn, inPipelineMetadata.getPipelineName());
                return RunFacets.getRunEvent(openLineage,  UUID.randomUUID(), jobEnd,inputs,  outputs);
            }
        }
        return null;
    }
}
