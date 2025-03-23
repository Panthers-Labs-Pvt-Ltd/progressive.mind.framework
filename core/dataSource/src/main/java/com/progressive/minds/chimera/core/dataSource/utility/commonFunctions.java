package com.progressive.minds.chimera.core.dataSource.utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progressive.minds.chimera.core.dataSource.formats.files.Json;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.spark.SparkCatalog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import static org.apache.spark.sql.functions.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class commonFunctions {
    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(commonFunctions.class);
    private static String loggerTagName = "Common Functions";

    public static Dataset<Row> renamePartitionKeysCase(Dataset<Row> dataframe, String partitionBy) {
        String[] partitionColumns = Arrays.stream(partitionBy.split(","))
                .map(String::trim)
                .toArray(String[]::new);

        String[] originalColumns = dataframe.columns();
        String[] renamedColumns = new String[originalColumns.length];

        for (int i = 0; i < originalColumns.length; i++) {
            String column = originalColumns[i];
            String lowercaseColumn = column;

            int index = Arrays.asList(partitionColumns).indexOf(lowercaseColumn);
            if (index != -1) {
                renamedColumns[i] = partitionBy.split(",")[index].trim();
            } else {
                renamedColumns[i] = column;
            }
        }

        Dataset<Row> renamedDF = dataframe.toDF(renamedColumns);
        return renamedDF;
    }

    /**
     * Merges extra columns to a given DataFrame.
     *
     * @param inDataFrameName The input DataFrame to which the extra columns will be added.
     * @param KeyValuePair    A JSON string representing a map of column names and their corresponding values.
     * @return  The updated DataFrame with the new columns added.
     * @throws JsonProcessingException If there is an error processing the JSON string.
     */

    public static Dataset<Row> MergeExtraColumnToDataFrame(Dataset<Row> inDataFrameName, String KeyValuePair)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> newColumns = objectMapper.readValue(KeyValuePair, Map.class);
        //  Add the new columns to the DataFrame
        for (Map.Entry<String, String> entry : newColumns.entrySet()) {
            String colName = entry.getKey();
            String colValue = entry.getValue();

            // Add the new column with the constant value
            inDataFrameName = inDataFrameName.withColumn(colName, lit(colValue));
        }
        return inDataFrameName;
    }

    /**
     * Drops duplicate rows from the given DataFrame based on specified columns.
     *
     * @param dedupColumns A comma-separated string of column names to consider for deduplication.
     *                     If the string is "*", all duplicates will be removed.
     *                     If null or empty, the original DataFrame will be returned.
     * @param sourceDataframe The input DataFrame from which duplicates will be removed.
     * @return A new DataFrame with duplicates removed based on the specified columns.
     *         If no columns are specified or if the specified columns do not exist in the DataFrame,
     *         the original DataFrame is returned.
     */

    // public static Dataset<Row> DropDuplicatesOnKey(String dedupColumns, Dataset<Row> sourceDataframe) {
    //     Dataset<Row> targetDataframe = sourceDataframe;
    //     if (dedupColumns != null && !dedupColumns.isEmpty()) {
    //         if (dedupColumns.trim().equals("*")) {
    //             targetDataframe = sourceDataframe.dropDuplicates();
    //         } else {
    //             boolean existFlag = true;
    //             String[] columns = dedupColumns.split(",");
    //             for (String e : columns) {
    //                 if (sourceDataframe.columns().toString().contains(e)) {
    //                     existFlag = false;
    //                     // Assuming logger is a logger instance
    //                     logger.logInfo("[Deduplication]- De Duplication Column Not Exist in DataFrame");
    //                 }
    //             }
    //             if (existFlag) {
    //                 targetDataframe = sourceDataframe.dropDuplicates(columns);
    //             }
    //         }
    //     }
    //     return targetDataframe;
    // }

    public static Dataset<Row> DropDuplicatesOnKey(String dedupColumns, Dataset<Row> sourceDataframe) {
        if (dedupColumns == null || dedupColumns.trim().isEmpty()) {
            return sourceDataframe; // Return original DataFrame if no deduplication columns are provided
        }

        // Trim and split the deduplication columns
        String[] columns = dedupColumns.trim().split(",");
        for (int i = 0; i < columns.length; i++) {
            columns[i] = columns[i].trim(); // Trim each column name
        }

        // Check if all specified columns exist in the DataFrame
        List<String> missingColumns = new ArrayList<>();
        for (String column : columns) {
            if (!Arrays.asList(sourceDataframe.columns()).contains(column)) {
                missingColumns.add(column);
            }
        }

        // Log missing columns (if any)
        if (!missingColumns.isEmpty()) {
            logger.logInfo("[Deduplication] - Columns not found in DataFrame: " + String.join(", ", missingColumns));
            return sourceDataframe; // Return original DataFrame if any columns are missing
        }

        // Perform deduplication
        if (columns.length == 1 && columns[0].equals("*")) {
            return sourceDataframe.dropDuplicates(); // Deduplicate on all columns
        } else {
            return sourceDataframe.dropDuplicates(columns); // Deduplicate on specified columns
        }
    }

    /**
     * Utility method to check if a given string is null or blank (contains only whitespace characters).
     *
     * @param input the input string to be checked
     * @return true if the input string is null or contains only whitespace characters, false otherwise
     */
    public static boolean isNullOrBlank(String input) {
        return input == null || input.trim().isEmpty();
    }

    /**
     * Checks which of the specified partition columns in the given DataFrame contain null or empty values.
     *
     * @param inDataFrame The input DataFrame containing the data to be checked.
     * @param partitionColumns An array of column names to check for null or empty values.
     * @return An array of column names that contain null or empty values.
     */
