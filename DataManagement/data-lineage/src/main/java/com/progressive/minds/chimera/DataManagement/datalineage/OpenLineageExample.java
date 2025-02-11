/*
package com.progressive.minds.chimera.DataManagement.datalineage;

import io.openlineage.spark.agent.lifecycle.plan.ColumnLevelLineageVisitor;
import io.openlineage.spark.agent.lifecycle.plan.InputDatasetVisitor;
import io.openlineage.spark.agent.lifecycle.plan.OutputDatasetVisitor;
import io.openlineage.spark.agent.util.ScalaConversionUtils;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;

import java.util.List;

public class OpenLineageExample {

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("OpenLineage Column Level Lineage")
                .master("local")
                .getOrCreate();

        // SQL Query to Parse
        String sql = "SELECT Year || Month || DayofMonth AS FULLDT FROM flight_data";
        LogicalPlan logicalPlan = spark.sessionState().sqlParser().parsePlan(sql);

        // Extract lineage using OpenLineage visitors
        extractLineage(logicalPlan);
    }

    public static void extractLineage(LogicalPlan logicalPlan) {
        // Instantiate OpenLineage Visitors for input/output dataset and column-level lineage
        InputDatasetVisitor inputVisitor = new InputDatasetVisitor();
        OutputDatasetVisitor outputVisitor = new OutputDatasetVisitor();
        ColumnLevelLineageVisitor columnVisitor = new ColumnLevelLineageVisitor();

        // Extract input/output datasets
        List<String> inputDatasets = ScalaConversionUtils.fromSeq(inputVisitor.apply(logicalPlan));
        List<String> outputDatasets = ScalaConversionUtils.fromSeq(outputVisitor.apply(logicalPlan));

        // Extract column-level lineage
        List<String> columnLineage = ScalaConversionUtils.fromSeq(columnVisitor.apply(logicalPlan));

        System.out.println("Input Datasets: " + inputDatasets);
        System.out.println("Output Datasets: " + outputDatasets);
        System.out.println("Column Lineage: " + columnLineage);
    }
}
*/
