package com.progressive.minds.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.core.io.ResourceLoader;

public class ChimeraSwaggerCustomizer implements GlobalOpenApiCustomizer {
  private static final Logger logger = LoggerFactory.getLogger(ChimeraSwaggerCustomizer.class);
  private final ChimeraAPIExampleLoader exampleLoader;

  public ChimeraSwaggerCustomizer(ResourceLoader resourceLoader) {
    this.exampleLoader = new ChimeraAPIExampleLoader(resourceLoader, new ObjectMapper());
  }

  @Override
  public void customise(OpenAPI openApi) {
    openApi
        .getPaths()
        .forEach(
            (path, pathItem) ->
                pathItem
                    .readOperations()
                    .forEach(operation -> processRequestBody(operation.getRequestBody(), openApi)));
  }

  private void processRequestBody(RequestBody requestBody, OpenAPI openApi) {
    if (requestBody == null) {
      return;
    }
    requestBody
        .getContent()
        .values()
        .forEach(mediaType -> setExampleFromSchema(mediaType, openApi));
  }

  private void setExampleFromSchema(MediaType mediaType, OpenAPI openApi) {
    Schema<?> schema = mediaType.getSchema();
    logger.info("Processing schema: {}", schema);
    if (schema == null) {
      return;
    }

    String schemaName = extractSchemaName(schema.get$ref());
    if (schemaName == null) {
      return;
    }

    Schema<?> resolvedSchema = openApi.getComponents().getSchemas().get(schemaName);
    if (resolvedSchema != null) {
      Object example = exampleLoader.loadExample(schemaName);
      if (example != null) {
        resolvedSchema.setExample(example);
        mediaType.setExample(example);
      }
    }
  }

  private String extractSchemaName(String ref) {
    if (ref == null) return null;
    int lastSlash = ref.lastIndexOf('/');
    return lastSlash == -1 ? null : ref.substring(lastSlash + 1);
  }
}
