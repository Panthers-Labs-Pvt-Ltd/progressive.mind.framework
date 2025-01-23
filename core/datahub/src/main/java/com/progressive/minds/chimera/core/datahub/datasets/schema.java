package com.progressive.minds.chimera.core.datahub.datasets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedin.common.*;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.GlossaryTermUrn;
import com.linkedin.common.urn.TagUrn;
import com.linkedin.schema.*;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class schema {
    // Tag class
    public static class Tag {
        public String name;
        public String value;
        public boolean isInternal;
    }

    // Property class
    public static class Property {
        public String name;
        public String value;
    }

    // ForeignKey class
    public static class ForeignKey {
        public String datasetName;
        public String datasetPlatform;
        public String origin;
        public String ForeignKeyName;
        public String ForeignKeyColumn;
        public boolean LogicalKey;
    }

    // GlossaryTerm class
    public static class GlossaryTerm {
        public String glossaryTermName;
        public String Documentations;
    }

    // Field class
    public static class Field {
        public String name;
        public String type;
        public String displayName;
        public String description;
        public String fieldCanonicalName;
        public int maxLength;
        public boolean isPartitionKey;
        public boolean isPrimaryKey;
        public boolean isSampleTime;
        public boolean isNullable;
        public List<ForeignKey> foreignKey;
        public List<Tag> tags;
        public List<GlossaryTerm> glossaryTerm;
    }


    public static class Dataset {
        public String id;
        public String name;
        public String displayName;
        public String description;
        public String FabricType;
        public String datasetPlatformName;
        public String qualifiedName;
        public String uri;
        public List<Tag> tags;
        public List<Property> properties;
        public List<Field> fields;
    }


 public static Dataset getDatasetInformation(String json) {
        // Create ObjectMapper instance
 ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(ManageDatasets.class);
     ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Deserialize the JSON string into a Dataset object and return it
            return objectMapper.readValue(json, Dataset.class);
        } catch (IOException e) {
            DatahubLogger.logError("Error While Processing Json Schema");
            return null; // return null if there's an error deserializing
        }
    }

    public static SchemaFieldDataType NativeTypeToSchemaType(Object value) {
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
/*
    public static GlobalTags setGlobalTags(List<Tag> datasetTags) {

        TagAssociationArray tagAssociationArray = new TagAssociationArray();
        for (Map.Entry<String, String> entry : datasetTags.entrySet()) {
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


    static GlossaryTerms setGlossaryTerms(List<schema.GlossaryTerm> glossaryTerms, String userName, String Doc) throws URISyntaxException {
        AuditStamp createdStamp = new AuditStamp()
                .setActor(new com.linkedin.common.urn.CorpuserUrn(userName))
                .setTime(Instant.now().toEpochMilli());

        GlossaryTermAssociationArray glossaryTermAssociationArray = new GlossaryTermAssociationArray();
        for (Map.Entry<String, String> entry : glossaryTerms.forEach().entrySet()) {
            String columnName = entry.getKey();
            String value = entry.getValue();
            String glossaryTermName = glossaryTerms.get(columnName.toLowerCase(Locale.ROOT));
            if (glossaryTermName != null & !glossaryTermName.isEmpty()) {
                com.linkedin.common.urn.GlossaryTermUrn glossaryTermUrn = com.linkedin.common.urn.GlossaryTermUrn.createFromString("urn:li:glossaryTerm:" + value);
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
    }*/


    public static GlobalTags setGlobalTags(List<Tag> datasetTags) {
        TagAssociationArray tagAssociationArray = new TagAssociationArray();

        // Iterate over the datasetTags list
        for (Tag datasetTag : datasetTags) {
            String tagName = datasetTag.name;
            String value = datasetTag.value;

                TagUrn tagUrn = new TagUrn(tagName);
                TagAssociation tagAssociation = new TagAssociation().setTag(tagUrn).setContext(value);
                tagAssociationArray.add(tagAssociation);
            }
        return new GlobalTags().setTags(tagAssociationArray);
    }

    public static GlossaryTerms setGlossaryTerms(List<schema.GlossaryTerm> glossaryTerms, String userName) throws URISyntaxException {
        // Create the audit stamp
        AuditStamp createdStamp = new AuditStamp()
                .setActor(new CorpuserUrn(userName))
                .setTime(Instant.now().toEpochMilli());

        // Create the glossary term association array
        GlossaryTermAssociationArray glossaryTermAssociationArray = new GlossaryTermAssociationArray();

        // Iterate through the glossary terms list
        for (schema.GlossaryTerm glossaryTerm : glossaryTerms) {
            String glossaryTermName = glossaryTerm.glossaryTermName;
            String documentations = glossaryTerm.Documentations;

            if (glossaryTermName != null && !glossaryTermName.isEmpty()) {

                GlossaryTermAssociation termAssociation = new GlossaryTermAssociation()
                        .setUrn(new GlossaryTermUrn(glossaryTermName))
                        .setContext(documentations)
                        .setActor(new CorpuserUrn(userName));
                glossaryTermAssociationArray.add(termAssociation);
            }
        }

        // Create the GlossaryTerms object and set properties
        GlossaryTerms glossaryTermObject = new GlossaryTerms();
        glossaryTermObject.setTerms(glossaryTermAssociationArray)
                .setAuditStamp(createdStamp);


        return glossaryTermObject;
    }
}
