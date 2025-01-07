package com.progressive.minds.chimera.core.databaseOps.utility;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class readYamlConfig {

    public static Map<String, Object> getEnvironmentProperties(String envName) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = readYamlConfig.class.getClassLoader()
                .getResourceAsStream("rdsConfig.yml")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: rdsConfig.yml");
            }
            // Parse YAML into a Map
            Map<String, Object> data = yaml.load(inputStream);
            Map<String, Object> application = (Map<String, Object>) data.get("application");
            Map<String, Object> environments = (Map<String, Object>) application.get("environment");
            String appName = (String) application.get("name");
            String appVersion = (String) application.get("version");

            System.out.println("Application Name: " + appName);
            System.out.println("Application Version: " + appVersion);

            if (environments.containsKey(envName)) {
                System.out.println("Application Environments: " + envName);
                return (Map<String, Object>) environments.get(envName);
            } else {
                System.out.println("Invalid environment. Defaulting to 'test'.");
                return (Map<String, Object>) environments.get("test"); // Default to test
            }
        } catch (Exception e) {
        e.printStackTrace();
        return null;
        }
    }
}