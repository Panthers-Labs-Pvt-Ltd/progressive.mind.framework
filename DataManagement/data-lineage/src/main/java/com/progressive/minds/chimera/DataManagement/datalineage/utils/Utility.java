package com.progressive.minds.chimera.DataManagement.datalineage.utils;


import org.apache.spark.sql.SparkSession;
// import org.apache.spark.sql.catalyst.parser.ParseException;
import java.text.ParseException;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
public class Utility {

    /**
     * Extracts all table names from the given SQL query using SparkSession.
     *
     * @param spark    the active SparkSession
     * @param sqlQuery the SQL query text
     * @return a set of table names
     */
   public static List<String> getTableNamesFromSQL(SparkSession spark, String sqlQuery) {
            Set<String> tableNames = new HashSet<>();
        LogicalPlan logicalPlan;
        try {
                // Parse the query to get the logical plan
                logicalPlan = spark.sessionState().sqlParser().parsePlan(sqlQuery);
                }
                 catch (Exception e) {
                    throw new RuntimeException("Failed to extract table names from query: " + sqlQuery, e);
                }
                logicalPlan.foreach(node -> {
                    if (node instanceof UnresolvedRelation) {
                        UnresolvedRelation relation = (UnresolvedRelation) node;
                        // Convert Scala collection to Java-compatible string
                        String tableName = relation.multipartIdentifier().mkString(".");
                        tableNames.add(tableName);
                    }
                    return null;
                });


            return new ArrayList<>(tableNames);
        }

    // Generic NVL method to handle null values
    public static <T> T nvl(T value, T defaultValue) {
        return (value != null) ? value : defaultValue;
    }


    public static StructType getDataFrameSchema(SparkSession sparkSession, String dataframeName) {
        try {
            // Check if the DataFrame exists using Spark catalog
            if (sparkSession.catalog().listTables().filter("name = '" + dataframeName + "'").count() > 0) {
                Dataset<Row> dataset = sparkSession.table(dataframeName);
                return dataset.schema();
            } else {
                System.out.println("DataFrame not found: Returning empty schema.");
                return new StructType(); // Return an empty schema if not found
            }
        } catch (Exception e) {
            // Catch any exceptions and return empty schema
            System.out.println("Exception occurred: " + e.getMessage());
            return new StructType();
        }
    }

    }


