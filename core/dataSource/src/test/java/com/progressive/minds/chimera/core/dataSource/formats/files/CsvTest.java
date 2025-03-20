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
    void testReadCsvWithSchema() {
        String sourcePath = "src/test/resources/sample.csv";
        String columnFilter = "name,age";
        String rowFilter = "age > 20";
        String customConfig = "header=true";
        Integer limit = 2;
        String schemaPath = "src/test/resources/schema.yml";
        Dataset<Row> result = Csv.read(sparkSession, "TestPipeline", sourcePath, columnFilter, rowFilter, customConfig, limit, schemaPath);
        assertNotNull(result);
        assertEquals(2, result.columns().length);
        assertTrue(result.count() <= 10);
    }

    @Test
    void testReadCsvWithInferSchema() {
        String sourcePath = "src/test/resources/sample.csv";
        String columnFilter = "name,age";
        String rowFilter = "age > 30";
        String customConfig = "header=true";
        Integer limit = 10;

        Dataset<Row> result = Csv.read(sparkSession, "TestPipeline", sourcePath, columnFilter, rowFilter, customConfig, limit, null);

        assertNotNull(result);
        assertEquals(2, result.columns().length);
        assertTrue(result.count() <= 10);
    }

    @Test
    void testWriteCsv() {
        String sourcePath = "src/test/resources/organizations.csv";
        String outputPath = "src/test/resources/output/organizations.csv";
        String compressionFormat = "gzip";
        String savingMode = "Overwrite";
        String partitioningKeys = "country,industry";
        String sortingKeys = "founded";
        String duplicationKeys = "name";
        String extraColumns = "";
        String extraColumnsValues = "";
        String customConfig = "header=true";

        sparkSession.sql("CREATE DATABASE IF NOT EXISTS testDB" );

        String schemaPath = "src/test/resources/organization.yml";

        Dataset<Row> sourceDataFrame = Csv.read(sparkSession, "TestPipeline", sourcePath, "", "", customConfig, null, schemaPath);

        try {
            Dataset<Row> result = Csv.write(sparkSession, "TestPipeline", "testDB", "organizations", sourceDataFrame, outputPath, compressionFormat, savingMode, partitioningKeys, sortingKeys, duplicationKeys, extraColumns, extraColumnsValues, customConfig);

            assertNotNull(result);
            assertTrue(result.columns().length > 0);
        } catch (Csv.DataSourceWriteException e) {
            fail("Write operation failed: " + e.getMessage());
        }
    }
}
