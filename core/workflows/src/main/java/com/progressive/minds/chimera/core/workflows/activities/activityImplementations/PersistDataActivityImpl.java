package com.progressive.minds.chimera.core.workflows.activities.activityImplementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progressive.minds.chimera.core.dataSource.formats.jdbc.jdbc;
import com.progressive.minds.chimera.core.dataSource.sourceTypes.JDBCWriter;
import com.progressive.minds.chimera.core.workflows.activities.PersistDataActivity;
import com.progressive.minds.chimera.core.workflows.activities.SharedSparkSession;
import com.progressive.minds.chimera.dto.PersistMetadata;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.Map;

public class PersistDataActivityImpl implements PersistDataActivity {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(jdbc.class);

    @Override
    public void persistData(PersistMetadata config) {
        String sinkType = config.getSinkSubType();

        switch (sinkType.toLowerCase()) {
            case "postgres", "mysql" -> {
                Map<String, String> params = prepareRelationalDBWriteParameters(config);
                SparkSession spark = SharedSparkSession.getSparkSession();
                System.out.println("In Persist");
                Dataset<Row> df = spark.sql(config.getTargetSql());
                df.show();
                if (df != null) {
                    boolean persistStatus = writeToJDBC(params, df);
                    System.out.println("Persist Status : " + persistStatus);


                }
            }
            default -> logger.logWarning("Unsupported extract source: " + config.getSinkType() + "." + sinkType);
        }
    }

    private Map<String, String> prepareRelationalDBWriteParameters(PersistMetadata metadata) {
        Map<String, String> param = new HashMap<>();
        param.put("sourceType", metadata.getSinkSubType());
        param.put("tableName", metadata.getTableName());
        param.put("databaseName", metadata.getDatabaseName());
        param.put("schemaName", metadata.getSchemaName());
        param.put("sqlQuery", metadata.getTargetSql());
        param.put("jdbcurl", metadata.getDataSourceConnection().getConnectionMetadata());

        // Extract credentials if available
        extractCredentials(metadata.getDataSourceConnection().getAuthenticationData(), param);
        logger.logInfo("Ready to extarct from Postgres Table with the paramters - " + param);

        return param;
    }

  private void extractCredentials(String input, Map<String, String> param) {
        if (input == null || input.isEmpty()) {
            logger.logWarning("No authentication data provided.");
            return;
        }

        try {
            // Extract JSON part after "authenticationData":
            System.out.println("inputJson - " + input);
            // Parse JSON using Jackson
            JsonNode rootNode = new ObjectMapper().readTree(input);
            param.put("userName", rootNode.path("username").asText());
            param.put("password", rootNode.path("password").asText());
        } catch (Exception e) {
            logger.logError("Error extracting credentials", e);
        }
    }

    private boolean writeToJDBC(Map<String, String> params, Dataset<Row> df) {
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
                    null);
        } catch (Exception e) {
            logger.logError("Error reading data from JDBC", e);
            return false;
        }
    }
}
