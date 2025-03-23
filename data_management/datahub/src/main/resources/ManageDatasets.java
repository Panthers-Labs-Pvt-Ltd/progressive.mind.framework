import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedin.common.*;
import com.linkedin.common.urn.*;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.DataPlatformUrn;
import com.linkedin.common.urn.DatasetUrn;
import com.linkedin.common.urn.GlossaryTermUrn;
import com.linkedin.common.urn.TagUrn;
import com.linkedin.data.template.StringArray;
import com.linkedin.mxe.MetadataChangeProposal;
import com.linkedin.schema.*;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.*;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.time.Instant;

import static com.progressive.minds.chimera.core.datahub.common.genericUtils.createProposal;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.emitProposal;

public class ManageDatasets {
    static ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(ManageDatasets.class);
    static String LoggerTag = "[DataHub- Create Dataset] -";

    /**
     * @param FieldName
     * @param FieldDescription
     * @param dataType
     * @param IsPartitioningKey
     * @param globalTags
     * @param glossaryTerms
     * @param IsPartOfKey
     * @param isNullable
     */
    public record DatasetsSchema(
            @NotNull String FieldName,
            @NotNull String FieldDescription,
            @NotNull String dataType,
            @Null boolean IsPartitioningKey,
            @Null Map<String, String> globalTags,
            @Null Map<String, String> glossaryTerms,
            @Null boolean IsPartOfKey,
            @Null boolean isNullable){

        // constructor with default values
        public DatasetsSchema(String FieldName, String FieldDescription, String dataType) {
            this(FieldName, FieldDescription, dataType, false, null, null,
                    false, true); // Default values
        }
    }

    public record ForeignKeyDatasetsRecords(
            @NotNull String fkDatasetPlatformName,
            @NotNull String fkDatasetName,
            @NotNull String fkDatasetOrigin,
            @NotNull String ForeignKeyName,
            @NotNull String[] ForeignKeyColumn
            ){}

    /**
     * @param datasetPlatformName
     * @param datasetName
     * @param datasetDescription
     * @param datasetOrigin
     * @param datasetSchemaFilePath
     * @param datasetSchemaType
     * @param datasetExternalUrl
     * @param datasetQualifiedName
     * @param PrimaryKeys
     * @param ForeignKeys
     * @param datasetTags
     * @param datasetUri
     * @param CustomProperties
     * @param datasetCreatedBy
     */
  public record DatasetsRecords(
          @NotNull String datasetPlatformName,
          @NotNull String datasetName,
            @NotNull String datasetDescription,
            @Null String datasetOrigin,
            @Null String datasetSchemaFilePath,
            @Null String datasetSchemaType,
            @Null String datasetExternalUrl,
            @Null String datasetQualifiedName,
            @Null String[]  PrimaryKeys,
            @Null List<ForeignKeyDatasetsRecords> ForeignKeys,
            @Null String[]  datasetTags,
            @Null String datasetUri,
            @Null Map<String, String> CustomProperties,
            @Null String datasetRawSchema,
            @NotNull String datasetCreatedBy
            ){}

