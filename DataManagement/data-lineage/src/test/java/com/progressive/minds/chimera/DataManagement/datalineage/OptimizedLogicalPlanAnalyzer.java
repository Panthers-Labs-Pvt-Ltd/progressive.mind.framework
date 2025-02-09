package com.progressive.minds.chimera.DataManagement.datalineage;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;
//import org.apache.spark.sql.catalyst.plans.logical.LogicalRelation;
import org.apache.spark.sql.execution.datasources.LogicalRelation;
import org.apache.spark.sql.catalyst.expressions.Attribute;
import scala.collection.JavaConverters;

import java.util.*;
import java.util.stream.Collectors;

public class OptimizedLogicalPlanAnalyzer {

    public static void main(String[] args) {
        // Initialize Spark session
        SparkSession spark = SparkSession.builder()
                .appName("OptimizedLogicalPlanAnalyzer")
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
                "SELECT department, AVG(age) AS avg_age " +
                        "FROM employees " +
                        "GROUP BY department " +
                        "ORDER BY avg_age DESC"
        );
        analyzeLogicalPlan(resultDF, "DEpartment average");

        // DataFrame 2: Departments
        List<Row> departmentsData = Arrays.asList(
                RowFactory.create("HR", "Human Resources"),
                RowFactory.create("Engineering", "Engineering"),
                RowFactory.create("Finance", "Finance")
        );
        StructType departmentsSchema = new StructType(new StructField[]{
                DataTypes.createStructField("department_code", DataTypes.StringType, false),
                DataTypes.createStructField("department_name", DataTypes.StringType, false)
        });
        Dataset<Row> departmentsDF = spark.createDataFrame(departmentsData, departmentsSchema);
        departmentsDF.createOrReplaceTempView("departments");

        // DataFrame 3: Salaries
        List<Row> salariesData = Arrays.asList(
                RowFactory.create(1, 5000),
                RowFactory.create(2, 6000),
                RowFactory.create(3, 7000),
                RowFactory.create(4, 5500),
                RowFactory.create(5, 4500)
        );
        StructType salariesSchema = new StructType(new StructField[]{
                DataTypes.createStructField("employee_id", DataTypes.IntegerType, false),
                DataTypes.createStructField("salary", DataTypes.IntegerType, false)
        });
        Dataset<Row> salariesDF = spark.createDataFrame(salariesData, salariesSchema);
        salariesDF.createOrReplaceTempView("salaries");

        // DataFrame 4: Projects
        List<Row> projectsData = Arrays.asList(
                RowFactory.create(1, "Project A"),
                RowFactory.create(2, "Project B"),
                RowFactory.create(3, "Project C"),
                RowFactory.create(4, "Project A"),
                RowFactory.create(5, "Project B")
        );
        StructType projectsSchema = new StructType(new StructField[]{
                DataTypes.createStructField("employee_id", DataTypes.IntegerType, false),
                DataTypes.createStructField("project_name", DataTypes.StringType, false)
        });
        Dataset<Row> projectsDF = spark.createDataFrame(projectsData, projectsSchema);
        projectsDF.createOrReplaceTempView("projects");

        // DataFrame 5: Bonuses
        List<Row> bonusesData = Arrays.asList(
                RowFactory.create(1, 1000),
                RowFactory.create(2, 1500),
                RowFactory.create(3, 2000),
                RowFactory.create(4, 1200),
                RowFactory.create(5, 800)
        );
        StructType bonusesSchema = new StructType(new StructField[]{
                DataTypes.createStructField("employee_id", DataTypes.IntegerType, false),
                DataTypes.createStructField("bonus", DataTypes.IntegerType, false)
        });
        Dataset<Row> bonusesDF = spark.createDataFrame(bonusesData, bonusesSchema);
        bonusesDF.createOrReplaceTempView("bonuses");

        // Perform complex and nested queries using SQL

        // Query 1: Join employees with departments and calculate average age by department
        Dataset<Row> avgAgeByDeptDF = spark.sql(
                "SELECT d.department_name, AVG(e.age) AS avg_age " +
                        "FROM employees e " +
                        "JOIN departments d ON e.department = d.department_code " +
                        "GROUP BY d.department_name"
        );
        analyzeLogicalPlan(avgAgeByDeptDF, "Query 1: Average Age by Department");

        // Query 2: Join employees with salaries and calculate total salary by department
        Dataset<Row> totalSalaryByDeptDF = spark.sql(
                "SELECT d.department_name, SUM(s.salary) AS total_salary " +
                        "FROM employees e " +
                        "JOIN salaries s ON e.id = s.employee_id " +
                        "JOIN departments d ON e.department = d.department_code " +
                        "GROUP BY d.department_name"
        );
        analyzeLogicalPlan(totalSalaryByDeptDF, "Query 2: Total Salary by Department");

        // Query 3: Nested query - Employees with salary greater than average salary
        Dataset<Row> highEarnersDF = spark.sql(
                "SELECT e.*, s.salary " +
                        "FROM employees e " +
                        "JOIN salaries s ON e.id = s.employee_id " +
                        "WHERE s.salary > (SELECT AVG(salary) FROM salaries)"
        );
        analyzeLogicalPlan(highEarnersDF, "Query 3: High Earners");

