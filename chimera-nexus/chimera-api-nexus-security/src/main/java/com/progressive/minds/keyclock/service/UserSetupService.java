package com.progressive.minds.keyclock.service;

import com.progressive.minds.keyclock.config.KeycloakSetupProperties;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserSetupService {

    private final Keycloak keycloak;

    public UserSetupService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

  public void createUsers(String realmName, List<KeycloakSetupProperties.Setup.User> users) {
        RealmResource realm = keycloak.realm(realmName);
        UsersResource usersResource = realm.users();

    users.forEach(
        userConfig -> {
          try {
            // Create user
            String userId = createUser(usersResource, userConfig);
            UserResource userResource = usersResource.get(userId);

            // Set password
            setUserPassword(userResource, userConfig);

            // Assign roles
            assignRoles(realm, userResource, userConfig);

            // Assign groups
            assignGroups(realm, userResource, userConfig);

          } catch (Exception e) {
            log.error("Failed to create user {}: {}", userConfig.getUsername(), e.getMessage());
            throw new RuntimeException("User creation failed", e);
          }
        });
  }

  private String createUser(
      UsersResource usersResource, KeycloakSetupProperties.Setup.User userConfig) {
    UserRepresentation user = new UserRepresentation();
    user.setUsername(userConfig.getUsername());
    user.setEmail(userConfig.getEmail());
    user.setFirstName(userConfig.getFirstName());
    user.setLastName(userConfig.getLastName());
    user.setEnabled(userConfig.isEnabled());

    try (Response response = usersResource.create(user)) {
      if (response.getStatus() == 409) {
        log.warn("User {} already exists", userConfig.getUsername());
        return findExistingUserId(usersResource, userConfig.getUsername());
      }
      if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
        throw new RuntimeException("User creation failed with status: " + response.getStatus());
      }
      return parseCreatedId(response);
    }
  }

  private String findExistingUserId(UsersResource usersResource, String username) {
    return usersResource.search(username).stream()
        .findFirst()
        .map(UserRepresentation::getId)
        .orElseThrow(() -> new RuntimeException("User " + username + " not found after conflict"));
  }

  private void setUserPassword(
      UserResource userResource, KeycloakSetupProperties.Setup.User userConfig) {
    if (!userConfig.getCredentials().isEmpty()) {
            CredentialRepresentation credential = new CredentialRepresentation();
      credential.setType(userConfig.getCredentials().get(0).getType());
      credential.setValue(userConfig.getCredentials().get(0).getValue());
            credential.setTemporary(false);
            userResource.resetPassword(credential);
    }
  }

  private void assignRoles(
      RealmResource realm,
      UserResource userResource,
      KeycloakSetupProperties.Setup.User userConfig) {
    userConfig
        .getRoles()
        .forEach(
            roleName -> {
              try {
                // First try client roles
                assignClientRole(realm, userResource, roleName);
              } catch (NotFoundException e) {
                // Fallback to realm roles
                assignRealmRole(realm, userResource, roleName);
              }
            });
  }

  private void assignClientRole(RealmResource realm, UserResource userResource, String roleName) {
    // Get client ID from your configuration or other service
    String clientId = "chimera_api_client"; // Should be injected/configurable
    ClientRepresentation client = realm.clients().findByClientId(clientId).get(0);
    RoleRepresentation role =
        realm.clients().get(client.getId()).roles().get(roleName).toRepresentation();

    userResource.roles().clientLevel(client.getId()).add(Collections.singletonList(role));
  }

  private void assignRealmRole(RealmResource realm, UserResource userResource, String roleName) {
    RoleRepresentation role = realm.roles().get(roleName).toRepresentation();
    userResource.roles().realmLevel().add(Collections.singletonList(role));
  }

  private void assignGroups(
      RealmResource realm,
      UserResource userResource,
      KeycloakSetupProperties.Setup.User userConfig) {
    userConfig
        .getGroups()
        .forEach(
            groupName -> {
              GroupRepresentation group =
                  realm.groups().groups().stream()
                      .filter(g -> g.getName().equals(groupName))
                      .findFirst()
                      .orElseThrow(() -> new RuntimeException("Group " + groupName + " not found"));

              userResource.joinGroup(group.getId());
            });
  }

  private String parseCreatedId(Response response) {
    String location = response.getLocation().toString();
    return location.substring(location.lastIndexOf('/') + 1);
  }
}
