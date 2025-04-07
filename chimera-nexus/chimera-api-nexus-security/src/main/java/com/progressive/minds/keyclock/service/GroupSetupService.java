package com.progressive.minds.keyclock.service;

import com.progressive.minds.keyclock.config.KeycloakSetupProperties;
import java.util.Collections;
import java.util.List;

import jakarta.ws.rs.NotFoundException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

@Service
public class GroupSetupService {

    private final Keycloak keycloak;

    public GroupSetupService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void createGroups(String realmName, List<KeycloakSetupProperties.Setup.Group> groups) {
        RealmResource realm = keycloak.realm(realmName);
        GroupsResource groupsResource = realm.groups();

        groups.forEach(groupConfig -> {
            List<GroupRepresentation> existing = groupsResource.groups(groupConfig.getName(), 0, 1);
            if (existing.isEmpty()) {
                GroupRepresentation group = new GroupRepresentation();
                group.setName(groupConfig.getName());
                groupsResource.add(group);

                GroupRepresentation createdGroup = groupsResource.groups().stream()
                        .filter(g -> g.getName().equals(groupConfig.getName()))
                        .findFirst().orElseThrow();

                groupConfig.getRoles().forEach(roleName -> {
                    RoleRepresentation role;
                    try {
                        role = realm.roles().get(roleName).toRepresentation();
                    } catch (NotFoundException e) {
                        RoleRepresentation newRole = new RoleRepresentation();
                        newRole.setName(roleName);
                        realm.roles().create(newRole);
                        role = realm.roles().get(roleName).toRepresentation();
                    }
                    groupsResource.group(createdGroup.getId())
                            .roles()
                            .realmLevel()
                            .add(Collections.singletonList(role));
                });
            }
        });
    }
}
