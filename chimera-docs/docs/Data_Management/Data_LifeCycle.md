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

### Jaya Need Clarity on these items 
  Who will be the owner of the data which is ingested from  external Datasources (Actual Data owner(Business) or Operartion Owner (layer) )
  How do we assign  the owenership to External datasource ? (what are the parameters we need to consider before assgning Ownership)
  Who is respoinsible for providing these details ?
  How/where do we store this metadata for external data sources?
  Are we going to attach these data contracts at the pipeline level ??
   
### Manish TBD items 

Business Owner: Responsible for the overall business aspects of the data.

#### Responsibilities

1. **Strategic Direction**: Aligns data with business strategy.
2. **Decision Making**: Makes decisions on data usage and sharing.
3. **Resource Allocation**: Allocates budget and personnel.
4. **Compliance and Risk Management**: Ensures compliance and mitigates risks.
5. **Stakeholder Communication**: Connects with stakeholders.
6. **Data Quality and Integrity**: Ensures data accuracy and reliability.

Technical Owner: Manages the technical aspects, such as databases and infrastructure.
#### Responsibilities

1. **Data Architecture**: Designs and maintains the data architecture.
2. **Data Integration**: Manages data integration processes.
3. **Data Security**: Implements and maintains security measures.
4. **Performance Optimization**: Ensures data systems are optimized for performance.
5. **Data Quality**: Monitors and maintains data quality.
6. **Troubleshooting**: Resolves technical issues related to data.
7. **Collaboration**: Works with data stewards and business owners.

The Technical Owner plays a critical role in ensuring the technical aspects of data management are robust, secure, and efficient.

Data Custodian: Handles the day-to-day management and maintenance of data.
#### Responsibilities

1. **Data Storage**: Manages data storage solutions.
2. **Data Maintenance**: Ensures data is updated, cleaned, and archived.
3. **Data Protection**: Implements security measures for data protection.
4. **Access Management**: Controls and monitors data access.
5. **Compliance**: Ensures compliance with regulations and policies.
6. **Documentation**: Maintains documentation for data management.
7. **Collaboration**: Works with stakeholders on data governance.

The Data Custodian plays a vital role in maintaining the integrity and security of data, ensuring that it is properly managed and protected throughout its lifecycle.

Data Steward: Ensures data quality, integrity, and compliance with standards.
#### Responsibilities

1. **Data Quality**: Ensures data accuracy and reliability through audits and validation.
2. **Data Governance**: Establishes and enforces data governance policies.
3. **Data Documentation**: Maintains documentation for data definitions and usage guidelines.
4. **Compliance**: Ensures compliance with regulations and organizational policies.
5. **Data Access**: Manages access controls for data.
6. **Training and Support**: Provides training on data standards and best practices.
7. **Issue Resolution**: Identifies and resolves data-related issues.
8. **Collaboration**: Works with stakeholders to promote data stewardship.

The Data Steward plays a vital role in maintaining the integrity, quality, and accessibility of data, ensuring that data is managed effectively and in compliance with standards.

Data Administrator: Manages data resources and access permissions.
#### Responsibilities

1. **Database Management**: Manages installation and maintenance of databases.
2. **Data Backup and Recovery**: Implements backup and recovery procedures.
3. **Data Security**: Ensures data security and access controls.
4. **Performance Tuning**: Optimizes database performance.
5. **User Support**: Provides support for data-related issues.
6. **Data Integration**: Manages data integration from various sources.
7. **Compliance**: Ensures compliance with regulations and policies.
8. **Documentation**: Maintains documentation for database configurations.
9. **Data Monitoring**: Monitors data 

Legal Owner: Oversees legal compliance and data protection regulations.
#### Responsibilities

1. **Legal Compliance**: Ensures compliance with data laws and regulations.
2. **Policy Development**: Develops and maintains data-related policies.
3. **Risk Management**: Identifies and mitigates legal risks.
4. **Contract Management**: Manages contracts related to data sharing and processing.
5. **Privacy Protection**: Ensures compliance with data privacy laws.
6. **Audit and Reporting**: Conducts legal audits and prepares compliance reports.
7. **Legal Guidance**: Provides legal advice on data-related issues.
8. **Incident Response**: Manages legal aspects of 

Privacy Officer: Ensures data privacy and compliance with privacy laws.
#### Responsibilities

1. **Privacy Compliance**: Ensures compliance with data privacy laws and regulations.
2. **Privacy Policies**: Develops and maintains privacy policies and procedures.
3. **Data Protection**: Implements measures to protect personal data.
4. **Privacy Training**: Provides training on data privacy issues and best practices.
5. **Risk Assessment**: Conducts privacy impact assessments.
6. **Incident Response**: Manages responses to data breaches and privacy incidents.
7. **Data Subject Rights**: Ensures individuals can exercise their rights regarding personal data.
8. **Audit and Reporting**: Conducts privacy audits and prepares reports.
9. **Privacy by Design**: Integrates privacy into system and process design.
10. **Liaison**: Acts as a contact for regulatory authorities and individuals on privacy 

