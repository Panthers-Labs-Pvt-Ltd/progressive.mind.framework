package com.progressive.minds.chimera.DataManagement.datalineage.facets;

import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineage.ColumnLineageDatasetFacet;
import io.openlineage.client.OpenLineage.ColumnLineageDatasetFacetFields;
import io.openlineage.client.OpenLineage.ColumnLineageDatasetFacetFieldsBuilder;
import io.openlineage.client.OpenLineage.InputField;
import io.openlineage.client.OpenLineage.InputFieldBuilder;

import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineage.ColumnLineageDatasetFacet;
import io.openlineage.client.OpenLineage.ColumnLineageDatasetFacetFields;
import io.openlineage.client.OpenLineage.ColumnLineageDatasetFacetFieldsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.openlineage.client.OpenLineage.ColumnLineageDatasetFacet;
import io.openlineage.client.OpenLineage.ColumnLineageDatasetFacetFieldsAdditional;
import io.openlineage.client.OpenLineage.ColumnLineageDatasetFacetFieldsAdditionalBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.progressive.minds.chimera.DataManagement.datalineage.models.OpenLineageDialect;
import com.progressive.minds.chimera.dto.*;
import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineageClientUtils;
import io.openlineage.sql.OpenLineageSql;
import io.openlineage.sql.SqlMeta;
import org.apache.hadoop.shaded.org.eclipse.jetty.websocket.common.frames.DataFrame;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.jetbrains.annotations.NotNull;
import za.co.absa.cobrix.spark.cobol.utils.SparkUtils;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static com.progressive.minds.chimera.DataManagement.datalineage.models.OpenLineageDialect.SPARKSQL;
import static com.progressive.minds.chimera.DataManagement.datalineage.utils.Utility.*;
import static io.openlineage.spark.agent.util.ScalaConversionUtils.toScalaOption;
public class DatasetFacets {

    public record ColumnLineageMap(String targetColumn, String sourceColumn, String sourceTable) {
        public ColumnLineageMap() {
            this("NA", "NA", "NA");
        }
    }
        static String STRING_DEFAULTS = "-";
    /**
     * OpenLineageChimera.DocumentationDatasetFacet is a facet in OpenLineageChimera used to capture documentation-related
     * metadata for a dataset. This facet is useful for adding extra documentation information, such as descriptions,
     * comments, and other helpful notes related to a dataset.
     * @param openLineageProducer
     * @param Description Description of Dataset
     * @param extraInfo   any other information you want to add in documentation in key value pair
     * @return DocumentationDatasetFacet
     */
    public static OpenLineage.DocumentationDatasetFacet
    getDocumentationDatasetFacet(OpenLineage openLineageProducer,
                                 String Description,  @Nullable Map<String, String> extraInfo) {

        OpenLineage.DocumentationDatasetFacetBuilder docInputFacet =
                openLineageProducer.newDocumentationDatasetFacetBuilder();
        extraInfo.forEach(docInputFacet::put);
        docInputFacet.description(Description);
        return docInputFacet.build();
    }

    public static OpenLineage.DatasourceDatasetFacet
    getDatasourceDatasetFacet(OpenLineage openLineageProducer,
                              String dataSourceName, String dataSourceURI,
                              @Nullable Map<String, String> extraInfo) throws URISyntaxException {
        OpenLineage.DatasourceDatasetFacetBuilder dataSourceFacet = openLineageProducer.newDatasourceDatasetFacetBuilder();
        extraInfo.forEach(dataSourceFacet::put);
        dataSourceFacet.name(dataSourceName).uri(new URI(dataSourceURI));
        return dataSourceFacet.build();
    }

