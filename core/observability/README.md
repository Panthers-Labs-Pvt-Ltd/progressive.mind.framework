# Infrastructure and Application Observability

## Overview

This project provides a robust observability stack for monitoring and tracing applications and infrastructure. 
It leverages **Prometheus**, **Jaeger**, **Grafana**, and **OpenTelemetry** to deliver end-to-end visibility into the system. 
The data processing engines, **Apache Spark** and **Apache Flink**, are integrated for real-time and batch processing 
to support complex observability workflows.

## Key Features

- **Infrastructure Monitoring**: Collect and analyze metrics using Prometheus.
- **Application Tracing**: Distributed tracing with Jaeger and OpenTelemetry.
- **Visualization**: Interactive dashboards using Grafana.
- **Real-Time and Batch Processing**: Observability data processing pipelines using Apache Flink and Apache Spark.

## Stack Components

### 1. Prometheus
Prometheus is used to scrape metrics from various components of the system. 
These metrics are stored for real-time querying and long-term storage.

- **Use Case**: Metric collection and alerting.
- **Integration**: Metrics from services, nodes, and custom exporters.

### 2. Jaeger
Jaeger provides distributed tracing capabilities to understand and debug application performance across microservices.

- **Use Case**: End-to-end tracing of requests.
- **Integration**: OpenTelemetry for instrumentation.

### 3. Grafana
Grafana offers a powerful visualization layer to create dashboards and alerts based on data from Prometheus and other sources.

- **Use Case**: Dashboard visualization and alerting.
- **Integration**: Prometheus, Jaeger, and custom data sources.

### 4. OpenTelemetry
OpenTelemetry serves as a unified framework for instrumenting code and collecting traces and metrics.

- **Use Case**: Application instrumentation.
- **Integration**: Jaeger for traces, Prometheus for metrics.

### 5. Apache Spark
Apache Spark processes observability data for batch workloads such as periodic summarization or historical analysis.

- **Use Case**: Batch processing and ETL.
- **Integration**: Read/write data from observability storage.

### 6. Apache Flink
Apache Flink handles real-time observability data streams for low-latency processing and alerting.

- **Use Case**: Real-time stream processing.
- **Integration**: Process data streams from OpenTelemetry and Prometheus.

## Architecture Diagram
![Observability Architecture]

## Setup Instructions

### Prerequisites
- Docker and Docker Compose
- Java 17
- Apache Maven (for Spark/Flink applications)

### Steps to Deploy
1. **Clone the Repository**


# Observability Project Roadmap

This is the roadmap for the observability project visualized using a Mermaid Gantt chart.

```mermaid
gantt
    title Observability Project Roadmap
    dateFormat  YYYY-MM-DD
    axisFormat  %d-%b

    section Completed (Day 0-30)
    Setup Prometheus and Grafana :done, 2024-12-01, 2024-12-30

    section Current Goals (Day 30-60)
    Plug-and-Play Grafana Templates :active, 2024-12-31, 2025-01-30
    Metrics for Spark and Kubernetes :active, 2024-12-31, 2025-01-30
    Log Aggregation :logging, 2025-01-15, 2025-02-15

    section Future Goals (Day 60-90)
    Setup and integrate open telemetry :tracing, 2025-01-15, 2025-02-29
    Setup Alerts and Notifications :alerts, 2025-02-01, 2025-03-01
    AI/ML Integration :ai-ml, 2025-02-01, 2025-03-01
```
# Explanation of the Plan

Setup Prometheus and Grafana: Basic configuration and operational setup.

Plug-and-Play Grafana Templates: Develop reusable data models in Grafana to make it easier to create visualizations.
Add Metrics for Spark and Kubernetes: Include metrics relevant to your application (e.g., Spark job performance) and infrastructure (e.g., Kubernetes node health).

Setup Alerts and Notifications: Configure alerts in Grafana based on dashboard thresholds for real-time issue detection.
Integrate AI/ML Models: Plan and implement AI/ML features for predictive analysis and anomaly detection.



