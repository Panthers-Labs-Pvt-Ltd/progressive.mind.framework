#!/bin/bash

# Log file
LOG_FILE="deploy_observability.log"
exec > >(tee -i $LOG_FILE)
exec 2>&1

# Set namespace
NAMESPACE="observability"

# Export host and port for the metrics collector daemon
export METRICS_COLLECTOR_HOST="0.0.0.0"
export METRICS_COLLECTOR_PORT="8083"

echo "Metrics collector daemon host: $METRICS_COLLECTOR_HOST and port: $METRICS_COLLECTOR_PORT"

# Create the namespace if it doesn't exist
echo "Checking if namespace '$NAMESPACE' exists..."
kubectl get namespace $NAMESPACE || kubectl create namespace $NAMESPACE
echo "Namespace '$NAMESPACE' is ready."

# Apply common resources
echo "Applying common resources..."
kubectl apply -f common/configmap.yaml -n $NAMESPACE
kubectl apply -f common/secret.yaml -n $NAMESPACE
kubectl apply -f prometheus/prometheus-config.yaml -n $NAMESPACE
echo "Common resources applied successfully."

# Apply Grafana dashboard
echo "Applying Grafana dashboard..."
kubectl apply -f grafana/grafana-dashboards-configmap.yaml -n $NAMESPACE
echo "Grafana dashboard applied successfully."

# Define the directories
DIRECTORIES=("grafana" "prometheus" "jaeger")

# Loop through each directory and apply its YAML files
for DIR in "${DIRECTORIES[@]}"; do
  echo "Applying resources in $DIR..."
  kubectl apply -f $DIR/deployment.yaml -n $NAMESPACE
  kubectl apply -f $DIR/service.yaml -n $NAMESPACE

  # Fetch and log the service details
  SERVICE_NAME=$(kubectl get service -n $NAMESPACE -l app=$DIR -o jsonpath='{.items[0].metadata.name}')
  SERVICE_PORT=$(kubectl get service $SERVICE_NAME -n $NAMESPACE -o jsonpath='{.spec.ports[0].nodePort}' 2>/dev/null || echo "Not a NodePort service")
  SERVICE_CLUSTER_IP=$(kubectl get service $SERVICE_NAME -n $NAMESPACE -o jsonpath='{.spec.clusterIP}' 2>/dev/null || echo "No ClusterIP assigned")
  SERVICE_EXTERNAL_IP=$(kubectl get service $SERVICE_NAME -n $NAMESPACE -o jsonpath='{.status.loadBalancer.ingress[0].ip}' 2>/dev/null || echo "No external IP assigned")

  echo "$DIR service details:"
  echo "  Namespace: $NAMESPACE"
  echo "  Cluster IP: $SERVICE_CLUSTER_IP"
  echo "  External IP: $SERVICE_EXTERNAL_IP"
  echo "  NodePort: $SERVICE_PORT"
  if [ "$SERVICE_PORT" != "Not a NodePort service" ]; then
    echo "  URL: http://$SERVICE_CLUSTER_IP:$SERVICE_PORT/"
  fi
  echo
done

# Apply Ingress resources
echo "Applying Ingress resources..."
kubectl apply -f ingress.yaml -n $NAMESPACE
echo "Ingress resources applied successfully."

# Call the metrics collector daemon JAR
echo "Starting metrics collector daemon..."
if pgrep -f "metrics-collector-daemon-1.0-SNAPSHOT.jar" > /dev/null; then
  echo "Metrics collector daemon is already running."
else
  if java -jar metrics-collector-daemon-1.0-SNAPSHOT.jar $METRICS_COLLECTOR_HOST $METRICS_COLLECTOR_PORT &> /dev/null; then
    echo "Metrics collector daemon started successfully."
  else
    echo "Failed to start metrics collector daemon." >&2
  fi
fi
echo "Deployment complete. Resources are being created in the '$NAMESPACE' namespace."

kubectl get service -n $NAMESPACE -l app=$DIR -o jsonpath='{.items[0].metadata.name}'
kubectl get service $SERVICE_NAME -n $NAMESPACE -o jsonpath='{.spec.clusterIP}' 2>/dev/null || echo "No ClusterIP assigned"