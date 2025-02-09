package com.progressive.minds.chimera.DataManagement.datalineage;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;
import org.apache.spark.sql.catalyst.expressions.Attribute;
import scala.collection.JavaConverters;

import java.util.*;
import java.util.stream.Collectors;

public class FieldWiseLogicalPlanAnalyzer {

    public static void main(String[] args) {
        // Initialize Spark session
        SparkSession spark = SparkSession.builder()
                .appName("FieldWiseLogicalPlanAnalyzer")
                .master("local[*]")
                .getOrCreate();

        // Create sample DataFrame
        List<Row> employeesData = Arrays.asList(
                RowFactory.create(1, "Alice", 30, "HR"),
                RowFactory.create(2, "Bob", 35, "Engineering"),
                RowFactory.create(3, "Charlie", 40, "Finance")
        );
        StructType employeesSchema = new StructType(new StructField[]{
                DataTypes.createStructField("id", DataTypes.IntegerType, false),
                DataTypes.createStructField("name", DataTypes.StringType, false),
                DataTypes.createStructField("age", DataTypes.IntegerType, false),
                DataTypes.createStructField("department", DataTypes.StringType, false)
        });
        Dataset<Row> employeesDF = spark.createDataFrame(employeesData, employeesSchema);
        employeesDF.createOrReplaceTempView("employees");

        // Perform a complex query
        Dataset<Row> resultDF = spark.sql(
                "SELECT department, max(name) as maxName,min(id) as firstid, AVG(age) AS avg_age " +
                        "FROM employees " +
                        "GROUP BY department " +
                        "ORDER BY avg_age DESC"
        );

        // Analyze the logical plan
        LogicalPlan logicalPlan = resultDF.queryExecution().analyzed(); // Use analyzed plan
        Map<String, List<String>> fieldTransformations = extractFieldWiseTransformations(logicalPlan);

        // Print the field-wise transformation details
        System.out.println("Field-wise Transformation Details:" + logicalPlan);
        for (Map.Entry<String, List<String>> entry : fieldTransformations.entrySet()) {
            System.out.println("Field: " + entry.getKey());
            System.out.println("Transformations: " + entry.getValue());
            System.out.println("-----------------------------");
        }

        // Stop Spark session
        spark.stop();
    }

    /**
     * Extracts field-wise transformation details from the logical plan.
     */
    private static Map<String, List<String>> extractFieldWiseTransformations(LogicalPlan plan) {
        Map<String, List<String>> fieldTransformations = new HashMap<>();
        traverseLogicalPlan(plan, fieldTransformations);
        return fieldTransformations;
    }

    /**
     * Recursively traverses the logical plan and extracts field-wise transformation details.
     */
    private static void traverseLogicalPlan(LogicalPlan plan, Map<String, List<String>> fieldTransformations) {
        // Extract transformation details
        String transformationType = plan.nodeName();

        // Get input and output fields
        List<String> inputFields = new ArrayList<>();
        List<String> outputFields = new ArrayList<>();

        // Get input fields from child nodes
        for (LogicalPlan child : JavaConverters.seqAsJavaList(plan.children())) {
            inputFields.addAll(
                    JavaConverters.seqAsJavaList(child.output())
                            .stream()
                            .map(Attribute::name)
                            .collect(Collectors.toList())
            );
        }

        // Get output fields
        outputFields.addAll(
                JavaConverters.seqAsJavaList(plan.output())
                        .stream()
                        .map(Attribute::name)
                        .collect(Collectors.toList())
        );

        // Update field-wise transformations
        for (String outputField : outputFields) {
            List<String> transformations = fieldTransformations.getOrDefault(outputField, new ArrayList<>());
            transformations.add(transformationType + " -> Input Fields: " + inputFields);
            fieldTransformations.put(outputField, transformations);
        }

        // Recursively process child plans
        for (LogicalPlan child : JavaConverters.seqAsJavaList(plan.children())) {
            traverseLogicalPlan(child, fieldTransformations);
        }
    }
}