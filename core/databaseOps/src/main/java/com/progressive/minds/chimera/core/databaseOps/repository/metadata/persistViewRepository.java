package com.progressive.minds.chimera.core.databaseOps.repository.metadata;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.exception.DatabaseException;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.persistView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.progressive.minds.chimera.core.databaseOps.utility.ColumnExtractor.extractGetterColumnNames;

public class persistViewRepository {
//    List<String> columnNames = extractGetterColumnNames(persistConfig.class);
//    int columnCount = columnNames.size();
//    String questionMarks = "?".repeat(columnCount).replace("", ", ").strip().substring(1);

    public List<persistView> getAllPersistDetails() {
        List<persistView> persistDetails = new ArrayList<>();
        //      String query = "SELECT " + String.join(", ", columnNames) + " FROM data_sources";
        String query = "SELECT * FROM persist_view";
//TODO: need to set the variables in the model class same as column names in the table.
//TODO : hard coding of the table name should be removed

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                persistView pv = mapResultSetToPersistDetails(resultSet);
                persistDetails.add(pv);
            }
        } catch (SQLException sqlEx) {
            handleSQLException(sqlEx);
        } catch (Exception ex) {
            // Handle any other exceptions
            String errorMessage = "Unexpected error while saving data sources Connections: " + ex.getMessage();
            throw new DatabaseException(errorMessage, ex);
        }

        return persistDetails;
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



    public List<persistView> getPersistDetailsWithFilters(Map<String, Object> filters) {
        // Base query with no filters
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM persist_view WHERE 1=1");
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
        List<persistView> result = new ArrayList<>();
        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameters in the prepared statement
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i)); // Parameter index starts from 1
            }

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    persistView pv = mapResultSetToPersistDetails(resultSet); // Map each row to a dataSources object
                    result.add(pv);
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

    private persistView mapResultSetToPersistDetails(ResultSet resultSet) throws SQLException {
        persistView pv = new persistView();
        pv.setPipelineName(resultSet.getString("pipeline_name"));
        pv.setSequenceNumber(resultSet.getInt("sequence_number"));
        pv.setDataSinkType(resultSet.getString("data_sink_type"));
        pv.setDataSinkSubType(resultSet.getString("data_sink_sub_type"));
        pv.setTargetDatabaseName(resultSet.getString("target_database_name"));
        pv.setTargetTableName(resultSet.getString("target_table_name"));
        pv.setTargetSchemaName(resultSet.getString("target_schema_name"));
        pv.setPartitionKeys(resultSet.getString("partition_keys"));
        pv.setTargetSqlText(resultSet.getString("target_sql_text"));
        pv.setTargetPath(resultSet.getString("target_path"));
        pv.setWriteMode(resultSet.getString("write_mode"));
        pv.setDataSourceConnectionName(resultSet.getString("data_source_connection_name"));
        pv.setSinkConfiguration(resultSet.getString("sink_configuration"));
        pv.setSortColumns(resultSet.getString("sort_columns"));
        pv.setDedupColumns(resultSet.getString("dedup_columns"));
        pv.setKafkaTopic(resultSet.getString("kafka_topic"));
        pv.setKafkaKey(resultSet.getString("kafka_key"));
        pv.setKafkaMessage(resultSet.getString("kafka_message"));
        pv.setReadDefaults((resultSet.getString("read_defaults")));
        pv.setWriteDefaults((resultSet.getString("write_defaults")));
        pv.setHost(resultSet.getString("host"));
        pv.setPort(resultSet.getInt("Port"));
        pv.setConnectionDatabaseName(resultSet.getString("connection_database_name"));
        pv.setConnectionSchemaName(resultSet.getString("connection_schema_name"));
        pv.setAuthenticationType(resultSet.getString("authentication_type"));
        pv.setUserName(resultSet.getString("user_name"));
        pv.setUserPassword(resultSet.getString("user_password"));
        pv.setCloudProvider(resultSet.getString("cloud_provider"));
        pv.setSecretName(resultSet.getString("secret_name"));
        pv.setGcpProjectId(resultSet.getString("gcp_project_id"));
        pv.setAzureKeyVaultUrl(resultSet.getString("azure_key_vault_url"));
        pv.setRole(resultSet.getString("role"));
        pv.setWarehouse(resultSet.getString("warehouse"));
        pv.setPrincipal(resultSet.getString("principal"));
        pv.setKeytab(resultSet.getString("keytab"));
        pv.setSslCert(resultSet.getString("sslcert"));
        pv.setSslKey(resultSet.getString("sslkey"));
        pv.setSslRootCert(resultSet.getString("sslrootcert"));
        pv.setToken(resultSet.getString("token"));
        pv.setKafkaBroker(resultSet.getString("kafka_broker"));
        pv.setKafkaKeystoreType(resultSet.getString("kafka_keystore_type"));
        pv.setKafkaKeystoreLocation(resultSet.getString("kafka_keystore_location"));
        pv.setKafkaKeystorePassword(resultSet.getString("kafka_keystore_password"));
        pv.setKafkaTruststoreType(resultSet.getString("kafka_truststore_type"));
        pv.setKafkaTruststoreLocation(resultSet.getString("kafka_truststore_location"));
        pv.setKafkaTruststorePassword(resultSet.getString("kafka_truststore_password"));
        pv.setKafkaKeyPassword(resultSet.getString("kafka_key_password"));


        return pv;

    }


}
