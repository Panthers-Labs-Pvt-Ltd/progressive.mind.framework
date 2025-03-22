package com.progressive.minds.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

public class ChimeraAPIExampleLoader {
  private static final Logger logger = LoggerFactory.getLogger(ChimeraAPIExampleLoader.class);
  private final ResourceLoader resourceLoader;
  private final ObjectMapper objectMapper;

  public ChimeraAPIExampleLoader(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
    this.resourceLoader = resourceLoader;
    this.objectMapper = objectMapper;
  }

  public Object loadExample(String className) {
    try {
      Resource resource =
          resourceLoader.getResource("classpath:sample_payloads/" + className + ".json");
      if (!resource.exists()) {
        logger.error("Missing example file: sample_payloads/{}.json", className);
        return null;
      }
      String json = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
      return objectMapper.readValue(json, Object.class);
    } catch (IOException e) {
      logger.error("Error loading example for {}: {}", className, e.getMessage());
      return null;
    }
  }
}
