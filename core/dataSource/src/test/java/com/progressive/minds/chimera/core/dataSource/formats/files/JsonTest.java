package com.progressive.minds.chimera.core.dataSource.formats.files;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    private static SparkSession sparkSession;

    @BeforeAll
    static void setup() {
        sparkSession = SparkSession.builder()
                .appName("JsonTest")
                .master("local")
     //           .config("spark.sql.columnNameOfCorruptRecord", "corrupt_record")
                .getOrCreate();
    }


    @Test
    void testReadJson() {
        String sourcePath = "src/test/resources/sample.json";
        String schemaPath = "src/test/resources/sampleJson_schema.yml";
        Dataset<Row> dataFrame = Json.read(sparkSession, "testPipeline", sourcePath, null, null, null, null, schemaPath);
        dataFrame.select("name", "age", "email","corrupt_record").show();
        assertNotNull(dataFrame);
        assertFalse(dataFrame.isEmpty());
    }

    @Test
    void testWriteJson() {
        String sourcePath = "src/test/resources/testJson.json";
        String schemaPath = "src/test/resources/testJson_schema.yml";
        String outputPath = "src/test/resources/output/json/languages";
        Dataset<Row> dataFrame = Json.read(sparkSession, "testPipeline", sourcePath, null, null, "multiline=true", null, schemaPath);
        assertNotNull(dataFrame);
        assertFalse(dataFrame.isEmpty());

        sparkSession.sql("CREATE DATABASE IF NOT EXISTS testDB" );
        try {
            Json.write(sparkSession, "testPipeline", "testDB", "languagestable", dataFrame, outputPath, null, "Overwrite", "language", null, null, null, null, null);
        } catch (Json.DataSourceWriteException e) {
            fail("Write operation failed: " + e.getMessage());
        }
    }
}
