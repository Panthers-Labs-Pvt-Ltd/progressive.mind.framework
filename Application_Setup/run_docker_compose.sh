#!/bin/bash

set -xeuo pipefail

# Export environment variables
# Export the versions of the observability tools
# Initalize Vault first to fetch the secrets
export OTEL_VERSION=0.38.0
export JAEGER_VERSION=1.29.0
export PROMETHEUS_VERSION=v3.1.0
export POSTGRES_VERSION=16.7
export GRAFANA_VERSION=7.5.5
export VAULT_VERSION=1.18.4

# Fetch credentials from Vault
# KC_DB_USERNAME=$(vault kv get -field=KC_DB_USERNAME secret/keycloak)
# KC_DB_PASSWORD=$(vault kv get -field=KC_DB_PASSWORD secret/keycloak)
# Export environment variables
export KC_DB_USERNAME=owner_chimera_application
export KC_DB_PASSWORD=owner_chimera_application

# Export the ports
export OTEL_GRPC_PORT=4317
export OTEL_HTTP_PORT=4318
export ZIPKIN_PORT=55681
export PROMETHEUS_PORT=8888
export JAEGER_UI_PORT=16686
export JAEGER_HTTP_PORT=14268
export JAEGER_GRPC_PORT=14250
export ZIPKIN_API_PORT=9411
export PROMETHEUS_UI_PORT=9090
export GRAFANA_PORT=6000
export POSTGRES_PORT=5432
export OPENSEARCH_VERSION=1.3.20
export TEMPORAL_VERSION=1.26.2
export TEMPORAL_ADMINTOOLS_VERSION=1.26.2
export TEMPORAL_UI_VERSION=2.35.0


# Get Docker version
docker_version=$(docker --version | awk '{print $3}' | sed 's/,//')

# Parse major, minor, and patch version
IFS='.' read -r major minor patch <<< "$docker_version"

# Check if Docker version is 20.10.0 or higher
if (( major > 20 || (major == 20 && minor >= 10) )); then
  # Run docker-compose
  echo "Running docker compose"
  docker compose -f docker-compose.yml up -d

  # Check the status of the containers
  echo "Checking the status of the containers"
  docker compose -f docker-compose.yml ps
else
  # Run docker-compose
  echo "Running docker-compose"
  docker-compose -f docker-compose.yml up -d

  # Check the status of the containers
  echo "Checking the status of the containers"
  docker-compose -f docker-compose.yml ps
fi