//    public static String[] isPartitionKeysNull(Dataset<Row> inDataFrame, String[] partitionColumns) {
//        List<String> nullOrEmptyColumns = new ArrayList<>();
//
//        for (String colName : partitionColumns) {
//            Dataset<Row> distinctValues = inDataFrame.select(new Column(colName)).distinct();
//            String[] colValues = distinctValues.collectAsList().stream()
//                    .map(row -> row.getString(0))
//                    .toArray(String[]::new);
//
//            boolean hasNullOrEmpty = false;
//            for (String value : colValues) {
//                if (value == null || value.trim().isEmpty()) {
//                    hasNullOrEmpty = true;
//                    break;
//                }
//            }
//
//            if (hasNullOrEmpty) {
//                nullOrEmptyColumns.add(colName);
//            }
//        }
//        return nullOrEmptyColumns.toArray(new String[0]);
//    }

    public static String[] isPartitionKeysNull(Dataset<Row> inDataFrame, String[] partitionColumns) {
        // Validate inputs
        if (inDataFrame == null || partitionColumns == null || partitionColumns.length == 0) {
            throw new IllegalArgumentException("Input DataFrame and partition columns cannot be null or empty.");
        }

        List<String> nullOrEmptyColumns = new ArrayList<>();

        for (String colName : partitionColumns) {
            // Check if the column exists in the DataFrame
            if (!Arrays.asList(inDataFrame.columns()).contains(colName)) {
                throw new IllegalArgumentException("Column '" + colName + "' does not exist in the DataFrame.");
            }

            // Use Spark's built-in functions to check for null or empty values
            long nullOrEmptyCount = inDataFrame
                    .filter(functions.col(colName).isNull().or(functions.col(colName).equalTo("")))
                    .count();

            if (nullOrEmptyCount > 0) {
                nullOrEmptyColumns.add(colName);
            }
        }

        return nullOrEmptyColumns.toArray(new String[0]);
    }


    public String getValue(String search, String inKeyValuePairs) {
        String returnString = "";
        List<HashMap<String, String>> userConfig = new Gson().fromJson(inKeyValuePairs,
                new TypeToken<List<HashMap<String, String>>>() {}.getType());

        for (HashMap<String, String> item : userConfig) {
            if (search.equals(item.get("Key"))) {
                returnString = item.get("Value");
                logger.logInfo("UserConfig - Config for Key [" + search + "] and Value is [" + returnString + "] ");
                break;
            }
        }

        return returnString;
    }
    public static String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
    public static Map<String, String> getConfig(String inUserConfig) {
        Map<String, String> map = new HashMap<>();
        String loggerTag = "getConfig";
        String defaultSparkConf = "{\"Key\":\"default\",\"Value\":\"default\"))}";

        String userConf = (inUserConfig != null) ? inUserConfig : defaultSparkConf;
        logger.logInfo("Setting User Defined and default Config for Key " + inUserConfig);

        List<HashMap<String, String>> userConfig = new Gson().fromJson(userConf,
                new TypeToken<List<HashMap<String, String>>>() {}.getType());

        logger.logInfo("User Config " + userConfig);

        for (HashMap<String, String> item : userConfig) {
            map.put(item.get("Key"), item.get("Value"));
        }

        logger.logInfo("User Config mapping Completed " + map.toString());

        return map;
    }
    public String getValue(String search, String inKeyValuePairs, String sourceFormat) {
        String defaultSparkConf = "[{\"Key\":\"default\",\"Value\":\"default\"}]";
        String returnString = "";
        String loggerTag = sourceFormat.toUpperCase(Locale.ROOT) + "Config";
        String userConf = inKeyValuePairs != null ? inKeyValuePairs : defaultSparkConf;

        try {
            BufferedReader source = new BufferedReader(
                    new InputStreamReader(
                            getClass().getResourceAsStream("/datasourceDefaults/" +
                                    sourceFormat.toLowerCase(Locale.ROOT) + ".defaults.json")
                    )
            );

            StringBuilder defaultConfigJson = new StringBuilder();
            String line;
            while ((line = source.readLine()) != null) {
                defaultConfigJson.append(line);
            }

            logger.logInfo(String.format("Setting User Defined and default Config for Key %s", search));

            List<HashMap<String, String>> userConfig = new Gson().fromJson(userConf,
                    new TypeToken<List<HashMap<String, String>>>(){}.getType());

            List<HashMap<String, String>> defaultConfig = new Gson().fromJson(defaultConfigJson.toString(),
                    new TypeToken<List<HashMap<String, String>>>(){}.getType());

            // Search in user config
            for (HashMap<String, String> item : userConfig) {
                if (search.equals(item.get("Key"))) {
                    returnString = item.get("Value");
                    logger.logInfo(String.format("User Defined Config for Key %s = %s", search, returnString));
                    break;
                }
            }

            // If not found in user config, search in default config
            if (returnString.isEmpty()) {
                for (HashMap<String, String> item : defaultConfig) {
                    if (search.equals(item.get("propertyname"))) {
                        returnString = item.get("defaultvalue");
                        logger.logInfo(String.format("User Defined Config for Key %s = %s", search, returnString));
                        break;
                    }
                }
            }

        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
        }

        return returnString;
    }


    /* Error During Compilation - error: for-each not applicable to expression type
    public String dynamicPartitions(String[] partitionColumnBy, Dataset<Row> tableDataFrame) {
         Dataset<Row> dropDf = tableDataFrame.select(partitionColumnBy[0].trim(),
                 partitionColumnBy).dropDuplicates(partitionColumnBy[0], partitionColumnBy);

         StringBuilder queryStrBuilder = new StringBuilder();
         for (Row row : dropDf.collect()) {
             StringBuilder rowBuilder = new StringBuilder();
             for (int colIndex = 0; colIndex < dropDf.columns().length; colIndex++) {
                 rowBuilder.append(dropDf.columns()[colIndex].trim())
                         .append(" = '")
                         .append(row.get(colIndex))
                         .append("'");
                 if (colIndex < dropDf.columns().length - 1) {
                     rowBuilder.append(",");
                 }
             }
             queryStrBuilder.append(rowBuilder.toString()).append("),PARTITION(");
         }
         String queryStr = queryStrBuilder.toString();
         logger.logInfo("Dynamic Partition Query", queryStr);
         return queryStr;
     }

     public String dynamicAddPartitions(String[] partitionColumnBy, Dataset<Row> tableDataFrame) {
         logger.logInfo("dynamicAddPartitions", "Initiated");
         Dataset<Row> dropDf = tableDataFrame.select(partitionColumnBy[0].trim(),
                 partitionColumnBy).dropDuplicates(partitionColumnBy[0], partitionColumnBy);

         StringBuilder queryStrBuilder = new StringBuilder();
         for (Row row : dropDf.collect()) {
             StringBuilder rowBuilder = new StringBuilder();
             for (int colIndex = 0; colIndex < dropDf.columns().length; colIndex++) {
                 rowBuilder.append(dropDf.columns()[colIndex].trim())
                         .append(" = '")
                         .append(row.get(colIndex))
                         .append("'");
                 if (colIndex < dropDf.columns().length - 1) {
                     rowBuilder.append(",");
                 }
             }
             queryStrBuilder.append(rowBuilder.toString()).append("),PARTITION(");
         }
         String queryStr = queryStrBuilder.toString();
         logger.logInfo("DynamicAddPartition Query", queryStr);
         return queryStr;
     }

     */
    public static DataType getDataTypeForSchema(String colName, String typeString) {
        DataType returnDataType = DataTypes.StringType;
        String dataType;
        String dataTypeLen = "";
        int precisionTemp = 0;
        int scaleTemp = 0;

        if (typeString.contains("(")) {
            Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
            Matcher matcher = pattern.matcher(typeString);
            if (matcher.find()) {
                dataTypeLen = matcher.group(1);
            } else {
                dataTypeLen = typeString;
            }
            dataType = typeString.split("\\(")[0];
        } else {
            dataType = typeString;
        }

        switch (dataType.toUpperCase()) {
            case "TINYINT":
                auditConversion(colName, typeString, "ByteType");
                returnDataType = DataTypes.ByteType;
                break;

            case "SMALLINT":
                auditConversion(colName, typeString, "ShortType");
                returnDataType = DataTypes.ShortType;
                break;

            case "INTEGER":
                auditConversion(colName, typeString, "IntegerType");
                returnDataType = DataTypes.IntegerType;
                break;

            case "BIGINT":
                auditConversion(colName, typeString, "LongType");
                returnDataType = DataTypes.LongType;
                break;

            case "FLOAT":
                auditConversion(colName, typeString, "FloatType");
                returnDataType = DataTypes.FloatType;
                break;

            case "DOUBLE":
                auditConversion(colName, typeString, "DoubleType");
                returnDataType = DataTypes.DoubleType;
                break;

            case "STRING":
                auditConversion(colName, typeString, "StringType");
                returnDataType = DataTypes.StringType;
                break;

            case "CHAR":
                auditConversion(colName, typeString, "CharType");
                if (dataTypeLen.isEmpty()) {
                    returnDataType = DataTypes.StringType;
                } else {
                    returnDataType = CharType$.MODULE$.apply(Integer.parseInt(dataTypeLen));
                }
                break;

            case "VARCHAR":
                auditConversion(colName, typeString, "StringType");
                if (dataTypeLen.isEmpty()) {
                    returnDataType = DataTypes.StringType;
                } else {
                    returnDataType = VarcharType$.MODULE$.apply(Integer.parseInt(dataTypeLen));
                }
                break;

            case "BINARY":
                auditConversion(colName, typeString, "BinaryType");
                returnDataType = DataTypes.BinaryType;
                break;

            case "DATE":
                auditConversion(colName, typeString, "DateType");
                returnDataType = DataTypes.DateType;
                break;

            case "BOOLEAN":
                auditConversion(colName, typeString, "BooleanType");
                returnDataType = DataTypes.BooleanType;
                break;

            case "DATETIME":
                auditConversion(colName, typeString, "DateType");
                returnDataType = DataTypes.DateType;
                break;

            case "TIMESTAMP":
                auditConversion(colName, typeString, "TimestampType");
                returnDataType = DataTypes.TimestampType;
                break;

            case "NULL":
                auditConversion(colName, typeString, "NullType");
                returnDataType = DataTypes.NullType;
                break;

            case "ARRAY":
                auditConversion(colName, typeString, "ArrayType");
                returnDataType = DataTypes.createArrayType(DataTypes.StringType, true);
                break;

            case "DECIMAL":
                auditConversion(colName, typeString, "DecimalType");
                if (!dataTypeLen.isEmpty() && dataTypeLen.split(",").length >= 2) {
                    precisionTemp = Integer.parseInt(dataTypeLen.split(",")[0]);
                    scaleTemp = Integer.parseInt(dataTypeLen.split(",")[1]);
                    Map<String, Integer> decimalScale = getDecimalScale(precisionTemp, scaleTemp);
                    precisionTemp = decimalScale.getOrDefault("precision", 38);
                    scaleTemp = decimalScale.getOrDefault("scale", 6);
                } else {
                    precisionTemp = 38;
                    scaleTemp = 6;
                }
                returnDataType = DataTypes.createDecimalType(precisionTemp, scaleTemp);
                break;

            default:
                logger.logWarning("Data Type Caster - Invalid Schema DataType Found for " + colName + " and Data Type " + typeString);
                logger.logWarning("Data Type Caster - Interpreting As StringType for Spark and Continuing");
                returnDataType = DataTypes.StringType;
        }
        return returnDataType;
    }

    private static void auditConversion(String colName, String typeString, String sparkType) {
        logger.logWarning("Data Type Caster - Interpreting As " + typeString + " for Spark Data Type " + sparkType + " For Column " + colName);
    }

    private static Map<String, Integer> getDecimalScale(int precision, int scale) {
        return getDecimalScale(precision, scale, 38, 6);
    }

    private static Map<String, Integer> getDecimalScale(int precision, int scale, int defaultPrecision, int defaultScale) {
        int adjustedScale = scale;
        int adjustedPrecision = precision;
        final int MAX_PRECISION = 39;
        final int MAX_SCALE = 38;
        Map<String, Integer> map = new HashMap<>();

        if (precision <= MAX_PRECISION && scale <= MAX_SCALE && scale <= precision) {
            adjustedScale = scale;
            adjustedPrecision = precision;
        } else {
            adjustedPrecision = Math.min(precision, MAX_PRECISION);
            adjustedScale = Math.min(scale, adjustedPrecision);
            adjustedScale = Math.min(MAX_SCALE, adjustedScale);
        }

        map.put("precision", adjustedPrecision);
        map.put("scale", adjustedScale);
        return map;
    }

    public StructType constructStructSchema(String inboundSchema, String appName) throws Exception {
        logger.logInfo("constructStructSchema Started");
        logger.logInfo("InboundSchema is " + inboundSchema);
        StructType structSchema = new StructType();
        try {
            List<HashMap<String, String>> listObj = new Gson().fromJson(inboundSchema,
                    new TypeToken<List<HashMap<String, String>>>() {}.getType());
            for (HashMap<String, String> item : listObj) {
                structSchema = structSchema.add(
                        item.get("FieldName"),
                        getDataTypeForSchema(item.get("FieldType"), appName),
                        Boolean.parseBoolean(item.get("Nullable")));
            }
        } catch (Exception e) {
            logger.logError("ProcessEvent - "+  e.getMessage());
            throw new Exception("EDLExecutionException.UNCAUGHT EXCEPTION");
        }
        logger.logInfo("ProcessEvent - constructStructSchema Completed");
        return structSchema;
    }
