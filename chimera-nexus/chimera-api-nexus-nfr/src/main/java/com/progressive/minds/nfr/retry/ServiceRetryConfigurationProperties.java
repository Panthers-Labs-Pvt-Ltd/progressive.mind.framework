package com.progressive.minds.nfr.retry;

import io.github.resilience4j.spring6.retry.configure.RetryConfigurationProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.IOException;
import java.util.List;

@Setter
@Getter
@ConfigurationProperties(prefix = "chimera.resilience.retry")
public class ServiceRetryConfigurationProperties extends RetryConfigurationProperties {
    private int maxAttempts = 3;
    private long waitDuration = 1000;
    private List<Class<? extends Throwable>> retryExceptions = List.of(IOException.class);
    private List<Class<? extends Throwable>> ignoreExceptions = List.of();
}