package com.progressive.minds.keyclock.service;

import com.progressive.minds.keyclock.config.KeycloakSetupProperties;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoleSetupService {
    private final Keycloak keycloak;

    public RoleSetupService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

  public void createRoles(
      String realmName, String clientId, List<KeycloakSetupProperties.Setup.Role> roles) {
        RealmResource realm = keycloak.realm(realmName);
    ClientResource clientResource = getClientResource(realm, clientId);
    RolesResource rolesResource = clientResource.roles();

    roles.forEach(
        role -> {
          try {
            createRoleIfNotExists(rolesResource, role);
          } catch (ResteasyWebApplicationException e) {
            handleRoleCreationError(role, e);
          }
        });
  }

  private ClientResource getClientResource(RealmResource realm, String clientId) {
    List<ClientRepresentation> clients = realm.clients().findByClientId(clientId);
    if (clients.isEmpty()) {
      throw new IllegalStateException(
          "Client " + clientId + " not found in realm " + realm.toRepresentation().getRealm());
    }
    return realm.clients().get(clients.get(0).getId());
  }

  private void createRoleIfNotExists(
      RolesResource rolesResource, KeycloakSetupProperties.Setup.Role roleConfig) {
    String roleName = roleConfig.getName();

    if (!roleExists(rolesResource, roleName)) {
      RoleRepresentation role = new RoleRepresentation();
      role.setName(roleName);
      role.setDescription(roleConfig.getDescription());
      role.setClientRole(true);

      rolesResource.create(role);
      log.info("Created client role: {}", roleName);
    } else {
      log.debug("Skipping existing client role: {}", roleName);
    }
  }

  private boolean roleExists(RolesResource rolesResource, String roleName) {
    try {
      return rolesResource.get(roleName) != null;
    } catch (NotFoundException ex) {
      return false;
    }
  }

  private void handleRoleCreationError(
      KeycloakSetupProperties.Setup.Role role, ResteasyWebApplicationException e) {
    if (e.getResponse().getStatus() == 409) {
      log.warn("Role {} already exists - skipping creation", role.getName());
    } else {
      log.error("Failed to create role {}: {}", role.getName(), e.getMessage());
      throw new RuntimeException("Role creation failed for: " + role.getName(), e);
    }
  }
}
