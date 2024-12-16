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
        } catch (Exception e) {
            throw new DatabaseException("Error fetching dataSources from the database.", e);
        }

        return transformConfig;
    }


    public void putTransformConfig(transformConfig transformConfig) {
        String query = "INSERT INTO transform_config (unique_id, pipeline_name, sequence_number, sql_text, " +
                "transform_dataframe_name, created_by, active_flag) " +
                "values (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, transformConfig.getUniqueId());
            preparedStatement.setString(2, transformConfig.getPipelineName());
            preparedStatement.setInt(3, transformConfig.getSequenceNumber());
            preparedStatement.setString(4, transformConfig.getSqlText());
            preparedStatement.setString(5, transformConfig.getTransformDataframeName());
            preparedStatement.setString(6, transformConfig.getCreatedBy());
            preparedStatement.setString(7, transformConfig.getActiveFlag());

            int rowsInserted = preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("rowsInserted : " + rowsInserted);
        } catch (Exception e) {
            throw new DatabaseException("Error saving user to the database.", e);
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

        } catch (SQLException e) {
            throw new DatabaseException("Error executing dynamic SELECT query", e);
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
            int returnCode = preparedStatement.executeUpdate();
            connection.commit();
            return returnCode;

        } catch (Exception e) {
            throw new DatabaseException("Error updating users in the database.", e);
        }
    }

    public int deleteFromTransformConfig(Map<String, Object> filters) {
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
            int returnCode = preparedStatement.executeUpdate();
            connection.commit();
            return returnCode;

        } catch (Exception e) {
            throw new DatabaseException("Error deleting users from the database.", e);
        }
    }
}
