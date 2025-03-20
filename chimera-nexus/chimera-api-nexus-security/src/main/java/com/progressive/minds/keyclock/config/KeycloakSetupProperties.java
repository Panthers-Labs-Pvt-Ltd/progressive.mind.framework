package com.progressive.minds.keyclock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "keycloak.setup")
public class KeycloakSetupProperties {
    private String realm;
    private Client client;
    private List<Role> roles;
    private List<Group> groups;
    private List<User> users;

    @Data
    public static class Client {
        private String clientId;
        private boolean enabled = true;
        private boolean publicClient = true;
        private List<String> redirectUris;
        private List<String> webOrigins;
    }

    @Data
    public static class Role {
        private String name;
        private String description;
    }

    @Data
    public static class Group {
        private String name;
        private List<String> roles;
    }

    @Data
    public static class User {
        private String username;
        private String password;
        private boolean enabled = true;
        private List<String> roles;
        private List<String> groups;
    }
}
