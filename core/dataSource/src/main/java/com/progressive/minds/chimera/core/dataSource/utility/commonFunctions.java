package com.progressive.minds.chimera.core.dataSource.utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Arrays;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;
import static org.apache.spark.sql.functions.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static Dataset<Row> DropDuplicatesOnKey(String dedupColumns, Dataset<Row> sourceDataframe) {
        Dataset<Row> targetDataframe = sourceDataframe;
        if (dedupColumns != null && !dedupColumns.isEmpty()) {
            if (dedupColumns.trim().equals("*")) {
                targetDataframe = sourceDataframe.dropDuplicates();
            } else {
                boolean existFlag = true;
                String[] columns = dedupColumns.split(",");
                for (String e : columns) {
                    if (sourceDataframe.columns().toString().contains(e)) {
                        existFlag = false;
                        // Assuming logger is a logger instance
                        logger.logInfo("[Deduplication]- De Duplication Column Not Exist in DataFrame");
                    }
                }
                if (existFlag) {
                    targetDataframe = sourceDataframe.dropDuplicates(columns);
                }
            }
        }
        return targetDataframe;
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
    public static String[] isPartitionKeysNull(Dataset<Row> inDataFrame, String[] partitionColumns) {
        List<String> nullOrEmptyColumns = new ArrayList<>();

        for (String colName : partitionColumns) {
            Dataset<Row> distinctValues = inDataFrame.select(new Column(colName)).distinct();
            String[] colValues = distinctValues.collectAsList().stream()
                    .map(row -> row.getString(0))
                    .toArray(String[]::new);

            boolean hasNullOrEmpty = false;
            for (String value : colValues) {
                if (value == null || value.trim().isEmpty()) {
                    hasNullOrEmpty = true;
                    break;
                }
            }

            if (hasNullOrEmpty) {
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
    public static Dataset<Row> mergeColumnsToDataFrame(Dataset<Row> tableDataFrame, String colName, String colValue) {
        // Split column names and values into lists
        List<String> colNames = Arrays.asList(colName.split(","));
        List<String> colValues = Arrays.asList(colValue.split(","));

        // Add each column-value pair to the DataFrame
        for (int i = 0; i < colNames.size(); i++) {
            String columnName = colNames.get(i);
            String columnValue = colValues.get(i);
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
}
