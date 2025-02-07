package com.progressive.minds.chimera.DataManagement.datalineage.models;

public enum DataSourcesTypes {
    FILE,
    RDBMS,
    NOSQL,
    OpenTableFormat,
    API;

    public static boolean isSupported(String types) {
        for (DataSourcesTypes d : DataSourcesTypes.values()) {
            if (d.name().equalsIgnoreCase(types)) {
                return true;
            }
        }
        return false;
    }
}