    /**
     * OpenLineageChimera.SchemaDatasetFacet is a facet in OpenLineageChimera used to capture schema-related metadata about a dataset.
     * This facet typically includes the fields (or columns) of the dataset, their names, types,
     * and other related properties.
     * @param openLineageProducer
     * @param schema Struct Type Schema
     * @return SchemaDatasetFacet
     */
    public static OpenLineage.SchemaDatasetFacet getDatasourceSchema(OpenLineage openLineageProducer, StructType schema) {

        List<OpenLineage.SchemaDatasetFacetFields> fields =
                Arrays.stream(schema.fields()) // Convert StructType fields to stream
                        .map(field -> openLineageProducer.newSchemaDatasetFacetFieldsBuilder()
                                .name(field.name())
                                .type(field.dataType().typeName())
                                .description(toScalaOption(field.getComment().getOrElse(() -> "NA")).get())
                                .build()
                        )
                        .collect(Collectors.toList());

        /*List<OpenLineage.SchemaDatasetFacetFields> fields =
                java.util.Arrays.stream(schema.fields()) // Convert StructType fields to stream
                        .map(field -> openLineageProducer.newSchemaDatasetFacetFieldsBuilder()
                                .name(field.name())
                                .type(field.dataType().typeName())
                                .description(field.getComment().get())
                                .build()
                        )
                        .collect(Collectors.toList());
*/
        return openLineageProducer.newSchemaDatasetFacetBuilder()
                .fields(fields)
                .build();
    }

    /**
     *  OpenLineageChimera.SymlinksDatasetFacet is used to capture and represent symlinks (symbolic links) that are associated
     *  with a dataset in the OpenLineageChimera framework. Symbolic links are references to other files or directories in the
     *  filesystem, which can point to the original dataset or other datasets. This facet helps track these symlinks
     *  and provides metadata about them, which is useful in data lineage and data tracking scenarios
     * @param openLineageProducer
     * @param symlinkUris List of Name Type and Namespace
     * @return
     */
    public static OpenLineage.SymlinksDatasetFacet getSymlinksDatasetFacet (OpenLineage openLineageProducer,
                                                                            @Nullable List<Map<String, String>> symlinkUris)  {

        // Example dynamic input for symlinks
    /*    List<Map<String, String>> symlinkData = List.of(
                Map.of(
                        "name", "symlink1",
                        "type", "dataset",
                        "namespace", "s3://bucket1"
                ),
                Map.of(
                        "name", "symlink2",
                        "type", "dataset",
                        "namespace", "s3://bucket2"
                )
        );*/



        // Prepare the list of SymlinksDatasetFacetIdentifiers dynamically
        List<OpenLineage.SymlinksDatasetFacetIdentifiers> symlinkIdentifiers = new ArrayList<>();
        for (Map<String, String> symlink : symlinkUris) {
            OpenLineage.SymlinksDatasetFacetIdentifiers identifier = openLineageProducer.newSymlinksDatasetFacetIdentifiersBuilder()
                    .name(symlink.getOrDefault("name", "unknown")) // Fallback to "unknown" if missing
                    .type(symlink.getOrDefault("type", "unknown"))
                    .namespace(symlink.getOrDefault("namespace", "unknown"))
                    .build();
            symlinkIdentifiers.add(identifier);
        }

        OpenLineage.SymlinksDatasetFacet symlinksDatasetFacet = openLineageProducer.newSymlinksDatasetFacetBuilder()
                .identifiers(symlinkIdentifiers)
                .build();
        return symlinksDatasetFacet;
    }


    /**
     * OpenLineageChimera.LifecycleStateChangeDatasetFacet is used to capture lifecycle state changes for datasets in the
     * OpenLineageChimera framework. This facet provides metadata related to changes in the state of a dataset, such as when
     * it is created, modified, dropped, or altered. It helps track the transitions and changes in a dataset's
     * lifecycle, which is crucial for data lineage, governance, and monitoring in data workflows.
     *
     * Tracking Dataset Changes:
     * This facet helps to track when datasets are created, updated, dropped, or otherwise modified in a data pipeline
     * or system. This information is essential for understanding the history and evolution of a dataset.
     *
     * Data Lifecycle Management:
     * It can be used to manage the lifecycle of datasets, ensuring that datasets are tracked as they move through
     * different states (e.g., from creation to deletion).
     *
     * Data Governance and Compliance:
     *Understanding when datasets change states is important for compliance, auditing, and governance. For example,
     *  you may need to know when a dataset is dropped to ensure it is archived or flagged appropriately.
     * @param openLineageProducer
     * @param DatasetName
     * @param DatasetNamespace
     * @param stageChange
     * @return
     */
    public static OpenLineage.LifecycleStateChangeDatasetFacet getDataSetStateChange(OpenLineage openLineageProducer,
                                                                                     String DatasetName , String DatasetNamespace, String stageChange )  {

        // Possible Values for LifecycleStateChange ALTER,CREATE, DROP,OVERWRITE,RENAME,TRUNCATE;

        OpenLineage.LifecycleStateChangeDatasetFacet LifeCycleStageChangeFacet =openLineageProducer.newLifecycleStateChangeDatasetFacetBuilder()
                .lifecycleStateChange(OpenLineage.LifecycleStateChangeDatasetFacet.LifecycleStateChange.valueOf(stageChange))
                .previousIdentifier(new OpenLineage.LifecycleStateChangeDatasetFacetPreviousIdentifierBuilder()
                        .name(DatasetName)
                        .namespace(DatasetNamespace)
                        .build())
                .build();

        OpenLineage.LifecycleStateChangeDatasetFacet lifecycleStateChangeFacet = openLineageProducer
                .newLifecycleStateChangeDatasetFacetBuilder()
                .lifecycleStateChange(LifeCycleStageChangeFacet.getLifecycleStateChange())
                .build();
        return lifecycleStateChangeFacet;
    }