    /**
     * @param datasetsInfo
     * @return
     * @throws Exception
     */
    public static String createDataset(DatasetsRecords datasetsInfo) throws Exception {


        DatasetUrn datasetUrn = UrnUtils.toDatasetUrn(datasetsInfo.datasetPlatformName, datasetsInfo.datasetName,
                datasetsInfo.datasetOrigin);

        CorpuserUrn userUrn = new CorpuserUrn(datasetsInfo.datasetCreatedBy);

        AuditStamp createdStamp = new AuditStamp()
                .setActor(new CorpuserUrn(userUrn.getUsernameEntity()))
                .setTime(Instant.now().toEpochMilli());

        AuditStamp lastModified = new AuditStamp()
                .setTime(Instant.now().toEpochMilli())
                .setActor(new CorpuserUrn(userUrn.getUsernameEntity()));

        SchemaFieldArray schemaFieldArray = new SchemaFieldArray();

        List<DatasetsSchema> SchemaLists = SchemaMetadata(datasetsInfo.datasetSchemaType,
                Paths.get(datasetsInfo.datasetSchemaFilePath).toFile());

        EditableSchemaFieldInfoArray editableSchemaFieldInfoArray = new EditableSchemaFieldInfoArray();
        EditableSchemaMetadata editableSchemaMetadata = new EditableSchemaMetadata()
                .setEditableSchemaFieldInfo(editableSchemaFieldInfoArray);

        for (DatasetsSchema schema : SchemaLists) {
            SchemaField schemaField = new SchemaField();

            schemaField.setFieldPath(schema.FieldName);
            schemaField.setLabel(schema.FieldName);
            schemaField.setIsPartitioningKey(schema.IsPartitioningKey);
            schemaField.setIsPartOfKey(schema.IsPartOfKey);
            schemaField.setNullable(schema.isNullable);
            schemaField.setType(mapNativeTypeToSchemaType(schema.dataType));
            schemaField.setNativeDataType(schema.dataType);
            schemaField.setDescription(schema.FieldDescription);
            schemaField.setLastModified(lastModified);

            EditableSchemaFieldInfo editableSchemaFieldInfo = new EditableSchemaFieldInfo();
            editableSchemaFieldInfo.setFieldPath(schema.FieldName);

            if (schema.globalTags != null && ! schema.globalTags.isEmpty())
            {
                //schemaField.setGlobalTags(getGlobalTags(schema.globalTags));
                editableSchemaFieldInfo.setGlobalTags(getGlobalTags(schema.globalTags));
            }
            if (schema.glossaryTerms != null && ! schema.glossaryTerms.isEmpty())
            {
                //schemaField.setGlossaryTerms(getGlossaryTerms(schema.glossaryTerms,userUrn.getUsernameEntity()));
                editableSchemaFieldInfo.setGlossaryTerms(getGlossaryTerms(schema.glossaryTerms,userUrn.getUsernameEntity(),""));
            }
            schemaFieldArray.add(schemaField);
            editableSchemaFieldInfoArray.add(editableSchemaFieldInfo);
        }

        StringArray primaryKeys = new StringArray();
        if(datasetsInfo.PrimaryKeys != null){
            primaryKeys.addAll(List.of(datasetsInfo.PrimaryKeys ));
        }

        if (datasetsInfo.ForeignKeys != null)
        {
            UrnArray ForeignFieldsUrnArray = new UrnArray();
        ForeignKeyConstraintArray foreignKeyConstraintArray = new ForeignKeyConstraintArray();
        datasetsInfo.ForeignKeys.forEach(record -> {
            ForeignFieldsUrnArray.add(UrnUtils.toDatasetUrn(record.fkDatasetPlatformName, record.fkDatasetName,
                    record.fkDatasetOrigin));

            ForeignKeyConstraint foreignKeyConstraint = new ForeignKeyConstraint()
                    .setForeignDataset(UrnUtils.toDatasetUrn(record.fkDatasetPlatformName, record.fkDatasetName,
                            record.fkDatasetOrigin))
                    .setForeignFields(ForeignFieldsUrnArray)
                    .setName(record.ForeignKeyName);
            foreignKeyConstraintArray.add(foreignKeyConstraint);
        });
        }

        SchemaMetadata schemaMetadata =
                new SchemaMetadata()
                        .setSchemaName(datasetsInfo.datasetName)
                        .setPlatform(new DataPlatformUrn(datasetsInfo.datasetPlatformName))
                        .setVersion(0L)
                        .setHash("")
                        .setFields(schemaFieldArray)
                        .setPlatformSchema(
                                SchemaMetadata.PlatformSchema.create(
                                        new OtherSchema().setRawSchema(datasetsInfo.datasetRawSchema)))
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

  /*      MetadataChangeProposal proposal =
                MetadataChangeProposalWrapper.builder()
                        .entityType("dataset")
                        .entityUrn(datasetUrn)
                        .upsert()
                        .aspect(schemaMetadata)
                        .build();

        retVal = emitProposal(proposal, "dataProduct");

*/
        return retVal.toString();
    }

