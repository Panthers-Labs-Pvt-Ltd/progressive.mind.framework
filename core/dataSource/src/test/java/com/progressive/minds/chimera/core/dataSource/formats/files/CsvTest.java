package com.progressive.minds.chimera.core.dataSource.formats.files;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CsvTest {

    private static SparkSession sparkSession;

    @BeforeAll
    static void setup() {
        sparkSession = SparkSession.builder()
                .appName("CSV Test")
                .master("local")
                .getOrCreate();
    }

    @Test
    void testReadCsv() {
        String sourcePath = "src/test/resources/sample.csv";
        String columnFilter = "name,age";
        String rowFilter = "age > 30";
        String customConfig = "header=true, inferSchema=true";
        Integer limit = 10;

        Dataset<Row> result = Csv.read(sparkSession, "TestPipeline", sourcePath, columnFilter, rowFilter, customConfig, limit);

        assertNotNull(result);
        assertEquals(2, result.columns().length);
        assertTrue(result.count() <= 10);
    }

    @Test
    void testWriteCsv() {
        String sourcePath = "src/test/resources/sample.csv";
        String outputPath = "src/test/resources/output/testOp.csv";
        String compressionFormat = "gzip";
        String savingMode = "Overwrite";
        String partitioningKeys = "name";
        String sortingKeys = "age";
        String duplicationKeys = "name";
        String extraColumns = "country";
        String extraColumnsValues = "USA";
        String customConfig = "header=true, inferSchema=true";

        sparkSession.sql("CREATE DATABASE IF NOT EXISTS testDB" );

        Dataset<Row> sourceDataFrame = Csv.read(sparkSession, "TestPipeline", sourcePath, "", "", customConfig, null);

        try {
            Dataset<Row> result = Csv.write(sparkSession, "TestPipeline", "testDB", "testTable", sourceDataFrame, outputPath, compressionFormat, savingMode, partitioningKeys, sortingKeys, duplicationKeys, extraColumns, extraColumnsValues, customConfig);

            assertNotNull(result);
            assertTrue(result.columns().length > 0);
        } catch (Csv.DataSourceWriteException e) {
            fail("Write operation failed: " + e.getMessage());
        }
    }
}
