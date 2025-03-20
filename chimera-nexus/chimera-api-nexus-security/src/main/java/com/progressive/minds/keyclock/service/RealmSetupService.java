package com.progressive.minds.keyclock.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RealmSetupService {

    private final Keycloak keycloak;

    public RealmSetupService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void createRealm(String realmName) {
        Optional<RealmRepresentation> existingRealm = keycloak.realms().findAll().stream()
                .filter(r -> r.getRealm().equals(realmName))
                .findFirst();

        if (existingRealm.isEmpty()) {
            RealmRepresentation realm = new RealmRepresentation();
            realm.setRealm(realmName);
            realm.setEnabled(true);
            keycloak.realms().create(realm);
        }
    }

    public void deleteRealm(String realmName) {
        keycloak.realms().realm(realmName).remove();
    }
}
