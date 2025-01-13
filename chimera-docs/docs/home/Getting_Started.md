# User Journey

## Client Onboarding

**Account Creation**: Client sign up Non-Disclosure Agreement.

Before the client interacts with Chimera, **Panthers Labs backend team** need to deploy Chimera services on Client Infrastructure. Here are the steps to be completed for readiness -

1. **Choosing Client Infrastructure** - Cloud or On-Premise
2. **Default Setup**: The backend team sets up default configurations for the client's organization - System Accounts, Default Groups and Accesses, Certificates, and other components for Kubernetes, AIML (Ray), Observability (Prometheus and Grafana), Orchestration(Temporal), API, and Metadata Manager Cluster with default project/namespace/domain. **Should this be on a single docker-compose or shell script?**
3. **Connecting to Key Services**: The backend team connects to the client's key services, such as incident and configuration management platform like ServiceNow, communication channels like Email Servers, Slack and Teams, GenAI services like OpenAI, User Authentication Services like LDAP, etc. Let's discuss any other services that need to be connected to Chimera.
4. **Hierarchical Organization**: The backend team sets up the client's organization structure. This includes setting up the client's account, defining line of business/strategic business units, and hierarchy of domains and subdomains including the organizational heads.
5. **Ontology and Taxonomy**: The backend team defines the ontology and taxonomy for the client's business conceptual data model.
6. **Final Step**: Chimera UI DNS set up and SSL certificate installation.

## Team Onboarding

1. **Initial Setup**: Users configure their workspace, including setting up data sources and defining access controls.
2. **Guided Tour**: A guided tour introduces users to the platform's features and capabilities.

Please see more [here](../UI.md#user-flow)
