package com.progressive.minds.chimera.core.datahub.datasets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedin.common.*;
import com.linkedin.common.urn.*;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.DataPlatformUrn;
import com.linkedin.common.urn.DatasetUrn;
import com.linkedin.common.url.Url;
import com.linkedin.data.template.StringArray;
import com.linkedin.data.template.StringMap;
import com.linkedin.dataset.DatasetProperties;
import com.linkedin.dataset.EditableDatasetProperties;
import com.linkedin.mxe.MetadataChangeProposal;
import com.linkedin.schema.*;
import com.progressive.minds.chimera.core.datahub.modal.*;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.core.datahub.domain.ManageDomain;
import com.progressive.minds.chimera.core.datahub.dataproduct.ManageDataProduct;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.progressive.minds.chimera.core.datahub.Constants.*;
import static com.progressive.minds.chimera.core.datahub.ownership.ManageOwners.addOwners;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.createProposal;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.emitProposal;
import static com.progressive.minds.chimera.core.datahub.datasets.schema.*;

public class ManageDatasets {
    static ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(ManageDatasets.class);
    static String LoggerTag = "[DataHub- Create Dataset] -";


    public static void createDataset(String DatasetDefinition, String createdBy) throws Exception {
        DatahubLogger.logInfo("Creating Datasets Initializing .....");
        Dataset datasetsInfo = schema.getDatasetInformation(DatasetDefinition);
        DatahubLogger.logInfo("Getting Datasets Information's... .....");
        if (datasetsInfo != null) {
            String fabricType = String.valueOf(FabricType.valueOf(datasetsInfo.getFabricType()));
            DatasetUrn datasetUrn = UrnUtils.toDatasetUrn(datasetsInfo.getDatasetPlatformName(), datasetsInfo.getName(), fabricType);
            CorpuserUrn userUrn = new CorpuserUrn(createdBy);

            AuditStamp createdStamp = new AuditStamp()
                    .setActor(new CorpuserUrn(userUrn.getUsernameEntity()))
                    .setTime(Instant.now().toEpochMilli());

            AuditStamp lastModified = new AuditStamp()
                    .setTime(Instant.now().toEpochMilli())
                    .setActor(new CorpuserUrn(userUrn.getUsernameEntity()));

            SchemaFieldArray schemaFieldArray = new SchemaFieldArray();

            EditableSchemaFieldInfoArray editableSchemaFieldInfoArray = new EditableSchemaFieldInfoArray();
            EditableSchemaMetadata editableSchemaMetadata = new EditableSchemaMetadata()
                    .setEditableSchemaFieldInfo(editableSchemaFieldInfoArray);

            List<Field> SchemaLists = datasetsInfo.getFields();
            ObjectMapper objectMapper = new ObjectMapper();

            StringArray primaryKeys = new StringArray();
            ForeignKeyConstraintArray foreignKeyConstraintArray = new ForeignKeyConstraintArray();

            for (Field schema : SchemaLists) {
                SchemaField schemaField = new SchemaField();

                schemaField.setFieldPath(schema.getFieldCanonicalName());
                schemaField.setLabel(schema.getDisplayName());
                schemaField.setIsPartitioningKey(schema.isPartitionKey());
                schemaField.setIsPartOfKey(schema.isPrimaryKey());
                schemaField.setNullable(schema.isNullable());
                schemaField.setType(NativeTypeToSchemaType(schema.getType()));
                schemaField.setNativeDataType(schema.getType());
                schemaField.setDescription(schema.getDescription());
                schemaField.setLastModified(lastModified);

                EditableSchemaFieldInfo editableSchemaFieldInfo = new EditableSchemaFieldInfo();
                editableSchemaFieldInfo.setFieldPath(schema.getFieldCanonicalName());

                if (schema.isPrimaryKey()) {
                    primaryKeys.add(schema.getName());
                }

                if (schema.getTags() != null && !schema.getTags().isEmpty()) {
                    editableSchemaFieldInfo.setGlobalTags(setGlobalTags(schema.getTags()));
                }
                if (schema.getGlossaryTerm() != null && !schema.getGlossaryTerm().isEmpty()) {
                    editableSchemaFieldInfo.setGlossaryTerms(setGlossaryTerms(schema.getGlossaryTerm(),
                            userUrn.getUsernameEntity()));
                }
                schemaFieldArray.add(schemaField);
                editableSchemaFieldInfoArray.add(editableSchemaFieldInfo);

                UrnArray ForeignFieldsUrnArray = new UrnArray();
                UrnArray SourceFieldsUrnArray = new UrnArray();
                SourceFieldsUrnArray.add(datasetUrn);

                if (schema.getForeignKey() != null && !schema.getForeignKey().isEmpty()) {
                    for (ForeignKey fk : schema.getForeignKey()) {
                        ForeignFieldsUrnArray.add(UrnUtils.toDatasetUrn(fk.getDatasetPlatform(), fk.getDatasetName(),
                                fk.getOrigin()));

                        ForeignKeyConstraint foreignKeyConstraint = new ForeignKeyConstraint()
                                .setForeignDataset(UrnUtils.toDatasetUrn(fk.getDatasetPlatform(), fk.getDatasetName(),
                                        fk.getOrigin()))
                                .setForeignFields(ForeignFieldsUrnArray)
                                .setSourceFields(SourceFieldsUrnArray)
                                .setName(fk.getForeignKeyName());
                        foreignKeyConstraintArray.add(foreignKeyConstraint);
                    }
                }
            }
            SchemaMetadata schemaMetadata =
                    new SchemaMetadata()
                            .setSchemaName(datasetsInfo.getName())
                            .setPlatform(new DataPlatformUrn(datasetsInfo.getDatasetPlatformName()))
                            .setVersion(0L)
                            .setHash("")
                            .setFields(schemaFieldArray)
                            .setPlatformSchema(
                                    SchemaMetadata.PlatformSchema.create(
                                            new OtherSchema().setRawSchema(objectMapper.writeValueAsString(datasetsInfo.getFields()))))
                            .setCreated(createdStamp)
                            .setLastModified(lastModified)
                            .setDataset(datasetUrn)
                            .setPrimaryKeys(primaryKeys)
                            .setForeignKeys(foreignKeyConstraintArray);

            MetadataChangeProposal proposal = createProposal(String.valueOf(datasetUrn), DATASET_ENTITY_NAME,
                    SCHEMA_METADATA_ASPECT_NAME, ACTION_TYPE, schemaMetadata);
            DatahubLogger.logInfo(LoggerTag + "Preparing for MetadataChangeProposal : " + proposal);
            String retVal = emitProposal(proposal, DATASET_ENTITY_NAME);

            DatahubLogger.logInfo("Dataset Creation Response " + retVal);

            MetadataChangeProposal editableSchemaMetadataProposal = createProposal(String.valueOf(datasetUrn), DATASET_ENTITY_NAME,
                    EDITABLE_SCHEMA_METADATA_ASPECT_NAME, ACTION_TYPE, editableSchemaMetadata);

            String retVal2 = emitProposal(editableSchemaMetadataProposal, DATASET_ENTITY_NAME);
            DatahubLogger.logInfo("Editable Schema Metadata For Dataset Creation Response " + retVal2);


            EditableDatasetProperties editableDatasetProperties = new EditableDatasetProperties()
                    .setDescription(datasetsInfo.getDescription())
                    .setLastModified(lastModified)
                    .setCreated(createdStamp)
                    .setName(datasetsInfo.getQualifiedName());

            MetadataChangeProposal editableDatasetPropertiesProposal = createProposal(String.valueOf(datasetUrn), DATASET_ENTITY_NAME,
                    EDITABLE_DATASET_PROPERTIES_ASPECT_NAME, ACTION_TYPE, editableDatasetProperties);
            String retVal3 = emitProposal(editableDatasetPropertiesProposal, DATASET_ENTITY_NAME);
            DatahubLogger.logInfo("Editable Dataset Properties Metadata For Dataset Creation Response " + retVal3);

            List<Property> propertyList = datasetsInfo.getProperties();
            Map<String, String> propertyMap = propertyList.stream()
                    .collect(Collectors.toMap(Property::getName, Property::getValue));

            StringMap MapCustomProperties = new StringMap();
            MapCustomProperties.putAll(propertyMap);

            DatasetProperties datasetProperties = new DatasetProperties()
                    .setName(datasetsInfo.getDisplayName())
                    .setCustomProperties(MapCustomProperties).
                    setDescription(datasetsInfo.getDescription())
                    .setQualifiedName(datasetsInfo.getQualifiedName())
                    .setExternalUrl(new com.linkedin.common.url.Url(datasetsInfo.getUri()));

            MetadataChangeProposal datasetPropertiesProposal = createProposal(String.valueOf(datasetUrn), DATASET_ENTITY_NAME,
                    DATASET_PROPERTIES_ASPECT_NAME, ACTION_TYPE, datasetProperties);
            String datasetPropertiesRetVal = emitProposal(datasetPropertiesProposal, "dataset");
            DatahubLogger.logInfo("Dataset Properties Metadata For Dataset Creation Response " + datasetPropertiesRetVal);

            GlobalTags globalTags = setGlobalTags(datasetsInfo.getTags());
            MetadataChangeProposal globalTagsProposal = createProposal(String.valueOf(datasetUrn), DATASET_ENTITY_NAME,
                    GLOBAL_TAGS_ASPECT_NAME, ACTION_TYPE, globalTags);
            String globalTagsRetVal = emitProposal(globalTagsProposal, DATASET_ENTITY_NAME);

            DatahubLogger.logInfo("DatasetGlobal Tags Metadata For Dataset Creation Response " + globalTagsRetVal);

            ManageDomain manageDomain = new ManageDomain();
            manageDomain.addDomain(datasetsInfo.getDomain(), datasetUrn.toString(), DATASET_ENTITY_NAME);
            DatahubLogger.logInfo("Domain Name Mapping For Dataset Creation Completed");

            Map<String, String> ownersMap = new HashMap<>();
            for (Owners owner : datasetsInfo.getOwners()) {
                ownersMap.put(owner.getName(), owner.getType());
            }

            addOwners(datasetUrn, DATASET_ENTITY_NAME, OWNERSHIP_ASPECT_NAME, ACTION_TYPE, ownersMap);
            DatahubLogger.logInfo("Ownership Mapping For Dataset Creation Completed");

            if (datasetsInfo.getGlossaryTerm() != null && !datasetsInfo.getGlossaryTerm().isEmpty()) {
                GlossaryTerms glossaryTerms = setGlossaryTerms(datasetsInfo.getGlossaryTerm(), userUrn.getUsernameEntity());
                MetadataChangeProposal glossaryTermsProposal = createProposal(String.valueOf(datasetUrn), DATASET_ENTITY_NAME,
                        GLOSSARY_TERMS_ASPECT_NAME, ACTION_TYPE, glossaryTerms);

                String glossaryTermsResponse = emitProposal(glossaryTermsProposal, DATASET_ENTITY_NAME);
                DatahubLogger.logInfo("Glossary Mapping For Dataset Creation Completed " + glossaryTermsResponse);

                InstitutionalMemory institutionalMemory = new InstitutionalMemory().setElements(
                        new InstitutionalMemoryMetadataArray(new InstitutionalMemoryMetadata()
                                .setDescription(datasetsInfo.getDescription())
                                .setCreateStamp(createdStamp)
                                .setUrl(new Url(datasetsInfo.getUri()))));

                MetadataChangeProposal InstitutionalMemoryProposal = createProposal(String.valueOf(datasetUrn), DATASET_ENTITY_NAME,
                        INSTITUTIONAL_MEMORY_ASPECT_NAME, ACTION_TYPE, institutionalMemory);
                String InstitutionalMemoryResponse = emitProposal(InstitutionalMemoryProposal, DATASET_ENTITY_NAME);
                DatahubLogger.logInfo("InstitutionalMemory Mapping For Dataset Creation Completed " + InstitutionalMemoryResponse);

                new ManageDataProduct().addAssetToDataProduct(datasetsInfo.getDataProductName(), datasetUrn.toString());
                DatahubLogger.logInfo("Data Product Mapping For Dataset Creation Completed ");
            }

        }
    }
}


