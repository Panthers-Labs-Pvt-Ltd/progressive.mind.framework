# Data Life cycle

Data life cycle refers to the stages that data goes through from its creation to its deletion. It includes the collection, storage, processing, analysis, and archiving of data. Managing the data life cycle effectively is essential for ensuring data quality, security, and compliance with regulations.

## Pre-requisites

1. Org is onboarded. Business/ontologies is onboarded.
2. Team is onboarded
3. Infra (Databub, Orchestration, Postgres for metadata, etc.) is setup

## Data Lifecycle Stages

### External Data sources (Data Sources)

External data sources are the starting point of the data life cycle. Data can be collected from various sources such as databases, files, APIs, and streaming services. External data sources can be structured or unstructured and may contain raw, unprocessed data.

You will need to define the data sources in the datahub (this needs to be done manually) you want to collect data from and establish connections to these sources. This ensures that you have the connection strings, uri, etc. to connect to the data sources. Things to consider when defining data sources include:
1. Ensure that firewall, connectivity, and reachability analysis are completed on these sources.
2. Data source type (e.g., database, file, API)
3. Connection details (e.g., host, port, username, password)

Datahub tries to pull the metadata within the established data sources. I am not convinced that this is the right to do at this stage. We should just test accesses and connectivity.

Validation required -
* These systems should be Golden Sources or Authoritative Data sources (ADS). Golden Sources / ADS (Yes or No) should be marked mandatory for each data source, with default to Y. 
> Please note that Golden sources or ADS can be defined at system level or table level. If it is defined at system level, all the tables in the system are considered as Golden sources or ADS. If it is defined at table level, only that table is considered as Golden sources or ADS - in such cases, we should not mark the data source as golden.
* We need a mechanism to define the Golden sources or ADS in the datahub, with re-certifation process. This may be a new development outside the datahub and may be a fork. 
* Whether the data source is a Golden Source or Authoritative Data Source (ADS) or not, is a business decision and should be decided by the data governance team.

### Dataset Onboarding

Data ingestion is the process of onboarded dataset from external sources and loading it into a data storage system. Data ingestion can be done in real-time or batch mode, depending on the requirements of the data pipeline.

Phases of data pipeline:

- Use data sources as available in Datahub (metadata host) to **connect** and **collect** data.
- During the collection, check if schema can be derived from the data source. If not, users need to define the schema. This schema definition needed to be published in datahub (if new) or validated against the existing schema or updated in datahub (if schema evolves and users have indicated "somehow" of this change). In essence, the golden source of schema of any dataset/product is Datahub.

#### Post Onboarding activities

1. Metadata population - Data owners, etc.
2. Confirmation of correctness of the schema. This is when the users can population business meanings, uniqueness, PII, and other stuff.

### Jaya
  Who will be the owner of the data which is ingested from  external Datasources (Actual Data owner(Business) or Operartion Owner (layer) )

### Internal Data Sources

## Appendix

### What is a datasource?

### What if an organization does not have a concept of Golden Source or Authoritative Data Source (ADS)?

