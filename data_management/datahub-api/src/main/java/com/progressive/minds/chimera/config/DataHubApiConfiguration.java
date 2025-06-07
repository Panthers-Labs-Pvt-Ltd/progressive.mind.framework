package com.progressive.minds.chimera.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;


@Configuration
public class DataHubApiConfiguration {
  /*  @Bean
  public ManagePipeline managePipeline(ManagePipeline managePipeline){
     return new ManagePipeline();
  }*/

  @Bean
  public HttpMessageConverter<Object> yamlHttpMessageConverter() {
    return new AbstractJackson2HttpMessageConverter(
            new YAMLMapper(),
            MediaType.valueOf("application/x-yaml")
    ) {};
  }

  @Bean
  public ObjectMapper yamlObjectMapper() {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    mapper.findAndRegisterModules();
    return mapper;
  }
}
