package com.progressive.minds.chimera.DataManagement.datalineage.utils;

import org.apache.spark.sql.catalyst.expressions.*;
import org.apache.spark.sql.catalyst.expressions.Stack;
import org.apache.spark.sql.catalyst.plans.logical.*;
import org.apache.spark.sql.sources.BaseRelation;
import org.apache.spark.sql.execution.datasources.v2.DataSourceV2Relation;
import scala.collection.JavaConverters;
import scala.collection.Seq;
import scala.collection.mutable.ArrayBuffer;
import java.util.List;

import org.apache.spark.sql.catalyst.expressions.*;
import scala.collection.Seq;
import scala.collection.JavaConverters;
import org.apache.spark.sql.catalyst.plans.logical.Sort;
import java.util.List;
import org.apache.spark.sql.sources.BaseRelation;
import org.apache.spark.sql.execution.datasources.v2.DataSourceV2Relation;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;
import org.apache.spark.sql.catalyst.expressions.Expression;
import org.apache.spark.sql.catalyst.plans.logical.*;
import org.apache.spark.sql.execution.datasources.LogicalRelation;
import scala.collection.JavaConverters;
import com.progressive.minds.chimera.core.dataSource.sourceTypes.FileReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation;
import org.apache.spark.sql.catalyst.expressions.Expression;

import java.util.*;

import static com.progressive.minds.chimera.DataManagement.datalineage.utils.ColumnLineageUtils.processExpression;
import static net.sf.jsqlparser.util.validation.metadata.NamedObject.alias;

