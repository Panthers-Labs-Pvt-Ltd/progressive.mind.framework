package com.progressive.minds.chimera.core.datahub.pipeline;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.progressive.minds.chimera.core.datahub.modal.Pipeline;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import java.io.IOException;

public class utils {

    public static Pipeline getPipelineInfo(String InputFormat) {
        ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(ManagePipeline.class);
        InputFormat = InputFormat.trim();
        String SchemaFormat;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        if (InputFormat.startsWith("{") || InputFormat.startsWith("["))  SchemaFormat="JSON";
        else if (InputFormat.contains(":") && !InputFormat.contains("{"))  SchemaFormat = "YAML";
        else throw new RuntimeException("Invalid Format of Dataset Definition");
        Pipeline pipeline;
        switch (SchemaFormat.toUpperCase()) {
            case "JSON":
                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);


                try {
                    pipeline = objectMapper.readValue(InputFormat, Pipeline.class);
                } catch (IOException e) {
                    DatahubLogger.logError("Error While Processing Json Schema " + e.getMessage());
                    return null;
                }
                break;
            case "YML":
            case "YAML":
                ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
                try {
                    pipeline =  yamlMapper.readValue(InputFormat, Pipeline.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                System.out.println("Unsupported format: " + SchemaFormat);
                throw new IllegalArgumentException("Invalid format provided: " + SchemaFormat);
        }
        return  pipeline;
    }
}
