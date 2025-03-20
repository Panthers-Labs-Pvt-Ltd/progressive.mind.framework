package com.progressive.minds.keyclock;

import com.progressive.minds.keyclock.config.KeycloakAdminProperties;
import com.progressive.minds.keyclock.config.KeycloakSetupProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties({KeycloakAdminProperties.class, KeycloakSetupProperties.class})
public class KeycloakAutoConfiguration {

    private final KeycloakAdminProperties properties;

    public KeycloakAutoConfiguration(KeycloakAdminProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Keycloak keycloakAdmin(KeycloakAdminProperties properties) {
        return KeycloakBuilder.builder()
                .serverUrl(properties.getServerUrl())
                .realm(properties.getRealm())
                .clientId(properties.getClientId())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .build();
    }
}
