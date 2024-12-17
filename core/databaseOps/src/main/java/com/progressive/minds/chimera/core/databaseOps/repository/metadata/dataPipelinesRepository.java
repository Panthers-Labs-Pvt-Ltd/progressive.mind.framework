package com.progressive.minds.chimera.core.databaseOps.repository.metadata;
import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.exception.DatabaseException;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataPipelines;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.progressive.minds.chimera.core.databaseOps.utility.ColumnExtractor.extractGetterColumnNames;

public class dataPipelinesRepository {
    List<String> columnNames = extractGetterColumnNames(dataPipelines.class);
    int columnCount = columnNames.size();
    String questionMarks = "?".repeat(columnCount).replace("", ", ").strip().substring(1);

    public List<dataPipelines> getAllDataPipelines() {
        List<dataPipelines> dataPipelines = new ArrayList<>();
        //      String query = "SELECT " + String.join(", ", columnNames) + " FROM data_sources";
        String query = "SELECT * FROM data_pipelines";
//TODO: need to set the variables in the model class same as column names in the table.
//TODO : hard coding of the table name should be removed

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                dataPipelines dp = mapResultSetToDataPipelines(resultSet);
                dataPipelines.add(dp);
            }
        } catch (SQLException sqlEx) {
            handleSQLException(sqlEx);
        } catch (Exception ex) {
            // Handle any other exceptions
            String errorMessage = "Unexpected error while saving data sources Connections: " + ex.getMessage();
            throw new DatabaseException(errorMessage, ex);
        }
        return dataPipelines;
    }


    public void putDataPipelines(dataPipelines dataPipelines) {
        String query = "INSERT INTO data_pipelines (pipeline_name, pipeline_description, process_mode, run_frequency," +
                " created_by, created_timestamp, updated_by, updated_timestamp, active_flag) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, dataPipelines.getPipelineName());
            preparedStatement.setString(2, dataPipelines.getPipelineDescription());
            preparedStatement.setString(3, dataPipelines.getProcessMode());
            preparedStatement.setString(4, dataPipelines.getRunFrequency());
            preparedStatement.setString(5, dataPipelines.getCreatedBy());
            preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(7, dataPipelines.getUpdatedBy());
            preparedStatement.setTimestamp(8, dataPipelines.getUpdatedTimestamp());
            preparedStatement.setString(9, dataPipelines.getActiveFlag());
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

    public void putDataPipelines(List<dataPipelines> dataPipelines) {
        if (!dataPipelines.isEmpty()) {
            dataPipelines.forEach(dp -> putDataPipelines(dp));
        }
    }

    public List<dataPipelines> getDataPipelinesWithFilters(Map<String, Object> filters) {
        // Base query with no filters
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM data_pipelines WHERE 1=1");
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
        List<dataPipelines> result = new ArrayList<>();
        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameters in the prepared statement
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i)); // Parameter index starts from 1
            }

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    dataPipelines dp = mapResultSetToDataPipelines(resultSet); // Map each row to a dataSources object
                    result.add(dp);
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

    private dataPipelines mapResultSetToDataPipelines(ResultSet resultSet) throws SQLException {
        dataPipelines dp = new dataPipelines();
        dp.setPipelineName(resultSet.getString("pipeline_name"));
        dp.setPipelineDescription(resultSet.getString("pipeline_description"));
        dp.setProcessMode(resultSet.getString("process_mode"));
        dp.setRunFrequency(resultSet.getString("run_frequency"));
        dp.setCreatedTimestamp(resultSet.getTimestamp("created_timestamp"));
        dp.setCreatedBy(resultSet.getString("created_by"));
        dp.setUpdatedTimestamp(resultSet.getTimestamp("updated_timestamp"));
        dp.setUpdatedBy(resultSet.getString("updated_by"));
        dp.setActiveFlag(resultSet.getString("active_flag"));

        return dp;

    }

    public int updateDataPipelines(Map<String, Object> updateFields, Map<String, Object> filters) {
        int returncode = 0;
        if (updateFields == null || updateFields.isEmpty()) {
            throw new IllegalArgumentException("Update fields cannot be null or empty");
        }

        StringBuilder queryBuilder = new StringBuilder("UPDATE data_pipelines SET ");
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
            returncode = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException sqlEx) {
            handleSQLException(sqlEx);
        } catch (Exception ex) {
            // Handle any other exceptions
            String errorMessage = "Unexpected error while saving data sources Connections: " + ex.getMessage();
            throw new DatabaseException(errorMessage, ex);
        }
        return returncode;
    }

    public int deleteFromDataPipelines(Map<String, Object> filters) {
        int returnCode =0;
        StringBuilder queryBuilder = new StringBuilder("DELETE FROM data_pipelines");

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
