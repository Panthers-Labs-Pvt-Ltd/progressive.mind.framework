
package com.progressive.minds.chimera.DataManagement.datalineage.utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.*;

public class ColumnLineageExtractor {
    public static void main(String[] args) throws Exception {
        // Example JSON String (Replace with actual JSON input)
        String jsonString = "[ {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"CancellationCode\"   },   \"lineage\" : [ {     \"origin\" : \"flight_data\",     \"name\" : \"CancellationCode\"   } ] }, {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"Distance\"   },   \"lineage\" : [ {     \"origin\" : \"flight_data\",     \"name\" : \"Distance\"   } ] }, {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"FULLDT\"   },   \"lineage\" : [ {     \"origin\" : \"flight_data\",     \"name\" : \"DayofMonth\"   }, {     \"origin\" : \"flight_data\",     \"name\" : \"Month\"   }, {     \"origin\" : \"flight_data\",     \"name\" : \"Year\"   } ] }, {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"FlightDate\"   },   \"lineage\" : [ {     \"origin\" : \"flight_data\",     \"name\" : \"FlightDate\"   } ] }, {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"Month\"   },   \"lineage\" : [ {     \"origin\" : \"flight_data\",     \"name\" : \"Month\"   } ] }, {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"Reporting_Airline\"   },   \"lineage\" : [ {     \"origin\" : \"flight_data\",     \"name\" : \"Reporting_Airline\"   } ] }, {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"Year\"   },   \"lineage\" : [ {     \"origin\" : \"flight_data\",     \"name\" : \"Year\"   } ] }, {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"avg_arrival_delay\"   },   \"lineage\" : [ {     \"origin\" : \"flight_data\",     \"name\" : \"ArrDelay\"   } ] }, {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"avg_flight_distance\"   },   \"lineage\" : [ {     \"origin\" : \"flight_data\",     \"name\" : \"Distance\"   } ] }, {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"cancellations\"   },   \"lineage\" : [ {     \"origin\" : \"CancellationInsights\",     \"name\" : \"cancellations\"   } ] }, {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"delayed_arrivals\"   },   \"lineage\" : [ {     \"origin\" : \"flight_data\",     \"name\" : \"ArrDelay\"   } ] }, {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"delayed_departures\"   },   \"lineage\" : [ {     \"origin\" : \"flight_data\",     \"name\" : \"DepDelay\"   } ] }, {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"destination_city\"   },   \"lineage\" : [ {     \"origin\" : \"flight_data\",     \"name\" : \"DestCityName\"   } ] }, {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"origin_city\"   },   \"lineage\" : [ {     \"origin\" : \"flight_data\",     \"name\" : \"OriginCityName\"   } ] }, {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"total_flights\"   },   \"lineage\" : [ {     \"origin\" : \"flight_data\",     \"name\" : \"Flight_Number_Reporting_Airline\"   } ] }, {   \"descendant\" : {     \"origin\" : \"unknown\",     \"name\" : \"unique_cancelled_flights\"   },   \"lineage\" : [ {     \"origin\" : \"flight_data\",     \"name\" : \"Flight_Number_Reporting_Airline\"   } ] } ]";

        // Jackson ObjectMapper for JSON parsing
        ObjectMapper mapper = new ObjectMapper();

        // Parse JSON array
        JsonNode jsonArray = mapper.readTree(jsonString);

        // List to store extracted lineage data
        List<Map<String, Object>> lineageList = new ArrayList<>();

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

        // Print extracted lineage
        lineageList.forEach(System.out::println);
    }
}
