package com.progressive.minds.chimera.foundational.chimeraUtils.FileSchema;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.spark.sql.SparkSession;

import java.net.URI;
import java.nio.file.Paths;
import java.util.*;

public class SchemaHandler {

    public String getSchema(SparkSession spark, String inSchemaLocation) throws Exception {
        List<String> schemaList = new ArrayList<>();
        String schemaFile = "";
        Optional<String> extn = Optional.of("");
        String schemaLocation = inSchemaLocation.endsWith("/") ? inSchemaLocation + "*" : inSchemaLocation;

        if (!schemaLocation.isEmpty()) {
            schemaList = getListOfFiles(schemaLocation);
            schemaList.removeIf(name -> !(name.toLowerCase(Locale.ROOT).endsWith(".ccl") ||
                    name.toLowerCase(Locale.ROOT).endsWith(".ccp") ||
                    name.toLowerCase(Locale.ROOT).endsWith(".cob") ||
                    name.toLowerCase(Locale.ROOT).endsWith(".cpy") ||
                    name.toLowerCase(Locale.ROOT).endsWith(".copybook")));

            if (schemaList.size() > 1) {
                throw new Exception("DataSourceException.MultipleFileFound");
            } else if (schemaList.isEmpty()) {
                //logger.logError("Missing Schema", "Schema File Doesn't Exist on " + inSchemaLocation);
                throw new Exception("DataSourceException.FileNotFound");
            } else {
                try {
                    if (schemaLocation.startsWith("s3:")) {
                        var s3Details = getBucketFromS3Path(inSchemaLocation);
                        String s3Bucket = s3Details.get(0);
                        String s3Prefix = s3Details.get(1);
                        schemaFile = "s3://" + s3Bucket + "/" + String.join("", schemaList);
                    } else {
                        schemaFile = String.join("", schemaList);
                    }
                    //logger.logInfo("getSchema", "Schema Full Path : " + schemaFile);
                } catch (Exception e) {
                    //logger.logWarning("Schema Parser", "Schema Generation Process Failed With Error " + e);
                    throw new Exception("DataSourceException.InvalidSchema");
                }
            }
            return schemaFile;
        } else {
            //logger.logWarning("Schema Parser", "Schema File Not Found Process Failed With Error");
            throw new Exception("DataSourceException.FileNotFound");
        }
    }

    public List<String> getListOfFiles(String dir) throws Exception {
        try {
            String fileName = Paths.get(dir).getFileName().toString();
            String folderName = Paths.get(dir).getParent().toString();
            String pattern = fileName;
            List<String> returnList = new ArrayList<>();
            String extension;

            if (pattern.startsWith("*.") && pattern.endsWith("*")) {
                extension = pattern.split("\\.")[1];
            } else if (pattern.startsWith("*") && pattern.endsWith("*")) {
                extension = pattern.replace("*", "");
            } else if (pattern.equals("*")) {
                extension = "";
            } else {
                extension = pattern;
            }

            if (dir.startsWith("s3")) {
                var s3Details = getBucketFromS3Path(dir);
                String s3Bucket = s3Details.get(0);
                String s3Prefix = s3Details.get(1);
                // returnList = getS3FilesList(s3Bucket, s3Prefix, extension);
                //logger.logInfo("getListOfFiles", String.join(",", returnList));
            } else {
                returnList = pullFileLists(folderName).stream()
                        .filter(file -> file.endsWith(extension))
                        .toList();
            }
            return returnList;
        } catch (Exception e) {
            //logger.logError("getListOfFiles", "Exception during Checking File existence");
            throw new Exception("DataSourceException.FileNotFound");
        }
    }

    public List<String> pullFileLists(String dirPath) throws Exception {
        try {
            List<String> files = new ArrayList<>();
            FileSystem filesystem = FileSystem.get(URI.create(dirPath), new Configuration());
            var iteratorNext = filesystem.listFiles(new Path(dirPath), true);

            while (iteratorNext.hasNext()) {
                LocatedFileStatus fs = iteratorNext.next();
                files.add(fs.getPath().toUri().getPath());
            }
            return files;
        } catch (Exception e) {
            //logger.logError("getListOfFiles", "Exception during Checking File existence " + e.getMessage());
            throw new Exception("DataSourceException.FileNotFound");
        }
    }

    // Placeholder for the missing methods
    private List<String> getBucketFromS3Path(String path) {
        // Implement the logic to extract bucket and prefix from S3 path
        return List.of("bucket-name", "prefix");
    }
}