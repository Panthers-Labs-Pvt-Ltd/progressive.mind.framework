#!/bin/bash

# Set namespace
NAMESPACE="observability"

# Create the namespace if it doesn't exist
echo "Checking if namespace '$NAMESPACE' exists..."
kubectl get namespace $NAMESPACE || kubectl create namespace $NAMESPACE
echo "Namespace '$NAMESPACE' is ready."

# Apply common resources
echo "Applying common resources..."
kubectl apply -f common/configmap.yaml -n $NAMESPACE
kubectl apply -f common/secret.yaml -n $NAMESPACE
kubectl apply -f prometheus/prometheus-configmap.yaml -n $NAMESPACE
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
# Apply the prometheus service monitor
# kubectl apply -f prometheus/prometheus-service-monitor.yaml -n $NAMESPACE

# Apply Ingress resources
echo "Applying Ingress resources..."
kubectl apply -f ingress.yaml -n $NAMESPACE
echo "Ingress resources applied successfully."

# Call the metrics collector daemon JAR
echo "Starting metrics collector daemon..."
java -jar metrics-collector-daemon-1.0-SNAPSHOT.jar &

echo "Deployment complete. Resources are being created in the '$NAMESPACE' namespace."

kubectl get service -n "observability" -l app=$DIR -o jsonpath='{.items[0].metadata.name}'
kubectl get service $SERVICE_NAME -n "observability" -o jsonpath='{.spec.clusterIP}' 2>/dev/null || echo "No ClusterIP assigned"