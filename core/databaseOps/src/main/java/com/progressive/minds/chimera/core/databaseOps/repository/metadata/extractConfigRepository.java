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
        } catch (Exception e) {
            throw new DatabaseException("Error fetching dataSources from the database.", e);
        }

        return extractConfigs;
    }


    public void putExtractConfig(extractConfig extractConfig) {
        String query = "INSERT INTO extract_config (unique_id, pipeline_name, sequence_number, data_source_type, " +
                "data_source_sub_type, file_name, file_path, schema_path, row_filter, column_filter, extract_dataframe_name," +
                " source_configuration, table_name, schema_name, sql_text, kafka_consumer_topic, kafka_consumer_group, " +
                "kafka_start_offset, data_source_connection_name, created_by, active_flag) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            preparedStatement.setString(20, extractConfig.getCreatedBy());
            preparedStatement.setString(21, extractConfig.getActiveFlag());

            int rowsInserted = preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("rowsInserted : " + rowsInserted);
        } catch (Exception e) {
            throw new DatabaseException("Error saving user to the database.", e);
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

        } catch (SQLException e) {
            throw new DatabaseException("Error executing dynamic SELECT query", e);
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
            int returnCode = preparedStatement.executeUpdate();
            connection.commit();
            return returnCode;

        } catch (Exception e) {
            throw new DatabaseException("Error updating users in the database.", e);
        }
    }

    public int deleteFromExtractConfig(Map<String, Object> filters) {
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
            int returnCode = preparedStatement.executeUpdate();
            connection.commit();
            return returnCode;

        } catch (Exception e) {
            throw new DatabaseException("Error deleting users from the database.", e);
        }
    }
}
