package com.progressive.minds.chimera.DataManagement.datalineage.models;

public enum OpenLineageDialect {
    ANSI,
    POSTGRESQL,
    MYSQL,
    ORACLE,
    TSQL,
    MSSQL,
    SQLITE,
    HIVE,
    REDSHIFT,
    DB2,
    SPARKSQL,
    PRESTO;

    public static boolean isSupported(String dialect) {
        for (OpenLineageDialect d : OpenLineageDialect.values()) {
            if (d.name().equalsIgnoreCase(dialect)) {
                return true;
            }
        }
        return false;
    }
}
