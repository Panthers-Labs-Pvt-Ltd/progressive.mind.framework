package com.progressive.minds.chimera.core.datalineage.models;

import com.progressive.minds.chimera.core.datalineage.models.metadata.extractMetadata;
import io.openlineage.client.OpenLineage;
import org.apache.spark.sql.types.StructType;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OpenLineageFactes {

    /**
     * OpenLineage.DocumentationDatasetFacet is a facet in OpenLineage used to capture documentation-related
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
     * OpenLineage.SchemaDatasetFacet is a facet in OpenLineage used to capture schema-related metadata about a dataset.
     * This facet typically includes the fields (or columns) of the dataset, their names, types,
     * and other related properties.
     * @param openLineageProducer
     * @param schema Struct Type Schema
     * @return SchemaDatasetFacet
     */
    public static OpenLineage.SchemaDatasetFacet getDatasourceSchema(OpenLineage openLineageProducer, StructType schema) {

        List<OpenLineage.SchemaDatasetFacetFields> fields =
                java.util.Arrays.stream(schema.fields()) // Convert StructType fields to stream
                        .map(field -> openLineageProducer.newSchemaDatasetFacetFieldsBuilder()
                                .name(field.name())
                                .type(field.dataType().typeName())
                                .description(field.getComment().get())
                                .build()
                        )
                        .collect(Collectors.toList());

        return openLineageProducer.newSchemaDatasetFacetBuilder()
                .fields(fields)
                .build();
    }

    /**
     *  OpenLineage.SymlinksDatasetFacet is used to capture and represent symlinks (symbolic links) that are associated
     *  with a dataset in the OpenLineage framework. Symbolic links are references to other files or directories in the
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
     * OpenLineage.LifecycleStateChangeDatasetFacet is used to capture lifecycle state changes for datasets in the
     * OpenLineage framework. This facet provides metadata related to changes in the state of a dataset, such as when
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
     * OpenLineage.OwnershipDatasetFacet is used in OpenLineage to capture metadata about the ownership of a dataset.
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


    /**
     * OpenLineage.InputDatasetFacet is used to capture metadata about input datasets in OpenLineage, specifically
     * the datasets that are consumed or used in a process, job, or transformation. This facet is important for
     * tracking data lineage, as it helps link input datasets to the outputs generated by a specific transformation
     * or job.
     * Use Cases :-
     *
     * Data Lineage Tracking: keeps track the source of data in a job or transformation pipeline. By associating input
     * datasets with their respective outputs, OpenLineage allows you to visualize the flow of data from one
     * system or process to another.
     *
     * Understanding Dependencies: By capturing which datasets are used as inputs to a job or task, OpenLineage helps
     * to map dependencies. Understanding these relationships is crucial for identifying downstream impacts of
     * changes to input datasets.
     *
     * Auditing and Compliance: Tracking input datasets is important for auditing purposes, as it ensures that the
     * right datasets are being used in the right processes. This information is valuable for compliance and governance.
     *
     * @param openLineageProducer
     * @param inExtractMetadata
     * @param extraInfo
     * @return
     * @throws URISyntaxException
     */
    public static OpenLineage.InputDatasetFacet
    getInputDatasetFacet(OpenLineage openLineageProducer,
                         extractMetadata inExtractMetadata,  @Nullable Map<String, String> extraInfo)
            throws URISyntaxException {
                OpenLineage.InputDatasetFacet inputs = (OpenLineage.InputDatasetFacet) List.of(openLineageProducer
                        .newInputDatasetBuilder()
                        .namespace(inExtractMetadata.getNameSpace())
                        .name(inExtractMetadata.getFileName())
                        .facets(openLineageProducer.newDatasetFacetsBuilder()
                        .dataSource(getDatasourceDatasetFacet(openLineageProducer,
                                inExtractMetadata.getDataSourceConnectionName(),inExtractMetadata.getDataSourceType(),
                                null))
                        .documentation(getDocumentationDatasetFacet(openLineageProducer,
                                inExtractMetadata.getPipelineName(), extraInfo))
                        .build())
                .build());

        return inputs;
    }

    /**
     * OpenLineage.OutputDatasetFacet is used in OpenLineage to capture metadata about output datasets in data
     * processing jobs or transformations. The facet provides details about datasets that are produced or generated
     * as a result of a transformation or pipeline. This information is crucial for tracking the flow of data from
     * one stage to the next, enabling end-to-end data lineage tracking.
     * Use Cases :-
     *
     * Data Lineage Tracking: By tracking the output datasets produced by a job or transformation, OpenLineage allows
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
     * @param inExtractMetadata
     * @param persistInfo
     * @return
     * @throws URISyntaxException
     */
    public static OpenLineage.OutputDataset
            getOutputDatasetFacet(OpenLineage openLineageProducer,  extractMetadata inExtractMetadata,
                                  @Nullable Map<String, String> persistInfo) throws URISyntaxException {

        /*OpenLineage.DatasetFacets DF = openLineageProducer.newDatasetFacetsBuilder()
                .schema(buildSchema(extractDf.schema))
                .dataSource(getDatasourceDatasetFacet(openLineageProducer,
                        inExtractMetadata.getDataSourceConnectionName(),inExtractMetadata.getDataSourceType(),
                        null))
                .documentation(getDocumentationDatasetFacet(openLineageProducer,
                        inExtractMetadata.getPipelineName(), persistInfo))
                .storage(openLineageProducer.newStorageDatasetFacet
                        ("Processed Layer", element.getFileFmt))
                .build();
*/
        OpenLineage.DocumentationDatasetFacet documentation = getDocumentationDatasetFacet(openLineageProducer,
                inExtractMetadata.getPipelineName(), persistInfo);

        OpenLineage.DatasourceDatasetFacet dataSource = getDatasourceDatasetFacet(openLineageProducer,
        inExtractMetadata.getDataSourceConnectionName(),inExtractMetadata.getDataSourceType(),
                null);

        OpenLineage.DatasetVersionDatasetFacet version = openLineageProducer.newDatasetVersionDatasetFacet("1");

        StructType inSchema =StructType.fromDDL(""); //TODO Provide Struct Schema
        OpenLineage.SchemaDatasetFacet schema = getDatasourceSchema(openLineageProducer, inSchema);
        OpenLineage.OwnershipDatasetFacet ownership = getDatasetOwners(openLineageProducer, null);
        OpenLineage.StorageDatasetFacet storage = openLineageProducer.newStorageDatasetFacet("storageLayer", "File Format");
        OpenLineage.ColumnLineageDatasetFacet columnLineage= null;
        OpenLineage.SymlinksDatasetFacet symlinks = getSymlinksDatasetFacet(openLineageProducer, null);
        OpenLineage.LifecycleStateChangeDatasetFacet lifecycleStateChange = getDataSetStateChange(openLineageProducer, "","","");

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

        OpenLineage.OutputDataset output = openLineageProducer
                .newOutputDatasetBuilder()
                .facets(datasetFacets)
                .outputFacets(outputFacets)
                .namespace("")
                .name("")
                .outputFacets(outputFacets)
                .facets(datasetFacets).build();
       return  output;
    }
}