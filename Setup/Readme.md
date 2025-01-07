# Chimera Setup Project Structure

This module contains Kubernetes manifests to deploy and manage a monitoring stack, 
including Prometheus, Grafana, and Jaeger, within a Kubernetes cluster. 
The setup is modular, with each component having its own directory for better organization and maintainability.

## Directory Structure

```
Chimera/setup/
├── common/
│   ├── configmap.yaml
│   ├── secret.yaml
├── grafana/
│   ├── deployment.yaml
│   ├── service.yaml
├── prometheus/
│   ├── deployment.yaml
│   ├── service.yaml
│   ├── prometheus-config.yaml
├── jaeger/
│   ├── deployment.yaml
│   ├── service.yaml
```

### 1. `common/`
This directory contains shared configurations used by multiple components.

- **`configmap.yaml`**: Stores shared environment variables such as ports and other configurations.
- **`secret.yaml`**: Stores sensitive data such as passwords. For example, Grafana admin credentials are defined here.

### 2. `grafana/`
This directory contains manifests for deploying and exposing Grafana.

- **`deployment.yaml`**: Defines the Grafana Deployment, specifying the container image, environment variables, and ports.
- **`service.yaml`**: Defines a Kubernetes Service for Grafana, exposing it internally or externally based on the configuration.

### 3. `prometheus/`
This directory contains manifests for deploying and exposing Prometheus.

- **`deployment.yaml`**: Defines the Prometheus Deployment, specifying the container image, environment variables, and ports.
- **`service.yaml`**: Defines a Kubernetes Service for Prometheus, exposing it internally or externally based on the configuration.
- **`prometheus-config.yaml`**: Provides the Prometheus configuration file (`prometheus.yml`) via a ConfigMap.

### 4. `jaeger/`
This directory contains manifests for deploying and exposing Jaeger.

- **`deployment.yaml`**: Defines the Jaeger Deployment, specifying the container image, environment variables, and ports.
- **`service.yaml`**: Defines a Kubernetes Service for Jaeger, exposing it internally or externally based on the configuration.

---

## Configuration Management

### ConfigMap (`common/configmap.yaml`)
Stores shared, non-sensitive configuration values. Example:

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: shared-config
  namespace: monitoring
data:
  GRAFANA_PORT: "3000"
  PROMETHEUS_PORT: "9090"
  JAEGER_COLLECTOR_HTTP_PORT: "14268"
  JAEGER_QUERY_HTTP_PORT: "16686"
```

### Secrets (`common/secret.yaml`)
Stores sensitive data. Example:

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: grafana-secret
  namespace: monitoring
type: Opaque
data:
  GF_SECURITY_ADMIN_PASSWORD: <base64-encoded-password>
```

Encode the password using:
```bash
echo -n 'your-password' | base64
```

---

## Deployment Instructions

1. **Create the Namespace:**
   ```bash
   kubectl create namespace observability
   ```

2. **Apply the Common Configurations:**
   ```bash
   kubectl apply -f setup/common/configmap.yaml
   kubectl apply -f setup/common/secret.yaml
   ```

3. **Deploy Grafana:**
   ```bash
   kubectl apply -f setup/grafana/deployment.yaml
   kubectl apply -f setup/grafana/service.yaml
   ```

4. **Deploy Prometheus:**
   ```bash
   kubectl apply -f setup/prometheus/deployment.yaml
   kubectl apply -f setup/prometheus/service.yaml
   kubectl apply -f setup/prometheus/prometheus-config.yaml
   ```

5. **Deploy Jaeger:**
   ```bash
   kubectl apply -f setup/jaeger/deployment.yaml
   kubectl apply -f setup/jaeger/service.yaml
   ```

---

## Accessing the Applications

- **Grafana:** Access via the Service (`ClusterIP`, `NodePort`, or `LoadBalancer`) configured in `grafana/service.yaml`. Example for `NodePort`:
  ```
  http://<node-ip>:<node-port>
  ```

- **Prometheus:** Similar to Grafana, access via the Service defined in `prometheus/service.yaml`.

- **Jaeger:** Access the UI and APIs via the Service defined in `jaeger/service.yaml`.

---

## Customization

- Update `common/configmap.yaml` to change shared configuration values such as ports.
- Update `common/secret.yaml` for sensitive data such as passwords.
- Modify the `deployment.yaml` and `service.yaml` files in each component's directory to adjust replicas, resource limits, or Service types.

---

## Future Enhancements

- Add Ingress resources for external access with custom domain names.
- Use Helm charts to parameterize and simplify the deployment process.
- Implement monitoring alerts and dashboards for Prometheus and Grafana.