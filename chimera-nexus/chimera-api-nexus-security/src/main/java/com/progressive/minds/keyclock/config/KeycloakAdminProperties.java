package com.progressive.minds.keyclock.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "keycloak.admin")
public class KeycloakAdminProperties {
    private String serverUrl;
    private String realm = "master";
    private String clientId = "admin-cli";
    private String username;
    private String password;
}
