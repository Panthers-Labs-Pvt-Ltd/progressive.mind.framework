package com.progressive.minds.keyclock.service;

import com.progressive.minds.keyclock.config.KeycloakSetupProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientSetupService {
    private final Keycloak keycloak;

    public ClientSetupService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void createClient(String realmName, KeycloakSetupProperties.Client clientConfig) {
        RealmResource realm = keycloak.realm(realmName);

        Optional<ClientRepresentation> existingClient = realm.clients().findAll().stream()
                .filter(c -> c.getClientId().equals(clientConfig.getClientId()))
                .findFirst();

        if (existingClient.isEmpty()) {
            ClientRepresentation client = new ClientRepresentation();
            client.setClientId(clientConfig.getClientId());
            client.setEnabled(clientConfig.isEnabled());
            client.setPublicClient(clientConfig.isPublicClient());
            client.setRedirectUris(clientConfig.getRedirectUris());
            client.setWebOrigins(clientConfig.getWebOrigins());
            realm.clients().create(client).close();
        }
    }
}