    /**
     * OpenLineageChimera.OwnershipDatasetFacet is used in OpenLineageChimera to capture metadata about the ownership of a dataset.
     *      This facet helps track information about the entities (users, teams, organizations, etc.) that own or manage a
     *      particular dataset. Ownership information is important for understanding who is responsible for the dataset and
     *      can help ensure compliance, governance, and security policies are properly implemented.
     *Use Cases :-
     * Data Governance : Knowing who owns or manages a dataset is essential for enforcing data governance policies.
     * Ownership information helps ensure that data access, modifications, and usage are properly controlled.
     *
     * Audit and Compliance: Ownership data is often required for audit trails and compliance with regulations.
     * Tracking ownership allows organizations to understand who has access to or is responsible for particular datasets
     *
     * Access Control:The ownership information can be used to enforce access controls, helping to restrict or allow
     * access to a dataset based on its owner.
     * @param openLineageProducer
     * @param ownerInfo
     * @return
     */
    public static OpenLineage.OwnershipDatasetFacet
    getDatasetOwners(OpenLineage openLineageProducer,
                     @Nullable Map<String, String> ownerInfo)  {

        // Extract owner fields from the map
        assert ownerInfo != null;
        String name = ownerInfo.getOrDefault("name", "Unknown");
        String type = ownerInfo.getOrDefault("type", "Unknown");
        List<OpenLineage.OwnershipDatasetFacetOwners> owners = new ArrayList<>();

        owners.add(openLineageProducer.newOwnershipDatasetFacetOwnersBuilder()
                .name(name)
                .type(type)
                .build()
        );

        OpenLineage.OwnershipDatasetFacet ownershipFacet = openLineageProducer.newOwnershipDatasetFacetBuilder()
                .owners(owners)
                .build();


        OpenLineage.OwnershipDatasetFacetOwnersBuilder ownersFacet= openLineageProducer.newOwnershipDatasetFacetOwnersBuilder();
        return ownershipFacet;
    }

/*
    private static Map<String, String> SourceTypesConfig(extractMetadata inExtractMetadata)
    {
        String SourceType = inExtractMetadata.getDataSourceType().toLowerCase(Locale.ROOT);
        switch (SourceType) {
            case "files" -> {
                System.out.print("Your logic here 0");
            }
            case "relational" -> {
                System.out.print("Your logic here 1");
            }
            case "nosql" -> {
                System.out.print("Your logic here 2 ");
            }
            case "stream" -> {
                System.out.print("Your logic here 3");
            }
            default -> {
                System.out.print("Your logic here 4");
            }
        }
        return null;
    }
*/


