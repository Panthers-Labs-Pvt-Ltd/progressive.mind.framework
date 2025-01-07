package com.progressive.minds.chimera.core.dataSource.formats.openTable;

import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class testIceberg {

    @Test
    void testReadWithValidInputs() throws Exception {
        // Mock SparkSession
        SparkSession sparkSession = Mockito.mock(SparkSession.class);
        DataFrameReader dataFrameReader = Mockito.mock(DataFrameReader.class);
        Dataset<Row> mockDataFrame = Mockito.mock(Dataset.class);

        // Mock the read behavior
        when(sparkSession.read()).thenReturn(dataFrameReader);
        when(dataFrameReader.format("iceberg")).thenReturn(dataFrameReader);
        when(dataFrameReader.options(anyMap())).thenReturn(dataFrameReader);
        when(dataFrameReader.load("test_catalog.test_db.test_table")).thenReturn(mockDataFrame);

        // Call the method
        Dataset<Row> result = iceberg.read(
                sparkSession, "test_catalog", "test_db", "test_table", null, null, null
        );

        // Assertions
        assertNotNull(result);
        verify(sparkSession.read().format("iceberg"), times(1)).load("test_catalog.test_db.test_table");
        verify(dataFrameReader, times(2)).format("iceberg");
        verify(dataFrameReader, times(1)).load("test_catalog.test_db.test_table");
    }

    @Test
    void testReadWithColumnFilter() throws Exception {
        // Mock SparkSession
        SparkSession sparkSession = Mockito.mock(SparkSession.class);
        DataFrameReader dataFrameReader = Mockito.mock(DataFrameReader.class);
        Dataset<Row> mockDataFrame = Mockito.mock(Dataset.class);
        Dataset<Row> selectedDataFrame = Mockito.mock(Dataset.class);

        // Mock the read behavior chain
        when(sparkSession.read()).thenReturn(dataFrameReader);
        when(dataFrameReader.format("iceberg")).thenReturn(dataFrameReader);
        when(dataFrameReader.options(anyMap())).thenReturn(dataFrameReader);
        when(dataFrameReader.load("test_catalog.test_db.test_table")).thenReturn(mockDataFrame);

        // Mock the select behavior
        when(mockDataFrame.select("col1", "col2")).thenReturn(selectedDataFrame);

        // Call the method
        Dataset<Row> result = iceberg.read(
                sparkSession, "test_catalog", "test_db", "test_table", "col1,col2", null, null
        );

        // Assertions
        assertNotNull(result);
        assertEquals(selectedDataFrame, result); // Verify the result matches the mocked select
        verify(mockDataFrame, times(1)).select("col1", "col2");
        verify(dataFrameReader, times(1)).format("iceberg");
        verify(dataFrameReader, times(1)).load("test_catalog.test_db.test_table"); // Updated to include catalog
    }


    @Test
    void testReadWithRowFilter() throws Exception {
        // Mock SparkSession
        SparkSession sparkSession = Mockito.mock(SparkSession.class);
        DataFrameReader dataFrameReader = Mockito.mock(DataFrameReader.class);
        Dataset<Row> mockDataFrame = Mockito.mock(Dataset.class);
        Dataset<Row> filteredDataFrame = Mockito.mock(Dataset.class); // Mock the result after where()

        // Mock the read behavior chain
        when(sparkSession.read()).thenReturn(dataFrameReader);
        when(dataFrameReader.format("iceberg")).thenReturn(dataFrameReader);
        when(dataFrameReader.options(anyMap())).thenReturn(dataFrameReader);
        when(dataFrameReader.load("test_catalog.test_db.test_table")).thenReturn(mockDataFrame);

        // Mock the where behavior
        when(mockDataFrame.where("col1 > 10")).thenReturn(filteredDataFrame);

        // Call the method
        Dataset<Row> result = iceberg.read(
                sparkSession, "test_catalog","test_db", "test_table", null, "col1 > 10", null
        );

        // Assertions
        assertNotNull(result);
        assertEquals(filteredDataFrame, result); // Verify the result matches the mocked where
        verify(mockDataFrame, times(1)).where("col1 > 10");
        verify(dataFrameReader, times(1)).format("iceberg");
        verify(dataFrameReader, times(1)).load("test_catalog.test_db.test_table");
    }

    @Test
    void testIcebergRead_withactualdata() throws Exception {
        SparkSession spark = SparkSession.builder()
                .appName("testIceberg")
                .master("local[*]")
                .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
//                .config("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkCatalog")
                .config("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkSessionCatalog")
                .config("spark.sql.catalog.spark_catalog.type", "hadoop")
                .config("spark.sql.catalog.spark_catalog.warehouse", "/home/chimera/data/warehouse")
                .getOrCreate();

 //       spark.sql("CREATE NAMESPACE IF NOT EXISTS local.db");
        spark.sql("DROP TABLE spark_catalog.db.sampleIcebergTable");


        // Read CSV file into a DataFrame
        Dataset<Row> DF = spark.read()
                .option("header", true)
                .csv("/home/chimera/data/input/csv/sample.csv");

       // Write to Iceberg table
        DF.writeTo("spark_catalog.db.sampleIcebergTable").create();

        // Read from Iceberg table
        Dataset<Row> readDF = iceberg.read(spark, "spark_catalog", "db", "sampleIcebergTable", null);
        assert DF.schema().equals(readDF.schema()) : "Dataframe Schemas are not same !!!";
        assert DF.count() == readDF.count() : "Row Counts are different !!!";
        Dataset<Row> diff1 = DF.except(readDF);
        Dataset<Row> diff2 = readDF.except(DF);

        assert diff1.isEmpty() && diff2.isEmpty() : "DataFrames are not equal!";

        Dataset<Row> readDFWithColumnFilter = iceberg.readWithColumnFilters(spark, "spark_catalog", "db", "sampleIcebergTable", "firstName,email", null);
        Dataset<Row> inpurDFWithColFilter = DF.select("firstName", "email");

        assert readDFWithColumnFilter.schema().equals(inpurDFWithColFilter.schema()) : "Dataframe Schemas are not same !!!";
        assert readDFWithColumnFilter.count() == inpurDFWithColFilter.count() : "Row Counts are different !!!";
        Dataset<Row> diff1WithColFilter = readDFWithColumnFilter.except(inpurDFWithColFilter);
        Dataset<Row> diff2WithColFilter = inpurDFWithColFilter.except(readDFWithColumnFilter);

        assert diff1WithColFilter.isEmpty() && diff2WithColFilter.isEmpty() : "DataFrames are not equal!";

        Dataset<Row> readDFWithRowFilter = iceberg.readWithRowFilters(spark, "spark_catalog", "db", "sampleIcebergTable", "email like '%@%' AND firstName=='Prashant'", null);

        Dataset<Row> InputDFWithRowFilter = DF.where("email like '%@%' AND firstName=='Prashant'");

        assert readDFWithRowFilter.schema().equals(InputDFWithRowFilter.schema()) : "Dataframe Schemas are not same !!!";
        assert readDFWithRowFilter.count() == InputDFWithRowFilter.count() : "Row Counts are different !!!";
        Dataset<Row> diff1WithRowFilter = readDFWithRowFilter.except(InputDFWithRowFilter);
        Dataset<Row> diff2WithRowFilter = InputDFWithRowFilter.except(readDFWithRowFilter);

        assert diff1WithRowFilter.isEmpty() && diff2WithRowFilter.isEmpty() : "DataFrames are not equal!";

        Dataset<Row> readDFWithRowColFilter = iceberg.read(spark, "spark_catalog", "db", "sampleIcebergTable", "firstName,email","email like '%@%' AND firstName=='Prashant'", null);

        Dataset<Row> InputDFWithRowColFilter = DF.select("firstName","email").where("email like '%@%' AND firstName=='Prashant'");

        assert readDFWithRowColFilter.schema().equals(InputDFWithRowColFilter.schema()) : "Dataframe Schemas are not same !!!";
        assert readDFWithRowColFilter.count() == InputDFWithRowColFilter.count() : "Row Counts are different !!!";
        Dataset<Row> diff1WithRowColFilter = readDFWithRowColFilter.except(InputDFWithRowColFilter);
        Dataset<Row> diff2WithRowColFilter = InputDFWithRowColFilter.except(readDFWithRowColFilter);

        assert diff1WithRowColFilter.isEmpty() && diff2WithRowColFilter.isEmpty() : "DataFrames are not equal!";
    }

    @Test
    void testIcebergWrite_withactualData() throws Exception {
        SparkSession spark = SparkSession.builder()
                .appName("testIceberg")
                .master("local[*]")
                .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
//                .config("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkCatalog")
                .config("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkSessionCatalog")
                .config("spark.sql.catalog.spark_catalog.type", "hadoop")
                .config("spark.sql.catalog.spark_catalog.warehouse", "/home/chimera/data/warehouse")
                .getOrCreate();

        Dataset<Row> DF = spark.read()
                .option("header", true)
                .csv("/home/chimera/data/input/csv/sampleNew.csv");

        iceberg.write(DF, spark, "spark_catalog", "db", "icebergWriteTestTable", "/home/chimera/data/warehouse/db/icebergWriteTestTable", null, null, null, null, null);
        Dataset<Row> readDf = iceberg.read(spark, "spark_catalog", "db", "icebergWriteTestTable", null);
        readDf.show();
    }



    }




