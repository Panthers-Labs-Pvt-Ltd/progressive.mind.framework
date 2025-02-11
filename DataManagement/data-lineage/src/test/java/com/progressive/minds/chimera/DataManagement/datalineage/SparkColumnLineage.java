package com.progressive.minds.chimera.DataManagement.datalineage;
import java.util.*;

import com.progressive.minds.chimera.core.dataSource.sourceTypes.FileReader;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.plans.logical.*;
import org.apache.spark.sql.catalyst.expressions.Attribute;
import org.apache.spark.sql.catalyst.plans.logical.Project;
import org.apache.spark.sql.catalyst.plans.logical.Filter;
import org.apache.spark.sql.catalyst.plans.logical.Join;
import org.apache.spark.sql.catalyst.plans.logical.Aggregate;
import org.apache.spark.sql.catalyst.plans.logical.Window;
import org.apache.spark.sql.catalyst.plans.logical.Sort;
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation;
import org.apache.spark.sql.catalyst.expressions.NamedExpression;
import scala.collection.JavaConverters;

public class SparkColumnLineage {

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

        // Get the logical plan from the query execution
        LogicalPlan logicalPlan = result.queryExecution().analyzed();
        System.out.println("=======================");
                System.out.println(logicalPlan);
        System.out.println("=======================");
        // Generate column lineage
        Map<String, Set<String>> columnLineage = extractColumnLineage(logicalPlan);

        // Print the column lineage
        for (Map.Entry<String, Set<String>> entry : columnLineage.entrySet()) {
            System.out.println("Column: " + entry.getKey() + " --> Lineage: " + entry.getValue());
        }
    }

    /**
     * Recursively extracts column lineage from the logical plan.
     */
    private static Map<String, Set<String>> extractColumnLineage(LogicalPlan logicalPlan) {
        Map<String, Set<String>> columnLineageMap = new HashMap<>();

        // Process different types of operations in the logical plan
        if (logicalPlan instanceof Project) {
            Project project = (Project) logicalPlan;
            List<String> targetColumns = getTargetColumns(project);
            List<String> sourceColumns = getSourceColumns(project);

            // Track the lineage: source column --> target column
            for (String target : targetColumns) {
                for (String source : sourceColumns) {
                    columnLineageMap.putIfAbsent(target, new HashSet<>());
                    columnLineageMap.get(target).add(source);
                }
            }

        }

        else if (logicalPlan instanceof Join) {
            Join join = (Join) logicalPlan;
            List<String> leftSource = getSourceColumns(join.left());
            List<String> rightSource = getSourceColumns(join.right());

            // Join combines columns from both sides
            for (String left : leftSource) {
                for (String right : rightSource) {
                    columnLineageMap.putIfAbsent(left, new HashSet<>());
                    columnLineageMap.putIfAbsent(right, new HashSet<>());
                    columnLineageMap.get(left).add(right);
                }
            }

            // Recursively process the children of the join

        /*        for (LogicalPlan child : JavaConverters.seqAsJavaList(join.children())) {
                columnLineageMap.putAll(extractColumnLineage(child));
            }*/

        } else if (logicalPlan instanceof Filter) {
            Filter filter = (Filter) logicalPlan;
            List<String> parentColumns = getSourceColumns(filter);
            List<String> filterColumns = getFilterColumns(filter);

            // Filter only references source columns, track lineage accordingly
            for (String filterCol : filterColumns) {
                for (String parentCol : parentColumns) {
                    columnLineageMap.putIfAbsent(filterCol, new HashSet<>());
                    columnLineageMap.get(filterCol).add(parentCol);
                }
            }

        } else if (logicalPlan instanceof Aggregate) {
            Aggregate aggregate = (Aggregate) logicalPlan;
            List<String> groupingColumns = getSourceColumns(aggregate);
            List<String> aggregatedColumns = getAggregationColumns(aggregate);

            // Aggregate uses grouping and aggregation columns, track lineage
            for (String groupBy : groupingColumns) {
                for (String aggregateCol : aggregatedColumns) {
                    columnLineageMap.putIfAbsent(groupBy, new HashSet<>());
                    columnLineageMap.get(groupBy).add(aggregateCol);
                }
            }

        } else if (logicalPlan instanceof Window) {
            Window window = (Window) logicalPlan;
            List<String> windowColumns = getSourceColumns(window);

            // Window function operates on columns, track their lineage
            for (String windowCol : windowColumns) {
                columnLineageMap.putIfAbsent(windowCol, new HashSet<>());
            }

        } else if (logicalPlan instanceof Sort) {
            Sort sort = (Sort) logicalPlan;
            List<String> sortColumns = getSourceColumns(sort);

            // Sort columns are passed through, so track their lineage
            for (String sortCol : sortColumns) {
                columnLineageMap.putIfAbsent(sortCol, new HashSet<>());
            }

        } else if (logicalPlan instanceof UnresolvedRelation) {
            UnresolvedRelation unresolvedRelation = (UnresolvedRelation) logicalPlan;
            // Relation is the source, no parent columns
            columnLineageMap.put(unresolvedRelation.tableName(), new HashSet<>());
        }

        // Recursively handle children
        for (LogicalPlan child : JavaConverters.seqAsJavaList(logicalPlan.children())) {
            columnLineageMap.putAll(extractColumnLineage(child));
        }

        return columnLineageMap;
    }

    /**
     * Extracts the columns used in the target of a Project.
     */
    private static List<String> getTargetColumns(Project project) {
        List<String> targetColumns = new ArrayList<>();
        for (NamedExpression expr : JavaConverters.seqAsJavaList(project.projectList())) {
            targetColumns.add(expr.toString());
        }
        return targetColumns;
    }

    /**
     * Extracts the columns used in the source of a Project.
     */
    private static List<String> getSourceColumns(LogicalPlan plan) {
        List<String> sourceColumns = new ArrayList<>();
        if (plan instanceof Project) {
            Project project = (Project) plan;
            for (NamedExpression expr : JavaConverters.seqAsJavaList(project.projectList())) {
                if (expr instanceof Attribute) {
                    sourceColumns.add(((Attribute) expr).name());
                }
            }
        }
        return sourceColumns;
    }

    /**
     * Extracts the columns used in a Filter condition.
     */
    private static List<String> getFilterColumns(Filter filter) {
        List<String> filterColumns = new ArrayList<>();
        // For simplicity, assuming the filter expression is just a column reference
        filterColumns.add(filter.condition().toString());
        return filterColumns;
    }

    /**
     * Extracts the columns used in aggregation operations.
     */
    private static List<String> getAggregationColumns(Aggregate aggregate) {
        List<String> aggregationColumns = new ArrayList<>();
        for (NamedExpression expr : JavaConverters.seqAsJavaList(aggregate.aggregateExpressions())) {
            aggregationColumns.add(expr.toString());
        }
        return aggregationColumns;
    }
}
