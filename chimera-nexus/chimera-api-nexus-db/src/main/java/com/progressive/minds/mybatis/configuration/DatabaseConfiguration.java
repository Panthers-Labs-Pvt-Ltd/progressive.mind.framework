package com.progressive.minds.mybatis.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** */
@Configuration
public class DatabaseConfiguration {
  @Bean
  @ConfigurationProperties("spring.datasource.hikari")
  public HikariDataSource dataSource(DataSourceProperties properties) {
    return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
  }
}
