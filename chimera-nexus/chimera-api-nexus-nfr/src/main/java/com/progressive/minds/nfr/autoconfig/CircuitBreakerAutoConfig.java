package com.progressive.minds.nfr.autoconfig;

import com.progressive.minds.nfr.circuitbreaker.CircuitBreakerProperties;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CircuitBreakerProperties.class)
@ConditionalOnClass(CircuitBreaker.class)
public class CircuitBreakerAutoConfig {

    private final CircuitBreakerProperties properties;

    public CircuitBreakerAutoConfig(CircuitBreakerProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        return CircuitBreakerRegistry.of(circuitBreakerConfig());
    }

    @Bean
    public CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
            .failureRateThreshold(properties.getFailureRateThreshold())
            .waitDurationInOpenState(Duration.ofMillis(properties.getWaitDurationInOpenStateMs()))
            .permittedNumberOfCallsInHalfOpenState(properties.getPermittedNumberOfCallsInHalfOpenState())
            .slidingWindowSize(properties.getSlidingWindowSize())
            .recordExceptions(convertToClasses(properties.getRecordExceptions()))
            .ignoreExceptions(convertToClasses(properties.getIgnoreExceptions()))
            .build();
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Throwable>[] convertToClasses(List<String> classNames) {
        return classNames.stream()
            .map(this::loadClass)
            .toArray(Class[]::new);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Throwable> loadClass(String className) {
        try {
            return (Class<? extends Throwable>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to load exception class: " + className, e);
        }
    }

   /* @Bean
    public RetryConfigCustomizer retryConfigCustomizer(KubernetesFeignPropertie props) {
        return RetryConfigCustomizer.of("feignRetry", builder ->
                builder.maxAttempts(props.getMaxRetryAttempts())
        );
    }*/
}