        // Query 4: Join employees with projects and count projects per employee
        Dataset<Row> projectsPerEmployeeDF = spark.sql(
                "SELECT e.id, e.name, COUNT(p.project_name) AS project_count " +
                        "FROM employees e " +
                        "LEFT JOIN projects p ON e.id = p.employee_id " +
                        "GROUP BY e.id, e.name"
        );
        analyzeLogicalPlan(projectsPerEmployeeDF, "Query 4: Projects per Employee");

        // Query 5: Join employees with bonuses and calculate total compensation (salary + bonus)
        Dataset<Row> totalCompensationDF = spark.sql(
                "SELECT e.id, e.name, (s.salary + b.bonus) AS total_compensation " +
                        "FROM employees e " +
                        "JOIN salaries s ON e.id = s.employee_id " +
                        "JOIN bonuses b ON e.id = b.employee_id"
        );
        analyzeLogicalPlan(totalCompensationDF, "Query 5: Total Compensation");

/*
        // Analyze the logical plan
        LogicalPlan logicalPlan = resultDF.queryExecution().logical();
        PlanDetails planDetails = extractPlanDetails(logicalPlan);

        // Print the extracted details
        System.out.println("Input Tables: " + planDetails.getInputTables());
        System.out.println("Transformations: " + planDetails.getTransformations());
        System.out.println("Sorting: " + planDetails.getSorting());
        System.out.println("Input Fields: " + planDetails.getInputFields());
        System.out.println("Output Fields: " + planDetails.getOutputFields());
*/

        // Stop Spark session
        spark.stop();
    }

    private static void analyzeLogicalPlan(Dataset<Row> resultDF, String message)
    {
        System.out.println(message);
        //LogicalPlan logicalPlan = resultDF.queryExecution().logical();
        LogicalPlan logicalPlan = resultDF.queryExecution().analyzed();

        PlanDetails planDetails = extractPlanDetails(logicalPlan);

        // Print the extracted details
        System.out.println("Logical Plan: " + logicalPlan);

        System.out.println("Input Tables: " + planDetails.getInputTables());
        System.out.println("Transformations: " + planDetails.getTransformations());
        System.out.println("Sorting: " + planDetails.getSorting());
        System.out.println("Input Fields: " + planDetails.getInputFields());
        System.out.println("Output Fields: " + planDetails.getOutputFields());
        System.out.println(message + "ends---------------------------------------");
    }
    /**
     * Extracts details from the logical plan.
     */
    private static PlanDetails extractPlanDetails(LogicalPlan plan) {
        PlanDetails planDetails = new PlanDetails();
        traverseLogicalPlan(plan, planDetails);
        return planDetails;
    }

    /**
     * Recursively traverses the logical plan and extracts details.
     */
    private static void traverseLogicalPlan(LogicalPlan plan, PlanDetails planDetails) {
        // Extract input tables
        if (plan instanceof LogicalRelation) {
            LogicalRelation relation = (LogicalRelation) plan;
            String tableName = relation.catalogTable().isDefined() ? relation.catalogTable().get().identifier().table() : "UnknownTable";
            planDetails.addInputTable(tableName);
        }

        // Extract transformation details
        String transformationType = plan.nodeName();
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

        // Add transformation details
        planDetails.addTransformation(
                new TransformationDetails(
                        transformationType,
                        inputFields,
                        outputFields
                )
        );

        // Check for sorting
        if (transformationType.equals("Sort")) {
            planDetails.setSorting(outputFields);
        }

        // Recursively process child plans
        for (LogicalPlan child : JavaConverters.seqAsJavaList(plan.children())) {
            traverseLogicalPlan(child, planDetails);
        }
    }

    /**
     * Class to hold plan details.
     */
    private static class PlanDetails {
        private final Set<String> inputTables = new HashSet<>();
        private final List<TransformationDetails> transformations = new ArrayList<>();
        private List<String> sorting = new ArrayList<>();
        private final Set<String> inputFields = new HashSet<>();
        private final Set<String> outputFields = new HashSet<>();

        public void addInputTable(String tableName) {
            inputTables.add(tableName);
        }

        public void addTransformation(TransformationDetails transformation) {
            transformations.add(transformation);
            inputFields.addAll(transformation.getInputFields());
            outputFields.addAll(transformation.getOutputFields());
        }

        public void setSorting(List<String> sorting) {
            this.sorting = sorting;
        }

        public Set<String> getInputTables() {
            return inputTables;
        }

        public List<TransformationDetails> getTransformations() {
            return transformations;
        }

        public List<String> getSorting() {
            return sorting;
        }

        public Set<String> getInputFields() {
            return inputFields;
        }

        public Set<String> getOutputFields() {
            return outputFields;
        }
    }

    /**
     * Class to hold transformation details.
     */
    private static class TransformationDetails {
        private final String transformationType;
        private final List<String> inputFields;
        private final List<String> outputFields;

        public TransformationDetails(String transformationType, List<String> inputFields, List<String> outputFields) {
            this.transformationType = transformationType;
            this.inputFields = inputFields;
            this.outputFields = outputFields;
        }

        public String getTransformationType() {
            return transformationType;
        }

        public List<String> getInputFields() {
            return inputFields;
        }

        public List<String> getOutputFields() {
            return outputFields;
        }

        @Override
        public String toString() {
            return "Transformation: " + transformationType +
                    ", Input Fields: " + inputFields +
                    ", Output Fields: " + outputFields;
        }
    }
}