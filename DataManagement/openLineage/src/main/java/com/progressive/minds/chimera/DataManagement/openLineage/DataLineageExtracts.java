package com.progressive.minds.chimera.DataManagement.openLineage;

import com.progressive.minds.chimera.DataManagement.datalineage.utils.ColumnLevelLineage;
import com.progressive.minds.chimera.dto.ExtractMetadata;
import io.openlineage.client.OpenLineage;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import java.util.*;

import static com.progressive.minds.chimera.DataManagement.datalineage.facets.DatasetFacets.*;
import static com.progressive.minds.chimera.DataManagement.datalineage.facets.JobFacets.JobStartFacet;
import static com.progressive.minds.chimera.DataManagement.datalineage.facets.JobFacets.getJobFacet;
import static com.progressive.minds.chimera.DataManagement.datalineage.utils.Utility.getDataFrameSchema;
import static com.progressive.minds.chimera.DataManagement.datalineage.utils.Utility.nvl;
import org.apache.commons.lang3.tuple.Pair;

public class DataLineageExtracts {
    private static final String STRING_DEFAULTS = "-";
    static String DATASET_URN ;



    public static Pair<List<OpenLineage.InputDataset>, List<OpenLineage.OutputDataset>>get(OpenLineage.RunEvent.EventType eventType,
                                                       ExtractMetadata extract,
                                                       Map<String, String> JobInformation,
                                                       SparkSession inSparkSession,
                                                       OpenLineage openLineageProducer) throws Exception {
        String NameSpaceId= extract.getPipelineName();
        Dataset<Row> extractDateFrame = inSparkSession.emptyDataFrame();
        if(inSparkSession.catalog().tableExists(extract.getDataframeName())) {
            extractDateFrame =
                    inSparkSession.sql("SELECT * FROM " + extract.getDataframeName()).limit(1);
        }
        List<OpenLineage.InputDataset> inputs = new ArrayList<>();
        List<OpenLineage.OutputDataset> outputs = new ArrayList<>();

        Pair<List<OpenLineage.InputDataset>, List<OpenLineage.OutputDataset>> extractLineageMap =
                Pair.of(new ArrayList<>(), new ArrayList<>());

  /*      OpenLineage.RunFacets runFacets = openLineageProducer.newRunFacetsBuilder()
                .nominalTime(openLineageProducer.newNominalTimeRunFacetBuilder()
                        .nominalStartTime(now())
                        .nominalEndTime(now())
                        .build())
                .build();

        OpenLineage.Run run = openLineageProducer.newRunBuilder()
                .runId(runIdd)
                .facets(runFacets)
                .build();*/

        OpenLineage.Job JobStartFacet =JobStartFacet(openLineageProducer,
                extract.getPipelineName(), extract.getPipelineName(), getJobFacet(openLineageProducer, JobInformation));

        if (Objects.requireNonNull(eventType) == OpenLineage.RunEvent.EventType.START) {
            OpenLineage.DatasetFacetsBuilder datasetFacets = openLineageProducer
                    .newDatasetFacetsBuilder();

            Map<String, String> extractInformation = new HashMap<>();
            extractInformation.put("sourceType", extract.getExtractSourceType());
            extractInformation.put("subSourceType", extract.getExtractSourceSubType());
            extractInformation.put("jobType", "Ingestion");

            // Adding Documentations
            datasetFacets.documentation(getDocumentationDatasetFacet(openLineageProducer,
                    extract.getPipelineName(), extractInformation));

            if (extract.getExtractSourceType() != null &&
                    !extract.getExtractSourceType().isEmpty()) {
                Map<String, String> dataSourceMap = new HashMap<>();
                switch (extract.getExtractSourceType().toLowerCase(Locale.ROOT)) {
                    case "files" -> {
                        dataSourceMap.put("type", nvl(extract.getExtractSourceType(), STRING_DEFAULTS));
                        dataSourceMap.put("subType", nvl(extract.getExtractSourceSubType(), STRING_DEFAULTS));
                        dataSourceMap.put("uri", "file");
                        dataSourceMap.put("name", nvl(extract.getFileMetadata().getFilePath(),STRING_DEFAULTS));
                        dataSourceMap.put("compression", nvl(extract.getFileMetadata().getCompressionType(), STRING_DEFAULTS));
                        DATASET_URN = extract.getDataframeName() + "DatasetUrn";
                    }
                    case "relational" -> {
                        dataSourceMap.put("ConnectionName", nvl(extract.getDataSourceConnection().getDataSourceConnectionName(), STRING_DEFAULTS));
                        dataSourceMap.put("DataSourceType", nvl(extract.getDataSourceConnection().getDataSourceType(), STRING_DEFAULTS));
                        dataSourceMap.put("DataSourceSubType", nvl(extract.getDataSourceConnection().getDataSourceSubType(), STRING_DEFAULTS));
                        dataSourceMap.put("Connection URL", nvl(extract.getDataSourceConnection().getConnectionMetadata(), STRING_DEFAULTS));
                        DATASET_URN = extract.getRelationalMetadata().getDatabaseName().toLowerCase() + "." +
                                extract.getRelationalMetadata().getTableName();                    }
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
                        dataSourceMap.put("ConnectionName", nvl(extract.getDataSourceConnection()
                                .getDataSourceConnectionName(), STRING_DEFAULTS));
                        DATASET_URN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                                "default", "default", System.getProperty("EXECUTION_ENV", "PROD"));
                    }
                }
                datasetFacets.dataSource(getDatasourceDatasetFacet(openLineageProducer,
                        extract.getDataSourceConnectionName(), extract.getDataSource().getDataSourceType(),
                        dataSourceMap));
            }

