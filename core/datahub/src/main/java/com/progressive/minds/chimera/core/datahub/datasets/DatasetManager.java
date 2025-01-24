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
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.core.datahub.domain.ManageDomain;
import com.progressive.minds.chimera.core.datahub.dataproduct.ManageDataProduct;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.progressive.minds.chimera.core.datahub.common.ManageOwners.addOwners;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.createProposal;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.emitProposal;
import static com.progressive.minds.chimera.core.datahub.datasets.schema.*;

public class DatasetManager {
    static ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(ManageDatasets.class);
    static String LoggerTag = "[DataHub- Create Dataset] -";


    public static void createDataset(String DatasetDefinition, String createdBy, String SchemaType) throws Exception {

        schema.Dataset datasetsInfo = schema.getDatasetInformation(DatasetDefinition);
        if (datasetsInfo != null) {
            String fabricType = String.valueOf(FabricType.valueOf(datasetsInfo.FabricType));
            DatasetUrn datasetUrn = UrnUtils.toDatasetUrn(datasetsInfo.datasetPlatformName, datasetsInfo.name, fabricType);
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

            List<schema.Field> SchemaLists = datasetsInfo.fields;
            ObjectMapper objectMapper = new ObjectMapper();

            StringArray primaryKeys = new StringArray();
            ForeignKeyConstraintArray foreignKeyConstraintArray = new ForeignKeyConstraintArray();

            for (schema.Field schema : SchemaLists) {
                SchemaField schemaField = new SchemaField();

                schemaField.setFieldPath(schema.fieldCanonicalName);
                schemaField.setLabel(schema.displayName);
                schemaField.setIsPartitioningKey(schema.isPartitionKey);
                schemaField.setIsPartOfKey(schema.isPrimaryKey);
                schemaField.setNullable(schema.isNullable);
                schemaField.setType(NativeTypeToSchemaType(schema.type));
                schemaField.setNativeDataType(schema.type);
                schemaField.setDescription(schema.description);
                schemaField.setLastModified(lastModified);

                EditableSchemaFieldInfo editableSchemaFieldInfo = new EditableSchemaFieldInfo();
                editableSchemaFieldInfo.setFieldPath(schema.fieldCanonicalName);

                if (schema.isPrimaryKey) {
                    primaryKeys.add(schema.name);
                }

                if (schema.tags != null && !schema.tags.isEmpty()) {
                    editableSchemaFieldInfo.setGlobalTags(setGlobalTags(schema.tags));
                }
                if (schema.glossaryTerm != null && !schema.glossaryTerm.isEmpty()) {
                    editableSchemaFieldInfo.setGlossaryTerms(setGlossaryTerms(schema.glossaryTerm, userUrn.getUsernameEntity()));
                }
                schemaFieldArray.add(schemaField);
                editableSchemaFieldInfoArray.add(editableSchemaFieldInfo);

                UrnArray ForeignFieldsUrnArray = new UrnArray();
                UrnArray SourceFieldsUrnArray = new UrnArray();
                SourceFieldsUrnArray.add(datasetUrn);

                if (schema.foreignKey != null && !schema.foreignKey.isEmpty()) {
                    for (schema.ForeignKey fk : schema.foreignKey) {
                        ForeignFieldsUrnArray.add(UrnUtils.toDatasetUrn(fk.datasetPlatform, fk.datasetName,
                                fk.origin));

                        ForeignKeyConstraint foreignKeyConstraint = new ForeignKeyConstraint()
                                .setForeignDataset(UrnUtils.toDatasetUrn(fk.datasetPlatform, fk.datasetName,
                                        fk.origin))
                                .setForeignFields(ForeignFieldsUrnArray)
                                .setSourceFields(SourceFieldsUrnArray)
                                .setName(fk.ForeignKeyName);
                        foreignKeyConstraintArray.add(foreignKeyConstraint);
                    }
                }
            }
            SchemaMetadata schemaMetadata =
                    new SchemaMetadata()
                            .setSchemaName(datasetsInfo.name)
                            .setPlatform(new DataPlatformUrn(datasetsInfo.datasetPlatformName))
                            .setVersion(0L)
                            .setHash("")
                            .setFields(schemaFieldArray)
                            .setPlatformSchema(
                                    SchemaMetadata.PlatformSchema.create(
                                            new OtherSchema().setRawSchema(objectMapper.writeValueAsString(datasetsInfo.fields))))
                            .setCreated(createdStamp)
                            .setLastModified(lastModified)
                            .setDataset(datasetUrn)
                            .setPrimaryKeys(primaryKeys)
                            .setForeignKeys(foreignKeyConstraintArray);

            MetadataChangeProposal proposal = createProposal(String.valueOf(datasetUrn), "dataset",
                    "schemaMetadata", "UPSERT", schemaMetadata);
            DatahubLogger.logInfo(LoggerTag + "Preparing for MetadataChangeProposal : " + proposal);
            String retVal = emitProposal(proposal, "dataProduct");

            MetadataChangeProposal proposal1 = createProposal(String.valueOf(datasetUrn), "dataset",
                    "editableSchemaMetadata", "UPSERT", editableSchemaMetadata);

            String retVal2 = emitProposal(proposal1, "dataset");


            EditableDatasetProperties editableDatasetProperties = new EditableDatasetProperties()
                    .setDescription(datasetsInfo.description)
                    .setLastModified(lastModified)
                    .setCreated(createdStamp)
                    .setName(datasetsInfo.qualifiedName);

            MetadataChangeProposal proposal2 = createProposal(String.valueOf(datasetUrn), "dataset",
                    "editableDatasetProperties", "UPSERT", editableDatasetProperties);
            String retVal3 = emitProposal(proposal2, "dataset");
            Map<String, String> customProperties = new HashMap<>();
            customProperties.put("encoding", "utf-8");

            StringMap MapCustomProperties = new StringMap();
            MapCustomProperties.putAll(customProperties);


            DatasetProperties datasetProperties = new DatasetProperties()
                    .setName(datasetsInfo.displayName)
                    .setCustomProperties(MapCustomProperties).
                    setDescription(datasetsInfo.description)
                    .setQualifiedName(datasetsInfo.qualifiedName)
                    .setExternalUrl(new com.linkedin.common.url.Url(datasetsInfo.uri));
            MetadataChangeProposal proposal4 = createProposal(String.valueOf(datasetUrn), "dataset",
                    "datasetProperties", "UPSERT", datasetProperties);
            String retVal4 = emitProposal(proposal4, "dataset");

            GlobalTags globalTags = setGlobalTags(datasetsInfo.tags);
            MetadataChangeProposal proposal5 = createProposal(String.valueOf(datasetUrn), "dataset",
                    "globalTags", "UPSERT", globalTags);
            String retVal5 = emitProposal(proposal5, "dataset");

            ManageDomain manageDomain = new ManageDomain();
            manageDomain.addDomain(datasetsInfo.domain, datasetUrn.toString(), "dataset");

            Map<String, String> ownersMap = new HashMap<>();
            for (Owners owner : datasetsInfo.owners) {
                ownersMap.put(owner.getName(), owner.getType());
            }

            addOwners(datasetUrn, "dataset", "ownership", "UPSERT", ownersMap);

            if (datasetsInfo.glossaryTerm != null && !datasetsInfo.glossaryTerm.isEmpty()) {
                GlossaryTerms glossaryTerms = setGlossaryTerms(datasetsInfo.glossaryTerm, userUrn.getUsernameEntity());
                MetadataChangeProposal glossaryTermsProposal = createProposal(String.valueOf(datasetUrn), "dataset",
                        "glossaryTerms", "UPSERT", glossaryTerms);
                String glossaryTermsemit = emitProposal(glossaryTermsProposal, "dataset");

                InstitutionalMemory institutionalMemory = new InstitutionalMemory().setElements(
                        new InstitutionalMemoryMetadataArray(new InstitutionalMemoryMetadata()
                                .setDescription(datasetsInfo.description)
                                .setCreateStamp(createdStamp)
                                .setUrl(new Url(datasetsInfo.uri))));

                MetadataChangeProposal InstitutionalMemoryProposal = createProposal(String.valueOf(datasetUrn), "dataset",
                        "institutionalMemory", "UPSERT", institutionalMemory);
                String InstitutionalMemoryemit = emitProposal(InstitutionalMemoryProposal, "dataset");

                new ManageDataProduct().addAssetToDataProduct(datasetsInfo.dataProductName, datasetUrn.toString());
            }

        }
    }
}


