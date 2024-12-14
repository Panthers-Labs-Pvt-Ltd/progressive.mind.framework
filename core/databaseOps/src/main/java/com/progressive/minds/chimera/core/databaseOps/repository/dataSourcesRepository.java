package com.progressive.minds.chimera.core.databaseOps.repository;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.exception.DatabaseException;
import com.progressive.minds.chimera.core.databaseOps.model.dataSources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
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
                dataSources.add(dataSource);
            }
        } catch (Exception e) {
            throw new DatabaseException("Error fetching dataSources from the database.", e);
        }

        return dataSources;
    }

    /*

    public void saveUser(User user) {
        String query = "INSERT INTO users (" + columnNames + ") VALUES (" + questionMarks + ")";

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DatabaseException("Error saving user to the database.", e);
        }
    }

    public List<User> getAllUsers(Map<String, Object> filters) {
        List<User> users = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT " + columnNames + " FROM users");

        // Add filters dynamically
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

            // Set filter values dynamically
            if (filters != null && !filters.isEmpty()) {
                int index = 1;
                for (Object value : filters.values()) {
                    preparedStatement.setObject(index++, value);
                }
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setEmail(resultSet.getString("email"));
                    users.add(user);
                }
            }

        } catch (Exception e) {
            throw new DatabaseException("Error fetching users from the database.", e);
        }

        return users;
    }
    public int updateUsers(Map<String, Object> updateFields, Map<String, Object> filters) {
        if (updateFields == null || updateFields.isEmpty()) {
            throw new IllegalArgumentException("Update fields cannot be null or empty");
        }

        StringBuilder queryBuilder = new StringBuilder("UPDATE users SET ");
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
            return preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new DatabaseException("Error updating users in the database.", e);
        }
    }
    public int deleteUsers(Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder("DELETE FROM users");

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
            return preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new DatabaseException("Error deleting users from the database.", e);
        }
    }

*/
}
