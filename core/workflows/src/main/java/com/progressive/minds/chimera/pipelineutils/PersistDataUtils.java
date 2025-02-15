package com.progressive.minds.chimera.pipelineutils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progressive.minds.chimera.core.dataSource.sourceTypes.JDBCWriter;
import com.progressive.minds.chimera.dto.PersistMetadata;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.Map;

public class PersistDataUtils {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(PersistDataUtils.class);

    public static void persistData (PersistMetadata config) {
        String sinkType = config.getSinkSubType();

        switch (sinkType.toLowerCase()) {
            case "postgres", "mysql" -> {
                Map<String, String> params = prepareRelationalDBWriteParameters(config);
                SparkSession spark = SharedSparkSession.getSparkSession();
                Dataset<Row> df = spark.sql(config.getTargetSql());
                if (df != null) {
                    boolean persistStatus = writeToJDBC(params, df);
                }
            }
            default -> logger.logWarning("Unsupported extract source: " + config.getSinkType() + "." + sinkType);
        }
    }

    public static Map<String, String> prepareRelationalDBWriteParameters(PersistMetadata metadata) {
        Map<String, String> param = new HashMap<>();
        param.put("sourceType", metadata.getSinkSubType());
        param.put("tableName", metadata.getTableName());
        param.put("databaseName", metadata.getDatabaseName());
        param.put("schemaName", metadata.getSchemaName());
        param.put("sqlQuery", metadata.getTargetSql());
        param.put("jdbcurl", metadata.getDataSourceConnection().getConnectionMetadata());

        // Extract credentials if available
        extractCredentials(metadata.getDataSourceConnection().getAuthenticationData(), param);
        logger.logInfo("Ready to extract from Postgres Table with the parameters - " + param);

        return param;
    }

    private static void extractCredentials(String input, Map<String, String> param) {
        if (input == null || input.isEmpty()) {
            logger.logWarning("No authentication data provided.");
            return;
        }

        try {
            JsonNode rootNode = new ObjectMapper().readTree(input);
            param.put("userName", rootNode.path("username").asText());
            param.put("password", rootNode.path("password").asText());
        } catch (Exception e) {
            //TODO : Throw ChimeraException
            logger.logError("Error extracting credentials", e);
        }
    }

    public static boolean writeToJDBC(Map<String, String> params, Dataset<Row> df) {
        try {
            return new JDBCWriter().write(
                    params.get("sourceType"),
                    df,
                    params.get("jdbcurl"),
                    params.get("userName"),
                    params.get("password"),
                    params.get("databaseName"),
                    params.get("tableName"),
                    params.getOrDefault("saveMode", "Append"),
                    //TODO : Add custom Config
                    null);
        } catch (Exception e) {
            //TODO : Throw ChimeraException
            logger.logError("Error reading data from JDBC", e);
            return false;
        }
    }
}
