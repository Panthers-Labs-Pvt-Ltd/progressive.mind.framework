package com.progressive.minds.chimera.DataManagement.datalineage.utils;

import com.progressive.minds.chimera.DataManagement.datalineage.facets.RunFacets;
import com.progressive.minds.chimera.dto.ExtractMetadata;
import com.progressive.minds.chimera.dto.PipelineMetadata;
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
import static com.progressive.minds.chimera.DataManagement.datalineage.facets.RunFacets.getRun;
import static com.progressive.minds.chimera.DataManagement.datalineage.utils.Utility.getDataFrameSchema;
import static com.progressive.minds.chimera.DataManagement.datalineage.utils.Utility.nvl;
import static java.time.ZonedDateTime.now;

public class extractsEvents {
    private static final String STRING_DEFAULTS = "-";
    private static String DATASET_URN ;

    public static OpenLineage.RunEvent buildExtractEvents(OpenLineage.RunEvent.EventType eventType,
                                                          OpenLineage openLineage,
                                                          String inPipelineUrn,
                                                          PipelineMetadata inPipelineMetadata,
                                                          SparkSession inSparkSession,
                                                          OpenLineage.JobFacets jobFacets )
    {
        List<ExtractMetadata> extractMetadataList = inPipelineMetadata.getExtractMetadata();
        List<OpenLineage.InputDataset> inputs = new ArrayList<>();
        List<OpenLineage.OutputDataset> outputs = new ArrayList<>();

        switch (eventType) {
            case START -> {
                OpenLineage.Job JobStartFacet =JobStartFacet(openLineage, inPipelineUrn, inPipelineMetadata.getPipelineName(), jobFacets);
                extractMetadataList.forEach(extract ->
                {

                    OpenLineage.Run runStart = getRun(openLineage, UUID.randomUUID());

                    String datasetUrn = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,PROD)",
                            extract.getDataSource().getDataSourceSubType(), extract.getSequenceNumber());
                    Map<String, String> extractInformation = new HashMap<>();
                    extractInformation.put("Source Type", extract.getExtractSourceType());
                    extractInformation.put("Sub Source Type", extract.getExtractSourceSubType());
                    extractInformation.put("JobType", "Ingestion");
                    Dataset<Row> dataframe = inSparkSession.sql("SELECT * from " + extract.getDataframeName()).limit(1);
           /*         try {
                        dataframe.createTempView(extract.getDataframeName());
                    } catch (AnalysisException e) {
                        throw new RuntimeException(e);
                    }*/
                    try {

                        OpenLineage.DatasetFacetsBuilder datasetFacets = openLineage.newDatasetFacetsBuilder();

                        // Adding Documentations
                        datasetFacets.documentation(getDocumentationDatasetFacet(openLineage,extract.getPipelineName(),
                                extractInformation));

                        // Adding Data Sources Information
                        if (extract.getExtractSourceType() != null &&
                                !extract.getExtractSourceType().isEmpty())
                        {
                            Map<String, String> dataSourceMap = new HashMap<>();
                            switch (extract.getExtractSourceType().toLowerCase(Locale.ROOT)) {
                                case "files" -> {
                                    dataSourceMap.put("Type", nvl(extract.getExtractSourceType(), STRING_DEFAULTS));
                                    dataSourceMap.put("Sub Type", nvl(extract.getExtractSourceSubType(), STRING_DEFAULTS));
                                    dataSourceMap.put("URI", nvl(extract.getFileMetadata().getFilePath(), STRING_DEFAULTS));
                                    dataSourceMap.put("Compression", nvl(extract.getFileMetadata().getCompressionType(), STRING_DEFAULTS));

                                    DATASET_URN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                                            getDataPlatform(extract.getFileMetadata().getFilePath())[0],
                                            getDataPlatform(extract.getFileMetadata().getFilePath())[1],
                                            System.getProperty("EXECUTION_ENV", "PROD"));
                                }
                                case "relational" -> {
                                    dataSourceMap.put("ConnectionName", nvl(extract.getDataSourceConnection().getDataSourceConnectionName(), STRING_DEFAULTS));
                                    dataSourceMap.put("DataSourceType", nvl(extract.getDataSourceConnection().getDataSourceType(), STRING_DEFAULTS));
                                    dataSourceMap.put("DataSourceSubType", nvl(extract.getDataSourceConnection().getDataSourceSubType(), STRING_DEFAULTS));
                                    dataSourceMap.put("Connection URL", nvl(extract.getDataSourceConnection().getConnectionMetadata(), STRING_DEFAULTS));
                                    DATASET_URN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                                            nvl(extract.getDataSourceConnection().getDataSourceSubType(), STRING_DEFAULTS),
                                            nvl(extract.getRelationalMetadata().getDatabaseName(), STRING_DEFAULTS) + "." +
                                                    nvl(extract.getRelationalMetadata().getTableName(), STRING_DEFAULTS),
                                            System.getProperty("EXECUTION_ENV", "PROD"));
                                }
                                case "nosql" -> {
                                    dataSourceMap.put("ConnectionName", nvl(extract.getDataSourceConnection().getDataSourceConnectionName(), STRING_DEFAULTS));
                                    dataSourceMap.put("DataSourceType", nvl(extract.getExtractSourceType(), STRING_DEFAULTS));
                                    dataSourceMap.put("DataSourceSubType", nvl(extract.getExtractSourceSubType(), STRING_DEFAULTS));
                                    dataSourceMap.put("Collection", nvl(extract.getNoSqlMetadata().getCollection(), STRING_DEFAULTS));
                                    dataSourceMap.put("Partitioner", nvl(extract.getNoSqlMetadata().getPartitioner(), STRING_DEFAULTS));
                                    DATASET_URN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                                            nvl(extract.getExtractSourceType(), STRING_DEFAULTS),
                                            nvl(extract.getNoSqlMetadata().getCollection(), STRING_DEFAULTS),
                                            System.getProperty("EXECUTION_ENV", "PROD"));
                                }
                                case "stream" -> {
                                    dataSourceMap.put("DataSourceType", nvl(extract.getExtractSourceType(), STRING_DEFAULTS));
                                    dataSourceMap.put("DataSourceSubType", nvl(extract.getExtractSourceSubType(), STRING_DEFAULTS));
                                    dataSourceMap.put("ConsumerTopic", nvl(extract.getStreamMetadata().getKafkaConsumerTopic(), STRING_DEFAULTS));
                                    dataSourceMap.put("ConsumerGroup", nvl(extract.getStreamMetadata().getKafkaConsumerGroup(), STRING_DEFAULTS));
                                    dataSourceMap.put("StartOffset", nvl(extract.getStreamMetadata().getKafkaStrtOffset(), STRING_DEFAULTS));
                                    dataSourceMap.put("MaxOffset", nvl(extract.getStreamMetadata().getKafkaMaxOffset(), STRING_DEFAULTS));
                                    dataSourceMap.put("PollingTimeout", nvl(extract.getStreamMetadata().getKafkaPollTimeout().toString(), STRING_DEFAULTS));
                                    dataSourceMap.put("TransactionalConsumer", nvl(extract.getStreamMetadata().getTranctnlCnsumrFlg(), STRING_DEFAULTS));
                                    dataSourceMap.put("WatermarkDuration", nvl(extract.getStreamMetadata().getWatrmrkDuration(), STRING_DEFAULTS));
                                    DATASET_URN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                                            nvl(extract.getExtractSourceSubType(), STRING_DEFAULTS),
                                            nvl(extract.getStreamMetadata().getKafkaConsumerGroup(), STRING_DEFAULTS) + "." +
                                                    nvl(extract.getStreamMetadata().getKafkaConsumerTopic(), STRING_DEFAULTS),
                                            System.getProperty("EXECUTION_ENV", "PROD"));
                                }
                                default -> {
                                    dataSourceMap.put("ConnectionName", nvl(extract.getDataSourceConnection().getDataSourceConnectionName(), STRING_DEFAULTS));
                                    DATASET_URN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)", "default", "default",
                                            System.getProperty("EXECUTION_ENV", "PROD"));
                                }
                            }
                            datasetFacets.dataSource(getDatasourceDatasetFacet(openLineage,extract.getDataSourceConnectionName(),
                                    extract.getDataSource().getDataSourceType(),dataSourceMap));
                        }
                        // Adding Storage layer Information
                        if (extract.getDataSourceConnection() != null &&
                                !extract.getDataSourceConnection().getDataSourceSubType().isEmpty())
                        {
                            datasetFacets.storage(openLineage.newStorageDatasetFacet("Storage Layer",
                                    extract.getDataSourceConnection().getDataSourceSubType()));
                        }

                        //TODO  -- Add Correct Dataset Owner
                        if(extract.getCreatedBy() != null && !extract.getCreatedBy().isEmpty()) {
                            Map<String, String> ownersMap = new HashMap<>();
                            ownersMap.put("Owning-Domain", nvl(extract.getCreatedBy(), STRING_DEFAULTS));
                            OpenLineage.OwnershipDatasetFacet ownership = getDatasetOwners(openLineage, ownersMap);
                            datasetFacets.ownership(ownership);
                        }

                        inputs.add(openLineage
                                .newInputDatasetBuilder()
                                .namespace(datasetUrn)
                                .name(extract.getDataframeName())
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
                                .newStorageDatasetFacet("Storage Layer", "In Memory");
                        outputDataset.storage(storage);

                        //TODO Add versioning Information's
                        OpenLineage.DatasetVersionDatasetFacet version = openLineage.newDatasetVersionDatasetFacet("1");
                        outputDataset.version(version);

                        OpenLineage.ColumnLineageDatasetFacet columnLineage =ColumnLevelLineage.get( "SELECT * from " + extract.getDataframeName(), dataframe);
                        outputDataset.columnLineage(columnLineage);

                        StructType inSchema = getDataFrameSchema(inSparkSession, extract.getDataframeName());
                        OpenLineage.SchemaDatasetFacet schema = getDatasourceSchema(openLineage, inSchema);
                        outputDataset.schema(schema);

                        outputs.add(openLineage
                                .newOutputDatasetBuilder()
                                .facets(outputDataset.build())
                                .namespace(datasetUrn)
                                .name(extract.getDataframeName())
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
