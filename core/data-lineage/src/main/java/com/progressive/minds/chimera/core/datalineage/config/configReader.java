package com.progressive.minds.chimera.core.datalineage.config;

import com.progressive.minds.chimera.core.datalineage.models.OpenLineageTransportTypes;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nullable;
import java.io.*;
import java.util.*;

public class configReader {
    static ChimeraLogger LineageLogger =  ChimeraLoggerFactory.getLogger(OpenLineageTransportTypes.class);

    /**
     * Flattens a nested map into a single map with keys as paths.
     *
     * @param parentKey The parent key, used to create a flattened key.
     * @param currentMap The current map being processed.
     * @param flattenedMap The map that stores flattened key-value pairs.
     */
    private static void flattenMap(String parentKey, Map<String, Object> currentMap, Map<String, Object> flattenedMap) {
        for (Map.Entry<String, Object> entry : currentMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Create a new key by appending the current key to the parent key
            String newKey = parentKey.isEmpty() ? key : parentKey + "." + key;

            // If the value is a map, recursively flatten it
            if (value instanceof Map) {
                flattenMap(newKey, (Map<String, Object>) value, flattenedMap);
            } else {
                // Otherwise, add the key-value pair to the flattened map
                flattenedMap.put(newKey, value);
            }
        }
    }

    public static InputStream getConfigs(String orgName, String userConfig, String defaultFileName) throws FileNotFoundException {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() {
                return -1;
            }
        };

        LineageLogger.logInfo("Reading Configs For " + orgName);

        if (null != userConfig && new File(userConfig).exists() && new File(userConfig).isFile()) {
            inputStream = new FileInputStream(userConfig);
            LineageLogger.logInfo("User Config Exists ....");

        }
        else {
            LineageLogger.logInfo("User Config File not found so local file will be actioned");
            inputStream = configReader.class.getClassLoader().getResourceAsStream(defaultFileName);
            LineageLogger.logInfo("In Jar " + defaultFileName + " Exist");
        }
        LineageLogger.logInfo(" Config File read Completed");
        return inputStream;
    }

    public static Map<String, Object> getEnvironmentProperties(String transportType, String inOrg,
                                                               @Nullable String UserConfig) throws IOException {
        Yaml yaml = new Yaml();
        List<String> validTransportTypes = new ArrayList<>();
        validTransportTypes.add("gcp");
        validTransportTypes.add("gcs");
        validTransportTypes.add("http");
        validTransportTypes.add("kafka");
        validTransportTypes.add("s3");
        validTransportTypes.add("file");
        validTransportTypes.add("console");
        validTransportTypes.add("composite");

        boolean found = validTransportTypes.stream().anyMatch(transportType.toLowerCase(Locale.ROOT).trim()::equals);
        Map<String, Object> data = Map.of();
        if (!found) {
            LineageLogger.logError("RuntimeException Invalid Transport Type Selected " + transportType);
            throw new RuntimeException("Invalid Transport Type Selected " + transportType);
        }
        else if (UserConfig != null && !UserConfig.contains(transportType + ".yaml")){
            // UserConfig.substring(UserConfig.lastIndexOf('/') + 1) Extract File Name
            LineageLogger.logError("RuntimeException Invalid User Config File For  " + transportType);
            LineageLogger.logError(transportType + " is not matching with file name " + UserConfig,
                    new IOException("Invalid User Config File For  " + transportType));
        }
        else {
            String ConfigFileName = "Transport/" + transportType + ".yaml";
            try (InputStream inputStream = getConfigs(inOrg, UserConfig, ConfigFileName))
                    {
                if (inputStream == null) {
                    LineageLogger.logError(transportType + " Input Stream is Null " + UserConfig,
                            new IOException("Input Stream is Null For  " + transportType));
                }
                // Parse YAML into a Map
                data = yaml.load(inputStream);
                Map<String, Object> flattenedMap = new HashMap<>();
                flattenMap("", data, flattenedMap);
                // Print all key-value pairs from the flattened map
                for (Map.Entry<String, Object> entry : flattenedMap.entrySet()) {
                    LineageLogger.logInfo("Key: " + entry.getKey() + " | Value: " + entry.getValue());

                }

                } catch (IOException e) {
                LineageLogger.logError(transportType + " Input Stream is Null " + UserConfig,
                        new IOException("Input Stream is Null For  " + transportType));

                LineageLogger.logError(transportType+ " Configuration Not Found", e);
                throw new RuntimeException(e);
            }
            catch (Exception e) {
                LineageLogger.logError("Exception Occurred During Config Read" + UserConfig,
                        new IOException("Exception Occurred During Config Read" , e));
                return null;
            }
        }
        return (Map<String, Object>) data.get("transport");
    }
}


