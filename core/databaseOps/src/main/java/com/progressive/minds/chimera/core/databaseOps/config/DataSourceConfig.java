package com.progressive.minds.chimera.core.databaseOps.config;

import com.progressive.minds.chimera.core.databaseOps.exception.ValidationException;
import com.progressive.minds.chimera.core.databaseOps.utility.CloudCredentials;
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
    static Map<String, String> credentials;
    static String RDSUserName = "";
    static String RDSPassword = "";
    static {
        String cloudProvider = "";
        String rdsSecretName = "";
        String ProjectIdOrVault = "";

        try {
            cloudProvider = (String) envProperties.getOrDefault("CloudProvider", "LOCAL");
            rdsSecretName = (String) envProperties.getOrDefault("RDSSecretName", "Invalid RDS Secret Name");
            if (cloudProvider.equalsIgnoreCase("gcp"))
                ProjectIdOrVault = (String) envProperties.get("GCPProjectId");
            else
                ProjectIdOrVault = (String) envProperties.get("AZUREKeyVaultURL");

            credentials = CloudCredentials.getCredentials(cloudProvider, rdsSecretName , ProjectIdOrVault);
            RDSUserName = credentials.get("username");
            RDSPassword = credentials.get("password");
        } catch (Exception e) {
            throw new ValidationException("Exception While Fetching Cloud Credentials " + cloudProvider + "For Secret"
             + rdsSecretName);
        }
    }

    static String defaultURL = "jdbc:postgresql://localhost:5432/postgres";
    static String jdbcURL = (String) envProperties.getOrDefault("url", defaultURL);
    static String userName = (String) envProperties.getOrDefault("username", RDSUserName);
    static String password = (String) envProperties.getOrDefault("password", RDSPassword);
    static String driver = (String) envProperties.getOrDefault("DriverClassName", "org.postgresql.Driver");
    static String maxPoolSize = (String) envProperties.getOrDefault("MaximumPoolSize", "10");
    static String minIdle = (String) envProperties.getOrDefault("MinimumIdle", "2");
    static String idleTimeout = (String) envProperties.getOrDefault("IdleTimeout", "600000");
    static String maxLifeTime = (String) envProperties.getOrDefault("MaxLifetime", "1800000");
    static String connectionTimeout = (String) envProperties.getOrDefault("ConnectionTimeout", "30000");


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