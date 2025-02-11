package com.progressive.minds.chimera.DataManagement.datalineage;

import com.progressive.minds.chimera.core.dataSource.sourceTypes.FileReader;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.NamedExpression;
import org.apache.spark.sql.catalyst.plans.logical.*;
import org.apache.spark.sql.catalyst.expressions.Attribute;
import org.apache.spark.sql.catalyst.plans.logical.Project;
import org.apache.spark.sql.catalyst.plans.logical.Join;
import org.apache.spark.sql.catalyst.plans.logical.Filter;
import org.apache.spark.sql.catalyst.plans.logical.Aggregate;
import org.apache.spark.sql.catalyst.plans.logical.Window;
import org.apache.spark.sql.catalyst.plans.logical.Sort;
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation;
import org.apache.spark.sql.catalyst.expressions.WindowSpecDefinition;
import scala.collection.JavaConverters;

import java.util.ArrayList;
import java.util.List;

public class SparkLogicalPlanExtractor {

    public static void main(String[] args) throws AnalysisException {
        SparkSession spark = SparkSession.builder()
                .appName("LogicalPlanExtractor")
                .master("local[*]")
                .config("spark.sql.legacy.allowUntypedScalaUDF", true)
                .config("spark.executor.heartbeatInterval", "20m")
                .config("spark.network.timeout", "30m")
                .getOrCreate();

        String Folder = System.getProperty("user.home") + "/chimera/core/dataSource/src/test/resources/flight_parquet";

        Dataset<Row> flight_data = spark.emptyDataFrame();
        flight_data = new FileReader().read("parquet", spark, "ParquetReaderTest",
                Folder, "", "",
                null, "", "",
                "", 0);
        flight_data.show(10, false);
        // dataFrame.printSchema();
        String SQL = "WITH RankedFlights AS (\n" +
                "  SELECT\n" +
                "    Year ||Month||DayofMonth AS FULLDT,\n" +
                "    Year,\n" +
                "    Month,\n" +
                "    DayofMonth,\n" +
                "    FlightDate,\n" +
                "    Reporting_Airline,\n" +
                "    Flight_Number_Reporting_Airline,\n" +
                "    OriginCityName,\n" +
                "    DestCityName,\n" +
                "    Distance,\n" +
                "    ArrDelay,\n" +
                "    DepDelay,\n" +
                "    ROW_NUMBER() OVER (PARTITION BY Reporting_Airline ORDER BY ArrDelay DESC) AS rank\n" +
                "  FROM flight_data\n" +
                "  WHERE Cancelled = '0' AND Diverted = '0' AND Distance IS NOT NULL\n" +
                "),\n" +
                "AggregatedPerformance AS (\n" +
                "  SELECT\n" +
                "    Year,\n" +
                "    Month,\n" +
                "    Reporting_Airline,\n" +
                "    COUNT(Flight_Number_Reporting_Airline) AS total_flights,\n" +
                "    SUM(CASE WHEN DepDelay > 0 THEN 1 ELSE 0 END) AS delayed_departures,\n" +
                "    SUM(CASE WHEN ArrDelay > 0 THEN 1 ELSE 0 END) AS delayed_arrivals,\n" +
                "    AVG(Distance) AS avg_flight_distance,\n" +
                "    AVG(CAST(ArrDelay AS FLOAT)) AS avg_arrival_delay\n" +
                "  FROM flight_data\n" +
                "  WHERE Cancelled = '0'\n" +
                "  GROUP BY Year, Month, Reporting_Airline\n" +
                "),\n" +
                "CancellationInsights AS (\n" +
                "  SELECT\n" +
                "    Year,\n" +
                "    Month,\n" +
                "    Reporting_Airline,\n" +
                "    COUNT(*) AS cancellations,\n" +
                "    COUNT(DISTINCT Flight_Number_Reporting_Airline) AS unique_cancelled_flights,\n" +
                "    CancellationCode\n" +
                "  FROM flight_data\n" +
                "  WHERE Cancelled = '1'\n" +
                "  GROUP BY Year, Month, Reporting_Airline, CancellationCode\n" +
                ")\n" +
                "SELECT\n" +
                "  rf.FULLDT,\n" +
                "  rf.Year,\n" +
                "  rf.Month,\n" +
                "  rf.FlightDate,\n" +
                "  rf.Reporting_Airline,\n" +
                "  rf.OriginCityName AS origin_city,\n" +
                "  rf.DestCityName AS destination_city,\n" +
                "  rf.Distance,\n" +
                "  ap.total_flights,\n" +
                "  ap.delayed_departures,\n" +
                "  ap.delayed_arrivals,\n" +
                "  ap.avg_flight_distance,\n" +
                "  ap.avg_arrival_delay,\n" +
                "  ci.cancellations,\n" +
                "  ci.unique_cancelled_flights,\n" +
                "  ci.CancellationCode\n" +
                "FROM RankedFlights rf\n" +
                "JOIN AggregatedPerformance ap \n" +
                "  ON rf.Year = ap.Year AND rf.Month = ap.Month AND rf.Reporting_Airline = ap.Reporting_Airline\n" +
                "LEFT JOIN CancellationInsights ci \n" +
                "  ON rf.Year = ci.Year AND rf.Month = ci.Month AND rf.Reporting_Airline = ci.Reporting_Airline\n" +
                "WHERE rf.rank = 1\n" +
                "ORDER BY rf.Year, rf.Month, rf.Reporting_Airline";

        flight_data.createOrReplaceTempView("flight_data");
        Dataset<Row> result = spark.sql(SQL);

        result.createOrReplaceTempView("flight_data");

        // Extract the logical plan
        LogicalPlan logicalPlan = result.queryExecution().logical();

        // Extract logical plan details
        List<String> logicalPlanDetails = extractLogicalPlanDetails(logicalPlan);

        // Print the extracted details
        for (String detail : logicalPlanDetails) {
            System.out.println(detail);
        }
    }

