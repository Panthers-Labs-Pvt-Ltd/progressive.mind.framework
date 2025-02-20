package com.progressive.minds.chimera.DataManagement.datalineage.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.progressive.minds.chimera.DataManagement.datalineage.datasources.OpenLineageDialect;
import com.progressive.minds.chimera.DataManagement.datalineage.facets.DatasetFacets;
import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineageClientUtils;
import io.openlineage.sql.OpenLineageSql;
import io.openlineage.sql.SqlMeta;
import org.apache.hadoop.shaded.org.eclipse.jetty.websocket.common.frames.DataFrame;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructField;
import za.co.absa.cobrix.spark.cobol.utils.SparkUtils;

import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class ColumnLevelLineage {
    /**
     * Extracts column lineage from JSON input.
     *
     * @param jsonString JSON string representing column lineage
     * @return List of extracted lineage mappings
     * @throws Exception If JSON parsing fails
     */
    public static List<Map<String, Object>> extractLineage(String jsonString) throws Exception {
        List<Map<String, Object>> lineageList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        // Parse JSON array
        JsonNode jsonArray = mapper.readTree(jsonString);

        // Iterate over JSON elements
        for (JsonNode element : jsonArray) {
            String targetColumn = element.path("descendant").path("name").asText();
            String targetOrigin = element.path("descendant").path("origin").asText();

            List<Map<String, String>> sourceColumns = new ArrayList<>();
            for (JsonNode source : element.path("lineage")) {
                Map<String, String> sourceColumn = new HashMap<>();
                sourceColumn.put("source_table", source.path("origin").asText());
                sourceColumn.put("source_column", source.path("name").asText());
                sourceColumns.add(sourceColumn);
            }

            // Store extracted information in a map
            Map<String, Object> lineageEntry = new HashMap<>();
            lineageEntry.put("target_column", targetColumn);
            lineageEntry.put("target_origin", targetOrigin);
            lineageEntry.put("source_columns", sourceColumns);

            lineageList.add(lineageEntry);
        }
        return lineageList;
    }

    public static OpenLineage.ColumnLineageDatasetFacet get2(String sqlQuery, Dataset<Row> dataframe)
            throws Exception {
        String PRODUCER_NAME = "https://github.com/OpenLineage/OpenLineage/tree/1.16.0/integration/spark";
        URI producer = URI.create(PRODUCER_NAME);
        OpenLineage openLineageProducer = new OpenLineage(producer);

        Map<String, OpenLineage.ColumnLineageDatasetFacetFieldsAdditional> DL = new HashMap<>();
        List<StructField> schema = Arrays.asList(dataframe.schema().fields());
        Map<String, DatasetFacets.ColumnLineageMap> ColMapping = new HashMap<>();

        String SQLQuery = sqlQuery;

        if (sqlQuery.toLowerCase(Locale.ROOT).replaceAll("\\s", "").startsWith("select*")) {
            SQLQuery = sqlQuery.replaceAll("\\*", String.join(",", dataframe.columns()));
        }
        SqlMeta SQLLineage = OpenLineageSql.parse(Arrays.asList(SQLQuery),
                OpenLineageDialect.SPARKSQL.toString()).get();
        String extractedLineage = SQLLineage.columnLineage().toString();
        List<OpenLineage.InputField> inputFields = new ArrayList<>();
        // OpenLineage.ColumnLineageDatasetFacetBuilder cb = openLineageProducer.newColumnLineageDatasetFacetBuilder();
        // cb.dataset()
        List<Map<String, Object>> list= extractLineage(extractedLineage);
        for (StructField field : schema) {
            list.stream()
                    .filter(map -> field.name().equals(map.get("target_column")))
                    .findFirst()
                    .ifPresent(map -> {
                        System.out.println("Target Column: " + field.name());

                        // Safely extract "source_columns"
                        Object sourceColumnsObj = map.get("source_columns");

                        if (sourceColumnsObj instanceof List) {
                            List<Map<String, String>> sourceColumns = (List<Map<String, String>>) sourceColumnsObj;
                            String Transformation = (sourceColumns.size() >1) ? "INDIRECT" : "DIRECT";
                            OpenLineage. InputFieldTransformations inputFieldTransformations =
                                    openLineageProducer.newInputFieldTransformations(Transformation,
                                            "SUBTYPE" , "DESC", false);

                            // Iterate and print source table and column
                            sourceColumns.forEach(entry -> {
                                String sourceTable = entry.get("source_table");
                                String sourceColumn = entry.get("source_column");
                                System.out.println("Source Table: " + sourceTable + ", Source Column: " + sourceColumn);
                                inputFields.add(new OpenLineage.InputFieldBuilder()
                                        .namespace(sourceTable + "_namespace")
                                        .name(sourceTable)
                                        .field(sourceColumn)
                                        .transformations(Collections.singletonList(inputFieldTransformations)).build());

                            });
                                                          OpenLineage.ColumnLineageDatasetFacetFieldsAdditional columnLineageDatasetFacetFieldsAdditional
                                        = openLineageProducer.newColumnLineageDatasetFacetFieldsAdditionalBuilder()
                                        .inputFields(inputFields)
                                        .build();
                                OpenLineage.ColumnLineageDatasetFacetFields a = openLineageProducer.newColumnLineageDatasetFacetFieldsBuilder()
                                        .put(field.name(), columnLineageDatasetFacetFieldsAdditional).build();
                                OpenLineage.ColumnLineageDatasetFacet cf =
                                        openLineageProducer.newColumnLineageDatasetFacet(a, inputFields);
                                String json = SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(cf));
                                System.out.print(json);
                        }

                        else {
                            System.err.println("Error: source_columns is not a List<Map<String, String>>. Actual type: " + sourceColumnsObj.getClass());
                        }
                    });
        }




/*

      for (StructField field : schema) {

            ColMapping.values().stream()
                    .filter(MapField -> MapField.targetColumn().replaceAll("\"", "").equalsIgnoreCase(field.name()))
                    .forEach(MapField -> {
                        if (MapField.targetColumn().replaceAll("\"", "").equalsIgnoreCase(field.name())) {
                            inputFields.add(new OpenLineage.InputFieldBuilder().namespace("namespace")
                                                    .name(MapField.sourceTable()).field(MapField.sourceColumn()).build());
                        }

                        OpenLineage.ColumnLineageDatasetFacetFieldsAdditional b = openLineageProducer.newColumnLineageDatasetFacetFieldsAdditionalBuilder()
                                .inputFields(inputFields)
                                .transformationType("STP")
                                .transformationDescription("No Transformation Description")
                                .build();

                        DL.put(field.name(), b);
                    });

        }
          OpenLineage.ColumnLineageDatasetFacetFieldsBuilder a = openLineageProducer.newColumnLineageDatasetFacetFieldsBuilder();
      System.out.print(DL);
        System.out.print(DL);
       DL.forEach((DLA) -> {
            a.put(DLA., DLA.getValue()).build();
        });

        ColumnLineageDatasetFacet cf = openLineageProducer.newColumnLineageDatasetFacet(a.build());
        return cf;*/
    return null;
    }
    public static OpenLineage.ColumnLineageDatasetFacet get(String sqlQuery, Dataset<Row> dataframe)
            throws Exception {
        String PRODUCER_NAME = "https://github.com/OpenLineage/OpenLineage/tree/1.16.0/integration/spark";
        URI producer = URI.create(PRODUCER_NAME);
        OpenLineage openLineageProducer = new OpenLineage(producer);

        Map<String, OpenLineage.ColumnLineageDatasetFacetFieldsAdditional> fieldAdditionalMap = new HashMap<>();
        List<StructField> schema = Arrays.asList(dataframe.schema().fields());

        // Adjust SQL query if it starts with "SELECT *"
        if (sqlQuery.toLowerCase(Locale.ROOT).replaceAll("\\s", "").startsWith("select*")) {
            sqlQuery = sqlQuery.replaceAll("\\*", String.join(",", dataframe.columns()));
        }

        // Extract lineage using OpenLineageSql
        SqlMeta sqlLineage = OpenLineageSql.parse(
                Arrays.asList(sqlQuery), OpenLineageDialect.SPARKSQL.toString()).get();
        String extractedLineage = sqlLineage.columnLineage().toString();
        List<Map<String, Object>> lineageList = extractLineage(extractedLineage);

        // Iterate over schema fields to construct lineage facets
        for (StructField field : schema) {
            Optional<Map<String, Object>> matchingLineage = lineageList.stream()
                    .filter(map -> field.name().equals(map.get("target_column")))
                    .findFirst();

            if (matchingLineage.isPresent()) {
                List<OpenLineage.InputField> inputFields = new ArrayList<>();
                Object sourceColumnsObj = matchingLineage.get().get("source_columns");

                if (sourceColumnsObj instanceof List) {
                    List<Map<String, String>> sourceColumns = (List<Map<String, String>>) sourceColumnsObj;
                    String transformationType = (sourceColumns.size() > 1) ? "INDIRECT" : "DIRECT";

                    OpenLineage.InputFieldTransformations inputFieldTransformations =
                            openLineageProducer.newInputFieldTransformations(transformationType,
                                    "SUBTYPE", "DESC", false);

                    // Iterate over source columns and create input fields
                    for (Map<String, String> entry : sourceColumns) {
                        String sourceTable = entry.get("source_table");
                        String sourceColumn = entry.get("source_column");

                        inputFields.add(new OpenLineage.InputFieldBuilder()
                                .namespace(sourceTable + "_namespace")
                                .name(sourceTable)
                                .field(sourceColumn)
                                .transformations(Collections.singletonList(inputFieldTransformations))
                                .build());
                    }

                    // Create additional field information for lineage
                    OpenLineage.ColumnLineageDatasetFacetFieldsAdditional fieldAdditional =
                            openLineageProducer.newColumnLineageDatasetFacetFieldsAdditionalBuilder()
                                    .inputFields(inputFields)
                                    .transformationType(transformationType)
                                    .transformationDescription("Derived from SQL query")
                                    .build();

                    fieldAdditionalMap.put(field.name(), fieldAdditional);
                }
            }
        }

        OpenLineage.ColumnLineageDatasetFacetFieldsBuilder columnLineageFieldsBuilder =
                openLineageProducer.newColumnLineageDatasetFacetFieldsBuilder();

        fieldAdditionalMap.forEach(columnLineageFieldsBuilder::put); // ✅ Correctly adds each entry

        OpenLineage.ColumnLineageDatasetFacetFields columnLineageFields = columnLineageFieldsBuilder.build();
        //String json = SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(openLineageProducer.newColumnLineageDatasetFacet(columnLineageFields, new ArrayList<>())));
        //System.out.print(json);
        // Create and return the ColumnLineageDatasetFacet
        return openLineageProducer.newColumnLineageDatasetFacet(columnLineageFields, new ArrayList<>());
    }

}