            // Adding Storage layer Information
            if (extract.getDataSourceConnection() != null &&
                    !extract.getDataSourceConnection().getDataSourceSubType().isEmpty()) {
                datasetFacets.storage(openLineageProducer.newStorageDatasetFacet("Storage Layer",
                        extract.getDataSourceConnection().getDataSourceSubType()));
            }

            //TODO  -- Add Correct Dataset Owner
            if (extract.getCreatedBy() != null && !extract.getCreatedBy().isEmpty()) {
                Map<String, String> ownersMap = new HashMap<>();
                ownersMap.put("Owning-Domain", nvl(extract.getCreatedBy(), STRING_DEFAULTS));
                OpenLineage.OwnershipDatasetFacet ownership = getDatasetOwners(openLineageProducer, ownersMap);
                datasetFacets.ownership(ownership);
            }

            StructType inSchema = getDataFrameSchema(inSparkSession, extract.getDataframeName());
            OpenLineage.SchemaDatasetFacet schema = getDatasourceSchema(openLineageProducer, inSchema);
            datasetFacets.schema(schema);

            inputs.add(openLineageProducer
                    .newInputDatasetBuilder()
                    .namespace(NameSpaceId)
                    .name(extract.getDataframeName())
                    .facets(datasetFacets.build())
                    .build());
            // Mapping Output Datasets =============================

            // Life Cycle Change
            OpenLineage.LifecycleStateChangeDatasetFacet lifecycleStateChange = getDataSetStateChange(openLineageProducer,
                    DATASET_URN, DATASET_URN, "CREATE");
            datasetFacets.lifecycleStateChange(lifecycleStateChange);

            // Storage
            OpenLineage.StorageDatasetFacet storage = openLineageProducer
                    .newStorageDatasetFacet("Storage Layer", "In Memory");
            datasetFacets.storage(storage);

            //TODO Add versioning Information's
            OpenLineage.DatasetVersionDatasetFacet version = openLineageProducer.newDatasetVersionDatasetFacet("1");
            datasetFacets.version(version);

            OpenLineage.ColumnLineageDatasetFacet columnLineage =
                    ColumnLevelLineage.get("SELECT * from " + extract.getDataframeName(), extractDateFrame);
            datasetFacets.columnLineage(columnLineage);

     /*       StructType inSchema = getDataFrameSchema(inSparkSession, extract.getDataframeName());
            OpenLineage.SchemaDatasetFacet schema = getDatasourceSchema(openLineageProducer, inSchema);
            datasetFacets.schema(schema);*/

            outputs.add(openLineageProducer
                    .newOutputDatasetBuilder()
                    .facets(datasetFacets.build())
                    .namespace(NameSpaceId)
                    .name(extract.getDataframeName())
                    .facets(datasetFacets.build()).build());
            //RunEventReturnValue = RunFacets.getRunEvent(openLineageProducer, runIdd, JobStartFacet, inputs, outputs);
        } /*else {
            OpenLineage.Job jobEnd = openLineageProducer.newJobBuilder()
                    .namespace(NameSpaceId)
                    .name(DATASET_URN)
                    .build();

            RunEventReturnValue = openLineageProducer.newRunEventBuilder()
                    .eventType(eventType)
                    .eventTime(now)
                    .inputs(inputs)
                    .outputs(outputs)
                    .run(run)
                    .job(jobEnd).build();
        }*/
        //return RunFacets.getRunEvent(openLineageProducer, runIdd, JobStartFacet, inputs, outputs);
        return Pair.of(inputs, outputs);
    }
}