    /**
     *
     * @param glossaryTerms Map of Glossary terms needs to be associated with Columns Map<ColumnName, GlossaryTerm>
     * @param userName      UserName or ID who is creating or modifying
     * @return              GlossaryTerms
     * @throws URISyntaxException
     */
    private static GlossaryTerms getGlossaryTerms(Map<String, String> glossaryTerms, String userName, String Doc) throws URISyntaxException {
        AuditStamp createdStamp = new AuditStamp()
                .setActor(new CorpuserUrn(userName))
                .setTime(Instant.now().toEpochMilli());

        GlossaryTermAssociationArray glossaryTermAssociationArray = new GlossaryTermAssociationArray();
        for (Map.Entry<String, String> entry : glossaryTerms.entrySet()) {
            String columnName = entry.getKey();
            String value = entry.getValue();
            String glossaryTermName = glossaryTerms.get(columnName.toLowerCase(Locale.ROOT));
            if (glossaryTermName != null & !glossaryTermName.isEmpty()) {
                DatahubLogger.logInfo(LoggerTag + String.format("Mapping Glossary Term %s With Datasets", glossaryTermName));
                GlossaryTermUrn glossaryTermUrn = GlossaryTermUrn.createFromString("urn:li:glossaryTerm:" + value);
                GlossaryTermAssociation termAssociation = new GlossaryTermAssociation()
                        .setUrn(glossaryTermUrn).setActor(new CorpuserUrn(userName));
                        //.setAttribution(new MetadataAttribution(""));
                glossaryTermAssociationArray.add(termAssociation);
            }
        }
        GlossaryTerms glossaryTerm = new GlossaryTerms();
        glossaryTerm.setTerms(glossaryTermAssociationArray).setAuditStamp(createdStamp)
                .schema()
                .setDoc(Doc);
        return  glossaryTerm;
    }

    /**
     *
     * @param datasetTags Map of Global tags needs to be associated with Columns Map<ColumnName, TagName>
     * @return GlobalTags
     */
    private static GlobalTags getGlobalTags(Map<String, String> datasetTags) {
        Map<String, String> globalTags = new HashMap<>();
        globalTags.put("tag1", "Value1");
        globalTags.put("tag2", "Value2");

        TagAssociationArray tagAssociationArray = new TagAssociationArray();
        for (Map.Entry<String, String> entry : globalTags.entrySet()) {
            String columnName = entry.getKey();
            String value = entry.getValue();
            String globalTagsName = globalTags.get(columnName.toLowerCase(Locale.ROOT));
            if (globalTagsName != null & !globalTagsName.isEmpty()) {
                TagUrn tagUrn = new TagUrn(value);
                TagAssociation tagAssociation = new TagAssociation().setTag(tagUrn);
                tagAssociationArray.add(tagAssociation);
            }
        }
        return new GlobalTags().setTags(tagAssociationArray);
    }

    private static List<DatasetsSchema> SchemaMetadata(String schemaType, File schemaFile) throws Exception {

        return switch (schemaType.toLowerCase()) {
            case "json" -> readJsonSchema(schemaFile);
            case "jsd" ->  readJsonSchemaDocument(schemaFile);
            case "avro" -> readAvroSchema(schemaFile);
            case "yaml" -> readYamlSchema(schemaFile);
            default -> throw new IllegalArgumentException("Unsupported schema type: " + schemaType);
        };
    }

