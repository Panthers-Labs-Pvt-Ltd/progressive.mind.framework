package com.progressive.minds.chimera.core.datahub.datasets;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

class Tag {
    public String name;
    public String value;
    public boolean isInternal;
}

class Property {
    public String name;
    public String value;
}

class ForeignKey {
    public String datasetName;
    public String datasetPlatform;
    public String origin;
    public String ForeignKeyName;
    public String ForeignKeyColumn;
    public boolean LogicalKey;
}

class GlossaryTerm {
    public String glossaryTermName;
    public String Documentations;
}

class Field {
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

class Dataset {
    public String id;
    public String name;
    public String displayName;
    public String description;
    public String origin;
    public String datasetPlatformName;
    public String qualifiedName;
    public String uri;
    public List<Tag> tags;
    public List<Property> properties;
    public List<Field> fields;
}


public class schema {
    public static Dataset processJson() {
        String json = "{\n" +
                "    \"id\": \"custom_dataset_id\",\n" +
                "    \"name\": \"custom_dataset\",\n" +
                "    \"displayName\": \"custom_dataset\",\n" +
                "    \"description\": \"custom_dataset\",\n" +
                "    \"origin\": \"custom\",\n" +
                "    \"datasetPlatformName\": \"oracle\",\n" +
                "    \"qualifiedName\": \"oracle\",\n" +
                "    \"uri\": \"\",\n" +
                "    \"tags\": [\n" +
                "        {\n" +
                "            \"name\": \"tagsname1\",\n" +
                "            \"value\": \"tagsvalue1\",\n" +
                "            \"isInternal\": false\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"tagsname2\",\n" +
                "            \"value\": \"tagsvalue2\",\n" +
                "            \"isInternal\": false\n" +
                "        }\n" +
                "    ],\n" +
                "    \"properties\": [\n" +
                "        {\n" +
                "            \"name\": \"propertiesname1\",\n" +
                "            \"value\": \"value1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"propertiesname2\",\n" +
                "            \"value\": \"value2\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"fields\": [\n" +
                "        {\n" +
                "            \"name\": \"resource_name\",\n" +
                "            \"type\": \"string\",\n" +
                "            \"displayName\": \"resource_name\",\n" +
                "            \"description\": \"resource_name\",\n" +
                "            \"fieldCanonicalName\": \"resource_name\",\n" +
                "            \"maxLength\": 24,\n" +
                "            \"isPartitionKey\": false,\n" +
                "            \"isPrimaryKey\": false,\n" +
                "            \"isSampleTime\": false,\n" +
                "            \"isNullable\": true,\n" +
                "            \"foreignKey\": [\n" +
                "                {\n" +
                "                    \"datasetName\": \"tagsname1\",\n" +
                "                    \"datasetPlatform\": \"tagsvalue1\",\n" +
                "                    \"origin\": \"tagsvalue1\",\n" +
                "                    \"ForeignKeyName\": \"tagsvalue1\",\n" +
                "                    \"ForeignKeyColumn\": \"tagsvalue1\",\n" +
                "                    \"LogicalKey\": false\n" +
                "                }\n" +
                "            ],\n" +
                "            \"tags\": [\n" +
                "                {\n" +
                "                    \"name\": \"tagsname1\",\n" +
                "                    \"value\": \"tagsvalue1\",\n" +
                "                    \"isInternal\": false\n" +
                "                }\n" +
                "            ],\n" +
                "            \"glossaryTerm\": [\n" +
                "                {\n" +
                "                    \"glossaryTermName\": \"tagsname1\",\n" +
                "                    \"Documentations\": \"tagsvalue1\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"glossaryTermName\": \"tagsname1\",\n" +
                "                    \"Documentations\": \"tagsvalue1\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        // Create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        Dataset dataset = new Dataset();
        try {
            // Deserialize the JSON string into a Dataset object
            dataset = objectMapper.readValue(json, Dataset.class);

            // Output the fields to verify the result
            System.out.println("Dataset Name: " + dataset.name);
            System.out.println("First Tag Name: " + dataset.tags.get(0).name);
            System.out.println("First Field Name: " + dataset.fields.get(0).name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataset;
    }
    public static void main(String[] args) {
     Dataset  dd =   processJson();
     System.out.println(dd);
    }
}
