package com.progressive.minds.chimera.core.databaseOps.utility;

import com.progressive.minds.chimera.core.databaseOps.annotation.Column;
import com.progressive.minds.chimera.core.databaseOps.annotation.Id;
import com.progressive.minds.chimera.core.databaseOps.annotation.Table;
import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.exception.DatabaseException;
import java.sql.PreparedStatement;
import java.util.List;
import java.lang.reflect.Field;
import java.sql.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public class RepositoryHelper {

    public static String extractTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) { // Check if the @Table annotation is present
            // Retrieve the annotation
            Table tableAnnotation = clazz.getAnnotation(Table.class);

            // Return the table name
            return tableAnnotation.name();
        } else {
            System.out.println("No @Table annotation found. Using default class name.");
        }

        // Default to the class name in lowercase (or snake_case if required)
        return clazz.getSimpleName().toLowerCase(); // Example: "DataPipelines" -> "data_pipelines";
    }

    public static String extractColumnNames(Class<?> clazz) {
        StringBuilder columnNames = new StringBuilder();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                columnNames.append(columnAnnotation.name()).append(", ");
            }
        }
        // Remove the trailing comma and space, if present
        if (columnNames.length() > 0) {
            columnNames.setLength(columnNames.length() - 2);
        }
        return columnNames.toString();
    }

    public static String getSQLQuery(Class<?> clazz) {
        String TableName = extractTableName(clazz);
        String Fields = extractColumnNames(clazz);
        int totalFields = Fields.length() - Fields.replace(",", "").length();
        totalFields = totalFields +1;
        String QuestionMarks = "?,".repeat(totalFields);
        if (QuestionMarks.endsWith(",")) {
            QuestionMarks = QuestionMarks.substring(0, QuestionMarks.length() - 1);
        }
        return format("INSERT INTO %s ( %s) Values (%s)", TableName, Fields, QuestionMarks);
    }

    public static Integer executeOrUpdateStatements(Class<?> clazz, Object temp) throws SQLException {
        Field[] fields = clazz.getDeclaredFields();
        String SQLQuery = getSQLQuery(clazz);
        String errorMessage = "";
        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery)) {

            // Loop through all fields and set corresponding values in the PreparedStatement
            int parameterIndex = 1;
            for (Field field : fields) {
                field.setAccessible(true); // Allow access to private fields
                Object value = field.get(temp); // Get the field value from dataPipelines object

                if (value != null) {
                    if (value instanceof String) {
                        preparedStatement.setString(parameterIndex, (String) value);
                    } else if (value instanceof Integer) {
                        preparedStatement.setInt(parameterIndex, (Integer) value);
                    } else if (value instanceof Timestamp) {
                        preparedStatement.setTimestamp(parameterIndex, (Timestamp) value);
                    } else if (value instanceof Boolean) {
                        preparedStatement.setBoolean(parameterIndex, (Boolean) value);
                    } else {
                        preparedStatement.setObject(parameterIndex, value); // Default for other types
                    }
                } else {
                    preparedStatement.setNull(parameterIndex, java.sql.Types.NULL); // Set null if the field value is null
                }
                parameterIndex++;
            }
            // Execute the insert operation
            int rowsInserted = preparedStatement.executeUpdate();

            connection.commit();
           return rowsInserted;

        } catch (SQLException sqlEx) {
            errorMessage = "Unexpected error while saving data pipelines: " + sqlEx.getMessage();
            throw new SQLException(errorMessage, sqlEx);
        } catch (Exception ex) {
            errorMessage = "Unexpected error while saving data pipelines: " + ex.getMessage();
            throw new DatabaseException(errorMessage, ex);
        }
    }



    public static <T> Integer executeSelect(Class<T> clazz, List<T> resultList) {
        String tableName = extractTableName(clazz);
        String columnNames = extractColumnNames(clazz);
        String query = String.format("SELECT %s FROM %s", columnNames, tableName);

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                T temp = clazz.getDeclaredConstructor().newInstance();

                // Populate the fields of the object
                populateFields(temp, resultSet);

                // Add the object to the result list
                resultList.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return the number of records processed
        return resultList.size();
    }

    private static void populateFields(Object obj, ResultSet resultSet) throws Exception {
        Class<?> clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            if (field.isAnnotationPresent(Column.class)) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                fieldName = columnAnnotation.name();
                System.out.println("Field " + field.getName() + " is annotated with " + fieldName);
            }
            try {
                Method setter = clazz.getMethod(setterName, field.getType());

                // Determine value from ResultSet based on field type
                Object value = null;
                if (field.getType() == String.class) {
                    value = resultSet.getString(fieldName);
                } else if (field.getType() == Timestamp.class) {
                    value = resultSet.getTimestamp(fieldName);
                } else if (field.getType() == int.class || field.getType() == Integer.class) {
                    value = resultSet.getInt(fieldName);
                } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                    value = resultSet.getBoolean(fieldName);
                }
                // Add other type mappings as needed

                if (value != null) {
                    setter.invoke(obj, value); // Dynamically invoke the setter

                }
            } catch (NoSuchMethodException | SQLException e) {
                // Skip fields that do not have corresponding setters or columns
                System.out.println("Could not map field: " + fieldName + " - " + e.getMessage());
            }
        }
    }
    public static <T> List<T> executeSelect(Class<T> clazz, Map<String, Object> filters) {
        // Extract table name based on the class
        String tableName = extractTableName(clazz);

        // Build the query dynamically based on filters
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ").append(tableName).append(" WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        for (Map.Entry<String, Object> filter : filters.entrySet()) {
            String column = filter.getKey();
            Object value = filter.getValue();

            // Append the filter condition to the query
            queryBuilder.append(" AND ").append(column).append(" = ?");
            parameters.add(value);
        }
        String query = queryBuilder.toString();

        // Execute the query and return the results
        List<T> resultList = new ArrayList<>();
        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameters in the prepared statement
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i)); // Parameter index starts from 1
            }

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Create a new instance of T dynamically
                    T obj = clazz.getDeclaredConstructor().newInstance();

                    // Populate fields of the object
                    populateFields(obj, resultSet);

                    // Add the object to the result list
                    resultList.add(obj);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unexpected error while executing query with filters: " + ex.getMessage(), ex);
        }

        return resultList;
    }
    public static <T> int executeUpdate(T obj) throws SQLException, IllegalAccessException {
        Class<?> clazz = obj.getClass();

        Field[] fields = clazz.getDeclaredFields();
        String tableName = extractTableName(clazz); // Use your method to get the table name.

        StringBuilder setClause = new StringBuilder("SET ");
        Map<Integer, Object> parameters = new HashMap<>();
        int index = 1;

        // For the fields to update (SET clause)
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);

            if (value != null && !field.isAnnotationPresent(Id.class)) { // Skip @Id fields in the SET clause
                String fieldName = field.getName();
                Column columnAnnotation = field.getAnnotation(Column.class);
                String columnName = (columnAnnotation != null) ? columnAnnotation.name() : fieldName;
                setClause.append(columnName).append(" = ?, ");

                parameters.put(index++, value);
            }
        }

        // Remove the trailing comma and space
        setClause.setLength(setClause.length() - 2);

        // Build the WHERE clause based on @Id fields
        StringBuilder whereClause = new StringBuilder("WHERE ");
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                Object value = field.get(obj);
                String fieldName = field.getName();
                Column columnAnnotation = field.getAnnotation(Column.class);
                String columnName = (columnAnnotation != null) ? columnAnnotation.name() : fieldName;

                whereClause.append(columnName).append(" = ? AND ");
                parameters.put(index++, value);
            }
        }

        // Remove the trailing " AND "
        whereClause.setLength(whereClause.length() - 4);

        // Final SQL query
        String sql = "UPDATE " + tableName + " " + setClause + " " + whereClause;

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set all the parameters in the prepared statement
            for (Map.Entry<Integer, Object> entry : parameters.entrySet()) {
                preparedStatement.setObject(entry.getKey(), entry.getValue());
            }

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlEx) {
            throw new SQLException("Error executing update: " + sqlEx.getMessage(), sqlEx);
        }
    }

    public static <T> int executeDelete(T obj) throws SQLException, IllegalAccessException {
        Class<?> clazz = obj.getClass();

        Field[] fields = clazz.getDeclaredFields();
        String tableName = extractTableName(clazz); // Use your method to get the table name.

        Map<Integer, Object> parameters = new HashMap<>();
        int index = 1;

          // Build the WHERE clause based on @Id fields
        StringBuilder whereClause = new StringBuilder("WHERE ");
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                Object value = field.get(obj);
                String fieldName = field.getName();
                Column columnAnnotation = field.getAnnotation(Column.class);
                String columnName = (columnAnnotation != null) ? columnAnnotation.name() : fieldName;

                whereClause.append(columnName).append(" = ? AND ");
                parameters.put(index++, value);
            }
        }

        // Remove the trailing " AND "
        whereClause.setLength(whereClause.length() - 4);

        // Final SQL query
        String sql = "DELETE FROM " + tableName + " " + whereClause.toString();

        try (Connection connection = DataSourceConfig.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set all the parameters in the prepared statement
            for (Map.Entry<Integer, Object> entry : parameters.entrySet()) {
                preparedStatement.setObject(entry.getKey(), entry.getValue());
            }

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlEx) {
            throw new SQLException("Error executing delete: " + sqlEx.getMessage(), sqlEx);
        }
    }
}