//    public static Dataset<Row> mergeColumnsToDataFrame(Dataset<Row> tableDataFrame, String colName, String colValue) {
//        // Split column names and values into lists
//        List<String> colNames = Arrays.asList(colName.split(","));
//        List<String> colValues = Arrays.asList(colValue.split(","));
//
//        // Add each column-value pair to the DataFrame
//        for (int i = 0; i < colNames.size(); i++) {
//            String columnName = colNames.get(i);
//            String columnValue = colValues.get(i);
//            tableDataFrame = tableDataFrame.withColumn(columnName, functions.lit(columnValue));
//        }
//
//        return tableDataFrame;
//    }


    public static Dataset<Row> mergeColumnsToDataFrame(Dataset<Row> tableDataFrame, String colName, String colValue) {
        // Validate inputs
        if (StringUtils.isBlank(colName) || StringUtils.isBlank(colValue)) {
            throw new IllegalArgumentException("Column names and values cannot be null or empty.");
        }

        // Split column names and values into lists
        List<String> colNames = Arrays.asList(colName.split(","));
        List<String> colValues = Arrays.asList(colValue.split(","));

        // Ensure the number of column names matches the number of values
        if (colNames.size() != colValues.size()) {
            throw new IllegalArgumentException("The number of column names must match the number of values.");
        }

        // Add each column-value pair to the DataFrame
        for (int i = 0; i < colNames.size(); i++) {
            String columnName = colNames.get(i).trim(); // Trim to remove extra spaces
            String columnValue = colValues.get(i).trim(); // Trim to remove extra spaces

            // Add the column with the specified value
            tableDataFrame = tableDataFrame.withColumn(columnName, functions.lit(columnValue));
        }

        return tableDataFrame;
    }
    /**
     * Sorts a DataFrame based on one or more keys.
     *
     * @param dataFrame The input DataFrame to sort.
     * @param sortKeys  A comma-separated string of column names to sort by.
     * @param ascending A boolean indicating whether the sorting should be ascending.
     * @return The sorted DataFrame.
     */
    public static Dataset<Row> sortDataFrame(Dataset<Row> dataFrame, String sortKeys, boolean ascending) {
        // Split the sort keys by comma
        String[] keys = sortKeys.split(",");

        // Create an array of Columns to sort by
        Column[] sortColumns = Arrays.stream(keys)
                .map(key -> ascending ? functions.col(key).asc() : functions.col(key).desc())
                .toArray(Column[]::new);

        // Sort the DataFrame
        return dataFrame.sort(sortColumns);
    }

    public static boolean tableExists(SparkSession spark, String catalogName, String databaseName, String tableName) {
        boolean exists;
        try {
            // Execute a SQL query to check if the table exists by limiting the rows
            spark.sql("SELECT 1 FROM " + catalogName + "." + databaseName + "." + tableName + " LIMIT 1").count();
            exists = true; // If no exception is thrown, the table exists
        } catch (Exception e) {
            // If an exception is caught, assume the table does not exist
            exists = false;
        }
        return exists;
    }

    public static void applyReaderOptions(String customConfig, DataFrameReader reader) {
        if (StringUtils.isNotEmpty(customConfig)) {
            String[] options = customConfig.split(",");
            for (String option : options) {
                String[] keyValue = option.split("=");
                if (keyValue.length == 2 && StringUtils.isNotEmpty(keyValue[0]) && StringUtils.isNotEmpty(keyValue[1])) {
                    reader.option(keyValue[0].trim(), keyValue[1].trim());
                } else {
                    logger.logWarning("Invalid custom option: " + option);
                }
            }
        }
        reader.option("columnNameOfCorruptRecord", "corrupt_record");
    }

    public static void applyOrInferSchema(String schemaPath, DataFrameReader reader) throws Exception {
        if (StringUtils.isNotEmpty(schemaPath)) {
            Map<String, Object> schemaConfig = getSchemaConfigFromYaml(schemaPath);
            if (schemaConfig != null && schemaConfig.containsKey("source-parameters")) {
                Map<String, Object> sourceParameters = (Map<String, Object>) schemaConfig.get("source-parameters");
                StructType schema = getSchemaFromConfig(sourceParameters);
                reader.schema(schema);
                logger.logInfo("Schema built and applied.");
                // Apply delimiter and quote options from schema config
                if (sourceParameters.containsKey("delimiter")) {
                    reader.option("delimiter", sourceParameters.get("delimiter").toString());
                }
                if (sourceParameters.containsKey("quote")) {
                    reader.option("quote", sourceParameters.get("quote").toString());
                }
            }
        } else {
            reader.option("inferSchema", "true");
            logger.logInfo("No schema provided. Inferring schema.");
        }
    }

    private static Map<String, Object> getSchemaConfigFromYaml(String schemaPath) throws Exception {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = Files.newInputStream(Paths.get(schemaPath))) {
            return yaml.load(inputStream);
        }
    }

    private static StructType getSchemaFromConfig(Map<String, Object> schemaConfig) {
        List<Map<String, Object>> attributes = (List<Map<String, Object>>) schemaConfig.get("attributes");

        StructField[] fields = attributes.stream()
                .map(attr -> DataTypes.createStructField(
                        attr.get("name").toString(),
                        getDataType(attr.get("type").toString()),
                        Boolean.parseBoolean(attr.get("nullable").toString())))
                .toArray(StructField[]::new);

        // Adding the ColumnNameOfCorruptField column
        StructField corruptField = DataTypes.createStructField(
                "corrupt_record", DataTypes.StringType, true);

        return new StructType(Stream.concat(Arrays.stream(fields), Stream.of(corruptField))
                .toArray(StructField[]::new));
    }

    private static DataType getDataType(String type) {
        switch (type.toLowerCase()) {
            case "tinyint": return DataTypes.ByteType;
            case "smallint": return DataTypes.ShortType;
            case "integer": return DataTypes.IntegerType;
            case "bigint": return DataTypes.LongType;
            case "float": return DataTypes.FloatType;
            case "double": return DataTypes.DoubleType;
            case "boolean": return DataTypes.BooleanType;
            case "long": return DataTypes.LongType;
            case "string": return DataTypes.StringType;
            case "char": return DataTypes.StringType;
            case "varchar": return DataTypes.StringType;
            case "date": return DataTypes.DateType;
            case "timestamp": return DataTypes.TimestampType;
            case "binary": return DataTypes.BinaryType;
            case "decimal": return DataTypes.createDecimalType(38, 10);
            case "datetime": return DataTypes.DateType;
            case "null": return DataTypes.NullType;
            case "array": return DataTypes.createArrayType(DataTypes.StringType, true);
            default: return DataTypes.StringType;
        }
    }

    public static Dataset<Row> applyLimit(Integer limit, Dataset<Row> dataFrame) {
        if (limit != null && limit > 0) {
            dataFrame = dataFrame.limit(limit);
        }
        return dataFrame;
    }

    public static Dataset<Row> filterRows(String rowFilter, Dataset<Row> dataFrame) {
        if (StringUtils.isNotEmpty(rowFilter)) {
            dataFrame = dataFrame.where(rowFilter);
        }
        return dataFrame;
    }

    public static Dataset<Row> filterColumns(String columnFilter, Dataset<Row> dataFrame) {
        if (StringUtils.isNotEmpty(columnFilter)) {
            String[] columnArray = columnFilter.split(",");
            if (columnArray.length > 0) {
                dataFrame = dataFrame.selectExpr(columnArray);
            }
        }
        return dataFrame;
    }

    public static @NotNull Dataset<Row> savePartitionedTable(String outputPath, String format, String savingMode, String partitioningKeys, Dataset<Row> tableDataFrame, String fullTableName, boolean tableExists) throws Json.DataSourceWriteException {
        List<String> partitionKeysList = Arrays.asList(partitioningKeys.replace("\"", "").split(","));
        tableDataFrame = renamePartitionKeysCase(tableDataFrame, partitioningKeys);
        String[] nullOrEmptyColumns = isPartitionKeysNull(tableDataFrame, partitionKeysList.toArray(new String[0]));
        if (nullOrEmptyColumns.length > 0) {
            String nonNullBlankColumns = Arrays.toString(nullOrEmptyColumns);
            throw new Json.DataSourceWriteException("Partition keys contain NULL or empty values: " + nonNullBlankColumns);
        }

        logger.logInfo((tableExists ? "Appending data to" : "Creating and writing data into") + " partitioned table: " + fullTableName);
        saveDataFrame(tableDataFrame, format, outputPath, savingMode, fullTableName, partitionKeysList, tableExists);
        return tableDataFrame;
    }

    public static void saveNonPartitionedTable(String outputPath, String format, String savingMode, Dataset<Row> tableDataFrame, String fullTableName, String compressionFormat, boolean tableExists) {
        logger.logInfo((tableExists ? "Appending data to" : "Creating and writing data into") + " non-partitioned table: " + fullTableName);
        saveDataFrame(tableDataFrame, format, outputPath, savingMode, fullTableName, null, tableExists, compressionFormat);
    }

    private static void saveDataFrame(Dataset<Row> dataFrame, String format, String outputPath, String savingMode, String tableName, List<String> partitioningKeys, boolean tableExists) {
        saveDataFrame(dataFrame, format, outputPath, savingMode, tableName, partitioningKeys, tableExists, null);
    }

    private static void saveDataFrame(Dataset<Row> dataFrame, String format, String outputPath, String savingMode, String tableName, List<String> partitioningKeys, boolean tableExists, String compressionFormat) {
        DataFrameWriter<Row> writer = dataFrame.write()
                .mode(SaveMode.valueOf(savingMode))
                .option("path", outputPath);

        if (compressionFormat != null) {
            writer.option("compression", compressionFormat);
        }

        if (partitioningKeys != null) {
            writer.partitionBy(partitioningKeys.toArray(new String[0]));
        }

        if (tableExists) {
            writer.json(outputPath);
        } else {
            writer.format(format).saveAsTable(tableName);
        }
    }

    public static Dataset<Row> processDeduplication(Dataset<Row> dataFrame, String duplicationKeys) {
        if (StringUtils.isNotBlank(duplicationKeys)) {
            logger.logInfo("Executing Deduplication");
            long beforeCount = dataFrame.count();
            dataFrame = DropDuplicatesOnKey(duplicationKeys, dataFrame);
            logger.logInfo(String.format("Deduplication completed. Before: %d, After: %d", beforeCount, dataFrame.count()));
        }
        return dataFrame;
    }

    public static Dataset<Row> processSorting(Dataset<Row> dataFrame, String sortingKeys) {
        if (StringUtils.isNotBlank(sortingKeys)) {
            logger.logInfo("Executing Sorting");
            return sortDataFrame(dataFrame, sortingKeys, true);
        }
        return dataFrame;
    }

    public static Dataset<Row> processExtraColumns(Dataset<Row> dataFrame, String extraColumns, String extraColumnsValues) {
        if (StringUtils.isNotBlank(extraColumns)) {
            logger.logInfo("Appending Extra Columns");
            return mergeColumnsToDataFrame(dataFrame, extraColumns, extraColumnsValues);
        }
        return dataFrame;
    }

    public static String getCompressionFormat(String compressionFormat, String defaultCompressionFormat) {
        return Optional.ofNullable(compressionFormat)
                .filter(StringUtils::isNotBlank)
                .map(format -> format.toLowerCase(Locale.ROOT))
                .orElse(defaultCompressionFormat);
    }



}

