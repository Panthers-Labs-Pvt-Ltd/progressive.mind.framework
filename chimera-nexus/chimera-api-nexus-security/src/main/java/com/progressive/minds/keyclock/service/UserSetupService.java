package com.progressive.minds.keyclock.service;

import com.progressive.minds.keyclock.config.KeycloakSetupProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserSetupService {

    private final Keycloak keycloak;

    public UserSetupService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void createUsers(String realmName, List<KeycloakSetupProperties.User> users) {
        RealmResource realm = keycloak.realm(realmName);
        UsersResource usersResource = realm.users();

        users.forEach(userConfig -> {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(userConfig.getUsername());
            user.setEnabled(userConfig.isEnabled());

            // Create user
            usersResource.create(user);

            // Set password
            UserResource userResource = usersResource.get(user.getId());
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(userConfig.getPassword());
            credential.setTemporary(false);
            userResource.resetPassword(credential);

            // Assign roles
            userConfig.getRoles().forEach(roleName -> {
                RoleRepresentation role = realm.roles().get(roleName).toRepresentation();
                userResource.roles().realmLevel().add(Collections.singletonList(role));
            });

            // Assign groups
            userConfig.getGroups().forEach(groupName -> {
                GroupRepresentation group = realm.groups().groups().stream()
                        .filter(g -> g.getName().equals(groupName))
                        .findFirst().orElseThrow();
                userResource.joinGroup(group.getId());
            });
        });
    }
}

