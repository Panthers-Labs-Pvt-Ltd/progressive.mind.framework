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

## Ownership

* API Ecosystem - **Vivek Tiwari**
  - APISIX / Kong
  - Keycloak
  - Vault
  - GRPC
  - GraphQL
  - REST
* Observability - **Mohit Ranjan**
  - Prometheus and its ecosystem (End to end)
  - Grafana and its ecosystem (End to end)
  - Jaeger and Fluentd (End to end)
  - Otel-collector (End to end)
  - Infra, Application, and Data Actionable Insights (End to end)
  - Notification and Alerts (End to end)
* Data Management - **Manish Kumar**
  - Datahub (End to end)
  - Trino (End to end)
  - OpenSearch (End to end)
  - Great Expectations (Usage) and AWS Deeque (End to end)
  - Redis and Redis Insight (Usage)
  - Postgres (Usage)
  - Druid/Pinot (Usage)
  - neo4j (Usage)
  - DuckDB (Usage)
  - Superset (End to end)
  - CI-CD and DataOps (End to end)
* Data Processing - **Prashant Kumar**
  - Spark (End to end)
  - Temporal (End to end)
  - Iceberg (End to end) && Apache Amoro
  - Fugue (End to end)
  - Parquet (End to end)
  - XTable (End to end)
  - Hive and REST catalog (Polaris) Metastore (End to end)
  - Kafka (Usage)
  - Flink (End to end)
  - Debezium and Flink CDC (End to end)
* AI / ML Platform - **Rahul Ranjan**
  - Ray
  - MLLib
  - SLM / LLM
  - Agentic AI
  - neo4J
  - Python ecosystem
  - AIOps

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
    - Deploy the `deploy_observability.sh` script in the root directory of the project.
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
    - Deploy the `deploy_observability_local.sh` script in the root directory of the project.
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

## Key learning for docker compose

### restart

The restart policy in a docker-compose.yml file defines the restart behavior of a container when it exits. It determines under what conditions Docker will automatically restart a stopped container. Here are the available options for the restart policy:

* **no**: The container will not be restarted automatically if it stops. 
* **always**: The container will always be restarted regardless of the exit status. This is useful for long-running services. 
* **on-failure**: The container will only be restarted if it exits with a non-zero exit status, indicating an error or failure. 
* **unless-stopped**: The container will always be restarted except when it is explicitly stopped by the user. This is similar to always, but it won't restart the container after it is manually stopped.
* Example
```yaml
version: '3.8'

services:
  web:
    image: nginx:latest
    restart: always
    ports:
      - "80:80"
```

Using the unless-stopped restart policy in Docker Compose means that the container will always be restarted except when it is explicitly stopped by the user. This policy is similar to always, but it won't restart the container after it is manually stopped.

However, in the case of a software bug causing the container to fail, this policy can have certain consequences:
* Infinite Restart Loop: If the container fails due to a bug, it will keep restarting indefinitely, potentially leading to resource exhaustion (e.g., CPU, memory) and affecting other services running on the same host. 
* Log Flooding: Continuous restarts can flood the logs with repetitive error messages, making it harder to diagnose the root cause of the issue. 
* Potential Data Corruption: Frequent restarts can lead to inconsistent states and potential data corruption, especially if the container writes to a database or file system. 
* Delayed Resolution: Automated restarts may give the false impression that the service is running correctly, delaying the detection and resolution of the underlying issue.

To mitigate these risks, consider using additional strategies such as:
* Health Checks: Implement health checks to monitor the application's health and restart it only when necessary. 
* Retry Logic: Incorporate retry logic in your application to handle transient errors gracefully. 
* Alerting: Set up monitoring and alerting (Prometheus and AlertManager) to notify you of frequent container restarts.

```yaml
version: '3.8'

services:
  web:
    image: nginx:latest
    restart: unless-stopped
    healthcheck:
      test: ["CMD-SHELL", "curl -f http://localhost || exit 1"]
      interval: 30s
      timeout: 10s
      retries: 3
    ports:
      - "80:80"
```

### stop_signal

The stop_signal parameter in a docker-compose.yml file specifies which system signal to send to a container to stop it. This is useful if your application needs a specific signal to perform a clean shutdown or to handle any cleanup tasks before the container stops.

By default, Docker sends the SIGTERM signal to stop a container, followed by SIGKILL if the container doesn't stop within a grace period. However, you can customize this behavior with the stop_signal parameter.

### ipc

* **Purpose**: Sets the IPC namespace mode for the container.
* **Use Case**: Useful to control how processes in the container share memory and other IPC resources.
* Options:
  * **ipc: "host"**: The container uses the host's IPC namespace.
  * **ipc: "container:<name_or_id>"**: Shares the IPC namespace with another container, allowing processes in both containers to communicate using shared memory.
  * **ipc: "shareable"**: The container shares the host's IPC namespace.
* Example:

```yaml
services:
  web:
    image: nginx:latest
    ipc: "host"
```

### isolation

* **Purpose**: Sets the container's isolation level for **Windows containers**.
* **Use Case**: Useful for specifying the container's isolation level for Windows containers to ensure security and resource management, such as hyperv, process, or default.
* Options:
  * **isolation: "hyperv"**: Uses Hyper-V isolation (each container has its own instance of Windows kernel).
  * **isolation: "process"**: Uses process isolation (containers share the kernel with the host).
  * **isolation: "default"**: Uses the default isolation technology configured in Docker daemon.

