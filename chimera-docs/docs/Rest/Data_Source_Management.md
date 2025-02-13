# Data Source Management

Data Sources or External Data Sources are the sources from which data is ingested into the platform and are typically not managed by Chimera. These sources can be databases, files, APIs, or other systems that contain data to be processed and analyzed.

When onboarding a new data source, users typically go through the following steps:

1. **Data Source Discovery and Configuration**: Users identify and connect data sources to the platform. This information can be stored in a metadata repository for easy access.
2. **Data Source Validation**: System should verify data source connectivity and data quality before ingestion. System assumes that data quality has been validated by data source owners. It is also assumed that these sources are golden sources and Chimera lineage stops at these sources.
3. **Data Source Ingestion**: Users can initiate or set schedule/event to data ingestion processes to bring data into the platform (Raw Layer). Please see details in the [Data_Ingestion.md](Data_Ingestion.md) document.
4. **Data Source Monitoring**: Users monitor data source health and performance to ensure continuous data flow.
5. **Data Source Management**: Users manage data sources, including updating configurations and troubleshooting issues.
6. **Data Source Governance**: System would automatically derive schema and other information. For missing information, users need to provide information in Catalog.
7. **Data Source Decommissioning**: Users decommission data sources that are no longer needed or relevant.
8. **Data Source Governance**: Users apply governance policies to data sources to ensure compliance and security. They should document data sources to maintain a comprehensive inventory and improve understandability and collaboration with users. Please see Catalog Management (??)
9. **Data Source Optimization**: System optimizes parameters for data sources for performance, cost, and efficiency.

Once onboarded, the data can be ingested.