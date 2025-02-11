/*
package com.progressive.minds.chimera.DataManagement.datalineage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.Attribute;
import org.apache.spark.sql.catalyst.expressions.Expression;
import org.apache.spark.sql.catalyst.expressions.NamedExpression;
import org.apache.spark.sql.catalyst.plans.logical.*;
import scala.collection.JavaConverters;

import java.util.ArrayList;
import java.util.List;

public class FieldLineageAnalyzer {

    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSession.builder()
                .appName("FieldLineageAnalyzer")
                .master("local[*]")
                .getOrCreate();

        String dataPath = System.getProperty("user.home") + "/chimera/core/dataSource/src/test/resources/flight_parquet";
        Dataset<Row> flightData = spark.read().parquet(dataPath);
        flightData.createOrReplaceTempView("flight_data");

        String sqlQuery = """
                SELECT Year, Month, OriginCityName, DestCityName 
                FROM flight_data 
                WHERE Cancelled = '0' AND Distance IS NOT NULL
                """;
        Dataset<Row> resultDF = spark.sql(sqlQuery);
        LogicalPlan logicalPlan = resultDF.queryExecution().analyzed();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.putArray("inTables").add("flight_data");
        rootNode.putArray("outTables");
        ArrayNode columnLineageNode = rootNode.putArray("columnLineage");

        extractFieldLineage(logicalPlan, columnLineageNode, mapper);

        String jsonOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        System.out.println(jsonOutput);

        spark.stop();
    }

    private static void extractFieldLineage(LogicalPlan plan, ArrayNode columnLineageNode, ObjectMapper mapper) {
        if (plan instanceof Project) {
            Project project = (Project) plan;
            List<NamedExpression> projectList = (List<NamedExpression>) JavaConverters.seqAsJavaList(project.projectList());
            processNamedExpressions(projectList, columnLineageNode, mapper, "Project");
        } else if (plan instanceof Aggregate) {
            Aggregate aggregate = (Aggregate) plan;
            List<NamedExpression> aggregateExpressions = (List<NamedExpression>) JavaConverters.seqAsJavaList(aggregate.aggregateExpressions());
            processNamedExpressions(aggregateExpressions, columnLineageNode, mapper, "Aggregate");
        }

        for (LogicalPlan child : JavaConverters.seqAsJavaList(plan.children())) {
            extractFieldLineage(child, columnLineageNode, mapper);
        }
    }

    private static void processNamedExpressions(List<NamedExpression> namedExpressions, ArrayNode columnLineageNode, ObjectMapper mapper, String transformationType) {
        for (NamedExpression namedExpression : namedExpressions) {
            ObjectNode lineageEntry = mapper.createObjectNode();

            // Descendant information
            ObjectNode descendantNode = lineageEntry.putObject("descendant");
            descendantNode.put("origin", "unknown");
            descendantNode.put("name", namedExpression.name());

            // Lineage transformation information
            ArrayNode lineageNode = lineageEntry.putArray("lineage");

            // Extract input fields from NamedExpression
            List<String> inputFields = extractInputFields(namedExpression);

            for (String inputField : inputFields) {
                String[] parts = inputField.split("\\.");
                ObjectNode fieldNode = mapper.createObjectNode();
                fieldNode.put("origin", parts.length > 1 ? parts[0] : "unknown");
                fieldNode.put("name", parts.length > 1 ? parts[1] : parts[0]);
                fieldNode.put("transformationDescription", "transformation desc");
                fieldNode.put("transformationType", transformationType);
                lineageNode.add(fieldNode);
            }

            lineageEntry.put("transformationDescription", "transformation desc");
            lineageEntry.put("transformationType", transformationType);
            columnLineageNode.add(lineageEntry);
        }
    }

    private static List<String> extractInputFields(Expression expression) {
        List<String> inputFields = new ArrayList<>();
        extractInputFieldsFromExpression(expression, inputFields);
        return inputFields;
    }

    private static void extractInputFieldsFromExpression(Expression expression, List<String> inputFields) {
        if (expression instanceof Attribute) {
            Attribute attribute = (Attribute) expression;
            String tableName = attribute.qualifier().headOption().getOrElse(() -> "unknown");
            String fieldName = attribute.name();
            inputFields.add(tableName + "." + fieldName);
        }

        for (Expression child : JavaConverters.seqAsJavaList(expression.children())) {
            extractInputFieldsFromExpression(child, inputFields);
        }
    }
}
*/
