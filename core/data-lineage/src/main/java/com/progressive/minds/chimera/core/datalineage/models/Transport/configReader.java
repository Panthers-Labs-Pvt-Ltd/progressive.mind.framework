package com.progressive.minds.chimera.core.datalineage.models.Transport;

import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
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

    public static Map<String, Object> getEnvironmentProperties(String transportType) throws IOException {
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

        if (!found) {
            LineageLogger.logError("RuntimeException Invalid Transport Type Selected " + transportType);

            throw new RuntimeException("Invalid Transport Type Selected " + transportType);
        } else {
            try (InputStream inputStream = configReader.class.getClassLoader()
                    .getResourceAsStream("Transport/" + transportType + ".yaml")) {
                if (inputStream == null) {
                    throw new IllegalArgumentException("File not found: rdsConfig.yml");
                }
                // Parse YAML into a Map
                Map<String, Object> data = yaml.load(inputStream);
                Map<String, Object> flattenedMap = new HashMap<>();
                flattenMap("", data, flattenedMap);
                // Print all key-value pairs from the flattened map
                for (Map.Entry<String, Object> entry : flattenedMap.entrySet()) {
                    LineageLogger.logDebug("Key: " + entry.getKey() + " | Value: " + entry.getValue());

                }

                return (Map<String, Object>) data.get("transport");
                } catch (IOException e) {
                LineageLogger.logError(transportType+ " Configuration Not Found", e);
                throw new RuntimeException(e);
            }
            catch (Exception e) {
                LineageLogger.logError("Exception Occurred During Config Read", e);
                return null;
            }
        }
    }
}


