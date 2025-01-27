package com.progressive.minds.chimera.core.datahub.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ibm.icu.impl.data.ResourceReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class JsonToYaml {
    public static void main(String[] args) throws IOException {
        // Sample JSON string
        String jsonString = new String(Objects.requireNonNull(ResourceReader.class.getClassLoader()
                .getResourceAsStream("pipeline.json")).readAllBytes(), StandardCharsets.UTF_8);

        // Create Jackson ObjectMapper for JSON and YAML
        ObjectMapper jsonMapper = new ObjectMapper();
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

        try {
            // Convert JSON string to a Java Object (Map or any other POJO)
            Object jsonObject = jsonMapper.readValue(jsonString, Object.class);

            // Convert the Java Object to YAML
            String yamlString = yamlMapper.writeValueAsString(jsonObject);

            // Print the YAML output
            System.out.println("YAML Output:");
            System.out.println(yamlString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
