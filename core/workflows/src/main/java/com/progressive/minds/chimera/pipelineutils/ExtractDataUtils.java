package com.progressive.minds.chimera.pipelineutils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progressive.minds.chimera.core.dataSource.sourceTypes.JDBCReader;
import com.progressive.minds.chimera.core.api_service.dto.ExtractMetadata;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExtractDataUtils {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(ExtractDataUtils.class);

    public static void getSourceType(ExtractMetadata config) {
        String sourceType = config.getExtractSourceSubType().toLowerCase();

        switch (sourceType) {
            case "postgres", "mysql" -> {
                Map<String, String> params = prepareRelationalDBReadParameters(config);
                SparkSession spark = SharedSparkSession.getSparkSession();
                Dataset<Row> DF = readFromJDBC(params, spark);
                if (DF != null) {
                    DF.createOrReplaceTempView(config.getDataframeName());
                }
            }
            default -> logger.logWarning("Unsupported extract source: " + config.getExtractSourceType() + "." + sourceType);
        }
    }

    public static Map<String, String> prepareRelationalDBReadParameters(ExtractMetadata metadata) {
        Map<String, String> param = new HashMap<>();
        param.put("sourceType", metadata.getExtractSourceSubType());
        param.put("tableName", metadata.getRelationalMetadata().getTableName());
        param.put("databaseName", metadata.getRelationalMetadata().getDatabaseName());
        param.put("schemaName", metadata.getRelationalMetadata().getSchemaName());

        String sqlQuery = Optional.ofNullable(metadata.getRelationalMetadata().getSqlText())
                .filter(query -> !query.isEmpty())
                .orElse("SELECT * FROM " + getSchemaPrefix(metadata) + metadata.getRelationalMetadata().getTableName());
        param.put("sqlQuery", sqlQuery);

        param.put("jdbcurl", metadata.getDataSourceConnection().getConnectionMetadata());

        // Extract credentials if available
        extractCredentials(metadata.getDataSourceConnection().getAuthenticationData(), param);
        return param;
    }

    private static String getSchemaPrefix(ExtractMetadata metadata) {
        String schema = metadata.getRelationalMetadata().getSchemaName();
        return (schema != null && !schema.isEmpty()) ? schema + "." : "";
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

    public static Dataset<Row> readFromJDBC(Map<String, String> params, SparkSession spark) {
        try {
            return new JDBCReader().read(
                    params.get("sourceType"),
                    spark,
                    params.get("sqlQuery"),
                    params.get("jdbcurl"),
                    params.get("userName"),
                    params.get("password"),
                    //TODO : Add custom config
                    null
            );
        } catch (Exception e) {
            //TODO : Throw ChimeraException
            logger.logError("Error reading data from JDBC", e);
            return null;
        }
    }
}
