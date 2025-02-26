package com.progressive.minds.chimera.DataManagement.datalineage.utils;

import com.progressive.minds.chimera.DataManagement.datalineage.facets.RunFacets;
import com.progressive.minds.chimera.dto.ExtractMetadata;
import com.progressive.minds.chimera.dto.PersistMetadata;
import com.progressive.minds.chimera.dto.PipelineMetadata;
import io.openlineage.client.OpenLineage;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

import java.util.*;

import static com.progressive.minds.chimera.DataManagement.datalineage.facets.DatasetFacets.*;
import static com.progressive.minds.chimera.DataManagement.datalineage.facets.JobFacets.JobEndFacet;
import static com.progressive.minds.chimera.DataManagement.datalineage.facets.JobFacets.JobStartFacet;
import static com.progressive.minds.chimera.DataManagement.datalineage.utils.Utility.getDataFrameSchema;
import static com.progressive.minds.chimera.DataManagement.datalineage.utils.Utility.nvl;

public class persistEvents {
    private static final String STRING_DEFAULTS = "-";
    private static String DATASET_URN ;

    public static OpenLineage.RunEvent buildExtractEvents(OpenLineage.RunEvent.EventType eventType,
                                                          OpenLineage openLineage,
                                                          String inPipelineUrn,
                                                          PipelineMetadata inPipelineMetadata,
                                                          SparkSession inSparkSession,
                                                          OpenLineage.JobFacets jobFacets )
    {
        List<PersistMetadata> persistMetadataList = inPipelineMetadata.getPersistMetadata();
        List<OpenLineage.InputDataset> inputs = new ArrayList<>();
        List<OpenLineage.OutputDataset> outputs = new ArrayList<>();

        switch (eventType) {
            case START -> {
                OpenLineage.Job JobStartFacet =JobStartFacet(openLineage, inPipelineUrn, inPipelineMetadata.getPipelineName(), jobFacets);
                persistMetadataList.forEach(persist ->
                {
                    String datasetUrn = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,PROD)",
                            persist.getDataSource().getDataSourceSubType(), persist.getSequenceNumber());
                    Map<String, String> persistInformation = new HashMap<>();
                    persistInformation.put("Target DataSource Type", persist.getDataSourceConnection().getDataSourceType());
                    persistInformation.put("Target Sub Source Type", persist.getDataSourceConnection().getDataSourceSubType());
                    persistInformation.put("JobType", "Ingestion");
                    Dataset<Row> dataframe = inSparkSession.sql(persist.getTargetSql()).limit(1);
            /*        try {
                        dataframe.createTempView(persist.getTableName());
                    } catch (AnalysisException e) {
                        throw new RuntimeException(e);
                    }*/
                    try {

                        OpenLineage.DatasetFacetsBuilder datasetFacets = openLineage.newDatasetFacetsBuilder();

                        // Adding Documentations
                        datasetFacets.documentation(getDocumentationDatasetFacet(openLineage,persist.getPipelineName(),
                                persistInformation));

                        // Adding Data Sources Information
                        if (persist.getDataSourceConnectionName() != null &&
                                !persist.getDataSourceConnectionName().isEmpty())
                        {
                            Map<String, String> dataSourceMap = new HashMap<>();
                            switch (persist.getDataSource().getDataSourceType().toLowerCase(Locale.ROOT)) {
                                case "files" -> {
                                    dataSourceMap.put("Type", nvl(persist.getDataSource().getDataSourceType(), STRING_DEFAULTS));
                                    dataSourceMap.put("Sub Type", nvl(persist.getDataSource().getDataSourceSubType(), STRING_DEFAULTS));
                                    DATASET_URN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                                            getDataPlatform(persist.getDataSource().getDataSourceSubType())[0],
                                            getDataPlatform(persist.getDataSource().getDataSourceSubType())[1],
                                            System.getProperty("EXECUTION_ENV", "PROD"));
                                }
                                case "relational" -> {
                                    dataSourceMap.put("ConnectionName", nvl(persist.getDataSourceConnection().getDataSourceConnectionName(), STRING_DEFAULTS));
                                    dataSourceMap.put("DataSourceType", nvl(persist.getDataSourceConnection().getDataSourceType(), STRING_DEFAULTS));
                                    dataSourceMap.put("DataSourceSubType", nvl(persist.getDataSourceConnection().getDataSourceSubType(), STRING_DEFAULTS));
                                    dataSourceMap.put("Connection URL", nvl(persist.getDataSourceConnection().getConnectionMetadata(), STRING_DEFAULTS));
                                    DATASET_URN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                                            nvl(persist.getDataSourceConnection().getDataSourceSubType(), STRING_DEFAULTS),
                                            nvl(persist.getDatabaseName(), STRING_DEFAULTS) + "." +
                                                    nvl(persist.getTableName(), STRING_DEFAULTS),
                                            System.getProperty("EXECUTION_ENV", "PROD"));
                                }
                                case "nosql" -> {
                                    dataSourceMap.put("ConnectionName", nvl(persist.getDataSourceConnection().getDataSourceConnectionName(), STRING_DEFAULTS));
                                    dataSourceMap.put("DataSourceType", nvl(persist.getSinkType(), STRING_DEFAULTS));
                                    dataSourceMap.put("DataSourceSubType", nvl(persist.getSinkSubType(), STRING_DEFAULTS));
                                    DATASET_URN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                                            nvl(persist.getSinkType(), STRING_DEFAULTS),
                                            nvl(persist.getSinkSubType(), STRING_DEFAULTS),
                                            System.getProperty("EXECUTION_ENV", "PROD"));
                                }
                                default -> {
                                    dataSourceMap.put("ConnectionName", nvl(persist.getDataSourceConnection().getDataSourceConnectionName(), STRING_DEFAULTS));
                                    DATASET_URN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)", "default", "default",
                                            System.getProperty("EXECUTION_ENV", "PROD"));
                                }
                            }
                            datasetFacets.dataSource(getDatasourceDatasetFacet(openLineage,persist.getDataSourceConnectionName(),
                                    persist.getDataSource().getDataSourceType(),dataSourceMap));
                        }
                        // Adding Storage layer Information
                        if (persist.getDataSourceConnection() != null &&
                                !persist.getDataSourceConnection().getDataSourceSubType().isEmpty())
                        {
                            datasetFacets.storage(openLineage.newStorageDatasetFacet("Storage Layer",
                                    persist.getDataSourceConnection().getDataSourceSubType()));
                        }

                        //TODO  -- Add Correct Dataset Owner
                        if(persist.getCreatedBy() != null && !persist.getCreatedBy().isEmpty()) {
                            Map<String, String> ownersMap = new HashMap<>();
                            ownersMap.put("Owning-Domain", nvl(persist.getCreatedBy(), STRING_DEFAULTS));
                            OpenLineage.OwnershipDatasetFacet ownership = getDatasetOwners(openLineage, ownersMap);
                            datasetFacets.ownership(ownership);
                        }

                        String TABLE_DB_NAME=persist.getDatabaseName() + "." + persist.getTableName();
                        inputs.add(openLineage
                                .newInputDatasetBuilder()
                                .namespace(datasetUrn)
                                .name(TABLE_DB_NAME)
                                .facets(datasetFacets.build())
                                .build());
                        // Mapping Output Datasets =============================

                        OpenLineage.DatasetFacetsBuilder outputDataset = datasetFacets;

                        // Life Cycle Change
                        String DEFAULT_WRITE_MODE= "OVERWRITE";
                        if (persist.getWriteMode().equalsIgnoreCase("append"))
                            DEFAULT_WRITE_MODE = "CREATE";

                        OpenLineage.LifecycleStateChangeDatasetFacet lifecycleStateChange = getDataSetStateChange(openLineage,
                                datasetUrn, datasetUrn, DEFAULT_WRITE_MODE);
                        outputDataset.lifecycleStateChange(lifecycleStateChange);

                        // Storage
                        OpenLineage.StorageDatasetFacet storage = openLineage
                                .newStorageDatasetFacet("Storage Layer", "Processed Layer");
                        outputDataset.storage(storage);

                        //TODO Add versioning Information's
                        OpenLineage.DatasetVersionDatasetFacet version = openLineage.newDatasetVersionDatasetFacet("1");
                        outputDataset.version(version);

                        OpenLineage.ColumnLineageDatasetFacet columnLineage =ColumnLevelLineage.get(persist.getTargetSql(), dataframe);
                        outputDataset.columnLineage(columnLineage);

                        StructType inSchema = dataframe.schema();
                        OpenLineage.SchemaDatasetFacet schema = getDatasourceSchema(openLineage, inSchema);
                        outputDataset.schema(schema);

                        outputs.add(openLineage
                                .newOutputDatasetBuilder()
                                .facets(outputDataset.build())
                                .namespace(datasetUrn)
                                .name(TABLE_DB_NAME)
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
