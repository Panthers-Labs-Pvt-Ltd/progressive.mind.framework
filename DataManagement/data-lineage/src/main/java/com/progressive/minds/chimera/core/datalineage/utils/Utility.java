package com.progressive.minds.chimera.core.datalineage.utils;

import org.apache.spark.sql.SparkSession;
// import org.apache.spark.sql.catalyst.parser.ParseException;
import java.text.ParseException;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

            return (List<String>) tableNames;
        }
    }


