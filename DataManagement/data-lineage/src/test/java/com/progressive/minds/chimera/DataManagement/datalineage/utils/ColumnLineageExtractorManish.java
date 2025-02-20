package com.progressive.minds.chimera.DataManagement.datalineage.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

public class ColumnLineageExtractorManish {
    private final ObjectMapper mapper;

    // Constructor
    public ColumnLineageExtractorManish() {
        this.mapper = new ObjectMapper();
    }

    /**
     * Extracts column lineage from JSON input and maps it to ColumnLineage objects.
     *
     * @param jsonString JSON string representing column lineage
     * @return List of ColumnLineage objects
     * @throws Exception If JSON parsing fails
     */
    public List<ColumnLineage> extractLineage(String jsonString) throws Exception {
        List<ColumnLineage> lineageList = new ArrayList<>();
        JsonNode jsonArray = mapper.readTree(jsonString);

        for (JsonNode element : jsonArray) {
            String targetColumn = element.path("descendant").path("name").asText();
            String targetOrigin = element.path("descendant").path("origin").asText();

            List<SourceColumn> sourceColumns = new ArrayList<>();
            for (JsonNode source : element.path("lineage")) {
                sourceColumns.add(new SourceColumn(
                        source.path("origin").asText(),
                        source.path("name").asText()
                ));
            }

            lineageList.add(new ColumnLineage(targetColumn, targetOrigin, sourceColumns));
        }
        return lineageList;
    }

    // Main method for testing
    public static void main(String[] args) {
        String jsonString = "[ { \"descendant\" : { \"origin\" : \"unknown\", \"name\" : \"CancellationCode\" }, \"lineage\" : [ { \"origin\" : \"flight_data\", \"name\" : \"CancellationCode\" } ] }, { \"descendant\" : { \"origin\" : \"unknown\", \"name\" : \"Distance\" }, \"lineage\" : [ { \"origin\" : \"flight_data\", \"name\" : \"Distance\" } ] } ]";

        ColumnLineageExtractorManish extractor = new ColumnLineageExtractorManish();
        try {
            List<ColumnLineage> lineageData = extractor.extractLineage(jsonString);
            lineageData.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
