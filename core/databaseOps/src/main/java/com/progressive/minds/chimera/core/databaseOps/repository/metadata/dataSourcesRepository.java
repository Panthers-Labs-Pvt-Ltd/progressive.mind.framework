package com.progressive.minds.chimera.core.databaseOps.repository.metadata;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.exception.DatabaseException;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataSources;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.progressive.minds.chimera.core.databaseOps.utility.ColumnExtractor.extractGetterColumnNames;


public class dataSourcesRepository {
    List<String> columnNames = extractGetterColumnNames(dataSources.class);
    int columnCount = columnNames.size();
    String questionMarks = "?".repeat(columnCount).replace("", ", ").strip().substring(1);

    public List<dataSources> getAllDataSources() {
        List<dataSources> dataSources = new ArrayList<>();
        //      String query = "SELECT " + String.join(", ", columnNames) + " FROM data_sources";
        String query = "SELECT * FROM data_sources";
//TODO: need to set the variables in the model class same as column names in the table.
//TODO : hard coding of the table name should be removed

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                dataSources ds = mapResultSetToDataSource(resultSet);
                dataSources.add(ds);
            }
        } catch (Exception e) {
            throw new DatabaseException("Error fetching dataSources from the database.", e);
        }

        return dataSources;
    }


    public void putDataSources(dataSources dataSource) {
        String query = "INSERT INTO data_sources (data_source_type, data_source_sub_type, description, read_defaults, write_defaults" +
                ", created_timestamp, created_by,  updated_timestamp, updated_by, active_flag) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            connection.setAutoCommit(true);
            preparedStatement.setString(1, dataSource.getDataSourceType());
            preparedStatement.setString(2, dataSource.getDataSourceSubType());
            preparedStatement.setString(3, dataSource.getDescription());
            preparedStatement.setString(4, dataSource.getReadDefaults());
            preparedStatement.setString(5, dataSource.getWriteDefaults());
            preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(7, dataSource.getCreatedBy());
            preparedStatement.setTimestamp(8, dataSource.getUpdatedTimestamp());
            preparedStatement.setString(9, dataSource.getUpdatedBy());
            preparedStatement.setString(10, dataSource.getActiveFlag());
            preparedStatement.executeUpdate();
        } catch (SQLException sqlEx) {
            handleSQLException(sqlEx);
        } catch (Exception ex) {
        // Handle any other exceptions
            String errorMessage = "Unexpected error while saving data source: " + ex.getMessage();
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


    public void putDataSources(List<dataSources> dataSources) {
        if (!dataSources.isEmpty()) {
            dataSources.forEach(ds -> putDataSources(ds));
        }
    }

    public List<dataSources> getDataSourcesWithFilters(Map<String, Object> filters) {
        // Base query with no filters
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM data_sources WHERE 1=1");
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
        List<dataSources> result = new ArrayList<>();
        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameters in the prepared statement
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i)); // Parameter index starts from 1
            }

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    dataSources ds = mapResultSetToDataSource(resultSet); // Map each row to a dataSources object
                    result.add(ds);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error executing dynamic SELECT query", e);
        }

        return result;


    }

    private dataSources mapResultSetToDataSource(ResultSet resultSet) throws SQLException {
        dataSources dataSource = new dataSources();
        dataSource.setDataSourceType(resultSet.getString("data_source_type"));
        dataSource.setDataSourceSubType(resultSet.getString("data_source_sub_type"));
        dataSource.setDescription(resultSet.getString("description"));
        dataSource.setReadDefaults(resultSet.getString("read_defaults"));
        dataSource.setWriteDefaults(resultSet.getString("write_defaults"));
        dataSource.setCreatedTimestamp(resultSet.getTimestamp("created_timestamp"));
        dataSource.setCreatedBy(resultSet.getString("created_by"));
        dataSource.setUpdatedTimestamp(resultSet.getTimestamp("updated_timestamp"));
        dataSource.setUpdatedBy(resultSet.getString("updated_by"));
        dataSource.setActiveFlag(resultSet.getString("active_flag"));
        return dataSource;

    }

    public int updateDataSources(Map<String, Object> updateFields, Map<String, Object> filters) {
        if (updateFields == null || updateFields.isEmpty()) {
            throw new IllegalArgumentException("Update fields cannot be null or empty");
        }

        StringBuilder queryBuilder = new StringBuilder("UPDATE data_sources SET ");
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

    public int deleteFromDataSources(Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder("DELETE FROM data_sources");

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


