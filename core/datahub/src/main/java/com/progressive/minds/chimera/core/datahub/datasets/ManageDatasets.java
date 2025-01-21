package com.progressive.minds.chimera.core.datahub.datasets;

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
import com.linkedin.schema.*;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;

public class ManageDatasets {
    ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(ManageDatasets.class);
    String LoggerTag = "[DataHub- Create Dataset] -";

    public record DatasetsSchema(
            String FieldName,
            String FieldDescription,
            String dataType,
            @Null boolean IsPartitioningKey,
            @Null Map<String, String> globalTags,
            @Null Map<String, String> glossaryTerms,
            @Null boolean IsPartOfKey,
            @Null boolean isNullable){

        // constructor with default values
        public DatasetsSchema(String FieldName, String FieldDescription, String dataType) {
            this(FieldName, FieldDescription, dataType, false, null, null, false, true); // Default values
        }
    };

/*    public record DatasetsRecords(
            @NotNull String name,
            @NotNull String documentation,
            @Null String origin,
            @Null String platformName,
            @Null String platformName,
            @Null String platformName,
            @Null String platformName,
            @Null String platformName,
            @Null String platformName,
            @Null String platformName,
            @Null String platformName,
            @Null String platformName,

            )*/

    public String createDataset(String platformName, String datasetName, String datasetOrigin, String createdBy) throws Exception {


        DatasetUrn datasetUrn = UrnUtils.toDatasetUrn(platformName, datasetName, datasetOrigin);
        CorpuserUrn userUrn = new CorpuserUrn(createdBy);

        AuditStamp createdStamp = new AuditStamp()
                .setActor(new CorpuserUrn(userUrn.getUsernameEntity()))
                .setTime(Instant.now().toEpochMilli());

        AuditStamp lastModified = new AuditStamp().setTime(Instant.now().toEpochMilli()).setActor(userUrn);
        String rawSchema = "";

        SchemaMetadata schemaMetadata =
                new SchemaMetadata()
                        .setSchemaName(platformName + "_raw_schema")
                        .setPlatform(new DataPlatformUrn(platformName))
                        .setVersion(0L)
                        .setHash("")
                        .setPlatformSchema(
                                SchemaMetadata.PlatformSchema.create(
                                        new OtherSchema().setRawSchema(rawSchema)))
                        .setLastModified(lastModified);

        SchemaFieldArray schemaFieldArray = new SchemaFieldArray();

        List<DatasetsSchema> SchemaLists = SchemaMetadata(null,null);

        for (DatasetsSchema schema : SchemaLists) {
            schemaFieldArray.add(new SchemaField()
                    .setFieldPath(schema.FieldName)
                    .setLabel(schema.FieldName)
                    .setGlobalTags(getGlobalTags(schema.globalTags))
                    .setGlossaryTerms(getGlossaryTerms(schema.glossaryTerms,userUrn.getUsernameEntity()))
                    .setIsPartitioningKey(schema.IsPartitioningKey)
                    .setIsPartOfKey(schema.IsPartOfKey)
                    .setNullable(schema.isNullable)
                    .setType(mapNativeTypeToSchemaType(schema.dataType))
                    .setNativeDataType(schema.dataType)
                    .setDescription(schema.FieldDescription)
                    .setLastModified(lastModified));
        }

        return "";
    }

    /**
     *
     * @param glossaryTerms Map of Glossary terms needs to be associated with Columns Map<ColumnName, GlossaryTerm>
     * @param userName      UserName or ID who is creating or modifying
     * @return              GlossaryTerms
     * @throws URISyntaxException
     */
    private GlossaryTerms getGlossaryTerms(Map<String, String> glossaryTerms, String userName) throws URISyntaxException {
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
                        .setUrn(glossaryTermUrn);
                glossaryTermAssociationArray.add(termAssociation);
            }
        }
        GlossaryTerms glossaryTerm = new GlossaryTerms()
                .setTerms(glossaryTermAssociationArray).setAuditStamp(createdStamp);
        return glossaryTerm;
    }

    /**
     *
     * @param globalTags Map of Global tags needs to be associated with Columns Map<ColumnName, TagName>
     * @return GlobalTags
     */
    private GlobalTags getGlobalTags(Map<String, String> globalTags) {
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

    private List<DatasetsSchema> SchemaMetadata(String schemaType, File schemaFile) throws Exception {

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
                datasetsSchemas.add(new DatasetsSchema(fieldName, fieldDescription, dataType, isPartitioningKey,null, null, isPartOfKey, isNullable));
            }
        }
        return datasetsSchemas;
    }

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
            datasetsSchemas.add(new DatasetsSchema(fieldName, fieldDescription, dataType, isPartitioningKey,null, null, isPartOfKey, isNullable));
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

            datasetsSchemas.add(new DatasetsSchema(fieldName, description, dataType, isPartitioningKey, null, null,isPartOfKey, isNullable));
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
        } else if (value instanceof java.sql.Time || value instanceof java.time.LocalTime) {
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
            // Default to StringType
            return new SchemaFieldDataType().setType(SchemaFieldDataType.Type.create(new StringType()));
        }
    }
}


