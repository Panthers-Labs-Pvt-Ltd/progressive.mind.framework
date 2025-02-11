package com.progressive.minds.chimera.DataManagement.datalineage;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.NamedExpression;
import org.apache.spark.sql.catalyst.parser.ParseException;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;
import org.apache.spark.sql.catalyst.plans.logical.Project;

import scala.collection.Seq;
import scala.collection.JavaConverters;

import java.util.List;

public class SeqIterationExample {

    public static void main(String[] args) throws ParseException {
        SparkSession spark = SparkSession.builder()
                .appName("Seq Iteration Example")
                .master("local")
                .getOrCreate();

        String query = "SELECT 'MANISH'||'--'||'KUMAR' name, age";
        LogicalPlan logicalPlan = spark.sessionState().sqlParser().parsePlan(query);

        if (logicalPlan instanceof Project) {
            Project projectNode = (Project) logicalPlan;
            Seq<NamedExpression> projectList = projectNode.projectList();

            // Convert Seq to Java List
            List<NamedExpression> namedExpressions = JavaConverters.seqAsJavaListConverter(projectList).asJava();

            // Iterate over the list
            for (NamedExpression expr : namedExpressions) {
                System.out.println("Expression: " + expr);
            }
        }
    }
}
