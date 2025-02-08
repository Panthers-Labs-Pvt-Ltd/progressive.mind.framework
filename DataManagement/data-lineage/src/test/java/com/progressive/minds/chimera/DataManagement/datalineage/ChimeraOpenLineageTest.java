package com.progressive.minds.chimera.DataManagement.datalineage;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;

import java.net.URI;
import java.net.URISyntaxException;
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
}