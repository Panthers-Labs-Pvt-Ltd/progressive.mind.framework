package com.progressive.minds.chimera.core.databaseOps.repository.metadata;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.exception.DatabaseException;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataSourcesConnections;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.progressive.minds.chimera.core.databaseOps.utility.ColumnExtractor.extractGetterColumnNames;

public class dataSourcesConnectionRepository {
    List<String> columnNames = extractGetterColumnNames(dataSourcesConnections.class);
    int columnCount = columnNames.size();
    String questionMarks = "?".repeat(columnCount).replace("", ", ").strip().substring(1);

    public List<dataSourcesConnections> getAllDataSourcesConnections() {
        List<dataSourcesConnections> dataSourcesConnections = new ArrayList<>();
        //      String query = "SELECT " + String.join(", ", columnNames) + " FROM data_sources";
        String query = "SELECT * FROM data_sources_connections";
//TODO: need to set the variables in the model class same as column names in the table.
//TODO : hard coding of the table name should be removed

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                dataSourcesConnections dsc = mapResultSetToDataSourceConnection(resultSet);
                dataSourcesConnections.add(dsc);
            }
        } catch (Exception e) {
            throw new DatabaseException("Error fetching dataSources from the database.", e);
        }

        return dataSourcesConnections;
    }


    public void putDataSourcesConnections(dataSourcesConnections dataSourceConnections) {
        String query = "INSERT INTO data_sources_connections (data_source_connection_name, data_source_type, data_source_sub_type," +
                " host, port, database_name, schema_name, authentication_type, user_name, user_password, role, warehouse," +
                " principal, keytab, sslcert, sslkey, sslrootcert, token, kafka_broker, kafka_keystore_type," +
                " kafka_keystore_location, kafka_keystore_password, kafka_truststore_type, kafka_truststore_location," +
                " kafka_truststore_password, kafka_key_password, created_by, created_timestamp, updated_by, updated_timestamp, active_flag) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, dataSourceConnections.getDataSourceConnectionName());
            preparedStatement.setString(2, dataSourceConnections.getDataSourceType());
            preparedStatement.setString(3, dataSourceConnections.getDataSourceSubType());
            preparedStatement.setString(4, dataSourceConnections.getHost());
            preparedStatement.setInt(5, dataSourceConnections.getPort());
            preparedStatement.setString(6, dataSourceConnections.getDatabaseName());
            preparedStatement.setString(7, dataSourceConnections.getSchemaName());
            preparedStatement.setString(8, dataSourceConnections.getAuthenticationType());
            preparedStatement.setString(9, dataSourceConnections.getUserName());
            preparedStatement.setString(10, dataSourceConnections.getUserPassword());
            preparedStatement.setString(11, dataSourceConnections.getRole());
            preparedStatement.setString(12, dataSourceConnections.getWarehouse());
            preparedStatement.setString(13, dataSourceConnections.getPrincipal());
            preparedStatement.setString(14, dataSourceConnections.getKeytab());
            preparedStatement.setString(15, dataSourceConnections.getSslCert());
            preparedStatement.setString(16, dataSourceConnections.getSslKey());
            preparedStatement.setString(17, dataSourceConnections.getSslRootCert());
            preparedStatement.setString(18, dataSourceConnections.getToken());
            preparedStatement.setString(19, dataSourceConnections.getKafkaBroker());
            preparedStatement.setString(20, dataSourceConnections.getKafkaKeystoreType());
            preparedStatement.setString(21, dataSourceConnections.getKafkaKeystoreLocation());
            preparedStatement.setString(22, dataSourceConnections.getKafkaKeystorePassword());
            preparedStatement.setString(23, dataSourceConnections.getKafkaTruststoreType());
            preparedStatement.setString(24, dataSourceConnections.getKafkaTruststoreLocation());
            preparedStatement.setString(25, dataSourceConnections.getKafkaTruststorePassword());
            preparedStatement.setString(26, dataSourceConnections.getKafkaKeyPassword());
            preparedStatement.setString(27, dataSourceConnections.getCreatedBy());
            preparedStatement.setTimestamp(28, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(29, dataSourceConnections.getUpdatedBy());
            preparedStatement.setTimestamp(30, dataSourceConnections.getUpdatedTimestamp());
            preparedStatement.setString(31, dataSourceConnections.getActiveFlag());
            int rowsInserted = preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("rowsInserted : " + rowsInserted);
        } catch (SQLException sqlEx) {
            handleSQLException(sqlEx);
        } catch (Exception ex) {
            // Handle any other exceptions
            String errorMessage = "Unexpected error while saving data sources Connections: " + ex.getMessage();
            throw new DatabaseException(errorMessage, ex);
        }
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
        }  else if ("23514".equals(sqlState)) {
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

    public void putDataSourcesConnections(List<dataSourcesConnections> dataSourcesConnections) {
        if (!dataSourcesConnections.isEmpty()) {
            dataSourcesConnections.forEach(dsc -> putDataSourcesConnections(dsc));
        }
    }

    public List<dataSourcesConnections> getDataSourcesConnectionsWithFilters(Map<String, Object> filters) {
        // Base query with no filters
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM data_sources_connections WHERE 1=1");
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
        List<dataSourcesConnections> result = new ArrayList<>();
        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameters in the prepared statement
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i)); // Parameter index starts from 1
            }

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    dataSourcesConnections dsc = mapResultSetToDataSourceConnection(resultSet); // Map each row to a dataSources object
                    result.add(dsc);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error executing dynamic SELECT query", e);
        }

        return result;


    }

    private dataSourcesConnections mapResultSetToDataSourceConnection(ResultSet resultSet) throws SQLException {
        dataSourcesConnections dsc = new dataSourcesConnections();
        dsc.setDataSourceConnectionName(resultSet.getString("data_source_connection_name"));
        dsc.setDataSourceType(resultSet.getString("data_source_type"));
        dsc.setDataSourceSubType(resultSet.getString("data_source_sub_type"));
        dsc.setHost(resultSet.getString("host"));
        dsc.setPort(resultSet.getInt("Port"));
        dsc.setDatabaseName(resultSet.getString("database_name"));
        dsc.setSchemaName(resultSet.getString("schema_name"));
        dsc.setAuthenticationType(resultSet.getString("authentication_type"));
        dsc.setUserName(resultSet.getString("user_name"));
        dsc.setUserPassword(resultSet.getString("user_password"));
        dsc.setRole(resultSet.getString("role"));
        dsc.setWarehouse(resultSet.getString("warehouse"));
        dsc.setPrincipal(resultSet.getString("principal"));
        dsc.setKeytab(resultSet.getString("keytab"));
        dsc.setSslCert(resultSet.getString("sslcert"));
        dsc.setSslKey(resultSet.getString("sslkey"));
        dsc.setSslRootCert(resultSet.getString("sslrootcert"));
        dsc.setToken(resultSet.getString("token"));
        dsc.setKafkaBroker(resultSet.getString("kafka_broker"));
        dsc.setKafkaKeystoreType(resultSet.getString("kafka_keystore_type"));
        dsc.setKafkaKeystoreLocation(resultSet.getString("kafka_keystore_location"));
        dsc.setKafkaKeystorePassword(resultSet.getString("kafka_keystore_password"));
        dsc.setKafkaTruststoreType(resultSet.getString("kafka_truststore_type"));
        dsc.setKafkaTruststoreLocation(resultSet.getString("kafka_truststore_location"));
        dsc.setKafkaTruststorePassword(resultSet.getString("kafka_truststore_password"));
        dsc.setKafkaKeyPassword(resultSet.getString("kafka_key_password"));
        dsc.setCreatedTimestamp(resultSet.getTimestamp("created_timestamp"));
        dsc.setCreatedBy(resultSet.getString("created_by"));
        dsc.setUpdatedTimestamp(resultSet.getTimestamp("updated_timestamp"));
        dsc.setUpdatedBy(resultSet.getString("updated_by"));
        dsc.setActiveFlag(resultSet.getString("active_flag"));

        return dsc;

    }

    public int updateDataSourcesConnections(Map<String, Object> updateFields, Map<String, Object> filters) {
        if (updateFields == null || updateFields.isEmpty()) {
            throw new IllegalArgumentException("Update fields cannot be null or empty");
        }

        StringBuilder queryBuilder = new StringBuilder("UPDATE data_sources_connections SET ");
        //TODO: Add updated_timestamp and updatedBy columns
        List<String> updateClauses = new ArrayList<>();

        // Build SET clause
        for (String key : updateFields.keySet()) {
            updateClauses.add(key + " = ?");
        }
        queryBuilder.append(String.join(", ", updateClauses));

        // Add WHERE clause if filters are provided
        if (filters != null && !filters.isEmpty()) {
            queryBuilder.append(" WHERE ");
            List<String> conditions = new ArrayList<>();
            for (String key : filters.keySet()) {
                conditions.add(key + " = ?");
            }
            queryBuilder.append(String.join(" AND ", conditions));
        }

        String query = queryBuilder.toString();

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            int index = 1;

            // Set update field values
            for (Object value : updateFields.values()) {
                preparedStatement.setObject(index++, value);
            }

            // Set filter values
            if (filters != null) {
                for (Object value : filters.values()) {
                    preparedStatement.setObject(index++, value);
                }
            }

            // Execute update and return affected row count
            int returncode = preparedStatement.executeUpdate();
            connection.commit();
            return returncode;

        } catch (Exception e) {
            throw new DatabaseException("Error updating users in the database.", e);
        }
    }

    public int deleteFromDataSourcesConnections(Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder("DELETE FROM data_sources_connections");

        // Add WHERE clause if filters are provided
        if (filters != null && !filters.isEmpty()) {
            queryBuilder.append(" WHERE ");
            List<String> conditions = new ArrayList<>();
            for (String key : filters.keySet()) {
                conditions.add(key + " = ?");
            }
            queryBuilder.append(String.join(" AND ", conditions));
        }

        String query = queryBuilder.toString();

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set filter values
            if (filters != null) {
                int index = 1;
                for (Object value : filters.values()) {
                    preparedStatement.setObject(index++, value);
                }
            }

            // Execute delete and return affected row count
            int returnCode = preparedStatement.executeUpdate();
            connection.commit();
            return returnCode;

        } catch (Exception e) {
            throw new DatabaseException("Error deleting users from the database.", e);
        }
    }
}
