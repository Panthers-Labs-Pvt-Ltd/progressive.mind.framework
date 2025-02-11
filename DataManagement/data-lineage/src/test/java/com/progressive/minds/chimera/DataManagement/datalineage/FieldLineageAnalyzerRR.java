/*
package com.progressive.minds.chimera.DataManagement.datalineage;

import org.apache.spark.sql.catalyst.plans.logical.*;
import scala.collection.JavaConverters;
import com.progressive.minds.chimera.core.dataSource.sourceTypes.FileReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation;
import org.apache.spark.sql.catalyst.expressions.Expression;
import org.apache.spark.sql.catalyst.expressions.NamedExpression;
import java.util.*;

public class FieldLineageAnalyzerRR {

    public static void main(String[] args) {
        // Initialize Spark session
        SparkSession spark = SparkSession.builder()
                .appName("FieldLineageAnalyzer")
                .master("local[*]")
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
        Dataset<Row> resultDF = spark.sql(SQL);

        // Analyze the logical plan
        LogicalPlan logicalPlan = resultDF.queryExecution().analyzed();
        parseLogicalPlan(logicalPlan);
       */
/* Map<String, FieldLineage> fieldLineageMap = extractFieldLineage(logicalPlan);

        // Print the field lineage details
        System.out.println("Field Lineage Details:");
        for (Map.Entry<String, FieldLineage> entry : fieldLineageMap.entrySet()) {
            System.out.println("Field: " + entry.getKey());
            System.out.println("Lineage: " + entry.getValue().getLineage());
            System.out.println("-----------------------------");
        }
*//*

        // Stop Spark session
        spark.stop();
    }

    private static void parseLogicalPlan(LogicalPlan logicalPlan) {
        // Check the type of logical plan and extract details accordingly
        if (logicalPlan instanceof Project) {
            Project project = (Project) logicalPlan;
            List<NamedExpression> projectList = JavaConverters.seqAsJavaList(project.projectList());
            System.out.println("Transformation Type: Project");
            for (NamedExpression expr : projectList) {
                System.out.println("Target Column: " + expr.name());
            }
            parseLogicalPlan(project.child());
        } else if (logicalPlan instanceof Aggregate) {
            Aggregate aggregate = (Aggregate) logicalPlan;
            List<NamedExpression> aggregateExpressions = JavaConverters.seqAsJavaList(aggregate.aggregateExpressions());
            System.out.println("Transformation Type: Aggregate");
            for (NamedExpression expr : aggregateExpressions) {
                System.out.println("Target Column: " + expr.name());
            }
            parseLogicalPlan(aggregate.child());
        } else if (logicalPlan instanceof Filter) {
            Filter filter = (Filter) logicalPlan;
            System.out.println("Transformation Type: Filter");
            System.out.println("Condition: " + filter.condition());
            parseLogicalPlan(filter.child());
        } else if (logicalPlan instanceof Join) {
            Join join = (Join) logicalPlan;
            System.out.println("Transformation Type: Join");
            System.out.println("Join Type: " + join.joinType());
            System.out.println("Condition: " + join.condition().get());
            parseLogicalPlan(join.left());
            parseLogicalPlan(join.right());
        } else if (logicalPlan instanceof SubqueryAlias) {
            SubqueryAlias subqueryAlias = (SubqueryAlias) logicalPlan;
            System.out.println("Subquery Alias: " + subqueryAlias.alias());
            parseLogicalPlan(subqueryAlias.child());
        } else if (logicalPlan instanceof UnresolvedRelation) {
            UnresolvedRelation relation = (UnresolvedRelation) logicalPlan;
            System.out.println("Source Table: " + relation.tableName());
        } else if (logicalPlan instanceof WithCTE) {
            WithCTE withCTE = (WithCTE) logicalPlan;
            System.out.println("Transformation Type: WithCTE");
            for (CTERelationDef cte : JavaConverters.seqAsJavaList(withCTE.cteDefs())) {
                System.out.println("CTE Name: " + cte.child());
                parseLogicalPlan(cte.child());
            }
            parseLogicalPlan(withCTE.plan());
        } else if (logicalPlan instanceof Window) {
            Window window = (Window) logicalPlan;
            List<NamedExpression> windowExpressions = JavaConverters.seqAsJavaList(window.windowExpressions());
            System.out.println("Transformation Type: Window");
            for (NamedExpression expr : windowExpressions) {
                System.out.println("Window Expression: " + expr.name());
            }
            parseLogicalPlan(window.child());
        }  if (logicalPlan instanceof Sort) {
            Sort sortPlan = (Sort) logicalPlan;
            List<Expression> sortOrder = (List<Expression>) sortPlan.order();

            for (Expression expr : sortOrder) {
                String orderStr = processSortOrder(expr);
                System.out.println("Sort order: " + orderStr);
            }

            // Process the child logical plan (the dataset being sorted)
            LogicalPlan child = sortPlan.child();
            processLogicalPlan(child);
        }else if (logicalPlan instanceof Union) {
            Union unionPlan = (Union) logicalPlan;
            List<LogicalPlan> children = (List<LogicalPlan>) unionPlan.children();
            for (LogicalPlan child : children) {
                parseLogicalPlan(child);
            }
        } else if (logicalPlan instanceof View) {
            // Handle View
        } else if (logicalPlan instanceof Except) {
            // Handle Except
        } else if (logicalPlan instanceof Intersect) {
            // Handle Intersect
        }  else if (logicalPlan instanceof Generate) {
            // Handle Generate
        } else if (logicalPlan instanceof Distinct) {
            // Handle Distinct
        } else if (logicalPlan instanceof Repartition) {
            // Handle Repartition
        } else {
            // Handle other types of logical plans if needed
            System.out.println("Other Logical Plan Type: " + logicalPlan.getClass().getSimpleName());
        }
    }
}
*/