#### Discussions 
### Chapters & Topics:
## Integration and Monitoring Discussion
They explored the monitoring aspects, particularly how to display metrics in Grafana, 
and discussed the challenges of embedding Grafana dashboards into a ReactJS application. 
Abhinav highlighted the importance of understanding the pipeline and block levels for effective monitoring.
* Spark Integration and Configuration
* Grafana Dashboard Integration with ReactJS

## Embedding Grafana Dashboards
Abhinav Kumar explained how to embed Grafana dashboards into web applications, 
highlighting the tool's capabilities.
Abhinav also mentioned the option to publish snapshots and the new public dashboard feature currently in preview.

## Data Platform Observability and Monitoring Strategies
Abhinav Kumar outlined the steps necessary for building a data platform, 
including the creation of a pipeline and the implementation of health checks and distributed tracing. 
He highlighted key metrics for server and Kubernetes monitoring, such as memory consumption and pod status. 
Additionally, he stressed the importance of using open-source solutions like Prometheus for data collection and monitoring.

## Monitoring and Observability in Data Platforms

Observability and Data Insights Discussion
Abhinav Kumar discussed the significance of data models in observability, 
stressing that without proper source embedding, insights cannot be effectively derived. He explained how CPU distribution 
impacts pods and the necessity of prioritizing certain pods for better performance.

## Problem Diagnosis and Predictive Observability

Observability and Time in Distributed Systems
Abhinav Kumar explained that while time is often used to track events in distributed systems,
it is not the most reliable metric due to its relative nature. 
He stressed the need for accurate logging in Prometheus to identify patterns that could indicate impending issues, 
such as increasing latency. The goal is to predict problems before they occur, allowing for proactive measures.

## Observability and Data Integration Strategies
Abhinav Kumar expressed concerns about the effectiveness of existing observability tools,
noting that they often leave users to make sense of the data without clear guidance. He stressed the necessity for a 
robust instrumentation process and the integration of multiple data sources to enhance troubleshooting capabilities. 
Mohit Ranjan added that predictive analysis based on collected data is essential for improving system reliability.

## Data Insights and System Development Strategies
Abhinav Kumar discussed the lengthy process of developing a system that requires gathering data and insights, 
suggesting that initial solutions might be provided for free to build customer confidence. He outlined the roles of team members, 
with Rahul focusing on knowledge gathering and Manish on governance. 
Mohit Ranjan agreed on the importance of a structured approach to ensure the system's effectiveness.

## Key Questions:

* Is there any specific data model that I should create for my enterprise?
* How can we effectively predict and troubleshoot problems in our observability framework?







Observability
 - Infrastructure Observability
   - Network Observability
   - Storage Observability
   - Compute Observability
     - Virtualization Observability
     - Container Observability
     - Orchestration Observability
     - Automation Observability
     - Configuration Observability
 - Application Observability
   - Code Observability
   - Data Observability
   - Business Observability
   - Security Observability
   - Compliance Observability
   - Cost Observability
   - Performance Observability
   - Reliability Observability
   - Scalability Observability
   - Availability Observability
   - Latency Observability
   - Throughput Observability
   - Efficiency Observability
   - Utilization Observability
   - Capacity Observability
   - Error Observability
   - Fault Observability
   - Failure Observability
   - Incident Observability
   - Problem Observability
   - Change Observability
   - Configuration Observability
   - Deployment Observability
   - Release Observability
     - Version Observability
     - Patch Observability
     - Upgrade Observability
   - Version Observability
     - Patch Observability
     - Upgrade Observability
 - Data Observability
 - Business Observability
 - Security Observability
 - Compliance Observability
 - Cost Observability
 - Performance Observability
 - Reliability Observability
 - Scalability Observability
 - Availability Observability
 - Latency Observability
 - Throughput Observability
 - Efficiency Observability
 - Utilization Observability
 - Capacity Observability
 - Error Observability
 - Fault Observability
 - Failure Observability
 - Incident Observability
 - Problem Observability
 - Change Observability
 - Configuration Observability
 - Deployment Observability
 - Release Observability
 - Version Observability
    - Patch Observability
    - Upgrade Observability

 - Setup

