package com.progressive.minds.nfr;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import java.time.Duration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "chimera.resilience")
@ConditionalOnClass(CircuitBreaker.class)
public class ResilienceAutoConfig {

    private final int timeoutDuration = 3000;
    private final int retryMaxAttempts = 3;
    private final int circuitBreakerFailureRate = 50;

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultResilienceConfig() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
            .timeLimiterConfig(TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofMillis(timeoutDuration))
                .build())
            .circuitBreakerConfig(CircuitBreakerConfig.custom()
                .failureRateThreshold(circuitBreakerFailureRate)
                .build())
            .build());
    }
}