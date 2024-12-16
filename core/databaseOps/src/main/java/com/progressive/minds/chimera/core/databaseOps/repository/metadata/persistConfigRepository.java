package com.progressive.minds.chimera.core.databaseOps.repository.metadata;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.exception.DatabaseException;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.persistConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.progressive.minds.chimera.core.databaseOps.utility.ColumnExtractor.extractGetterColumnNames;

public class persistConfigRepository {
    List<String> columnNames = extractGetterColumnNames(persistConfig.class);
    int columnCount = columnNames.size();
    String questionMarks = "?".repeat(columnCount).replace("", ", ").strip().substring(1);

    public List<persistConfig> getAllPersistConfig() {
        List<persistConfig> persistConfigs = new ArrayList<>();
        //      String query = "SELECT " + String.join(", ", columnNames) + " FROM data_sources";
        String query = "SELECT * FROM persist_config";
//TODO: need to set the variables in the model class same as column names in the table.
//TODO : hard coding of the table name should be removed

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                persistConfig pc = mapResultSetToPersistConfig(resultSet);
                persistConfigs.add(pc);
            }
        } catch (Exception e) {
            throw new DatabaseException("Error fetching dataSources from the database.", e);
        }

        return persistConfigs;
    }


    public void putPersistConfig(persistConfig persistConfig) {
        String query = "INSERT INTO persist_config (unique_id, pipeline_name, sequence_number, data_sink_type, data_sink_sub_type," +
                " target_database_name, target_table_name, target_schema_name, partition_keys, target_sql_text, target_path," +
                " write_mode, data_source_connection_name, sink_configuration, sort_columns, dedup_columns, " +
                "kafka_topic, kafka_key, kafka_message, created_by, active_flag) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, persistConfig.getUniqueId());
            preparedStatement.setString(2, persistConfig.getPipelineName());
            preparedStatement.setInt(3, persistConfig.getSequenceNumber());
            preparedStatement.setString(4, persistConfig.getDataSinkType());
            preparedStatement.setString(5, persistConfig.getDataSinkSubType());
            preparedStatement.setString(6, persistConfig.getTargetDatabaseName());
            preparedStatement.setString(7, persistConfig.getTargetTableName());
            preparedStatement.setString(8, persistConfig.getTargetSchemaName());
            preparedStatement.setString(9, persistConfig.getPartitionKeys());
            preparedStatement.setString(10, persistConfig.getTargetSqlText());
            preparedStatement.setString(11, persistConfig.getTargetPath());
            preparedStatement.setString(12, persistConfig.getWriteMode());
            preparedStatement.setString(13, persistConfig.getDataSourceConnectionName());
            preparedStatement.setString(14, persistConfig.getSinkConfiguration());
            preparedStatement.setString(15, persistConfig.getSortColumns());
            preparedStatement.setString(16, persistConfig.getDedupColumns());
            preparedStatement.setString(17, persistConfig.getKafkaTopic());
            preparedStatement.setString(18, persistConfig.getKafkaKey());
            preparedStatement.setString(19, persistConfig.getKafkaMessage());
            preparedStatement.setString(20, persistConfig.getCreatedBy());
            preparedStatement.setString(21, persistConfig.getActiveFlag());

            int rowsInserted = preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("rowsInserted : " + rowsInserted);
        } catch (Exception e) {
            throw new DatabaseException("Error saving user to the database.", e);
        }
    }

    public void putPersistConfig(List<persistConfig> persistConfig) {
        if (!persistConfig.isEmpty()) {
            persistConfig.forEach(pc -> putPersistConfig(pc));
        }
    }

    public List<persistConfig> getPersistConfigWithFilters(Map<String, Object> filters) {
        // Base query with no filters
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM persist_config WHERE 1=1");
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
        List<persistConfig> result = new ArrayList<>();
        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameters in the prepared statement
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i)); // Parameter index starts from 1
            }

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    persistConfig pc = mapResultSetToPersistConfig(resultSet); // Map each row to a dataSources object
                    result.add(pc);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error executing dynamic SELECT query", e);
        }

        return result;


    }

    private persistConfig mapResultSetToPersistConfig(ResultSet resultSet) throws SQLException {
        persistConfig pc = new persistConfig();
        pc.setUniqueId(resultSet.getInt("unique_id"));
        pc.setPipelineName(resultSet.getString("pipeline_name"));
        pc.setSequenceNumber(resultSet.getInt("sequence_number"));
        pc.setDataSinkType(resultSet.getString("data_sink_type"));
        pc.setDataSinkSubType(resultSet.getString("data_sink_sub_type"));
        pc.setTargetDatabaseName(resultSet.getString("target_database_name"));
        pc.setTargetTableName(resultSet.getString("target_table_name"));
        pc.setTargetSchemaName(resultSet.getString("target_schema_name"));
        pc.setPartitionKeys(resultSet.getString("partition_keys"));
        pc.setTargetSqlText(resultSet.getString("target_sql_text"));
        pc.setTargetPath(resultSet.getString("target_path"));
        pc.setWriteMode(resultSet.getString("write_mode"));
        pc.setDataSourceConnectionName(resultSet.getString("data_source_connection_name"));
        pc.setSinkConfiguration(resultSet.getString("sink_configuration"));
        pc.setSortColumns(resultSet.getString("sort_columns"));
        pc.setDedupColumns(resultSet.getString("dedup_columns"));
        pc.setKafkaTopic(resultSet.getString("kafka_topic"));
        pc.setKafkaKey(resultSet.getString("kafka_key"));
        pc.setKafkaMessage(resultSet.getString("kafka_message"));
        pc.setCreatedTimestamp(resultSet.getTimestamp("created_timestamp"));
        pc.setCreatedBy(resultSet.getString("created_by"));
        pc.setUpdatedTimestamp(resultSet.getTimestamp("updated_timestamp"));
        pc.setUpdatedBy(resultSet.getString("updated_by"));
        pc.setActiveFlag(resultSet.getString("active_flag"));

        return pc;

    }

    public int updatePersistConfig(Map<String, Object> updateFields, Map<String, Object> filters) {
        if (updateFields == null || updateFields.isEmpty()) {
            throw new IllegalArgumentException("Update fields cannot be null or empty");
        }

        StringBuilder queryBuilder = new StringBuilder("UPDATE persist_config SET ");
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

    public int deleteFromPersistConfig(Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder("DELETE FROM Persist_config");

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
