package com.progressive.minds.chimera.core.databaseOps.repository.metadata;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.exception.DatabaseException;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.transformConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.progressive.minds.chimera.core.databaseOps.utility.ColumnExtractor.extractGetterColumnNames;

public class transformConfigRepository {
    List<String> columnNames = extractGetterColumnNames(transformConfig.class);
    int columnCount = columnNames.size();
    String questionMarks = "?".repeat(columnCount).replace("", ", ").strip().substring(1);

    public List<transformConfig> getAllTransformConfig() {
        List<transformConfig> transformConfig = new ArrayList<>();
        //      String query = "SELECT " + String.join(", ", columnNames) + " FROM data_sources";
        String query = "SELECT * FROM transform_config";
//TODO: need to set the variables in the model class same as column names in the table.
//TODO : hard coding of the table name should be removed

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                transformConfig tc = mapResultSetToTransformConfig(resultSet);
                transformConfig.add(tc);
            }
        } catch (SQLException sqlEx) {
            handleSQLException(sqlEx);
        } catch (Exception ex) {
            // Handle any other exceptions
            String errorMessage = "Unexpected error while saving data sources Connections: " + ex.getMessage();
            throw new DatabaseException(errorMessage, ex);
        }

        return transformConfig;
    }


    public void putTransformConfig(transformConfig transformConfig) {
        String query = "INSERT INTO transform_config (unique_id, pipeline_name, sequence_number, sql_text, " +
                "transform_dataframe_name, created_timestamp, created_by, updated_by, updated_timestamp, active_flag) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, transformConfig.getUniqueId());
            preparedStatement.setString(2, transformConfig.getPipelineName());
            preparedStatement.setInt(3, transformConfig.getSequenceNumber());
            preparedStatement.setString(4, transformConfig.getSqlText());
            preparedStatement.setString(5, transformConfig.getTransformDataframeName());
            preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(7, transformConfig.getCreatedBy());
            preparedStatement.setString(8, transformConfig.getUpdatedBy());
            preparedStatement.setTimestamp(9, transformConfig.getUpdatedTimestamp());
            preparedStatement.setString(10, transformConfig.getActiveFlag());

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


    public void putTransformConfig(List<transformConfig> transformConfig) {
        if (!transformConfig.isEmpty()) {
            transformConfig.forEach(tc -> putTransformConfig(tc));
        }
    }

    public List<transformConfig> getTransformConfigWithFilters(Map<String, Object> filters) {
        // Base query with no filters
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM transform_config WHERE 1=1");
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
        List<transformConfig> result = new ArrayList<>();
        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameters in the prepared statement
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i)); // Parameter index starts from 1
            }

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transformConfig tc = mapResultSetToTransformConfig(resultSet); // Map each row to a dataSources object
                    result.add(tc);
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

    private transformConfig mapResultSetToTransformConfig(ResultSet resultSet) throws SQLException {
        transformConfig tc = new transformConfig();
        tc.setUniqueId(resultSet.getInt("unique_id"));
        tc.setPipelineName(resultSet.getString("pipeline_name"));
        tc.setSequenceNumber(resultSet.getInt("sequence_number"));
        tc.setSqlText(resultSet.getString("sql_text"));
        tc.setTransformDataframeName(resultSet.getString("transform_dataframe_name"));
        tc.setCreatedTimestamp(resultSet.getTimestamp("created_timestamp"));
        tc.setCreatedBy(resultSet.getString("created_by"));
        tc.setUpdatedTimestamp(resultSet.getTimestamp("updated_timestamp"));
        tc.setUpdatedBy(resultSet.getString("updated_by"));
        tc.setActiveFlag(resultSet.getString("active_flag"));

        return tc;

    }

    public int updateTransformConfig(Map<String, Object> updateFields, Map<String, Object> filters) {
        int returnCode =0;
        if (updateFields == null || updateFields.isEmpty()) {
            throw new IllegalArgumentException("Update fields cannot be null or empty");
        }

        StringBuilder queryBuilder = new StringBuilder("UPDATE transform_config SET ");
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
            returnCode = preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException sqlEx) {
            handleSQLException(sqlEx);
        } catch (Exception ex) {
            // Handle any other exceptions
            String errorMessage = "Unexpected error while saving data sources Connections: " + ex.getMessage();
            throw new DatabaseException(errorMessage, ex);
        }
        return returnCode;
    }

    public int deleteFromTransformConfig(Map<String, Object> filters) {
        int returnCode =0;
        StringBuilder queryBuilder = new StringBuilder("DELETE FROM transform_config");

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
            returnCode = preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException sqlEx) {
            handleSQLException(sqlEx);
        } catch (Exception ex) {
            // Handle any other exceptions
            String errorMessage = "Unexpected error while saving data sources Connections: " + ex.getMessage();
            throw new DatabaseException(errorMessage, ex);
        }
        return returnCode;
    }
}
