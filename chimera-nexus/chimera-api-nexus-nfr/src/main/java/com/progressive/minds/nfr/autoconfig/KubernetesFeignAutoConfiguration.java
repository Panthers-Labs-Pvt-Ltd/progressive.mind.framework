package com.progressive.minds.nfr.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnCloudPlatform(CloudPlatform.KUBERNETES)
//@EnableConfigurationProperties(KubernetesFeignProperties.class)
@EnableFeignClients
public class KubernetesFeignAutoConfiguration {

 /*   @Bean
    public Feign.Builder feignBuilder(
        CircuitBreakerFactory circuitBreakerFactory,
        KubernetesFeignProperties properties
    ) {
        return Feign.builder()
            .retryer(new Retryer.Default(
                properties.getConnectTimeout(), 
                properties.getReadTimeout(), 
                properties.getMaxRetryAttempts()
            ))
            .errorDecoder(new KubernetesErrorDecoder())
            .requestInterceptor(template -> {
                if (circuitBreakerFactory != null) {
                    template.header("X-Circuit-Breaker-Enabled", "true");
                }
            });
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadBalancerClient feignLoadBalancerClient(
        KubernetesClientServicesList servicesList
    ) {
        return new KubernetesLoadBalancerClient(servicesList);
    }*/
}