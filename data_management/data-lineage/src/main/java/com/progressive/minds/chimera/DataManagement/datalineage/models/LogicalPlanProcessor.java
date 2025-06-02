package com.progressive.minds.chimera.DataManagement.datalineage.models;


import org.apache.spark.sql.catalyst.expressions.*;
import org.apache.spark.sql.catalyst.plans.logical.*;
import scala.collection.JavaConverters;

import java.util.List;

import org.apache.spark.sql.catalyst.plans.logical.Sort;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;
import org.apache.spark.sql.catalyst.expressions.Expression;
import org.apache.spark.sql.execution.datasources.LogicalRelation;
import com.progressive.minds.chimera.core.data_source.sourceTypes.FileReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.spark.sql.catalyst.expressions.AttributeReference;
import org.apache.spark.sql.execution.CommandExecutionMode;

/*@JsonInclude(JsonInclude.Include.NON_NULL)
class TableInfo {
    public List<String> inTables;
    public List<String> outTables;

    public TableInfo() {
        this.inTables = new ArrayList<>();
        this.outTables = new ArrayList<>();
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class ColumnLineage {
    public Descendant descendant;
    public List<Lineage> lineage;

    public ColumnLineage() {
        this.lineage = new ArrayList<>();
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class Descendant {
    public String origin;
    public String name;
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class Lineage {
    public String origin;
    public String name;
    public String transformationType;
    public String transformationDesc;
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class PlanOutput {
    public TableInfo tableInfo;
    public List<ColumnLineage> columnLineages;

    public PlanOutput() {
        this.tableInfo = new TableInfo();
        this.columnLineages = new ArrayList<>();
    }
}*/

public class LogicalPlanProcessor {

    private static PlanOutput planOutput = new PlanOutput();

