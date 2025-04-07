package com.progressive.minds.keyclock;

import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.Map;

public class KeycloakDefaultProperties implements EnvironmentPostProcessor {
    private final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();

    @Override
    public void postProcessEnvironment(
            ConfigurableEnvironment environment,
            SpringApplication application
    ) {
        try {
            // Load defaults from classpath
            Resource resource = new ClassPathResource("default-keycloak.yaml");

            if (resource.exists()) {
                // Add as LAST property source (allow overrides)
                loader.load("keycloakDefaults", resource)
                        .forEach(ps -> environment.getPropertySources().addLast(ps));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load Keycloak defaults", e);
        }
    }
}