public class FieldLineageAnalyzer {

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
       /* Map<String, FieldLineage> fieldLineageMap = extractFieldLineage(logicalPlan);

        // Print the field lineage details
        System.out.println("Field Lineage Details:");
        for (Map.Entry<String, FieldLineage> entry : fieldLineageMap.entrySet()) {
            System.out.println("Field: " + entry.getKey());
            System.out.println("Lineage: " + entry.getValue().getLineage());
            System.out.println("-----------------------------");
        }
*/
        // Stop Spark session
        spark.stop();
    }

    /*private static void parseLogicalPlan(LogicalPlan logicalPlan) {
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
        }
        if (logicalPlan instanceof Sort) {
            Sort sortPlan = (Sort) logicalPlan;
            Seq<SortOrder> scalaSortOrder = sortPlan.order();
            List<SortOrder> javaSortOrder = JavaConverters.seqAsJavaList(scalaSortOrder);
            for (Expression expr : javaSortOrder) {
                String orderStr = processExpression(expr);
                System.out.println("Sort order: " + orderStr);
            }*//*
            List<Expression> sortOrder = (List<Expression>) sortPlan.order();

            for (Expression expr : sortOrder) {
                String orderStr = ColumnLineageUtils.processSortOrder(expr);
                System.out.println("Sort order: " + orderStr);
            }*//*
            LogicalPlan child = sortPlan.child();
            parseLogicalPlan(child);
        } else if (logicalPlan instanceof Union) {
            Union unionPlan = (Union) logicalPlan;
            List<LogicalPlan> children = (List<LogicalPlan>) unionPlan.children();
            for (LogicalPlan child : children) {
                parseLogicalPlan(child);
            }
        } else if (logicalPlan instanceof View) {
                View viewPlan = (View) logicalPlan;
                LogicalPlan child = viewPlan.child();
                parseLogicalPlan(child);
                System.out.println("Handled View logical plan with alias: ");
    }
    else if (logicalPlan instanceof Except) {
                Except exceptPlan = (Except) logicalPlan;

                // Retrieve the left and right logical plans
                LogicalPlan left = exceptPlan.left();
                LogicalPlan right = exceptPlan.right();

                // Process the child logical plans
                parseLogicalPlan(left);
                parseLogicalPlan(right);
            }
             else if (logicalPlan instanceof Intersect) {
            // Handle Intersect
            Intersect intersectPlan = (Intersect) logicalPlan;

            // Retrieve the left and right logical plans
            LogicalPlan left = intersectPlan.left();
            LogicalPlan right = intersectPlan.right();

            // Process the child logical plans
            parseLogicalPlan(left);
            parseLogicalPlan(right);

            System.out.println("Handled Intersect logical plan.");
        } else if (logicalPlan instanceof Generate) {
            // Handle Generate
            Generate generatePlan = (Generate) logicalPlan;
            // Retrieve the generator
            Generator generator = generatePlan.generator();


            // Process the generator expression
            String generatorStr = ColumnLineageUtils.processGenerator(generator);

            System.out.println("Handled Generate logical plan with generator: " + generatorStr);

            // Process the child logical plan
            LogicalPlan child = generatePlan.child();
            parseLogicalPlan(child);
        } else if (logicalPlan instanceof Distinct) {
            // Handle Distinct
            Distinct distinctPlan = (Distinct) logicalPlan;

            // Process the child logical plan (the dataset with duplicates)
            LogicalPlan child = distinctPlan.child();
            parseLogicalPlan(child);

            System.out.println("Handled Distinct logical plan.");
        } else if (logicalPlan instanceof Repartition) {
            // Handle Repartition
            Repartition repartitionPlan = (Repartition) logicalPlan;

            // Retrieve the number of partitions
            int numPartitions = repartitionPlan.numPartitions();

            // Process the child logical plan (the dataset being repartitioned)
            LogicalPlan child = repartitionPlan.child();
            parseLogicalPlan(child);

            System.out.println("Handled Repartition logical plan with " + numPartitions + " partitions.");
        }
        else if  (logicalPlan instanceof LogicalRelation){
            LogicalRelation logicalRelation = (LogicalRelation) logicalPlan;

            // Retrieve the relation (this can be a BaseRelation, DataSourceV2Relation, etc.)
            BaseRelation  relation = logicalRelation.relation();

            // Process the relation based on its type
            String relationStr = processRelation(relation);
            System.out.println("Handled LogicalRelation logical plan with relation: " + relationStr);
        }
        else {
            

            System.out.println("Other Logical Plan Type: " + logicalPlan.getClass().getSimpleName());
        }
    }
    public static String processRelation(Object relation) {
        if (relation instanceof BaseRelation) {
            BaseRelation baseRelation = (BaseRelation) relation;
            return "BaseRelation(" + baseRelation.toString() + ")";
        } else if (relation instanceof DataSourceV2Relation) {
            DataSourceV2Relation dataSourceV2Relation = (DataSourceV2Relation) relation;
            return "DataSourceV2Relation(" + dataSourceV2Relation.toString() + ")";
        } else {
            // Default case for other types of relations
            return relation.toString();
        }
    }
*/
    public static void parseLogicalPlan(LogicalPlan logicalPlan) {
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
        } else if (logicalPlan instanceof Sort) {
            Sort sortPlan = (Sort) logicalPlan;
            Seq<SortOrder> scalaSortOrder = sortPlan.order();
            List<SortOrder> javaSortOrder = JavaConverters.seqAsJavaList(scalaSortOrder);
            for (SortOrder expr : javaSortOrder) {
                String orderStr = processExpression(expr);
                System.out.println("Sort order: " + orderStr);
            }
            LogicalPlan child = sortPlan.child();
            parseLogicalPlan(child);
        } else if (logicalPlan instanceof Union) {
            Union unionPlan = (Union) logicalPlan;
            List<LogicalPlan> children = JavaConverters.seqAsJavaList(unionPlan.children());
            for (LogicalPlan child : children) {
                parseLogicalPlan(child);
            }
        } else if (logicalPlan instanceof View) {
            View viewPlan = (View) logicalPlan;
            LogicalPlan child = viewPlan.child();
            parseLogicalPlan(child);
            System.out.println("Handled View logical plan with alias: " + viewPlan.child());
        } else if (logicalPlan instanceof Except) {
            Except exceptPlan = (Except) logicalPlan;
            LogicalPlan left = exceptPlan.left();
            LogicalPlan right = exceptPlan.right();
            parseLogicalPlan(left);
            parseLogicalPlan(right);
        } else if (logicalPlan instanceof Intersect) {
            Intersect intersectPlan = (Intersect) logicalPlan;
            LogicalPlan left = intersectPlan.left();
            LogicalPlan right = intersectPlan.right();
            parseLogicalPlan(left);
            parseLogicalPlan(right);
            System.out.println("Handled Intersect logical plan.");
        } else if (logicalPlan instanceof Generate) {
            Generate generatePlan = (Generate) logicalPlan;
            Generator generator = generatePlan.generator();
            String generatorStr = processGenerator(generator);
            System.out.println("Handled Generate logical plan with generator: " + generatorStr);
            LogicalPlan child = generatePlan.child();
            parseLogicalPlan(child);
        } else if (logicalPlan instanceof Distinct) {
            Distinct distinctPlan = (Distinct) logicalPlan;
            LogicalPlan child = distinctPlan.child();
            parseLogicalPlan(child);
            System.out.println("Handled Distinct logical plan.");
        } else if (logicalPlan instanceof Repartition) {
            Repartition repartitionPlan = (Repartition) logicalPlan;
            int numPartitions = repartitionPlan.numPartitions();
            LogicalPlan child = repartitionPlan.child();
            parseLogicalPlan(child);
            System.out.println("Handled Repartition logical plan with " + numPartitions + " partitions.");
        } else if (logicalPlan instanceof LogicalRelation) {
            LogicalRelation logicalRelation = (LogicalRelation) logicalPlan;
            BaseRelation relation = logicalRelation.relation();
            String relationStr = processRelation(relation);
            System.out.println("Handled LogicalRelation logical plan with relation: " + relationStr);
        } else {
            System.out.println("Other Logical Plan Type: " + logicalPlan.getClass().getSimpleName());
        }
    }

    public static String processExpression(Expression expr) {
        if (expr instanceof AttributeReference) {
            AttributeReference attr = (AttributeReference) expr;
            return attr.name();
        } else if (expr instanceof Literal) {
            Literal literal = (Literal) expr;
            return literal.value().toString();
        } else {
            return expr.toString();
        }
    }

    public static String processGenerator(Generator generator) {
        if (generator instanceof Explode) {
            Explode explode = (Explode) generator;
            String child = processExpression(explode.child());
            return "Explode(" + child + ")";
        } else if (generator instanceof JsonTuple) {
            JsonTuple jsonTuple = (JsonTuple) generator;
            // Assuming jsonTuple has a method to retrieve child expressions
            Seq<Expression> children = jsonTuple.children();
            List<Expression> javaChildren = JavaConverters.seqAsJavaList(children);
            StringBuilder sb = new StringBuilder("JsonTuple(");
            for (Expression expr : javaChildren) {
                sb.append(processExpression(expr)).append(", ");
            }
            sb.setLength(sb.length() - 2); // Remove trailing comma and space
            sb.append(")");
            return sb.toString();
        } else if (generator instanceof Stack) {
            Stack stack = (Stack) generator;
            // Assuming stack has a method to retrieve child expressions
            Seq<Expression> children = stack.children();
            List<Expression> javaChildren = JavaConverters.seqAsJavaList(children);
            StringBuilder sb = new StringBuilder("Stack(");
            for (Expression expr : javaChildren) {
                sb.append(processExpression(expr)).append(", ");
            }
            sb.setLength(sb.length() - 2); // Remove trailing comma and space
            sb.append(")");
            return sb.toString();
        } else {
            return generator.toString();
        }
    }

    public static String processRelation(Object relation) {
        if (relation instanceof BaseRelation) {
            BaseRelation baseRelation = (BaseRelation) relation;
            return "BaseRelation(" + baseRelation.toString() + ")";
        } else if (relation instanceof DataSourceV2Relation) {
            DataSourceV2Relation dataSourceV2Relation = (DataSourceV2Relation) relation;
            return "DataSourceV2Relation(" + dataSourceV2Relation.toString() + ")";
        } else {
            return relation.toString();
        }
    }
}
