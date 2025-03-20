package com.progressive.minds.keyclock.service;

import com.progressive.minds.keyclock.config.KeycloakSetupProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleSetupService {

    private final Keycloak keycloak;

    public RoleSetupService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void createRoles(String realmName, List<KeycloakSetupProperties.Role> roles) {
        RealmResource realm = keycloak.realm(realmName);

        roles.forEach(roleConfig -> {
            if (realm.roles().get(roleConfig.getName()) == null) {
                RoleRepresentation role = new RoleRepresentation();
                role.setName(roleConfig.getName());
                role.setDescription(roleConfig.getDescription());
                realm.roles().create(role);
            }
        });
    }
}