> Please note In Unix and Linux systems, container isolation is achieved through namespaces and control groups (cgroups). These mechanisms provide process isolation, resource limitation, and security for containers. Here are some key components:
> * Namespaces: Provide isolated instances of global system resources, such as process IDs, network interfaces, and filesystem mounts. Each container gets its own set of namespaces, ensuring it operates independently of other containers.
> * Cgroups: Manage and limit the resources (CPU, memory, disk I/O, etc.) that a container can use. Cgroups ensure that containers do not interfere with each other's resource usage. 
> * Seccomp: Provides system call filtering to enhance security by restricting the system calls a container can make.
> * AppArmor/SELinux: Enforce security policies to further isolate containers and control their access to system resources.

### cgroup

Control Groups, commonly known as cgroups, are a Linux kernel feature used to manage, limit, and prioritize resources such as CPU, memory, disk I/O, and network for a group of processes. They are particularly useful in containerized environments like Docker for the following purposes:

* **Resource Isolation**: Cgroups isolate resource usage between different groups of processes, ensuring that one group doesn't consume all available resources and affect other processes.
* **Resource Limitation**: Cgroups allow you to set limits on resources for processes. For example, you can limit the amount of CPU time, memory usage, or disk I/O a container can use.
* **Resource Allocation**: Cgroups enable you to allocate specific resources to different groups of processes based on their requirements and priorities.
* **Resource Monitoring**: Cgroups provide mechanisms to monitor the resource usage of processes, helping in identifying and managing resource bottlenecks.
* **Improved Performance and Stability**: By managing and controlling resource usage, cgroups ensure better performance and stability for applications running on the same host.
* Example Usage in Docker

```yaml
version: '3.8'

services:
  web:
    image: nginx:latest
    deploy:
      resources:
        limits:
          cpus: "0.5"
          memory: "512M"
        reservations:
          cpus: "0.25"
          memory: "256M"
    ports:
    - "80:80"
```
### cgroup_parent

* **Purpose**: Specifies the parent cgroup for a container, allowing you to organize and manage resources hierarchically.
* **Use Case**: Useful when you want to group containers under a specific cgroup to manage their resources together.
* Example:

```yaml
services:
  web:
  image: nginx:latest
  cgroup_parent: /docker/my_custom_group
```

### cap_add and cap_drop

* **Purpose**: Add or drop Linux capabilities to or from a container.
* **Use Case**: Fine-tune the permissions and capabilities a container has, enhancing security by granting only the necessary capabilities.
* Example (cap_add):

```yaml
services:
  web:
    image: nginx:latest
  cap_add:
    - NET_ADMIN
```
* Example (cap_drop):

```yaml
services:
web:
image: nginx:latest
cap_drop:
- MKNOD
```

### dns

* **Purpose**: Set custom DNS servers for a container.
* **Use Case**: Override the default DNS servers used by a container provided by the Docker daemon, useful for custom network configurations or resolving internal domain names.
* Example:

```yaml
services:
  web:
    image: nginx:latest
    dns: # Google's public DNS servers
      - 8.8.8.8 
      - 8.8.4.4
```

### dns_search

* **Purpose**: Set custom DNS search domains for a container.
* **Use Case**: Specify search domains to be used for DNS resolution within the container, useful for resolving short hostnames or internal domain names to fully qualified domain names (FQDNs).
* Example:

```yaml
services:
  web:
    image: nginx:latest
    dns_search:
      - example.com
      - subdomain.example.com
```

### dns_opt

* **Purpose**: Set additional DNS options for the container's /etc/resolv.conf file.
* **Use Case**: Customize DNS resolution behavior, such as setting the timeout, or attempts, or enabling DNSSEC validation.
* Example:

```yaml
services:
  web:
    image: nginx:latest
    dns_opt:
      - timeout:2
      - attempts:3
```

### domainname

* **Purpose**: Set the domain name for a container.
* **Use Case**: Useful when you want to specify the domain name for a container, which can be used for internal DNS resolution, particularly in multi-container applications.
* Example:

```yaml
services:
  web:
    image: nginx:latest
    domainname: example.com
```

### hostname

* **Purpose**: Set the hostname for a container.
* **Use Case**: Useful when you want to specify a custom hostname for the container.
* Example:

```yaml
services:
  web:
    image: nginx:latest
    hostname: my-nginx-container
```

### extra_hosts

* **Purpose**: Add custom hostnames to the container's /etc/hosts file.
* **Use Case**: Map custom hostnames to IP addresses that are not in DNS, useful for testing or accessing internal services.
* Example:

```yaml
services:
  web:
    image: nginx:latest
    extra_hosts:
      - "host.docker.internal:host-gateway"
```

### cpu_count

* **Purpose**: Limits the number of CPUs available to a container.
* **Use Case**: Control the CPU resources a container can use, improving performance and resource allocation.
* Example:

```yaml
services:
  web:
    image: nginx:latest
    deploy:
      resources:
        limits:
          cpus: "2"
```


100GB

**When we prioritize performance**
* Fetch from metadata of the block size used - 128MB / 256MB
* Best possible scenario is - if we are able to read all the dataset in a single go
  * 100GB*1028MB = 102800MB
  * 102800MB/128MB = 803.125 cores
* Ideal configure for an executor - 4 cores.
* So, number of executors - 803.125/4 = 200.78 executors
* 100GB/compression factor. Assume 25% compressed, then 100/.25 = 400GB
* Per executor - 400GB/200 = 2GB
* Using unified memory - 2GB*2 = 4GB

**When we prioritize cost**
