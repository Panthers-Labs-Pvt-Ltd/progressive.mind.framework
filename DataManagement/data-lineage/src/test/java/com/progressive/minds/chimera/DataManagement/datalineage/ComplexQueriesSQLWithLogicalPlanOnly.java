package com.progressive.minds.chimera.DataManagement.datalineage;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;

import java.util.Arrays;
import java.util.List;

public class ComplexQueriesSQLWithLogicalPlanOnly {

    public static void main(String[] args) {
        // Initialize Spark session
        SparkSession spark = SparkSession.builder()
                .appName("ComplexQueriesSQLWithLogicalPlan")
                .master("local[*]")
                .getOrCreate();

        // Create 5 DataFrames with sample data

        // DataFrame 1: Employees
        List<Row> employeesData = Arrays.asList(
                RowFactory.create(1, "Alice", 30, "HR"),
                RowFactory.create(2, "Bob", 35, "Engineering"),
                RowFactory.create(3, "Charlie", 40, "Finance"),
                RowFactory.create(4, "David", 25, "Engineering"),
                RowFactory.create(5, "Eve", 28, "HR")
        );
        StructType employeesSchema = new StructType(new StructField[]{
                DataTypes.createStructField("id", DataTypes.IntegerType, false),
                DataTypes.createStructField("name", DataTypes.StringType, false),
                DataTypes.createStructField("age", DataTypes.IntegerType, false),
                DataTypes.createStructField("department", DataTypes.StringType, false)
        });
        Dataset<Row> employeesDF = spark.createDataFrame(employeesData, employeesSchema);
        employeesDF.createOrReplaceTempView("employees");

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

        // Stop Spark session
        spark.stop();
    }

    private static void analyzeLogicalPlan(Dataset<Row> df, String queryName) {
        System.out.println("Analyzing Logical Plan for: " + queryName);
        LogicalPlan logicalPlan = df.queryExecution().logical();
        System.out.println(logicalPlan);
        System.out.println("----------------------------------------");
    }
}