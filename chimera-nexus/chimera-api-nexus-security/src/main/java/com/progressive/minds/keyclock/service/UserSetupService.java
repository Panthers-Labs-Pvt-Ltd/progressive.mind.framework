package com.progressive.minds.keyclock.service;

import com.progressive.minds.keyclock.config.KeycloakSetupProperties;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
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

    public void createUsers(String realmName, String clientId, List<KeycloakSetupProperties.Setup.User> users) {
        RealmResource realm = keycloak.realm(realmName);
        UsersResource usersResource = realm.users();

        for (KeycloakSetupProperties.Setup.User userConfig : users) {
            try {
                // Fix 1: Check if user exists properly
                if (!usersResource.searchByUsername(userConfig.getUsername(), true).isEmpty()) {
                    log.info("User {} already exists, skipping creation", userConfig.getUsername());
                    continue; // Use continue instead of break
                }

                String userId = createUser(usersResource, userConfig);
                UserResource userResource = usersResource.get(userId);

                setUserPassword(userResource, userConfig);
                assignRoles(realm, clientId, userResource, userConfig);
                assignGroups(realm, userResource, userConfig);

            } catch (Exception e) {
                log.error("Failed to create user {}: {}", userConfig.getUsername(), e.getMessage());
                throw new RuntimeException("User creation failed", e);
            }
        }
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
                }
                if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
                    throw new RuntimeException("User creation failed with status: " + response.getStatus());
                }
                return parseCreatedId(response);
            }
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

    private void assignRoles(RealmResource realm, String clientId, UserResource userResource,
                             KeycloakSetupProperties.Setup.User userConfig) {
        userConfig.getRoles().forEach(roleName -> {
            try {
                // Fix 2: Check client existence first
                List<ClientRepresentation> clients = realm.clients().findByClientId(clientId);
                if (clients.isEmpty()) {
                    throw new RuntimeException("Client " + clientId + " not found");
                }

                // Fix 3: Handle both role types with existence checks
                try {
                    assignClientRole(realm, clientId, userResource, roleName);
                } catch (NotFoundException e) {
                    assignRealmRole(realm, userResource, roleName);
                }
            } catch (NotFoundException e) {
                // Handle missing role by creating it
                createRealmRoleIfMissing(realm, roleName);
                assignRealmRole(realm, userResource, roleName);
            }
        });
    }
    private void createRealmRoleIfMissing(RealmResource realm, String roleName) {
        try {
            realm.roles().get(roleName).toRepresentation();
        } catch (NotFoundException e) {
            RoleRepresentation newRole = new RoleRepresentation();
            newRole.setName(roleName);
            realm.roles().create(newRole);
        }
    }

    private void assignClientRole(RealmResource realm, String clientId, UserResource userResource, String roleName) {
        ClientRepresentation client = realm.clients().findByClientId(clientId).get(0);
        ClientResource clientResource = realm.clients().get(client.getId());

        // Check if client role exists
        try {
            RoleRepresentation role = clientResource.roles().get(roleName).toRepresentation();
            userResource.roles().clientLevel(client.getId()).add(List.of(role));
        } catch (NotFoundException e) {
            // Create client role if missing
            RoleRepresentation newRole = new RoleRepresentation();
            newRole.setName(roleName);
            clientResource.roles().create(newRole);
            RoleRepresentation createdRole = clientResource.roles().get(roleName).toRepresentation();
            userResource.roles().clientLevel(client.getId()).add(List.of(createdRole));
        }
    }

    private void assignRealmRole(RealmResource realm, UserResource userResource, String roleName) {
        try {
            RoleRepresentation role = realm.roles().get(roleName).toRepresentation();
            userResource.roles().realmLevel().add(List.of(role));
        } catch (NotFoundException e) {
            createRealmRoleIfMissing(realm, roleName);
            RoleRepresentation role = realm.roles().get(roleName).toRepresentation();
            userResource.roles().realmLevel().add(List.of(role));
        }
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
