package com.progressive.minds.chimera.DataManagement.datalineage.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.progressive.minds.chimera.core.dataSource.sourceTypes.FileReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColumnLevelLineageTest {

    @Test
    void getTest() throws Exception {
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
        String SQL = """
                 WITH RankedFlights AS ( 
                 SELECT 
                     Year ||Month||DayofMonth AS FULLDT, 
                     Year, 
                     Month, 
                     DayofMonth, 
                     FlightDate, 
                     Reporting_Airline, 
                     Flight_Number_Reporting_Airline, 
                     OriginCityName, 
                     DestCityName, 
                     Distance, 
                     ArrDelay, 
                     DepDelay, 
                     ROW_NUMBER() OVER (PARTITION BY Reporting_Airline ORDER BY ArrDelay DESC) AS rank 
                   FROM flight_data 
                   WHERE Cancelled = '0' AND Diverted = '0' AND Distance IS NOT NULL 
                 ), 
                 AggregatedPerformance AS ( 
                   SELECT 
                     Year, 
                     Month, 
                     Reporting_Airline, 
                     COUNT(Flight_Number_Reporting_Airline) AS total_flights, 
                     SUM(CASE WHEN DepDelay > 0 THEN 1 ELSE 0 END) AS delayed_departures, 
                     SUM(CASE WHEN ArrDelay > 0 THEN 1 ELSE 0 END) AS delayed_arrivals, 
                     AVG(Distance) AS avg_flight_distance, 
                     AVG(CAST(ArrDelay AS FLOAT)) AS avg_arrival_delay 
                   FROM flight_data 
                   WHERE Cancelled = '0' 
                   GROUP BY Year, Month, Reporting_Airline 
                 ), 
                 CancellationInsights AS ( 
                   SELECT 
                     Year, 
                     Month, 
                     Reporting_Airline, 
                     COUNT(*) AS cancellations, 
                     COUNT(DISTINCT Flight_Number_Reporting_Airline) AS unique_cancelled_flights, 
                     CancellationCode 
                   FROM flight_data 
                   WHERE Cancelled = '1' 
                   GROUP BY Year, Month, Reporting_Airline, CancellationCode 
                 ) 
                 SELECT 
                   rf.FULLDT, 
                   rf.Year, 
                   rf.Month, 
                   rf.FlightDate, 
                   rf.Reporting_Airline, 
                   rf.OriginCityName AS origin_city, 
                   rf.DestCityName AS destination_city, 
                   rf.Distance, 
                   ap.total_flights, 
                   ap.delayed_departures, 
                   ap.delayed_arrivals, 
                   ap.avg_flight_distance, 
                   ap.avg_arrival_delay, 
                   ci.cancellations, 
                   ci.unique_cancelled_flights, 
                   ci.CancellationCode 
                 FROM RankedFlights rf 
                 JOIN AggregatedPerformance ap  
                   ON rf.Year = ap.Year AND rf.Month = ap.Month AND rf.Reporting_Airline = ap.Reporting_Airline 
                 LEFT JOIN CancellationInsights ci  
                   ON rf.Year = ci.Year AND rf.Month = ci.Month AND rf.Reporting_Airline = ci.Reporting_Airline 
                 WHERE rf.rank = 1 
                 ORDER BY rf.Year, rf.Month, rf.Reporting_Airline""";

        flight_data.createOrReplaceTempView("flight_data");
        Dataset<Row> resultDF = spark.sql(SQL);
        ColumnLevelLineage.get2(SQL,resultDF );

    }
}