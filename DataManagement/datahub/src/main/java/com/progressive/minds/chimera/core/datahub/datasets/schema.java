package com.progressive.minds.chimera.core.datahub.datasets;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkedin.common.*;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.GlossaryTermUrn;
import com.linkedin.common.urn.TagUrn;
import com.linkedin.schema.*;
import com.progressive.minds.chimera.core.datahub.tag.ManageTags;
import com.progressive.minds.chimera.core.datahub.modal.Dataset;
import com.progressive.minds.chimera.core.datahub.modal.GlossaryTerm;
import com.progressive.minds.chimera.core.datahub.modal.Tag;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

import java.util.List;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class schema {

 public static Dataset getDatasetInformation(String InputFormat) {
 ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(ManageDatasets.class);
     InputFormat = InputFormat.trim();
 String SchemaFormat;

    if (InputFormat.startsWith("{") || InputFormat.startsWith("["))  SchemaFormat="JSON";
     else if (InputFormat.contains(":") && !InputFormat.contains("{"))  SchemaFormat = "YAML";
     else throw new RuntimeException("Invalid Format of Dataset Definition");
     Dataset dataset;
     switch (SchemaFormat.toUpperCase()) {
         case "JSON":
             ObjectMapper objectMapper = new ObjectMapper();
             try {
                 dataset = objectMapper.readValue(InputFormat, Dataset.class);
             } catch (IOException e) {
                 DatahubLogger.logError("Error While Processing Json Schema " + e.getMessage());
                 return null;
             }
             break;
         case "YML":
         case "YAML":
             ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
             try {
                 dataset =  yamlMapper.readValue(InputFormat, Dataset.class);
             } catch (JsonProcessingException e) {
                 throw new RuntimeException(e);
             }
             break;
         default:
             System.out.println("Unsupported format: " + SchemaFormat);
             throw new IllegalArgumentException("Invalid format provided: " + SchemaFormat);
     }
     return  dataset;
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
        } else if (value instanceof Integer || value instanceof Long ||value instanceof Number||
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

    public static GlobalTags setGlobalTags(List<Tag> datasetTags) throws IOException, ExecutionException, InterruptedException {
        TagAssociationArray tagAssociationArray = new TagAssociationArray();

        // Iterate over the datasetTags list
        for (Tag datasetTag : datasetTags) {
            String tagName = datasetTag.getName();
            String value = datasetTag.getValue();

                TagUrn tagUrn = new TagUrn(tagName);
                TagAssociation tagAssociation = new TagAssociation().setTag(tagUrn).setContext(value);
                tagAssociationArray.add(tagAssociation);
                ManageTags.createTags(tagName,value);
            }
         return new GlobalTags().setTags(tagAssociationArray);
    }

    public static GlossaryTerms setGlossaryTerms(List<GlossaryTerm> glossaryTerms, String userName)  {
        // Create the audit stamp
        AuditStamp createdStamp = new AuditStamp()
                .setActor(new CorpuserUrn(userName))
                .setTime(Instant.now().toEpochMilli());

        // Create the glossary term association array
        GlossaryTermAssociationArray glossaryTermAssociationArray = new GlossaryTermAssociationArray();

        // Iterate through the glossary terms list
        for (GlossaryTerm glossaryTerm : glossaryTerms) {
            String glossaryTermName = glossaryTerm.getGlossaryTermName();
            String documentations = glossaryTerm.getDocumentations();

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
