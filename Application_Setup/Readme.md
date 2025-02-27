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

## Configuration Management

## Version Support

| Framework or Tool        | Version     | Support |
|--------------------------|-------------|---------|
| OS Architecture          | amd64       |         |
| OS - Linux               | Alpine:3.21 |         |
| Postgres                 | 16.7        |         |
| Spark                    | 3.5.4       |         |
| Python                   | 3.10        |         |
| Java                     | 17.0.12     |         |
| Redis                    | 7.4.0-v3    |         |
| Redis-Insight            | 2.66.0      |         |
| Kong                     | 3.9.0       |         |
| Prometheus               | 3.1.0       |         |
| Jaeger                   | 1.29.0      |         |
| Temporal server and tool | 1.26.2      |         |
| Temporal UI              | 2.35.0      |         |
| Vault                    | 1.18.4      |         |
| keycloack                | 26.0        |         |
| grafana                  | 7.5.5       |         |
| flink                    | 1.20.1      |         |
| Flink Kafka connector    | 3.4.0       |         |
| Flink CDC                | 3.3.0       |         |
| OpenSearch               | 1.3.20      |         |
| Superset                 | 1.3.2       |         |
| Otel-collector           | 0.38        |         |
| Trino                    | 471         |         |
| Datahub                  | ??          |         |
| Kafka                    | 3.7.2       |         |

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

### Deployment Instructions
## To deploy on server
- **Prerequisites**:
    - A Kubernetes cluster with `kubectl` configured.
    - Deploy the deploy_observability.sh script in the root directory of the project.
    - Run the script using the following command:
    ```bash
    ./deploy_observability.sh
    ```
## To deploy on local for local development and testing
- **Prerequisites**:
   - We can directly trigger the script to deploy the observability stack and other applications on the local machine. 
   - which does the docker-compose up
   - Deploy the run_docker_compose.sh script in the root directory of the project.
   - Run the script using the following command:
    ```bash
    ./run_docker_compose.sh
    ```
   
    - A Kubernetes cluster with `kubectl` configured.
    - Minikube installed for local testing.
    - Deploy the deploy_observability_local.sh script in the root directory of the project.
    - Run the script using the following command:
    ```bash
    ./deploy_observability_local.sh
    ```

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

## Future Enhancements

- Add Ingress resources for external access with custom domain names.
- Use Helm charts to parameterize and simplify the deployment process.
- Implement monitoring alerts and dashboards for Prometheus and Grafana.