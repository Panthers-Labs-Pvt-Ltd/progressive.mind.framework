package com.progressive.minds.keyclock;

import com.progressive.minds.keyclock.config.KeycloakSetupProperties;
import com.progressive.minds.keyclock.service.*;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(KeycloakSetupProperties.class)
@AutoConfigureAfter({KeycloakSetupProperties.class})
public class KeycloakAutoConfiguration {

    private final KeycloakSetupProperties properties;

    public KeycloakAutoConfiguration(KeycloakSetupProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Keycloak keycloakAdmin() {
        KeycloakSetupProperties.Admin admin = properties.getAdmin();
        return KeycloakBuilder.builder()
                .serverUrl(admin.getServerUrl())
                .realm(admin.getRealm())
                .clientId(admin.getClientId())
                .username(admin.getUsername())
                .password(admin.getPassword())
                .build();
    }

  @Bean
  @ConditionalOnProperty(prefix = "keycloak.setup", name = "enabled", havingValue = "true")
  public KeycloakInitializerRunner keycloakInitializerRunner(
      Keycloak keycloakInstance,
      KeycloakSetupProperties keycloakProperties,
      RealmSetupService realmSetupService,
      ClientSetupService clientSetupService,
      RoleSetupService roleSetupService,
      GroupSetupService groupSetupService,
      UserSetupService userSetupService) {

    return new KeycloakInitializerRunner(
        keycloakInstance,
        keycloakProperties,
        realmSetupService,
        clientSetupService,
        roleSetupService,
        groupSetupService,
        userSetupService);
  }

  @Bean
  public RealmSetupService realmService(Keycloak keycloak) {
    return new RealmSetupService(keycloak);
  }

  @Bean
  public ClientSetupService clientService(Keycloak keycloak) {
    return new ClientSetupService(keycloak);
  }

  @Bean
  public RoleSetupService roleSetupService(Keycloak keycloak) {
    return new RoleSetupService(keycloak);
  }

  @Bean
  public UserSetupService userSetupService(Keycloak keycloak) {
    return new UserSetupService(keycloak);
  }

  @Bean
  public GroupSetupService groupSetupService(Keycloak keycloak) {
    return new GroupSetupService(keycloak);
  }
}
