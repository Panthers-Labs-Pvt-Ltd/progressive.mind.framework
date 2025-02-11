package com.progressive.minds.chimera.DataManagement.datalineage;

import com.progressive.minds.chimera.core.dataSource.sourceTypes.FileReader;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;
import org.apache.spark.sql.types.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.progressive.minds.chimera.consumer.DBAPIClient;
import com.progressive.minds.chimera.dto.PipelineMetadata;
import io.openlineage.client.OpenLineage;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.progressive.minds.chimera.DataManagement.datalineage.ChimeraOpenLineage.OpenLineageWrapper;
import static com.progressive.minds.chimera.DataManagement.datalineage.FieldWiseLogicalPlanAnalyzer.extractFieldWiseTransformations;
import static com.progressive.minds.chimera.DataManagement.datalineage.facets.DatasetFacets.getColumnLevelLineage;

class ChimeraOpenLineageTest {
    SparkSession  spark = SparkSession.builder()
            .appName("Shared Spark Session")
            .master("local[*]")
            .getOrCreate();

    @Test
    void openLineageWrapper() throws IOException, InterruptedException {
        OpenLineage.RunEvent.EventType eventType = OpenLineage.RunEvent.EventType.START;

        DBAPIClient dbClient = new DBAPIClient();
        PipelineMetadata inPipelineMetadata = dbClient.get("http://localhost:8080/api/v1/pipelineMetadata/Test_Pipeline",
                new TypeReference<PipelineMetadata>() {});
        Map<String, String> lineageMap = new HashMap<>();
        lineageMap.putIfAbsent("FileName" , "/home/manish/lineage.json");
       OpenLineageWrapper(eventType,inPipelineMetadata,  spark, "file",lineageMap);
    }

    @Test
    void ColumnLineageTest() throws URISyntaxException {
        // Sample data for first DataFrame
        List<Row> data1 = Arrays.asList(
                RowFactory.create(1, "Alice"),
                RowFactory.create(2, "Bob"),
                RowFactory.create(3, "Charlie")
        );

        // Define schema for first DataFrame
        StructType schema1 = new StructType(new StructField[]{
                new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("name", DataTypes.StringType, false, Metadata.empty())
        });

        Dataset<Row> df1 = spark.createDataFrame(data1, schema1);


        // Sample data for second DataFrame
        List<Row> data2 = Arrays.asList(
                RowFactory.create(1, 5000.0),
                RowFactory.create(2, 7000.0),
                RowFactory.create(3, 6000.0)
        );

        // Define schema for second DataFrame
        StructType schema2 = new StructType(new StructField[]{
                new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("salary", DataTypes.DoubleType, false, Metadata.empty())
        });
        Dataset<Row> df2 = spark.createDataFrame(data2, schema2);

        // Register DataFrames as temporary views
        df1.createOrReplaceTempView("employees");
        df2.createOrReplaceTempView("salaries");

        // Execute a SELECT query combining both DataFrames
        String query = "SELECT e.id, e.name, s.salary FROM employees e JOIN salaries s ON e.id = s.id";
        Dataset<Row> result = spark.sql(query);
        result.createOrReplaceTempView("global_people");
        spark.sql("SELECT * from global_people").show();
        // getColumnLevelLineage(query,"global_people",spark);
        OpenLineage openLineageProducer = new OpenLineage(new URI("MANSIH"));
        getColumnLevelLineage(openLineageProducer,query, "global_people","COLnamespace",spark);

    }

    @Test
    void TestLineage() throws URISyntaxException {
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
        Dataset<Row> FRD = spark.sql(SQL);
        FRD.count();
        OpenLineage openLineageProducer = new OpenLineage(new URI("MANSIH"));
        getColumnLevelLineage(openLineageProducer,SQL, "FRD","COLnamespace",spark);
        LogicalPlan logicalPlan = FRD.queryExecution().analyzed(); // Use analyzed plan
        Map<String, List<String>> fieldTransformations = extractFieldWiseTransformations(logicalPlan);
        System.out.println(FRD.schema());
        // Print the field-wise transformation details
        System.out.println("Field-wise Transformation Details:" + logicalPlan);
        for (Map.Entry<String, List<String>> entry : fieldTransformations.entrySet()) {
            System.out.println("Field: " + entry.getKey());
            System.out.println("Transformations: " + entry.getValue());
            System.out.println("-----------------------------");
        }

    }
}