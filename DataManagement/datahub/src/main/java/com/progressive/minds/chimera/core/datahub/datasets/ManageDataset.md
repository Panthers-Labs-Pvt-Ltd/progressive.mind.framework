# DatasetManager

The `DatasetManager` class is responsible for managing datasets within the DataHub platform. It provides methods to create datasets and map various properties such as schema, owners, domains, global tags, and glossary terms.

## Methods

### `createDataset`

Creates a new dataset with the specified properties.

#### Parameters

- `DatasetDefinition` (String): JSON string defining the dataset properties.
- `createdBy` (String): Username of the creator.

#### Throws

- `Exception`

## Usage

To use the `DatasetManager` class, call the `createDataset` method with the appropriate parameters.

```java
String datasetDefinition = "{...}"; // JSON string defining the dataset
String createdBy = "username";

DatasetManager.createDataset(datasetDefinition, createdBy);
```

## Sample JSON Description

```json
{
  "dataProductName": "Finance",
  "name": "dept",
  "displayName": "chimeradb.dept",
  "description": "Employee Department Tables",
  "fabricType": "DEV",
  "datasetPlatformName": "oracle",
  "qualifiedName": "chimeradb.dept",
  "uri": "http://pantherlabs.io/dept.sql",
  "domain" : "natwestdomain",
  "tags": [
    {
      "name": "department",
      "value": "this table contains department related mapping"
    },
    {
      "name": "employee dept",
      "value": "this table contains employee department mapping"
    }
  ],
  "properties": [
    {
      "name": "PII",
      "value": "Yes, Internal department"
    },
    {
      "name": "Confidential",
      "value": "Yes"
    }
  ],
  "owners": [
    {
      "name": "Manish Kumar",
      "type": "BUSINESS_OWNER"
    },
    {
      "name": "Prashant Kumar",
      "type": "Data Creator"
    }
  ],
  "glossaryTerm": [
    {
      "glossaryTermName": "dataset glossary",
      "Documentations": "glossary TermName Department Documentations"
    },
    {
      "glossaryTermName": "dataset 1 glossary",
      "Documentations": "glossary TermName Department Documentations"
    }
  ],
  "fields": [
    {
      "name": "deptno",
      "type": "Integer",
      "displayName": "DepartmentNumber",
      "description": "DepartmentNumber Column indicated valid department number",
      "fieldCanonicalName": "dept.deptno",
      "maxLength": 2,
      "isPartitionKey": false,
      "isPrimaryKey": true,
      "isNullable": false,
      "foreignKey": null,
      "tags": [
        {
          "name": "deptno",
          "value": "Attribute will keep unique deptno"
        }
      ],
      "glossaryTerm": [
        {
          "glossaryTermName": "Department",
          "Documentations": "glossary TermName Department Documentations"
        },
        {
          "glossaryTermName": "DeptNo",
          "Documentations": "glossary TermName Department Documentations"
        }
      ]
    },
    {
      "name": "dname",
      "type": "string",
      "displayName": "DepartmentName",
      "description": "Department Name Description",
      "fieldCanonicalName": "dept.dname",
      "maxLength": 14,
      "isPartitionKey": false,
      "isPrimaryKey": false,
      "isSampleTime": false,
      "isNullable": false,
      "foreignKey": null,
      "tags": null,
      "glossaryTerm":null
    },
    {
      "name": "loc",
      "type": "string",
      "displayName": "Location",
      "description": "Location Name Description",
      "fieldCanonicalName": "dept.loc",
      "maxLength": 14,
      "isPartitionKey": false,
      "isPrimaryKey": false,
      "isSampleTime": false,
      "isNullable": false,
      "foreignKey": null,
      "tags": null,
      "glossaryTerm":null
    }
  ]
}
```

## Sample YAML Description

```yaml
dataProductName: Finance
name: dept
displayName: chimeradb.dept
description: Employee Department Tables
fabricType: DEV
datasetPlatformName: oracle
qualifiedName: chimeradb.dept
uri: http://pantherlabs.io/dept.sql
domain: natwestdomain
tags:
  - name: department
    value: this table contains department related mapping
  - name: employee dept
    value: this table contains employee department mapping
properties:
  - name: PII
    value: Yes, Internal department
  - name: Confidential
    value: Yes
owners:
  - name: Manish Kumar
    type: BUSINESS_OWNER
  - name: Prashant Kumar
    type: Data Creator
glossaryTerm:
  - glossaryTermName: dataset glossary
    Documentations: glossary TermName Department Documentations
  - glossaryTermName: dataset 1 glossary
    Documentations: glossary TermName Department Documentations
fields:
  - name: deptno
    type: Integer
    displayName: DepartmentNumber
    description: DepartmentNumber Column indicated valid department number
    fieldCanonicalName: dept.deptno
    maxLength: 2
    isPartitionKey: false
    isPrimaryKey: true
    isNullable: false
    foreignKey: null
    tags:
      - name: deptno
        value: Attribute will keep unique deptno
    glossaryTerm:
      - glossaryTermName: Department
        Documentations: glossary TermName Department Documentations
      - glossaryTermName: DeptNo
        Documentations: glossary TermName Department Documentations
  - name: dname
    type: string
    displayName: DepartmentName
    description: Department Name Description
    fieldCanonicalName: dept.dname
    maxLength: 14
    isPartitionKey: false
    isPrimaryKey: false
    isSampleTime: false
    isNullable: false
    foreignKey: null
    tags: null
    glossaryTerm: null
  - name: loc
    type: string
    displayName: Location
    description: Location Name Description
    fieldCanonicalName: dept.loc
    maxLength: 14
    isPartitionKey: false
    isPrimaryKey: false
    isSampleTime: false
    isNullable: false
    foreignKey: null
    tags: null
    glossaryTerm: null
```

## Test Cases

To ensure the functionality of the `DatasetManager` class, you can write test cases using a testing framework like JUnit.

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DatasetManagerTest {

    @Test
    public void testCreateDataset() {
        String datasetDefinition = "{...}"; // JSON string defining the dataset
        String createdBy = "username";

        assertDoesNotThrow(() -> {
            DatasetManager.createDataset(datasetDefinition, createdBy);
        });
    }

    // Add more test cases as needed
}
```

## Logging

The class uses `ChimeraLogger` for logging various steps and actions performed during the execution of its methods. Ensure that the logging configuration is properly set up to capture these logs.

## Dependencies

- `com.linkedin.common.*`
- `com.linkedin.dataset.*`
- `com.progressive.minds.chimera.core.datahub.*`
- `com.progressive.minds.chimera.foundational.logging.*`

Ensure that these dependencies are included in your project to use the `DatasetManager` class.