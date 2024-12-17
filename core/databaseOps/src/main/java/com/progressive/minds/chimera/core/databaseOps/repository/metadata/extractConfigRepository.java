package com.progressive.minds.chimera.core.databaseOps.repository.metadata;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.exception.DatabaseException;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.extractConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.progressive.minds.chimera.core.databaseOps.utility.ColumnExtractor.extractGetterColumnNames;

public class extractConfigRepository {
    List<String> columnNames = extractGetterColumnNames(extractConfig.class);
    int columnCount = columnNames.size();
    String questionMarks = "?".repeat(columnCount).replace("", ", ").strip().substring(1);

    public List<extractConfig> getAllExtractConfig() {
        List<extractConfig> extractConfigs = new ArrayList<>();
        //      String query = "SELECT " + String.join(", ", columnNames) + " FROM data_sources";
        String query = "SELECT * FROM extract_config";
//TODO: need to set the variables in the model class same as column names in the table.
//TODO : hard coding of the table name should be removed

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                extractConfig ec = mapResultSetToExtractConfig(resultSet);
                extractConfigs.add(ec);
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


    public void putExtractConfig(extractConfig extractConfig) {
        String query = "INSERT INTO extract_config (unique_id, pipeline_name, sequence_number, data_source_type, " +
                "data_source_sub_type, file_name, file_path, schema_path, row_filter, column_filter, extract_dataframe_name," +
                " source_configuration, table_name, schema_name, sql_text, kafka_consumer_topic, kafka_consumer_group, " +
                "kafka_start_offset, data_source_connection_name, created_timestamp, created_by, updated_by, updated_timestamp, active_flag) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, extractConfig.getUniqueId());
            preparedStatement.setString(2, extractConfig.getPipelineName());
            preparedStatement.setInt(3, extractConfig.getSequenceNumber());
            preparedStatement.setString(4, extractConfig.getDataSourceType());
            preparedStatement.setString(5, extractConfig.getDataSourceSubType());
            preparedStatement.setString(6, extractConfig.getFileName());
            preparedStatement.setString(7, extractConfig.getFilePath());
            preparedStatement.setString(8, extractConfig.getSchemaPath());
            preparedStatement.setString(9, extractConfig.getRowFilter());
            preparedStatement.setString(10, extractConfig.getColumnFilter());
            preparedStatement.setString(11, extractConfig.getExtractDataframeName());
            preparedStatement.setString(12, extractConfig.getSourceConfiguration());
            preparedStatement.setString(13, extractConfig.getTableName());
            preparedStatement.setString(14, extractConfig.getSchemaName());
            preparedStatement.setString(15, extractConfig.getSqlText());
            preparedStatement.setString(16, extractConfig.getKafkaConsumerTopic());
            preparedStatement.setString(17, extractConfig.getKafkaConsumerGroup());
            preparedStatement.setString(18, extractConfig.getKafkaStartOffset());
            preparedStatement.setString(19, extractConfig.getDataSourceConnectionName());
            preparedStatement.setTimestamp(20, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(21, extractConfig.getCreatedBy());
            preparedStatement.setString(22, extractConfig.getUpdatedBy());
            preparedStatement.setTimestamp(23, extractConfig.getUpdatedTimestamp());
            preparedStatement.setString(24, extractConfig.getActiveFlag());

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

    public void putExtractConfig(List<extractConfig> extractConfig) {
        if (!extractConfig.isEmpty()) {
            extractConfig.forEach(ec -> putExtractConfig(ec));
        }
    }

    public List<extractConfig> getExtractConfigWithFilters(Map<String, Object> filters) {
        // Base query with no filters
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM extract_config WHERE 1=1");
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
        List<extractConfig> result = new ArrayList<>();
        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameters in the prepared statement
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i)); // Parameter index starts from 1
            }

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    extractConfig dsc = mapResultSetToExtractConfig(resultSet); // Map each row to a dataSources object
                    result.add(dsc);
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

    private extractConfig mapResultSetToExtractConfig(ResultSet resultSet) throws SQLException {
        extractConfig ec = new extractConfig();
        ec.setUniqueId(resultSet.getInt("unique_id"));
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
        ec.setCreatedTimestamp(resultSet.getTimestamp("created_timestamp"));
        ec.setCreatedBy(resultSet.getString("created_by"));
        ec.setUpdatedTimestamp(resultSet.getTimestamp("updated_timestamp"));
        ec.setUpdatedBy(resultSet.getString("updated_by"));
        ec.setActiveFlag(resultSet.getString("active_flag"));

        return ec;

    }

    public int updateExtractConfig(Map<String, Object> updateFields, Map<String, Object> filters) {
        int returnCode = 0;
        if (updateFields == null || updateFields.isEmpty()) {
            throw new IllegalArgumentException("Update fields cannot be null or empty");
        }

        StringBuilder queryBuilder = new StringBuilder("UPDATE extract_config SET ");
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

    public int deleteFromExtractConfig(Map<String, Object> filters) {
        int returnCode = 0;
        StringBuilder queryBuilder = new StringBuilder("DELETE FROM extract_config");

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