    /**
     * OpenLineageChimera.InputDatasetFacet is used to capture metadata about input datasets in OpenLineageChimera, specifically
     * the datasets that are consumed or used in a process, job, or transformation. This facet is important for
     * tracking data lineage, as it helps link input datasets to the outputs generated by a specific transformation
     * or job.
     * Use Cases :-
     *
     * Data Lineage Tracking: keeps track the source of data in a job or transformation pipeline. By associating input
     * datasets with their respective outputs, OpenLineageChimera allows you to visualize the flow of data from one
     * system or process to another.
     *
     * Understanding Dependencies: By capturing which datasets are used as inputs to a job or task, OpenLineageChimera helps
     * to map dependencies. Understanding these relationships is crucial for identifying downstream impacts of
     * changes to input datasets.
     *
     * Auditing and Compliance: Tracking input datasets is important for auditing purposes, as it ensures that the
     * right datasets are being used in the right processes. This information is valuable for compliance and governance.
     *
     * @param openLineageProducer
     * @param inExtractMetadata
     * @return
     * @throws URISyntaxException
     */
    //
    public static List<OpenLineage.InputDataset>
    InputDatasetFacet(OpenLineage openLineageProducer,
                      ExtractMetadata inExtractMetadata,
                              PipelineMetadata inPipelineMetadata, SparkSession inSparkSession
                    )
            throws URISyntaxException {
        List<OpenLineage.InputDataset> inputs = new ArrayList<>();
        List<String>TableNames =new ArrayList<>();

        if (inExtractMetadata.getRelationalMetadata() != null)
        {
        String SQLQuery = nvl(inExtractMetadata.getRelationalMetadata().getSqlText(),
                "SELECT * FROM " + inExtractMetadata.getRelationalMetadata().getTableName());
        TableNames = getTableNamesFromSQL(inSparkSession, SQLQuery);
        }
        else
        {
            TableNames.add("Unknown");
        }

        TableNames.forEach(instanceRun -> {
            String Domain = inPipelineMetadata.getOrgHierName();
            String doc = "";
            Map<String, String> dataSourceMap = new HashMap<>();

            Map<String, String> documentationMap = new HashMap<>();
            doc = "Extract Stage of Pipeline " + inExtractMetadata.getPipelineName();
            documentationMap.put("ExtractDescription", doc);

            Map<String, String> extractInfo = new HashMap<>();
            extractInfo.put("Key", "-");

            Map<String, String> ownersMap = new HashMap<>();
            ownersMap.put("Owning-Domain", nvl(Domain, STRING_DEFAULTS));
            List<Map<String, String>> symlinkUris = new ArrayList<>();

            OpenLineage.DocumentationDatasetFacet documentation = getDocumentationDatasetFacet(openLineageProducer,
                    inExtractMetadata.getPipelineName(), documentationMap);
            String datasetURN = "";
            String SourceType = inExtractMetadata.getExtractSourceType().toLowerCase(Locale.ROOT);
            switch (SourceType) {
                case "files" -> {
                    dataSourceMap.put("Type", nvl(inExtractMetadata.getExtractSourceType(), STRING_DEFAULTS));
                    dataSourceMap.put("Sub Type", nvl(inExtractMetadata.getExtractSourceSubType(), STRING_DEFAULTS));
                    dataSourceMap.put("URI", nvl(inExtractMetadata.getFileMetadata().getFilePath(), STRING_DEFAULTS));
                    dataSourceMap.put("Compression", nvl(inExtractMetadata.getFileMetadata().getCompressionType(), STRING_DEFAULTS));

                    datasetURN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                            getDataPlatform(inExtractMetadata.getFileMetadata().getFilePath())[0],
                            getDataPlatform(inExtractMetadata.getFileMetadata().getFilePath())[1],
                            System.getProperty("EXECUTION_ENV", "PROD"));
                }
                case "relational" -> {
                    dataSourceMap.put("ConnectionName", nvl(inExtractMetadata.getDataSourceConnection().getDataSourceConnectionName(), STRING_DEFAULTS));
                    dataSourceMap.put("DataSourceType", nvl(inExtractMetadata.getDataSourceConnection().getDataSourceType(), STRING_DEFAULTS));
                    dataSourceMap.put("DataSourceSubType", nvl(inExtractMetadata.getDataSourceConnection().getDataSourceSubType(), STRING_DEFAULTS));
                    dataSourceMap.put("Connection URL", nvl(inExtractMetadata.getDataSourceConnection().getConnectionMetadata(), STRING_DEFAULTS));
                    datasetURN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                            nvl(inExtractMetadata.getDataSourceConnection().getDataSourceSubType(), STRING_DEFAULTS),
                            nvl(inExtractMetadata.getRelationalMetadata().getDatabaseName(), STRING_DEFAULTS) + "." +
                                    nvl(instanceRun, STRING_DEFAULTS),
                            System.getProperty("EXECUTION_ENV", "PROD"));
                }
                case "nosql" -> {
                    dataSourceMap.put("ConnectionName", nvl(inExtractMetadata.getDataSourceConnection().getDataSourceConnectionName(), STRING_DEFAULTS));
                    dataSourceMap.put("DataSourceType", nvl(inExtractMetadata.getExtractSourceType(), STRING_DEFAULTS));
                    dataSourceMap.put("DataSourceSubType", nvl(inExtractMetadata.getExtractSourceSubType(), STRING_DEFAULTS));
                    dataSourceMap.put("Collection", nvl(inExtractMetadata.getNoSqlMetadata().getCollection(), STRING_DEFAULTS));
                    dataSourceMap.put("Partitioner", nvl(inExtractMetadata.getNoSqlMetadata().getPartitioner(), STRING_DEFAULTS));
                    datasetURN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                            nvl(inExtractMetadata.getExtractSourceType(), STRING_DEFAULTS),
                            nvl(inExtractMetadata.getNoSqlMetadata().getCollection(), STRING_DEFAULTS),
                            System.getProperty("EXECUTION_ENV", "PROD"));
                }
                case "stream" -> {
                    dataSourceMap.put("DataSourceType", nvl(inExtractMetadata.getExtractSourceType(), STRING_DEFAULTS));
                    dataSourceMap.put("DataSourceSubType", nvl(inExtractMetadata.getExtractSourceSubType(), STRING_DEFAULTS));
                    dataSourceMap.put("ConsumerTopic", nvl(inExtractMetadata.getStreamMetadata().getKafkaConsumerTopic(), STRING_DEFAULTS));
                    dataSourceMap.put("ConsumerGroup", nvl(inExtractMetadata.getStreamMetadata().getKafkaConsumerGroup(), STRING_DEFAULTS));
                    dataSourceMap.put("StartOffset", nvl(inExtractMetadata.getStreamMetadata().getKafkaStrtOffset(), STRING_DEFAULTS));
                    dataSourceMap.put("MaxOffset", nvl(inExtractMetadata.getStreamMetadata().getKafkaMaxOffset(), STRING_DEFAULTS));
                    dataSourceMap.put("PollingTimeout", nvl(inExtractMetadata.getStreamMetadata().getKafkaPollTimeout().toString(), STRING_DEFAULTS));
                    dataSourceMap.put("TransactionalConsumer", nvl(inExtractMetadata.getStreamMetadata().getTranctnlCnsumrFlg(), STRING_DEFAULTS));
                    dataSourceMap.put("WatermarkDuration", nvl(inExtractMetadata.getStreamMetadata().getWatrmrkDuration(), STRING_DEFAULTS));
                    datasetURN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                            nvl(inExtractMetadata.getExtractSourceSubType(), STRING_DEFAULTS),
                            nvl(inExtractMetadata.getStreamMetadata().getKafkaConsumerGroup(), STRING_DEFAULTS) + "." +
                                    nvl(inExtractMetadata.getStreamMetadata().getKafkaConsumerTopic(), STRING_DEFAULTS),
                            System.getProperty("EXECUTION_ENV", "PROD"));
                }
                default -> {
                    dataSourceMap.put("ConnectionName", nvl(inExtractMetadata.getDataSourceConnection().getDataSourceConnectionName(), STRING_DEFAULTS));
                    datasetURN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)", "default", "default",
                            System.getProperty("EXECUTION_ENV", "PROD"));
                }
            }

            OpenLineage.DatasourceDatasetFacet dataSource = null;
            try {
                dataSource = getDatasourceDatasetFacet(openLineageProducer,
                        inExtractMetadata.getDataSourceConnectionName(), inExtractMetadata.getDataSource().getDataSourceType(),
                        dataSourceMap);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }


            StructType inSchema = getDataFrameSchema(inSparkSession, inExtractMetadata.getDataframeName());
            OpenLineage.DatasetVersionDatasetFacet version = openLineageProducer.newDatasetVersionDatasetFacet("1");
            OpenLineage.SchemaDatasetFacet schema = getDatasourceSchema(openLineageProducer, inSchema);
            OpenLineage.OwnershipDatasetFacet ownership = getDatasetOwners(openLineageProducer, ownersMap);
            OpenLineage.StorageDatasetFacet storage = openLineageProducer.newStorageDatasetFacet("storageLayer", "File Format");
            OpenLineage.SymlinksDatasetFacet symlinks = getSymlinksDatasetFacet(openLineageProducer, symlinkUris);

            OpenLineage.DatasetFacets datasetFacets = openLineageProducer.newDatasetFacetsBuilder()
                    .documentation(documentation)
                    .dataSource(dataSource)
                    .schema(schema)
                    .ownership(ownership)
                    .storage(storage)
                    .version(version)
                    .symlinks(symlinks)
                    .build();

            inputs.add(openLineageProducer
                    .newInputDatasetBuilder()
                    .namespace(datasetURN)
                    .name(datasetURN)
                    .facets(datasetFacets)
                    .build());
        });
        return inputs;
    }

    /**
     * OpenLineageChimera.OutputDatasetFacet is used in OpenLineageChimera to capture metadata about output datasets in data
     * processing jobs or transformations. The facet provides details about datasets that are produced or generated
     * as a result of a transformation or pipeline. This information is crucial for tracking the flow of data from
     * one stage to the next, enabling end-to-end data lineage tracking.
     * Use Cases :-
     *
     * Data Lineage Tracking: By tracking the output datasets produced by a job or transformation, OpenLineageChimera allows
     * you to visualize the entire data flow, from input to output. This provides a clear picture of how data is
     * transformed through various stages of a pipeline.
     *
     * Audit and Compliance: Knowing the details of output datasets is important for audit trails. By tracking the
     * datasets produced by a job or task, organizations can ensure that they comply with governance, security,
     * and data management policies.
     *
     * Monitoring and Debugging: When debugging data pipeline issues, having access to metadata about the output
     * datasets can help identify where things went wrong in the processing pipeline and what datasets were affected.
     * @param openLineageProducer
     * @return
     * @throws URISyntaxException
     */
    public static OpenLineage.OutputDataset
    setOutputDataset(OpenLineage openLineageProducer,  PersistMetadata inPersistMetadata,
                     PipelineMetadata inPipelineMetadata, SparkSession inSparkSession) throws URISyntaxException {

        Map<String, String> persistInfo = new HashMap<>();
        Map<String, String> dataSourceMap = new HashMap<>();
        String datasetURN;
        String datasetName=nvl(inPersistMetadata.getDatabaseName(), STRING_DEFAULTS) + "." +
                nvl(inPersistMetadata.getTableName(), STRING_DEFAULTS);
        String SourceType = inPersistMetadata.getDataSourceConnection().getDataSourceType().toLowerCase(Locale.ROOT);
        StructType inSchema = getDataFrameSchema(inSparkSession, inPersistMetadata.getTargetSql());

        switch (SourceType) {
                 case "relational" -> {
                dataSourceMap.put("ConnectionName", nvl(inPersistMetadata.getDataSourceConnection().getDataSourceConnectionName(), STRING_DEFAULTS));
                dataSourceMap.put("DataSourceType", nvl(inPersistMetadata.getDataSourceConnection().getDataSourceType(), STRING_DEFAULTS));
                dataSourceMap.put("DataSourceSubType", nvl(inPersistMetadata.getDataSourceConnection().getDataSourceSubType(), STRING_DEFAULTS));
                dataSourceMap.put("Connection URL", nvl(inPersistMetadata.getDataSourceConnection().getConnectionMetadata(), STRING_DEFAULTS));
                datasetURN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)",
                        nvl(inPersistMetadata.getDataSourceConnection().getDataSourceSubType(), STRING_DEFAULTS),
                        datasetName,
                        System.getProperty("EXECUTION_ENV", "PROD"));
            }
            default -> {
                dataSourceMap.put("ConnectionName", nvl(inPersistMetadata.getDataSourceConnection().getDataSourceConnectionName(), STRING_DEFAULTS));
                datasetURN = String.format("urn:li:dataset:(urn:li:dataPlatform:%s,%s,%s)", "default", "default",
                        System.getProperty("EXECUTION_ENV", "PROD"));
            }
        }

        OpenLineage.DocumentationDatasetFacet documentation = getDocumentationDatasetFacet(openLineageProducer,
                inPersistMetadata.getPipelineName(), persistInfo);

        OpenLineage.DatasourceDatasetFacet dataSource = getDatasourceDatasetFacet(openLineageProducer,
                inPersistMetadata.getDataSourceConnectionName(),inPersistMetadata.getDataSource().getDataSourceType(),
                dataSourceMap);

        Map<String, String> ownersMap = new HashMap<>();
        ownersMap.put("Owning-Domain", nvl(inPipelineMetadata.getOrgHierName(), STRING_DEFAULTS));
        List<Map<String, String>> symlinkUris = new ArrayList<>();

        OpenLineage.DatasetVersionDatasetFacet version = openLineageProducer.newDatasetVersionDatasetFacet("1");

        OpenLineage.SchemaDatasetFacet schema = getDatasourceSchema(openLineageProducer, inSchema);
        OpenLineage.OwnershipDatasetFacet ownership = getDatasetOwners(openLineageProducer, ownersMap);
        OpenLineage.StorageDatasetFacet storage = openLineageProducer
                .newStorageDatasetFacet("storageLayer", inPersistMetadata.getDataSourceConnection().getDataSourceSubType());
        OpenLineage.SymlinksDatasetFacet symlinks = getSymlinksDatasetFacet(openLineageProducer, symlinkUris);

        OpenLineage.ColumnLineageDatasetFacet columnLineage = openLineageProducer.newColumnLineageDatasetFacetBuilder().build();


        OpenLineage.LifecycleStateChangeDatasetFacet lifecycleStateChange = getDataSetStateChange(openLineageProducer,
                datasetURN,datasetURN,"CREATE");

        OpenLineage.DatasetFacets datasetFacets  = openLineageProducer.newDatasetFacetsBuilder()
                .documentation(documentation)
                .columnLineage(columnLineage)
                .dataSource(dataSource)
                .schema(schema)
                .ownership(ownership)
                .storage(storage)
                .version(version)
                .symlinks(symlinks)
                .lifecycleStateChange(lifecycleStateChange)
                .build();

        OpenLineage.OutputDatasetFacet outputDatasetFacet = openLineageProducer.newOutputDatasetFacet();

        OpenLineage.OutputDatasetOutputFacets outputFacets = openLineageProducer.newOutputDatasetOutputFacetsBuilder()
                .put("DatasetName",outputDatasetFacet)
                .build();

        return openLineageProducer
                .newOutputDatasetBuilder()
                .facets(datasetFacets)
                .outputFacets(outputFacets)
                .namespace(datasetURN)
                .name(datasetName)
                .outputFacets(outputFacets)
                .facets(datasetFacets).build();
    }

    private static String @NotNull [] getDataPlatform(String inFullPath)
    {
        String dataPlatform ;
        String filePath ;

        if (inFullPath.contains("://")) {
            dataPlatform = inFullPath.split("://")[0];
            filePath = inFullPath.split("://")[1];
        }
        else if (inFullPath.contains(":/")) {
            dataPlatform = inFullPath.split(":/")[0];
            filePath = inFullPath.split(":/")[1];
            }
        else {
            dataPlatform = "file";
            filePath = inFullPath.split("://")[0];
        }
        return new String[]{dataPlatform, filePath};
    }

    public static void getColumnLevelLineage
            (OpenLineage openLineageProducer, String SQLQuery, String dataframe, String namespace, SparkSession inSparkSession)
    {
        // return type OpenLineage.ColumnLineageDatasetFacet
        //            (OpenLineage openLineageProducer, String SQLQuery, String dataframe, String namespace, SparkSession inSparkSession)
        StructType inSchema = getDataFrameSchema(inSparkSession, dataframe);
        List<String> columns = new ArrayList<>();
        String modifiedQuery = "";
        for (StructField field : inSchema.fields()) {
            columns.add(field.name());
        }
        if (SQLQuery.toLowerCase(Locale.ROOT).replaceAll("\\s+", "").startsWith("select*")) {
            String columnList = String.join(",", columns);
             modifiedQuery = SQLQuery.replace("*", columnList);
        }
        else
        {
            modifiedQuery = SQLQuery;
        }

       SqlMeta SQLLineage = OpenLineageSql.parse(Arrays.asList(modifiedQuery), String.valueOf(SPARKSQL)).get();
        String extractedLineage = SQLLineage.columnLineage().toString();

        ObjectMapper mapper  = JsonMapper.builder()
            .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
            .build();
        try {
            JsonNode receivedMessage  = mapper.readTree(extractedLineage);
            Integer StartNum = 0;
            Map<String, ColumnLineageMap> colMapping = new HashMap<>();

            for (JsonNode element : receivedMessage) {
                JsonNode targetColumnNode = element.path("descendant").get("name");
                String targetColumn = targetColumnNode != null ? targetColumnNode.asText() : "Unknown";

                try {
                    JsonNode lineageNode = mapper.readTree(element.get("lineage").toString());
                    Iterator<JsonNode> subElements = lineageNode.elements();

                    while (subElements.hasNext()) {
                        JsonNode subElement = subElements.next();

                        String subElementName = subElement.get("name") != null ? subElement.get("name").asText() : "Unknown";
                        String subElementOrigin = subElement.get("origin") != null ? subElement.get("origin").asText() : "Unknown";

                        String key = StartNum + "#" + targetColumn;
                        colMapping.put(key, new ColumnLineageMap(targetColumn, subElementName, subElementOrigin));

                        StartNum++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            colMapping.forEach((key, value) -> System.out.println(key + " -> " + value));
            List<ColumnLineageDatasetFacetFields> fields = new ArrayList<>();

            // Process each entry in the input map
            for (Map.Entry<String, ColumnLineageMap> entry : colMapping.entrySet()) {
                String targetColumn = entry.getKey();
                ColumnLineageMap lineageMap = entry.getValue();

                OpenLineage.InputFieldTransformationsBuilder IT = openLineageProducer.newInputFieldTransformationsBuilder()
                        .description("description")
                        .masking(false)
                        .type("transformation Type")
                        .subtype("transformation Subtype");
                OpenLineage.InputFieldTransformations IFT =IT.build();

                // Build the input field
                InputField inputField = new InputFieldBuilder()
                        .namespace("source_namespace")
                        .name(lineageMap.sourceTable)
                        .field(lineageMap.sourceColumn).transformations(Collections.singletonList(IFT))
                        .build();

                ColumnLineageDatasetFacetFieldsAdditionalBuilder CLD = new ColumnLineageDatasetFacetFieldsAdditionalBuilder()
                        .inputFields(Collections.singletonList(inputField))
                        .transformationType("STP")
                        .transformationDescription("NO TRA");

                // Build the field-level lineage
                ColumnLineageDatasetFacetFields fieldLineage = new ColumnLineageDatasetFacetFieldsBuilder()
                        .put(lineageMap.targetColumn, CLD.build())
                        .build();

                // Add to the fields map
                fields.add(fieldLineage);
            }
            String LL = SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(fields));
        System.out.println("fieldsfieldsfieldsfields ====" + LL);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        /*
        List<StructField> schema = Arrays.asList(inSchema.fields());


        for (StructField field : schema) {
            String fieldName = field.name();
             Map<String, ColumnLineageMap> colMapping = new HashMap<>();

            List<Map.Entry<String, ColumnLineageMap>> searchColumn = colMapping.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().targetColumn().replaceAll("\"", "")
                            .equalsIgnoreCase(fieldName))
                    .collect(Collectors.toList());


            List<ColumnLineageDatasetFacetFieldsAdditionalInputField> inputFields = new ArrayList<>();
ColumnLineageDatasetFacetFieldsAdditionalBuilder CB = new ColumnLineageDatasetFacetFieldsAdditionalBuilder()

            for (Map.Entry<String, ColumnLineageMap> mapField : searchColumn) {
                ColumnLineageMap columnLineageMap = mapField.getValue();

                if (columnLineageMap.targetColumn().replaceAll("\"", "")
                        .equalsIgnoreCase(fieldName)) {
                    inputFields.add(openLineageProducer.newColumnLineageDatasetFacetFieldsAdditionalInputFields(
                            "namespace",
                            columnLineageMap.sourceTable().replaceAll("\"", ""),
                            columnLineageMap.sourceColumn().replaceAll("\"", "")
                    ));
                }
            }

            ColumnLineageDatasetFacetFieldsAdditional additionalFields =
                    openLineageProducer.newColumnLineageDatasetFacetFieldsAdditionalBuilder()
                            .inputFields(inputFields)
                            .transformationType("STP")
                            .transformationDescription("No Transformation Description")
                            .build();

            dl.put(fieldName, additionalFields);
        }
*/

    }
}
