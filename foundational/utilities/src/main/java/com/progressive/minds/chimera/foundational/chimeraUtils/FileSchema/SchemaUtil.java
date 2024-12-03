package com.progressive.minds.chimera.foundational.chimeraUtils.FileSchema;

import com.univocity.parsers.conversions.Conversions;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.avro.SchemaConverters;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;
import org.apache.avro.Schema;
import scala.collection.JavaConverters;
import scala.collection.mutable.HashMap;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class SchemaUtil {

    private static HashMap<String, String> delimiterMap = new HashMap<>();
    private static HashMap<String, String> quotesMap = new HashMap<>();
    // private static logger logger = new logger();

    public static StructType getSchema(SparkSession spark, String inSchemaLocation, String inOutputDataFrame) throws Exception {
        List<String> schemaList = null;
        String schemaFile = "";
        Optional<String> extn = Optional.of("");
        String schemaLocation = inSchemaLocation.endsWith("/") ? inSchemaLocation + "*" : inSchemaLocation;

        if (!schemaLocation.isEmpty()) {
            schemaList = getListOfFiles(schemaLocation);
            schemaList.removeIf(name -> !(name.toLowerCase(Locale.ROOT).endsWith(".yml") ||
                    name.toLowerCase(Locale.ROOT).endsWith(".avsc") ||
                    name.toLowerCase(Locale.ROOT).endsWith(".json") ||
                    name.toLowerCase(Locale.ROOT).endsWith(".jsd")));

            if (schemaList.size() > 1) {
                //logger.logError("Multiple Schema", "Multiple Schema Exist on " + inSchemaLocation);
                throw new Exception("DataSourceException.MultipleFileFound");
            } else if (schemaList.isEmpty()) {
                //logger.logError("Missing Schema", "Schema File Doesn't Exist on " + inSchemaLocation);
                throw new Exception("DataSourceException.FileNotFound");
            } else {
                try {
                    if (schemaLocation.startsWith("s3:")) {
                        String[] s3Details = getBucketFromS3Path(inSchemaLocation);
                        schemaFile = "s3://" + s3Details[0] + "/" + String.join("", schemaList);
                    } else {
                        schemaFile = String.join("", schemaList);
                    }
                    //logger.logInfo("getSchema", "Schema Full Path : " + schemaFile);
                    extn = Optional.of("." + schemaFile.split("\\.")[1]);
                    String schemaFileName = schemaFile.split("\\.")[0];

                    switch (extn.get().toLowerCase(Locale.ROOT)) {
                        case ".yml":
                            FileSchema FileSchema = fetchSchemaFromS3(schemaFileName, spark, extn.get());
                            String DelimiterChar = String.valueOf(getDelimiter().toChar());
                            String QuoteChar = String.valueOf(getQuotechar().toChar());
                            setStructDelimiter(inOutputDataFrame, DelimiterChar);
                            setStructQuotes(inOutputDataFrame, QuoteChar);
                            return FileSchema.getAttributeStruct();
                        case ".avsc":
                            JavaRDD<String> avro = spark.read().textFile(schemaFileName + extn.get()).toJavaRDD();
                            String avroString = String.join("\n", avro.collect());
                            Schema avroSchema = new Schema.Parser().parse(avroString);
                            return (StructType) SchemaConverters.toSqlType(avroSchema).dataType();
                        case ".json":
                            JavaRDD<String> json = spark.read().textFile(schemaFileName + extn.get()).toJavaRDD();
                            String jsonString = String.join("\n", json.collect());
                            return (StructType) DataType.fromJson(jsonString);
                        case ".jsd":
                            List<String> src = Files.readAllLines(Paths.get(schemaFileName + extn.get()));
                            return org.zalando.spark.jsonschema.SchemaConverter.convertContent(String.join("\n", src));
                        default:
                            //logger.logWarning("Schema Parser", "Unsupported Schema Type Found, File with .yml / .json /.avsc and .jsd is Supported");
                            throw new Exception("DataSourceException.UnsupportedSchemaType");
                    }
                } catch (Exception e) {
                    //logger.logWarning("Schema Parser", "Schema Generation Process Failed With Error " + e);
                    throw new Exception("DataSourceException.InvalidSchema");
                }
            }
        } else {
            //logger.logWarning("Schema Parser", "Schema File Not Found Process Failed With Error");
            throw new Exception("DataSourceException.FileNotFound");
        }
    }

    private static Conversions getQuotechar() {
        System.out.print("WIP");
        return null;
    }

    private static Conversions getDelimiter() {
        System.out.print("WIP");
        return null;

    }

    private static FileSchema fetchSchemaFromS3(String schemaFileName, SparkSession spark, String s) {
        System.out.print("WIP");
        return null;
    }

    public static void setStructDelimiter(String key, String value) {
        delimiterMap.put(key, value);
    }

    public static void setStructQuotes(String key, String value) {
        quotesMap.put(key, value);
    }

    private static List<String> getListOfFiles(String schemaLocation) {
        // Implement logic to list files in the given schema location
        return List.of(); // Placeholder
    }

    private static String[] getBucketFromS3Path(String s3Path) {
        // Implement logic to extract bucket and prefix from S3 path
        return new String[]{"bucket-name", "prefix"}; // Placeholder
    }
}