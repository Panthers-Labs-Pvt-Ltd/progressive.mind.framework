package com.progressive.minds.chimera.core.databaseOps.utility;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ColumnExtractor {
    public static List<String> extractGetterColumnNames(Class<?> clazz) {
        List<String> columnNames = new ArrayList<>();

        for (Method method : clazz.getDeclaredMethods()) {
            String methodName = method.getName();
            if (methodName.startsWith("get") && method.getParameterCount() == 0
                    && method.getReturnType() != void.class) {
                String columnName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                columnNames.add(columnName);
            }
        }

        return columnNames;
    }
}