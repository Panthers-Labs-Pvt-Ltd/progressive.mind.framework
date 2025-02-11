/*
package com.progressive.minds.chimera.DataManagement.test;

import org.apache.spark.sql.types.DataTypes;
import scala.collection.Seq;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.spark.sql.catalyst.plans.logical.*;
import org.apache.spark.sql.catalyst.expressions.*;
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation;
import org.apache.spark.sql.execution.LogicalRDD;
import org.apache.spark.sql.types.StructType;
import java.util.*;

public class ColumnLineageExtractor {

    public static void main(String[] args) throws JsonProcessingException {
        // Example: Initialize Spark session
        // SparkSession spark = SparkSession.builder().appName("ColumnLineageExtractor").getOrCreate();

        // Assuming `logicalPlan` is your LogicalPlan
        LogicalPlan logicalPlan = getLogicalPlan();

        // Extract column lineage
        Map<String, List<String>> columnLineage = extractColumnLineage(logicalPlan);

        // Generate OpenLineage event
        String lineageJson = generateOpenLineageEvent(columnLineage);
        System.out.println(lineageJson);
    }

    public static LogicalPlan getLogicalPlan() {
        // Replace with actual logic to fetch your logical plan.
        return new LogicalRDD(null, new StructType()) {
            @Override
            public Seq<Attribute> output() {
                // Return output columns here
                return scala.collection.JavaConverters.asScalaBuffer(Arrays.asList(
                        new AttributeReference("id", DataTypes.IntegerType, true,null,null,null) {}
                )).toSeq();
            }
        };
    }

    public static Map<String, List<String>> extractColumnLineage(LogicalPlan logicalPlan) {
        Map<String, List<String>> columnLineageMap = new HashMap<>();

        if (logicalPlan instanceof Project) {
            Project project = (Project) logicalPlan;
            List<String> sourceColumns = getSourceColumns(project.child());
            for (NamedExpression expr : project.projectList()) {
                String targetColumn = expr.name();
                for (String sourceColumn : sourceColumns) {
                    columnLineageMap.putIfAbsent(targetColumn, new ArrayList<>());
                    columnLineageMap.get(targetColumn).add(sourceColumn);
                }
            }
        } else if (logicalPlan instanceof Join) {
            Join join = (Join) logicalPlan;
            List<String> leftSource = getSourceColumns(join.left());
            List<String> rightSource = getSourceColumns(join.right());

            for (String left : leftSource) {
                for (String right : rightSource) {
                    columnLineageMap.putIfAbsent(left, new ArrayList<>());
                    columnLineageMap.putIfAbsent(right, new ArrayList<>());
                    columnLineageMap.get(left).add(right);
                }
            }

            for (LogicalPlan child : join.children()) {
                columnLineageMap.putAll(extractColumnLineage(child));
            }
        }

        return columnLineageMap;
    }

    private static List<String> getSourceColumns(LogicalPlan logicalPlan) {
        List<String> sourceColumns = new ArrayList<>();

        if (logicalPlan instanceof UnresolvedRelation) {
            UnresolvedRelation relation = (UnresolvedRelation) logicalPlan;
            sourceColumns.add(relation.tableName());
        } else if (logicalPlan instanceof Project) {
            Project project = (Project) logicalPlan;
            for (NamedExpression expr : project.projectList()) {
                sourceColumns.add(expr.name());
            }
        }

        return sourceColumns;
    }

    public static String generateOpenLineageEvent(Map<String, List<String>> columnLineage) throws JsonProcessingException {
        // Generate JSON event for OpenLineage
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "START");
        event.put("eventTime", "2025-02-11T12:34:56.000Z");

        Map<String, Object> job = new HashMap<>();
        job.put("namespace", "food_delivery");
        job.put("name", "top_delivery_times_calculation");
        event.put("job", job);

        List<Map<String, String>> inputs = new ArrayList<>();
        Map<String, String> input = new HashMap<>();
        input.put("namespace", "food_delivery");
        input.put("name", "public.delivery_7_days");
        inputs.add(input);
        event.put("inputs", inputs);

        List<Map<String, Object>> outputs = new ArrayList<>();
        Map<String, Object> output = new HashMap<>();
        output.put("namespace", "food_delivery");
        output.put("name", "public.top_delivery_times");

        Map<String, Object> columnLineageFacet = new HashMap<>();
        columnLineageFacet.put("_producer", "https://github.com/MarquezProject/marquez/blob/main/docker/metadata.json");
        columnLineageFacet.put("_schemaURL", "https://openlineage.io/spec/facets/1-0-1/ColumnLineageDatasetFacet.json");

        Map<String, Object> fields = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : columnLineage.entrySet()) {
            String column = entry.getKey();
            List<String> sourceColumns = entry.getValue();

            Map<String, Object> fieldLineage = new HashMap<>();
            List<Map<String, Object>> inputFields = new ArrayList<>();
            for (String sourceColumn : sourceColumns) {
                Map<String, Object> inputField = new HashMap<>();
                inputField.put("namespace", "food_delivery");
                inputField.put("name", "public.delivery_7_days");
                inputField.put("field", sourceColumn);

                List<Map<String, Object>> transformations = new ArrayList<>();
                Map<String, Object> transformation = new HashMap<>();
                transformation.put("type", "DIRECT");
                transformation.put("subtype", "IDENTITY");
                transformations.add(transformation);

                inputField.put("transformations", transformations);
                inputFields.add(inputField);
            }

            fieldLineage.put("inputFields", inputFields);
            fields.put(column, fieldLineage);
        }

        columnLineageFacet.put("fields", fields);
        output.put("facets", columnLineageFacet);
        outputs.add(output);

        event.put("outputs", outputs);

        return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(event);
    }
}
*/
