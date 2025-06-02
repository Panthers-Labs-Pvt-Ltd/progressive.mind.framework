package com.progressive.minds.chimera.core.data_source.utility;

import lombok.Getter;
import java.util.Arrays;
import java.util.Optional;

@Getter
public enum JdbcDriver {
    MARIADB("mariadb", "org.mariadb.jdbc.Driver"),
    MYSQL("mysql", "com.mysql.cj.jdbc.Driver"),
    ORACLE("oracle", "oracle.jdbc.driver.OracleDriver"),
    DB2("db2", "com.ibm.db2.jcc.DB2Driver"),
    MSSQL("mssql", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    POSTGRES("postgres", "org.postgresql.Driver"),
    TERADATA("teradata", "com.teradata.jdbc.TeraDriver"),
    REDSHIFT("redshift", "com.amazon.redshift.jdbc42.Driver");

    private final String sourceType;
    private final String driverClass;

    JdbcDriver(String sourceType, String driverClass) {
        this.sourceType = sourceType;
        this.driverClass = driverClass;
    }

    public static Optional<String> getDriver(String sourceType) {
        return Arrays.stream(values())
                .filter(driver -> driver.sourceType.equalsIgnoreCase(sourceType))
                .map(JdbcDriver::getDriverClass)
                .findFirst();
    }
}
