package com.progressive.minds.nfr.circuitbreaker;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "chimera.resilience.circuitbreaker")
public class CircuitBreakerProperties {
    private int failureRateThreshold = 50;
    private int waitDurationInOpenStateMs = 1000;
    private int permittedNumberOfCallsInHalfOpenState = 2;
    private int slidingWindowSize = 2;
    private List<String> recordExceptions = List.of("java.io.IOException", "java.util.concurrent.TimeoutException");
    private List<String> ignoreExceptions = List.of("com.progressive.minds.BusinessException");

}