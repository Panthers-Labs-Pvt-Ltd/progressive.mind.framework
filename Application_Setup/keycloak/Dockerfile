FROM quay.io/keycloak/keycloak:latest as builder

# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# Configure database vendor (this is static and won't change)
ENV KC_DB=postgres

WORKDIR /opt/keycloak

# Generate a self-signed certificate for demo purposes only
RUN keytool -genkeypair \
    -storepass password \
    -storetype PKCS12 \
    -keyalg RSA \
    -keysize 2048 \
    -dname "CN=server" \
    -alias server \
    -ext "SAN:c=DNS:localhost,IP:127.0.0.1" \
    -keystore conf/server.keystore

# Build Keycloak for production
RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:latest

# Copy built Keycloak from the builder stage
COPY --from=builder /opt/keycloak/ /opt/keycloak/

# Expose the port Keycloak will listen on
EXPOSE 8080

# Entrypoint script for Keycloak
ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