    /*public static void parseLogicalPlan(LogicalPlan logicalPlan) {
        if (logicalPlan instanceof Project) {
            Project project = (Project) logicalPlan;
            List<NamedExpression> projectList = JavaConverters.seqAsJavaList(project.projectList());
            for (NamedExpression expr : projectList) {
                ColumnLineage columnLineage = new ColumnLineage();
                Descendant descendant = new Descendant();
                descendant.origin = "unknown";
                descendant.name = expr.name();
                columnLineage.descendant = descendant;
                Lineage lineage = new Lineage();
                lineage.origin = "flight_data";
                lineage.name = expr.name();
                lineage.transformationType = "Project";
                lineage.transformationDesc = "SELECT ATTRIBUTES WITH SUBQUERY ALIAS RankedFlights";
                columnLineage.lineage.add(lineage);
                planOutput.columnLineages.add(columnLineage);
            }
            parseLogicalPlan(project.child());
        } else if (logicalPlan instanceof Aggregate) {
            Aggregate aggregate = (Aggregate) logicalPlan;
            List<NamedExpression> aggregateExpressions = JavaConverters.seqAsJavaList(aggregate.aggregateExpressions());
            for (NamedExpression expr : aggregateExpressions) {
                ColumnLineage columnLineage = new ColumnLineage();
                Descendant descendant = new Descendant();
                descendant.origin = "unknown";
                descendant.name = expr.name();
                columnLineage.descendant = descendant;
                Lineage lineage = new Lineage();
                lineage.origin = "flight_data";
                lineage.name = expr.name();
                lineage.transformationType = "Aggregate";
                lineage.transformationDesc = "AGGREGATE FUNCTION";
                columnLineage.lineage.add(lineage);
                planOutput.columnLineages.add(columnLineage);
            }
            parseLogicalPlan(aggregate.child());
        } else if (logicalPlan instanceof Filter) {
            Filter filter = (Filter) logicalPlan;
            parseLogicalPlan(filter.child());
        } else if (logicalPlan instanceof Join) {
            Join join = (Join) logicalPlan;
            parseLogicalPlan(join.left());
            parseLogicalPlan(join.right());
        } else if (logicalPlan instanceof SubqueryAlias) {
            SubqueryAlias subqueryAlias = (SubqueryAlias) logicalPlan;
            parseLogicalPlan(subqueryAlias.child());
        } else if (logicalPlan instanceof UnresolvedRelation) {
            UnresolvedRelation relation = (UnresolvedRelation) logicalPlan;
        } else if (logicalPlan instanceof WithCTE) {
            WithCTE withCTE = (WithCTE) logicalPlan;
            for (CTERelationDef cte : JavaConverters.seqAsJavaList(withCTE.cteDefs())) {
                parseLogicalPlan(cte.child());
            }
            parseLogicalPlan(withCTE.plan());
        } else if (logicalPlan instanceof Window) {
            Window window = (Window) logicalPlan;
            List<NamedExpression> windowExpressions = JavaConverters.seqAsJavaList(window.windowExpressions());
            for (NamedExpression expr : windowExpressions) {
            }
            parseLogicalPlan(window.child());
        } else if (logicalPlan instanceof Sort) {
            Sort sortPlan = (Sort) logicalPlan;
            List<SortOrder> javaSortOrder = JavaConverters.seqAsJavaList(sortPlan.order());
            for (SortOrder expr : javaSortOrder) {
            }
            parseLogicalPlan(sortPlan.child());
        } else if (logicalPlan instanceof Union) {
            Union unionPlan = (Union) logicalPlan;
            List<LogicalPlan> children = JavaConverters.seqAsJavaList(unionPlan.children());
            for (LogicalPlan child : children) {
                parseLogicalPlan(child);
            }
        } else if (logicalPlan instanceof View) {
            View viewPlan = (View) logicalPlan;
            parseLogicalPlan(viewPlan.child());
        } else if (logicalPlan instanceof Except) {
            Except exceptPlan = (Except) logicalPlan;
            parseLogicalPlan(exceptPlan.left());
            parseLogicalPlan(exceptPlan.right());
        } else if (logicalPlan instanceof Intersect) {
            Intersect intersectPlan = (Intersect) logicalPlan;
            parseLogicalPlan(intersectPlan.left());
            parseLogicalPlan(intersectPlan.right());
        } else if (logicalPlan instanceof Generate) {
            Generate generatePlan = (Generate) logicalPlan;
            parseLogicalPlan(generatePlan.child());
        } else if (logicalPlan instanceof Distinct) {
            Distinct distinctPlan = (Distinct) logicalPlan;
            parseLogicalPlan(distinctPlan.child());
        } else if (logicalPlan instanceof Repartition) {
            Repartition repartitionPlan = (Repartition) logicalPlan;
            parseLogicalPlan(repartitionPlan.child());
        } else if (logicalPlan instanceof LogicalRelation) {
            LogicalRelation logicalRelation = (LogicalRelation) logicalPlan;
        } else {
        }
    }*/
    public static void parseLogicalPlan(LogicalPlan logicalPlan) {
        if (logicalPlan instanceof Project) {
            Project project = (Project) logicalPlan;
            List<NamedExpression> projectList = JavaConverters.seqAsJavaList(project.projectList());
            for (NamedExpression expr : projectList) {
                ColumnLineage columnLineage = new ColumnLineage();
                Descendant descendant = new Descendant();
                descendant.origin = "unknown";
                descendant.name = expr.name();
                columnLineage.descendant = descendant;
                Lineage lineage = new Lineage();
                //lineage.origin = getSourceTable(expr);
                lineage.name = expr.name();
                lineage.transformationType = "Project";
                lineage.transformationDesc = "Projection";
                columnLineage.lineage.add(lineage);
                planOutput.columnLineages.add(columnLineage);
            }
            parseLogicalPlan(project.child());
        } else if (logicalPlan instanceof Aggregate) {
            Aggregate aggregate = (Aggregate) logicalPlan;
            List<NamedExpression> aggregateExpressions = JavaConverters.seqAsJavaList(aggregate.aggregateExpressions());
            for (NamedExpression expr : aggregateExpressions) {
                ColumnLineage columnLineage = new ColumnLineage();
                Descendant descendant = new Descendant();
                descendant.origin = "unknown";
                descendant.name = expr.name();
                columnLineage.descendant = descendant;
                Lineage lineage = new Lineage();
                //lineage.origin = getSourceTable(expr);
                lineage.name = expr.name();
                lineage.transformationType = "Aggregate";
                lineage.transformationDesc = "Aggregation";
                columnLineage.lineage.add(lineage);
                planOutput.columnLineages.add(columnLineage);
            }
            parseLogicalPlan(aggregate.child());
        } else if (logicalPlan instanceof Filter) {
            Filter filter = (Filter) logicalPlan;
            parseLogicalPlan(filter.child());
        } else if (logicalPlan instanceof Join) {
            Join join = (Join) logicalPlan;
            parseLogicalPlan(join.left());
            parseLogicalPlan(join.right());
        } else if (logicalPlan instanceof SubqueryAlias) {
            SubqueryAlias subqueryAlias = (SubqueryAlias) logicalPlan;
            parseLogicalPlan(subqueryAlias.child());
        } else if (logicalPlan instanceof UnresolvedRelation) {
            UnresolvedRelation relation = (UnresolvedRelation) logicalPlan;
        } else if (logicalPlan instanceof WithCTE) {
            WithCTE withCTE = (WithCTE) logicalPlan;
            for (CTERelationDef cte : JavaConverters.seqAsJavaList(withCTE.cteDefs())) {
                parseLogicalPlan(cte.child());
            }
            parseLogicalPlan(withCTE.plan());
        } else if (logicalPlan instanceof Window) {
            Window window = (Window) logicalPlan;
            List<NamedExpression> windowExpressions = JavaConverters.seqAsJavaList(window.windowExpressions());
            for (NamedExpression expr : windowExpressions) {
            }
            parseLogicalPlan(window.child());
        } else if (logicalPlan instanceof Sort) {
            Sort sortPlan = (Sort) logicalPlan;
            List<SortOrder> javaSortOrder = JavaConverters.seqAsJavaList(sortPlan.order());
            for (SortOrder expr : javaSortOrder) {
            }
            parseLogicalPlan(sortPlan.child());
        } else if (logicalPlan instanceof Union) {
            Union unionPlan = (Union) logicalPlan;
            List<LogicalPlan> children = JavaConverters.seqAsJavaList(unionPlan.children());
            for (LogicalPlan child : children) {
                parseLogicalPlan(child);
            }
        } else if (logicalPlan instanceof View) {
            View viewPlan = (View) logicalPlan;
            parseLogicalPlan(viewPlan.child());
        } else if (logicalPlan instanceof Except) {
            Except exceptPlan = (Except) logicalPlan;
            parseLogicalPlan(exceptPlan.left());
            parseLogicalPlan(exceptPlan.right());
        } else if (logicalPlan instanceof Intersect) {
            Intersect intersectPlan = (Intersect) logicalPlan;
            parseLogicalPlan(intersectPlan.left());
            parseLogicalPlan(intersectPlan.right());
        } else if (logicalPlan instanceof Generate) {
            Generate generatePlan = (Generate) logicalPlan;
            parseLogicalPlan(generatePlan.child());
        } else if (logicalPlan instanceof Distinct) {
            Distinct distinctPlan = (Distinct) logicalPlan;
            parseLogicalPlan(distinctPlan.child());
        } else if (logicalPlan instanceof Repartition) {
            Repartition repartitionPlan = (Repartition) logicalPlan;
            parseLogicalPlan(repartitionPlan.child());
        } else if (logicalPlan instanceof LogicalRelation) {
            LogicalRelation logicalRelation = (LogicalRelation) logicalPlan;
        } else {
        }
    }

    private static String getSourceTable(Expression expr) {
        // Placeholder method to determine source table or alias
        // Implement logic to identify source table or alias from the expression
        return "flight_data";
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

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("LogicalPlanProcessor")
                .master("local[*]")
                .config("spark.sql.legacy.allowUntypedScalaUDF", true)
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
        LogicalPlan logicalPlanq = resultDF.queryExecution().analyzed();

        LogicalPlan logicalPlan = spark.sessionState().executePlan(logicalPlanq, CommandExecutionMode.ALL()).optimizedPlan();

        parseLogicalPlan(logicalPlan);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(planOutput);
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        spark.stop();
    }
}
