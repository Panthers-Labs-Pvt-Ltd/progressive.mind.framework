package com.progressive.minds.chimera;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.progressive.minds.chimera.examples")
@MapperScan("com.progressive.minds.chimera.examples.mapper.generated")
public class ChimeraNexusExamplesApplication {
  public static void main(String[] args) {
    SpringApplication.run(ChimeraNexusExamplesApplication.class, args);
  }
}
