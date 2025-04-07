package com.progressive.minds.keyclock.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Data
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakSetupProperties {

  private Admin admin = new Admin();
  private Setup setup = new Setup();

  @Data
  public static class Admin {
    private String serverUrl;
    private String username;
    private String password;
    private String realm = "master";
    private String clientId = "admin-cli";
  }

  @Data
  public static class Setup {
    private boolean enabled = true;
    private String realm;
    private Client client;
    private List<Role> roles = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    @Data
    public static class Client {
      private String clientId;
      private boolean enabled = true;
      private boolean publicClient;
      private boolean standardFlowEnabled;
      private boolean directAccessGrantsEnabled;
      private List<ProtocolMapper> protocolMappers = new ArrayList<>();
      private Map<String, String> attributes = new HashMap<>();
      private List<String> webOrigins = new ArrayList<>();
      private List<String> redirectUris = new ArrayList<>();

      @Data
      public static class ProtocolMapper {
        private String name;
        private String protocol;
        private String protocolMapper;
        private Map<String, String> config = new HashMap<>();
      }
    }

    @Data
    public static class Role {
      private String name;
      private String description;
    }

    @Data
    public static class Group {
      private String name;
      private List<String> roles = new ArrayList<>();
    }

    @Data
    public static class User {
      private String username;
      private String email;
      private String firstName;
      private String lastName;
      private boolean enabled = true;
      private List<Credential> credentials = new ArrayList<>();
      private List<String> roles = new ArrayList<>();
      private List<String> groups = new ArrayList<>();

      @Data
      public static class Credential {
        private String type;
        private String value;
      }
    }
    }
}