    private static List<DatasetsSchema> readJsonSchema(File schemaFile) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(schemaFile);
        List<DatasetsSchema> datasetsSchemas = new ArrayList<>();
        JsonNode fields = rootNode.path("fields");
        if (fields.isArray()) {
            for (JsonNode field : fields) {
                String fieldName = field.path("name").asText();
                String fieldDescription = field.path("description").asText("");
                String dataType = field.path("type").asText();
                boolean isPartitioningKey = field.path("isPartitioningKey").asBoolean(false);
                boolean isPartOfKey = field.path("isPartOfKey").asBoolean(false);
                boolean isNullable = field.path("nullable").asBoolean(true);
                datasetsSchemas.add(new DatasetsSchema(fieldName, fieldDescription, dataType, isPartitioningKey,
                        null, null, isPartOfKey, isNullable));
            }
        }
        return datasetsSchemas;
    }

    // protobuf TODO
    private static List<DatasetsSchema> readAvroSchema(File schemaFile) throws Exception {
        return readJsonSchema(schemaFile);
    }

    private static List<DatasetsSchema> readYamlSchema(File schemaFile) throws Exception {
        Yaml yaml = new Yaml();
        Map<String, Object> root = yaml.load(new FileInputStream(schemaFile));
        List<Map<String, Object>> fields = (List<Map<String, Object>>) root.get("fields");
        List<DatasetsSchema> datasetsSchemas = new ArrayList<>();

        for (Map<String, Object> field : fields) {
            String fieldName = (String) field.get("name");
            String fieldDescription = (String) field.getOrDefault("description", "");
            String dataType = (String) field.get("type");
            boolean isPartitioningKey = (boolean) field.getOrDefault("isPartitioningKey", false);
            boolean isPartOfKey = (boolean) field.getOrDefault("isPartOfKey", false);
            boolean isNullable = (boolean) field.getOrDefault("nullable", true);
            datasetsSchemas.add(new DatasetsSchema(fieldName, fieldDescription, dataType, isPartitioningKey,
                    null, null, isPartOfKey, isNullable));
        }
        return datasetsSchemas;
    }
    private static List<DatasetsSchema> readJsonSchemaDocument(File schemaFile) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(schemaFile);
        JsonNode propertiesNode = rootNode.path("properties");
        List<DatasetsSchema> datasetsSchemas = new ArrayList<>();
        Iterator<String> fieldNames = propertiesNode.fieldNames();

        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode fieldNode = propertiesNode.get(fieldName);

            String dataType = fieldNode.path("type").asText();
            String description = fieldNode.path("description").asText("");
            boolean isNullable = fieldNode.path("nullable").asBoolean(true);
            boolean isPartitioningKey = fieldNode.path("partitioningKey").asBoolean(false);
            boolean isPartOfKey = fieldNode.path("keyElement").asBoolean(false);

            datasetsSchemas.add(new DatasetsSchema(fieldName, description, dataType, isPartitioningKey,
                    null, null,isPartOfKey, isNullable));
        }
        return datasetsSchemas;
    }

    public static SchemaFieldDataType mapNativeTypeToSchemaType(Object value) {
        if (value == null) {
            return new SchemaFieldDataType().setType(SchemaFieldDataType.Type.create(new NullType()));
        } else if (value instanceof Boolean) {
            return new SchemaFieldDataType().setType(SchemaFieldDataType.Type.create(new BooleanType()));
        } else if (value instanceof byte[]) {
            return new SchemaFieldDataType().setType(SchemaFieldDataType.Type.create(new BytesType()));
        } else if (value instanceof String) {
            return new SchemaFieldDataType().setType(SchemaFieldDataType.Type.create(new StringType()));
        } else if (value instanceof Integer || value instanceof Long ||
                value instanceof Float || value instanceof Double ||
                value instanceof BigDecimal) {
            return new SchemaFieldDataType().setType(SchemaFieldDataType.Type.create(new NumberType()));
        } else if (value instanceof java.util.Date || value instanceof java.time.LocalDate) {
            return new SchemaFieldDataType().setType(SchemaFieldDataType.Type.create(new DateType()));
        } else if (value instanceof java.time.LocalTime) {
            return new SchemaFieldDataType().setType(SchemaFieldDataType.Type.create(new TimeType()));
        } else if (value.getClass().isEnum()) {
            return new SchemaFieldDataType().setType(SchemaFieldDataType.Type.create(new EnumType()));
        } else if (value instanceof Map) {
            return new SchemaFieldDataType().setType(SchemaFieldDataType.Type.create(new MapType()));
        } else if (value instanceof List || value.getClass().isArray()) {
            return new SchemaFieldDataType().setType(SchemaFieldDataType.Type.create(new ArrayType()));
        }  else if (value.getClass().isRecord()) {
            return new SchemaFieldDataType().setType(SchemaFieldDataType.Type.create(new RecordType()));
        }
        else {
            return new SchemaFieldDataType().setType(SchemaFieldDataType.Type.create(new StringType()));
        }
    }
}


