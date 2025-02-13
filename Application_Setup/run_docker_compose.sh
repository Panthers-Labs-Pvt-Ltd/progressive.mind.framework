#!/bin/bash

# Export environment variables
# Export the versions of the observability tools
export OTEL_VERSION=0.38.0
export JAEGER_VERSION=1.29.0
#export PROMETHEUS_VERSION=3.1.0
export POSTGRES_VERSION=13-alpine3.17
export GRAFANA_VERSION=7.5.5

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
export GRAFANA_PORT=3000
export POSTGRES_PORT=5432

# Run docker-compose
echo "Running docker-compose"
docker-compose -f docker-compose.yml up -d

# Check the status of the containers
echo "Checking the status of the containers"
docker-compose -f docker-compose.yml ps
