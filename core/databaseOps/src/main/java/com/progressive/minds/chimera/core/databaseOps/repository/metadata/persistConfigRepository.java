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
        } catch (SQLException sqlEx) {
            handleSQLException(sqlEx);
        } catch (Exception ex) {
            // Handle any other exceptions
            String errorMessage = "Unexpected error while saving data sources Connections: " + ex.getMessage();
            throw new DatabaseException(errorMessage, ex);
        }

        return persistConfigs;
    }


    public void putPersistConfig(persistConfig persistConfig) {
        String query = "INSERT INTO persist_config (pipeline_name, sequence_number, data_sink_type, data_sink_sub_type," +
                " target_database_name, target_table_name, target_schema_name, partition_keys, target_sql_text, target_path," +
                " write_mode, data_source_connection_name, sink_configuration, sort_columns, dedup_columns, " +
                "kafka_topic, kafka_key, kafka_message, created_timestamp, created_by, updated_by, updated_timestamp, active_flag) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, persistConfig.getPipelineName());
            preparedStatement.setInt(2, persistConfig.getSequenceNumber());
            preparedStatement.setString(3, persistConfig.getDataSinkType());
            preparedStatement.setString(4, persistConfig.getDataSinkSubType());
            preparedStatement.setString(5, persistConfig.getTargetDatabaseName());
            preparedStatement.setString(6, persistConfig.getTargetTableName());
            preparedStatement.setString(7, persistConfig.getTargetSchemaName());
            preparedStatement.setString(8, persistConfig.getPartitionKeys());
            preparedStatement.setString(9, persistConfig.getTargetSqlText());
            preparedStatement.setString(10, persistConfig.getTargetPath());
            preparedStatement.setString(11, persistConfig.getWriteMode());
            preparedStatement.setString(12, persistConfig.getDataSourceConnectionName());
            preparedStatement.setString(13, persistConfig.getSinkConfiguration());
            preparedStatement.setString(14, persistConfig.getSortColumns());
            preparedStatement.setString(15, persistConfig.getDedupColumns());
            preparedStatement.setString(16, persistConfig.getKafkaTopic());
            preparedStatement.setString(17, persistConfig.getKafkaKey());
            preparedStatement.setString(18, persistConfig.getKafkaMessage());
            preparedStatement.setTimestamp(19, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(20, persistConfig.getCreatedBy());
            preparedStatement.setString(21, persistConfig.getUpdatedBy());
            preparedStatement.setTimestamp(22, persistConfig.getUpdatedTimestamp());
            preparedStatement.setString(23, persistConfig.getActiveFlag());

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

        } catch (SQLException sqlEx) {
            handleSQLException(sqlEx);
        } catch (Exception ex) {
            // Handle any other exceptions
            String errorMessage = "Unexpected error while saving data sources Connections: " + ex.getMessage();
            throw new DatabaseException(errorMessage, ex);
        }

        return result;


    }

    private persistConfig mapResultSetToPersistConfig(ResultSet resultSet) throws SQLException {
        persistConfig pc = new persistConfig();
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

    public int updatePersistConfig(Map<String, Object> updateFields, Map<String, Object> filters, String updatedBy) {
        int returnCode =0;
        if (updateFields == null || updateFields.isEmpty()) {
            throw new IllegalArgumentException("Update fields cannot be null or empty");
        }

        //add updated_timestamp and updated_by columns in the updateFields Maps
        updateFields.put("updated_timestamp", new Timestamp(System.currentTimeMillis()));
        updateFields.put("updated_by", updatedBy);

        StringBuilder queryBuilder = new StringBuilder("UPDATE persist_config SET ");
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

    public int deleteFromPersistConfig(Map<String, Object> filters) {
        int returnCode =0;
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
