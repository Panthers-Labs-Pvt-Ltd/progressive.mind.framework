package com.progressive.minds.keyclock.service;

import com.progressive.minds.keyclock.config.KeycloakSetupProperties;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientSetupService {
    private final Keycloak keycloak;

    public ClientSetupService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

  public void createClient(String realmName, KeycloakSetupProperties.Setup.Client clientConfig) {
        RealmResource realm = keycloak.realm(realmName);

    try {
      // Create or update base client
      ClientRepresentation clientRep = createClientRepresentation(clientConfig);
      String clientId = createOrUpdateClient(realm, clientRep);
      ClientResource clientResource = realm.clients().get(clientId);

      // Configure client components
      configureClientCredentials(clientResource, clientConfig);
      configureProtocolMappers(clientResource, clientConfig.getProtocolMappers());

    } catch (ResteasyWebApplicationException e) {
      handleClientConfigurationError(clientConfig.getClientId(), e);
    }
  }

  private ClientRepresentation createClientRepresentation(
      KeycloakSetupProperties.Setup.Client config) {
    ClientRepresentation client = new ClientRepresentation();
    client.setClientId(config.getClientId());
    client.setName(config.getClientId());
    client.setEnabled(config.isEnabled());
    client.setPublicClient(config.isPublicClient());
    client.setRedirectUris(config.getRedirectUris());
    client.setWebOrigins(config.getWebOrigins());
    client.setAdminUrl(config.getRedirectUris().get(0));
    client.setStandardFlowEnabled(config.isStandardFlowEnabled());
    client.setDirectAccessGrantsEnabled(config.isDirectAccessGrantsEnabled());
    client.setAttributes(config.getAttributes());
    return client;
  }

  private String createOrUpdateClient(RealmResource realm, ClientRepresentation client) {
    List<ClientRepresentation> existingClients =
        realm.clients().findByClientId(client.getClientId());

    if (existingClients.isEmpty()) {
      try (Response response = realm.clients().create(client)) {
        String createdId = parseCreatedId(response);
        log.info("Created new client: {}", client.getClientId());
        return createdId;
      }
    } else {
      String existingId = existingClients.get(0).getId();
      ClientResource clientResource = realm.clients().get(existingId);
      clientResource.update(client);
      log.info("Updated existing client: {}", client.getClientId());
      return existingId;
    }
  }

  private void configureClientCredentials(
      ClientResource clientResource, KeycloakSetupProperties.Setup.Client config) {
    if (!config.isPublicClient()) {
      CredentialRepresentation cred = new CredentialRepresentation();
      cred.setType(CredentialRepresentation.SECRET);
      cred.setValue(generateClientSecret());
      clientResource.invalidateRotatedSecret();
      log.debug(
          "Configured credentials for client: {}", clientResource.toRepresentation().getClientId());
        }
  }

  private void configureProtocolMappers(
      ClientResource clientResource,
      List<KeycloakSetupProperties.Setup.Client.ProtocolMapper> mappers) {
    ProtocolMappersResource mappersResource = clientResource.getProtocolMappers();

    mappers.forEach(
        mapper -> {
          if (!protocolMapperExists(mappersResource, mapper.getName())) {
            createProtocolMapper(mappersResource, mapper);
          } else {
            log.debug("Skipping existing protocol mapper: {}", mapper.getName());
          }
        });
    }

  private boolean protocolMapperExists(ProtocolMappersResource mappersResource, String mapperName) {
    return mappersResource.getMappers().stream().anyMatch(m -> m.getName().equals(mapperName));
  }

  private void createProtocolMapper(
      ProtocolMappersResource mappersResource,
      KeycloakSetupProperties.Setup.Client.ProtocolMapper mapperConfig) {
    try {
      ProtocolMapperRepresentation mapper = new ProtocolMapperRepresentation();
      mapper.setName(mapperConfig.getName());
      mapper.setProtocol(mapperConfig.getProtocol());
      mapper.setProtocolMapper(mapperConfig.getProtocolMapper());
      mapper.setConfig(mapperConfig.getConfig());

        try (Response response = mappersResource.createMapper(mapper)) {
            log.info("createMapper response: {}", response.getStatus());
        }
    } catch (ResteasyWebApplicationException e) {
      if (e.getResponse().getStatus() == Response.Status.CONFLICT.getStatusCode()) {
        log.warn("Protocol mapper {} already exists", mapperConfig.getName());
      } else {
        throw new RuntimeException(
            "Failed to create protocol mapper: " + mapperConfig.getName(), e);
      }
    }
  }

  private void handleClientConfigurationError(String clientId, ResteasyWebApplicationException e) {
    if (e.getResponse().getStatus() == Response.Status.CONFLICT.getStatusCode()) {
      log.warn("Client {} configuration conflict: {}", clientId, e.getMessage());
    } else {
      log.error("Client configuration failed for {}: {}", clientId, e.getMessage());
      throw new RuntimeException("Client configuration failed for: " + clientId, e);
    }
  }

  private String parseCreatedId(Response response) {
    String location = response.getLocation().toString();
    return location.substring(location.lastIndexOf('/') + 1);
  }

  private String generateClientSecret() {
    return UUID.randomUUID().toString().replace("-", "")
        + Long.toHexString(System.currentTimeMillis());
  }
}
