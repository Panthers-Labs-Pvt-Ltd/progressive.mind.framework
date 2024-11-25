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
    public static DataSource getDataSource() {

    Map<String, Object> envProperties = getEnvironmentProperties(envName);
    Map<String, String> credentials;
    String RDSUserName = "";
    String RDSPassword = "";
        String cloudProvider = "";
        String rdsSecretName = "";
        String ProjectIdOrVault = "";

        try {
            cloudProvider = (String) envProperties.getOrDefault("CloudProvider", "LOCAL");
            if (!cloudProvider.equalsIgnoreCase("local"))
            {
            rdsSecretName = (String) envProperties.get("RDSSecretName");
            if (cloudProvider.equalsIgnoreCase("gcp"))
                ProjectIdOrVault = (String) envProperties.get("GCPProjectId");
            else if (cloudProvider.equalsIgnoreCase("azure"))
                ProjectIdOrVault = (String) envProperties.get("AZUREKeyVaultURL");
            else
                ProjectIdOrVault = "";
            credentials = CloudCredentials.getCredentials(cloudProvider, rdsSecretName , ProjectIdOrVault);
            RDSUserName = credentials.get("username");
            RDSPassword = credentials.get("password");
            }
        } catch (Exception e) {
            throw new ValidationException("Exception While Fetching Cloud Credentials " + cloudProvider + "For Secret"
             + rdsSecretName);
        }


   String defaultURL = "jdbc:postgresql://localhost:5432/postgres";
   String jdbcURL = (String) envProperties.getOrDefault("url", defaultURL);
   String userName = (String) envProperties.getOrDefault("username", RDSUserName);
   String password = (String) envProperties.getOrDefault("password", RDSPassword);
   String driver = (String) envProperties.getOrDefault("DriverClassName", "org.postgresql.Driver");
   String maxPoolSize = (String) envProperties.getOrDefault("MaximumPoolSize", "10");
   String minIdle = (String) envProperties.getOrDefault("MinimumIdle", "2");
   String idleTimeout = (String) envProperties.getOrDefault("IdleTimeout", "600000");
   String maxLifeTime = (String) envProperties.getOrDefault("MaxLifetime", "1800000");
   String connectionTimeout = (String) envProperties.getOrDefault("ConnectionTimeout", "30000");


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