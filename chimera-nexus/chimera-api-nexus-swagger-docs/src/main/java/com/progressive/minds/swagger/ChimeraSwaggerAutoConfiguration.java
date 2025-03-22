package com.progressive.minds.swagger;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;

@AutoConfiguration
@ConditionalOnClass(OpenAPI.class)
public class ChimeraSwaggerAutoConfiguration {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Chimera API Documentation")
                .version("1.0")
                .description("Auto-generated API docs with examples"));
  }

  @Bean
  public OperationCustomizer operationCustomizer() {
    return (Operation operation, HandlerMethod handlerMethod) -> {
      Set<String> existingParams =
          operation.getParameters() != null
              ? operation.getParameters().stream()
                  .map(p -> p.getIn() + ":" + p.getName())
                  .collect(Collectors.toSet())
              : new HashSet<>();

      Arrays.stream(handlerMethod.getMethodParameters())
          .forEach(
              param -> {
                PathVariable pathVar = param.getParameterAnnotation(PathVariable.class);
                RequestParam reqParam = param.getParameterAnnotation(RequestParam.class);

                if (pathVar != null) {
                  String paramName = resolveAnnotationName(pathVar.name(), pathVar.value(), param);
                  String uniqueKey = "path:" + paramName;

                  if (!existingParams.contains(uniqueKey)) {
                    operation.addParametersItem(createPathParam(paramName));
                    existingParams.add(uniqueKey);
                  }
                } else if (reqParam != null) {
                  String paramName =
                      resolveAnnotationName(reqParam.name(), reqParam.value(), param);
                  String uniqueKey = "query:" + paramName;

                  if (!existingParams.contains(uniqueKey)) {
                    operation.addParametersItem(createQueryParam(paramName, reqParam.required()));
                    existingParams.add(uniqueKey);
                  }
                }
              });
      return operation;
    };
  }

  private String resolveAnnotationName(String nameAttr, String valueAttr, MethodParameter param) {
    if (!nameAttr.isEmpty()) {
      return nameAttr;
    }
    if (!valueAttr.isEmpty()) {
      return valueAttr;
    }
    return param.getParameter().getName();
  }

  private Parameter createPathParam(String name) {
    return new Parameter().name(name).in("path").required(true);
  }

  private Parameter createQueryParam(String name, boolean required) {
    return new Parameter().name(name).in("query").required(required);
  }

  @Bean
  public ModelConverter exampleModelConverter(ResourceLoader resourceLoader) {
    return new ChimeraAPIExampleResolver(resourceLoader);
  }

  @Bean
  public GlobalOpenApiCustomizer chimeraSwaggerCustomizer(ResourceLoader resourceLoader) {
    return new ChimeraSwaggerCustomizer(resourceLoader);
  }
}
