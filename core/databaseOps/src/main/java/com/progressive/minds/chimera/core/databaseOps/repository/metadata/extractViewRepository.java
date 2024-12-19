package com.progressive.minds.chimera.core.databaseOps.repository.metadata;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.exception.DatabaseException;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.extractView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.progressive.minds.chimera.core.databaseOps.utility.ColumnExtractor.extractGetterColumnNames;

public class extractViewRepository {
//    List<String> columnNames = extractGetterColumnNames(extractConfig.class);
//    int columnCount = columnNames.size();
//    String questionMarks = "?".repeat(columnCount).replace("", ", ").strip().substring(1);

    public List<extractView> getAllExtractDetails() {
        List<extractView> extractConfigs = new ArrayList<>();
        //      String query = "SELECT " + String.join(", ", columnNames) + " FROM data_sources";
        String query = "SELECT * FROM extract_view";
//TODO: need to set the variables in the model class same as column names in the table.
//TODO : hard coding of the table name should be removed

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                extractView ev = mapResultSetToExtractDetails(resultSet);
                extractConfigs.add(ev);
            }
        } catch (SQLException sqlEx) {
            handleSQLException(sqlEx);
        } catch (Exception ex) {
            // Handle any other exceptions
            String errorMessage = "Unexpected error while saving data sources Connections: " + ex.getMessage();
            throw new DatabaseException(errorMessage, ex);
        }
        return extractConfigs;
    }



    private void handleSQLException(SQLException sqlEx) {
        String sqlState = sqlEx.getSQLState();
        int errorCode = sqlEx.getErrorCode();

        if ("08001".equals(sqlState)) {
            // SQL State 08001: Unable to connect to the database
            throw new DatabaseException("Database connection error: " + sqlEx.getMessage(), sqlEx);
        } else if ("23000".equals(sqlState)) {
            // SQL State 23000: Integrity constraint violation
            throw new DatabaseException("Data integrity violation: " + sqlEx.getMessage(), sqlEx);
        } else if ("23503".equals(sqlState)) {
            // SQL State 23505: Foreign Key Violation
            throw new DatabaseException("Foreign Key Violation. Record is missing in Parent Table." + sqlEx.getMessage(), sqlEx);
        } else if ("23505".equals(sqlState)) {
            // SQL State 23505: Duplicate Key Violation
            throw new DatabaseException("A Record with the given key already exists. " + sqlEx.getMessage(), sqlEx);
        } else if ("23514".equals(sqlState)) {
            // SQL State 23514: Duplicate Key Violation
            throw new DatabaseException("Check Constraint is violated. Check the valid Fileds combination. " + sqlEx.getMessage(), sqlEx);
        } else if ("42000".equals(sqlState)) {
            // SQL State 42000: Syntax error or access violation
            throw new DatabaseException("SQL syntax error or access violation: " + sqlEx.getMessage(), sqlEx);
        } else {
            // Default case for unhandled SQL exceptions
            String errorMessage = String.format("Unhandled SQL exception [SQLState: %s, ErrorCode: %d]: %s",
                    sqlState, errorCode, sqlEx.getMessage());
            throw new DatabaseException(errorMessage, sqlEx);
        }
    }


    public List<extractView> getExtractDetailsWithFilters(Map<String, Object> filters) {
        // Base query with no filters
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM extract_view WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        // Iterate over the filters map and build the query dynamically
        for (Map.Entry<String, Object> filter : filters.entrySet()) {
            String column = filter.getKey();
            Object value = filter.getValue();

            // Append the filter condition to the query
            queryBuilder.append(" AND ").append(column).append(" = ?");
            parameters.add(value);
        }
        String query = queryBuilder.toString();
        //TODO: Add more filter conditions / Operators

        // Execute the query and return the results
        List<extractView> result = new ArrayList<>();
        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameters in the prepared statement
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i)); // Parameter index starts from 1
            }

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    extractView ev = mapResultSetToExtractDetails(resultSet); // Map each row to a dataSources object
                    result.add(ev);
                }
            }

        } catch (SQLException sqlEx) {
            handleSQLException(sqlEx);
        } catch (Exception ex) {
            // Handle any other exceptions
            String errorMessage = "Unexpected error while saving data sources Connections: " + ex.getMessage();
            throw new DatabaseException(errorMessage, ex);
        }
        return result;


    }

    private extractView mapResultSetToExtractDetails(ResultSet resultSet) throws SQLException {
        extractView ec = new extractView();
        ec.setPipelineName(resultSet.getString("pipeline_name"));
        ec.setSequenceNumber(resultSet.getInt("sequence_number"));
        ec.setDataSourceType(resultSet.getString("data_source_type"));
        ec.setDataSourceSubType(resultSet.getString("data_source_sub_type"));
        ec.setFileName(resultSet.getString("file_name"));
        ec.setFilePath(resultSet.getString("file_path"));
        ec.setSchemaPath(resultSet.getString("schema_path"));
        ec.setRowFilter(resultSet.getString("row_filter"));
        ec.setColumnFilter(resultSet.getString("column_filter"));
        ec.setExtractDataframeName(resultSet.getString("extract_dataframe_name"));
        ec.setSourceConfiguration(resultSet.getString("source_configuration"));
        ec.setTableName(resultSet.getString("table_name"));
        ec.setSchemaName(resultSet.getString("schema_name"));
        ec.setSqlText(resultSet.getString("sql_text"));
        ec.setKafkaConsumerTopic(resultSet.getString("kafka_consumer_topic"));
        ec.setKafkaConsumerGroup(resultSet.getString("kafka_consumer_group"));
        ec.setKafkaStartOffset(resultSet.getString("kafka_start_offset"));
        ec.setDataSourceConnectionName(resultSet.getString("data_source_connection_name"));
        ec.setReadDefaults((resultSet.getString("read_defaults")));
        ec.setWriteDefaults((resultSet.getString("write_defaults")));
        ec.setHost(resultSet.getString("host"));
        ec.setPort(resultSet.getInt("Port"));
        ec.setConnectionDatabaseName(resultSet.getString("connection_database_name"));
        ec.setConnectionSchemaName(resultSet.getString("connection_schema_name"));
        ec.setAuthenticationType(resultSet.getString("authentication_type"));
        ec.setUserName(resultSet.getString("user_name"));
        ec.setUserPassword(resultSet.getString("user_password"));
        ec.setCloudProvider(resultSet.getString("cloud_provider"));
        ec.setSecretName(resultSet.getString("secret_name"));
        ec.setGcpProjectId(resultSet.getString("gcp_project_id"));
        ec.setAzureKeyVaultUrl(resultSet.getString("azure_key_vault_url"));
        ec.setRole(resultSet.getString("role"));
        ec.setWarehouse(resultSet.getString("warehouse"));
        ec.setPrincipal(resultSet.getString("principal"));
        ec.setKeytab(resultSet.getString("keytab"));
        ec.setSslCert(resultSet.getString("sslcert"));
        ec.setSslKey(resultSet.getString("sslkey"));
        ec.setSslRootCert(resultSet.getString("sslrootcert"));
        ec.setToken(resultSet.getString("token"));
        ec.setKafkaBroker(resultSet.getString("kafka_broker"));
        ec.setKafkaKeystoreType(resultSet.getString("kafka_keystore_type"));
        ec.setKafkaKeystoreLocation(resultSet.getString("kafka_keystore_location"));
        ec.setKafkaKeystorePassword(resultSet.getString("kafka_keystore_password"));
        ec.setKafkaTruststoreType(resultSet.getString("kafka_truststore_type"));
        ec.setKafkaTruststoreLocation(resultSet.getString("kafka_truststore_location"));
        ec.setKafkaTruststorePassword(resultSet.getString("kafka_truststore_password"));
        ec.setKafkaKeyPassword(resultSet.getString("kafka_key_password"));


        return ec;

    }

}