    private static List<String> extractLogicalPlanDetails(LogicalPlan logicalPlan) {
        List<String> planDetails = new ArrayList<>();

        if (logicalPlan instanceof Project) {
            Project project = (Project) logicalPlan;
            planDetails.add("Transformation: Project");
            planDetails.add("Target Attributes: " + getTargetAttributes(project));
            planDetails.add("Source Attributes: " + getSourceAttributes(project));
            planDetails.add("Source Tables: " + getSourceTables(project));
        } else if (logicalPlan instanceof Join) {
            Join join = (Join) logicalPlan;
            planDetails.add("Transformation: Join");
            planDetails.add("Join Condition: " + join.condition());
            planDetails.add("Left Source: " + getSourceTables(join.left()));
            planDetails.add("Right Source: " + getSourceTables(join.right()));
        } else if (logicalPlan instanceof Filter) {
            Filter filter = (Filter) logicalPlan;
            planDetails.add("Transformation: Filter");
            planDetails.add("Condition: " + filter.condition());
            planDetails.add("Source Tables: " + getSourceTables(filter));
        } else if (logicalPlan instanceof Aggregate) {
            Aggregate aggregate = (Aggregate) logicalPlan;
            planDetails.add("Transformation: Aggregate");
            planDetails.add("Group By: " + aggregate.groupingExpressions());
            planDetails.add("Aggregations: " + aggregate.aggregateExpressions());
            planDetails.add("Source Tables: " + getSourceTables(aggregate));
        } else if (logicalPlan instanceof Window) {
            Window window = (Window) logicalPlan;
            planDetails.add("Transformation: Window");
            // WindowSpecDefinition is used to access window function details
            //WindowSpecDefinition windowSpec = window.windowSpec();
            planDetails.add("Window Partitioning: " + window.partitionSpec());
            planDetails.add("Window Ordering: " + window.orderSpec());
            planDetails.add("Source Tables: " + getSourceTables(window));
        } else if (logicalPlan instanceof Sort) {
            Sort sort = (Sort) logicalPlan;
            planDetails.add("Transformation: Sort");
            planDetails.add("Sort Order: " + sort.order());
            planDetails.add("Source Tables: " + getSourceTables(sort));
        } else if (logicalPlan instanceof UnresolvedRelation) {
            UnresolvedRelation unresolvedRelation = (UnresolvedRelation) logicalPlan;
            planDetails.add("Source Table: " + unresolvedRelation.tableName());
        }

        // Recursively handle child nodes using Java-style iteration
        for (LogicalPlan child : JavaConverters.seqAsJavaList(logicalPlan.children())) {
            planDetails.addAll(extractLogicalPlanDetails(child));
        }

        return planDetails;
    }

    private static String getSourceTables(LogicalPlan plan) {
        if (plan instanceof UnresolvedRelation) {
            UnresolvedRelation unresolvedRelation = (UnresolvedRelation) plan;
            return unresolvedRelation.tableName();
        } else if (plan instanceof Project || plan instanceof Filter || plan instanceof Aggregate ||
                plan instanceof Join || plan instanceof Window || plan instanceof Sort) {
            // If not a source table, get source tables from child plans
            List<String> sourceTables = new ArrayList<>();
            for (LogicalPlan child : JavaConverters.seqAsJavaList(plan.children())) {
                sourceTables.add(getSourceTables(child));
            }
            return String.join(", ", sourceTables);
        }
        return "Unknown";
    }

    private static String getTargetAttributes(Project project) {
        List<String> targetAttributes = new ArrayList<>();
        for (NamedExpression expr : JavaConverters.seqAsJavaList(project.projectList())) {
            targetAttributes.add(expr.toString());
        }
        return String.join(", ", targetAttributes);
    }

    private static String getSourceAttributes(Project project) {
        List<String> sourceAttributes = new ArrayList<>();
        for (NamedExpression expr : JavaConverters.seqAsJavaList(project.projectList())) {
            if (expr instanceof Attribute) {
                sourceAttributes.add(((Attribute) expr).name());
            }
        }
        return String.join(", ", sourceAttributes);
    }
}
