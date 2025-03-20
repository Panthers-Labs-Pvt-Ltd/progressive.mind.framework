package com.progressive.minds.keyclock.service;

import com.progressive.minds.keyclock.config.KeycloakSetupProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GroupSetupService {

    private final Keycloak keycloak;

    public GroupSetupService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void createGroups(String realmName, List<KeycloakSetupProperties.Group> groups) {
        RealmResource realm = keycloak.realm(realmName);
        GroupsResource groupsResource = realm.groups();

        groups.forEach(groupConfig -> {
            List<GroupRepresentation> existing = groupsResource.groups(groupConfig.getName(), 0, 1);
            if (existing.isEmpty()) {
                GroupRepresentation group = new GroupRepresentation();
                group.setName(groupConfig.getName());
                groupsResource.add(group);

                // Assign roles to group
                GroupRepresentation createdGroup = groupsResource.groups().stream()
                        .filter(g -> g.getName().equals(groupConfig.getName()))
                        .findFirst().orElseThrow();

                groupConfig.getRoles().forEach(roleName -> {
                    RoleRepresentation role = realm.roles().get(roleName).toRepresentation();
                    groupsResource.group(createdGroup.getId()).roles().realmLevel().add(Collections.singletonList(role));
                });
            }
        });
    }
}
