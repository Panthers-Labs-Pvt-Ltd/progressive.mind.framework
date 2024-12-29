package com.progressive.minds.chimera;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.progressive.minds.chimera.mapper")
public class ApiServiceApplication{

  public static void main(String[] args) {
    SpringApplication.run(ApiServiceApplication.class, args);
  }
}