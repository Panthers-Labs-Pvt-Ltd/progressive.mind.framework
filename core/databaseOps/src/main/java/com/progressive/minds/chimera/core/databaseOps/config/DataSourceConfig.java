package com.progressive.minds.chimera.core.databaseOps.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

import static com.progressive.minds.chimera.core.databaseOps.utility.readYamlConfig.*;

public class DataSourceConfig {

    private static DataSource dataSource;
    static String envName = System.getProperty("CHIMERA_EXE_ENV", "dev");
    static Map<String, Object> envProperties = getEnvironmentProperties(envName);
    
    static String jdbcURL = (String) envProperties.get("url");
    static String userName = (String) envProperties.get("username");
    static String password = (String) envProperties.get("password");
    static String driver = (String) envProperties.get("DriverClassName");
    static String maxPoolSize = (String) envProperties.get("MaximumPoolSize");
    static String minIdle = (String) envProperties.get("MinimumIdle");
    static String idleTimeout = (String) envProperties.get("IdleTimeout");
    static String maxLifeTime = (String) envProperties.get("MaxLifetime");
    static String connectionTimeout = (String) envProperties.get("ConnectionTimeout");


    public static DataSource getDataSource() {
        HikariConfig config = new HikariConfig();

        if (Objects.equals(envName, "UnitTest")) {
            config.setJdbcUrl(jdbcURL);
            config.setUsername(userName);
            config.setPassword(password);
            config.setDriverClassName(driver);
        } else {
            config.setJdbcUrl(jdbcURL);
            config.setUsername(userName);
            config.setPassword(password);
            config.setDriverClassName(driver);
            config.setMaximumPoolSize(Integer.parseInt(maxPoolSize));
            config.setMinimumIdle(Integer.parseInt(minIdle));
            config.setIdleTimeout(Long.parseLong(idleTimeout));
            config.setMaxLifetime(Long.parseLong(maxLifeTime));
            config.setConnectionTimeout(Long.parseLong(connectionTimeout));
            config.setAutoCommit(false);
        }
        return new HikariDataSource(config);
    }
}