Information Security Officer: Focuses on data security and protection against breaches.
#### Responsibilities

1. **Security Policies**: Develops and enforces information security policies.
2. **Risk Management**: Identifies and mitigates security risks.
3. **Security Awareness**: Provides training on information security best practices.
4. **Incident Response**: Manages responses to security incidents.
5. **Security Audits**: Conducts regular security audits and assessments.
6. **Access Control**: Manages access controls for sensitive information.
7. **Threat Monitoring**: Monitors for potential security threats.
8. **Compliance**: Ensures compliance with security regulations and standards.
9. **Security Architecture**: Designs and maintains security infrastructure.
10. **Collaboration**: Works with stakeholders to ensure a holistic approach 

Chief Data Officer (CDO): Responsible for the overall data strategy and governance.
#### Responsibilities

1. **Data Strategy**: Develops and oversees the organization's data strategy.
2. **Data Governance**: Establishes data governance frameworks and policies.
3. **Data Management**: Ensures data quality, integrity, and security.
4. **Data Analytics**: Promotes the use of data analytics for decision-making.
5. **Data Innovation**: Identifies opportunities for data innovation.
6. **Stakeholder Engagement**: Aligns data initiatives with organizational priorities.
7. **Compliance**: Ensures compliance with data regulations and standards.
8. **Data Culture**: Fosters a data-driven culture and promotes data literacy.
9. **Resource Allocation**: Allocates resources for data projects and initiatives.
10. **Performance Monitoring**: Monitors and reports on data initiative 

End User: Uses the data for specific applications or decision-making.
#### Responsibilities

1. **Data Utilization**: Uses data for specific applications and decision-making.
2. **Data Interpretation**: Understands and derives insights from data.
3. **Data Compliance**: Adheres to data usage policies and guidelines.
4. **Feedback**: Provides feedback on data quality and usability.
5. **Data Security**: Uses data responsibly and securely.
6. **Collaboration**: Works with other stakeholders to maximize data value.

Product Owner: Manages data related to specific products or services.
#### Responsibilities

1. **Product Vision**: Defines and communicates the product vision and goals.
2. **Backlog Management**: Manages the product backlog, including data requirements.
3. **Data Utilization**: Uses data to inform product decisions and innovations.
4. **Stakeholder Collaboration**: Works with stakeholders to gather requirements and feedback.
5. **User Stories**: Develops user stories with data needs and insights.
6. **Performance Metrics**: Defines and tracks key performance indicators (KPIs).
7. **Data Quality**: Ensures the quality and integrity of product-related data.
8. **Compliance**: Ensures compliance with data regulations and policies.
9. **Release Planning**: Plans and coordinates product releases with data implementation.
10. **User Feedback**: Analyzes user feedback for continuous product 

Project Manager: Oversees data within the context of specific projects.

#### Responsibilities

1. **Project Planning**: Defines project scope, objectives, and deliverables.
2. **Resource Management**: Allocates resources, including budget and personnel.
3. **Timeline Management**: Creates and maintains project schedules and timelines.
4. **Risk Management**: Identifies and mitigates project risks.
5. **Quality Assurance**: Ensures project deliverables meet quality standards.
6. **Stakeholder Communication**: Keeps stakeholders informed and engaged.
7. **Data Integration**: Oversees data integration into project workflows.
8. **Performance Monitoring**: Tracks project progress and performance metrics.
9. **Issue Resolution**: Resolves project-related issues and challenges.
10. **Documentation**: Maintains project documentation.
11. **Compliance**: Ensures project activities comply with regulations and policies.

Compliance Officer: Ensures data handling complies with relevant regulations.
#### Responsibilities

1. **Regulatory Compliance**: Ensures compliance with relevant laws and regulations.
2. **Policy Development**: Develops and maintains data-related policies and procedures.
3. **Risk Assessment**: Identifies and mitigates compliance risks.
4. **Training and Awareness**: Provides training on compliance requirements and best practices.
5. **Monitoring and Auditing**: Conducts regular compliance audits and monitors adherence.
6. **Incident Response**: Manages responses to compliance breaches.
7. **Documentation**: Maintains records of compliance activities and audits.
8. **Stakeholder Communication**: Communicates compliance updates to stakeholders.
9. **Continuous Improvement**: Improves compliance processes to adapt to changes.
10. **Liaison**: Acts as a contact for regulatory authorities and auditors.

### Internal Data Sources

## Appendix

### What is a datasource?

### What if an organization does not have a concept of Golden Source or Authoritative Data Source (ADS)?

