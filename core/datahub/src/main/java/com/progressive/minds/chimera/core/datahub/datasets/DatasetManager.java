package com.progressive.minds.chimera.core.datahub.datasets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedin.common.*;
import com.linkedin.common.urn.*;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.DataPlatformUrn;
import com.linkedin.common.urn.DatasetUrn;
import com.linkedin.data.template.StringArray;
import com.linkedin.mxe.MetadataChangeProposal;
import com.linkedin.schema.*;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Map;

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

            for (schema.Field schema : SchemaLists) {
                SchemaField schemaField = new SchemaField();

                schemaField.setFieldPath(schema.fieldCanonicalName);
                schemaField.setLabel(schema.displayName);
                schemaField.setIsPartitioningKey(schema.isPartitionKey);
                schemaField.setIsPartOfKey(schema.isPartitionKey);
                schemaField.setNullable(schema.isNullable);
                schemaField.setType(NativeTypeToSchemaType(schema.type));
                schemaField.setNativeDataType(schema.type);
                schemaField.setDescription(schema.description);
                schemaField.setLastModified(lastModified);

                EditableSchemaFieldInfo editableSchemaFieldInfo = new EditableSchemaFieldInfo();
                editableSchemaFieldInfo.setFieldPath(schema.fieldCanonicalName);

                if(schema.isPrimaryKey){
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
                    // .setForeignKeys(foreignKeyConstraintArray)
                    ;

            MetadataChangeProposal proposal = createProposal(String.valueOf(datasetUrn), "dataset",
                    "schemaMetadata", "UPSERT", schemaMetadata);
            DatahubLogger.logInfo(LoggerTag + "Preparing for MetadataChangeProposal : " + proposal);
            String retVal = emitProposal(proposal, "dataProduct");

            MetadataChangeProposal proposal1 = createProposal(String.valueOf(datasetUrn), "dataset",
                    "editableSchemaMetadata", "UPSERT", editableSchemaMetadata);

            String retVal2 = emitProposal(proposal1, "dataProduct");

            System.out.println(schemaMetadata);
            System.out.println(retVal2);
        }
    }
}


