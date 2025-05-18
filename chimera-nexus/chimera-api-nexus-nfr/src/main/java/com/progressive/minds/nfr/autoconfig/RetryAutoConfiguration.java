package com.progressive.minds.nfr.autoconfig;

import com.progressive.minds.nfr.retry.ServiceRetryConfigurationProperties;
import io.github.resilience4j.core.ContextAwareScheduledThreadPoolExecutor;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.spring6.fallback.FallbackDecorators;
import io.github.resilience4j.spring6.fallback.FallbackExecutor;
import io.github.resilience4j.spring6.retry.configure.RetryAspect;
import io.github.resilience4j.spring6.retry.configure.RetryAspectExt;
import io.github.resilience4j.spring6.spelresolver.SpelResolver;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(ServiceRetryConfigurationProperties.class)
@ConditionalOnClass(Retry.class)
public class RetryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RetryRegistry retryRegistry(ServiceRetryConfigurationProperties properties) {
        return RetryRegistry.ofDefaults();
    }

    @Bean
    public RetryAspect retryAspect(
            ServiceRetryConfigurationProperties properties,
            RetryRegistry retryRegistry,
            ObjectProvider<List<RetryAspectExt>> retryAspectExtList,
            FallbackExecutor fallbackExecutor,
            SpelResolver spelResolver,
            @Autowired(required = false) ContextAwareScheduledThreadPoolExecutor executor
    ) {
        return new RetryAspect(
                properties,
                retryRegistry,
                retryAspectExtList.getIfAvailable(ArrayList::new),
                fallbackExecutor,
                spelResolver,
                executor
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public FallbackExecutor fallbackExecutor(SpelResolver spelResolver, FallbackDecorators fallbackDecorators) {
        return new FallbackExecutor(spelResolver, fallbackDecorators);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpelResolver spelResolver() {
        return new SpelResolver() {
            @Override
            public String resolve(Method method, Object[] arguments, String spelExpression) {
                return "";
            }
        };
    }
}