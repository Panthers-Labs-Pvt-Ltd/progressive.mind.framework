package com.progressive.minds.chimera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.progressive.minds.chimera")
public class ChimeraDataIngestionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChimeraDataIngestionApplication.class, args);
    }
}
