# Uniqueness

Uniqueness measures how well data is unique and distinct from other data. Unique data is free from duplicates, redundancies, and inconsistencies. Uniqueness can be measured using metrics such as the number of unique values, the number of duplicate values, and the percentage of unique values in a dataset.

Uniqueness can be measured by identifying duplicate records, redundant records, or inconsistent records in a dataset. Uniqueness can be measured using metrics such as duplicate rate, redundancy rate, and uniqueness score.

To identify if a record is duplicate or redundant, we can use the following methods:

1. Identify columns with identifies unique records: Identify columns that uniquely identify each record in the dataset. These columns can be used to check for duplicates and redundancies in the data.
2. Profile the data: Profile the data to identify unique values, duplicate values, and redundant values in the dataset. Use data profiling tools such as Great Expectations, TensorFlow Data Validation, and AWS Deeque to automate the profiling of data and identify uniqueness issues.
3. Report which records are duplicates: Report which records are duplicates or redundant in the dataset. Use data quality tools to identify duplicate records and assess the uniqueness of the data.

## Two use cases for uniqueness issues in data

Based on identifiers, we can identify three use cases for uniqueness issues in data:
1. Same entity with different identifiers: In this use case, the same entity is represented by different identifiers in the dataset. For example, a customer may have multiple customer IDs in different systems or databases. To identify uniqueness issues, we need to map the different identifiers to a common identifier and remove duplicates or redundancies in the data.
2. Same entity with same identifier: In this use case, the same entity is represented by the same identifier in the dataset. For example, a customer may have the same customer ID in different systems or databases. To identify uniqueness issues, we need to check for duplicates or redundancies in the data and remove them to ensure data uniqueness.

Based on data representations, we can identify two use cases for uniqueness issues in data:
1. Same data with different representations: In this use case, the same data is represented in different formats or representations in the dataset. For example, a date may be represented in different date formats or a name may be represented in different spellings. To identify uniqueness issues, we need to standardize the data representations and remove duplicates or redundancies in the data.
2. Different data with same representation: In this use case, different data is represented in the same format or representation in the dataset. For example, two different customers may have the same name or address. To identify uniqueness issues, we need to check for duplicates or redundancies in the data and remove them to ensure data uniqueness.

## Importance of uniqueness in Master Data and Reference Data Management

Uniqueness is important in Master Data and Reference Data Management to ensure data quality, data integrity, and data consistency. Uniqueness helps to identify and remove duplicates, redundancies, and inconsistencies in the data. Uniqueness is essential for data integration, data migration, and data governance to ensure that data is unique and distinct from other data.

