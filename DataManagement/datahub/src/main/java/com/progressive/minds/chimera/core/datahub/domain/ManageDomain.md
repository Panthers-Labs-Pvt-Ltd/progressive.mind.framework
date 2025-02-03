# ManageDomain

The `ManageDomain` class is responsible for managing domains within the DataHub platform. It provides methods to add domains to various entities such as datasets and data products.

## Methods

### `addDomain`

Adds a domain to a specified entity.

#### Parameters

- `domainName` (String): Name of the domain to be added.
- `entityUrn` (String): URN of the entity to which the domain will be added.
- `entityType` (String): Type of the entity (e.g., "dataset", "dataProduct").

#### Throws

- `Exception`

## Usage

To use the `ManageDomain` class, instantiate it and call the `addDomain` method with the appropriate parameters.

```java
ManageDomain manageDomain = new ManageDomain();
manageDomain.addDomain("sampleDomain", "urn:li:dataset:(urn:li:dataPlatform:hive,SampleDataset,PROD)", "dataset");
```

## Test Cases

To ensure the functionality of the `ManageDomain` class, you can write test cases using a testing framework like JUnit.

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ManageDomainTest {

    @Test
    public void testAddDomain() {
        ManageDomain manageDomain = new ManageDomain();
        assertDoesNotThrow(() -> {
            manageDomain.addDomain("sampleDomain", "urn:li:dataset:(urn:li:dataPlatform:hive,SampleDataset,PROD)", "dataset");
        });
    }

    // Add more test cases as needed
}
```

## Logging

The class uses `ChimeraLogger` for logging various steps and actions performed during the execution of its methods. Ensure that the logging configuration is properly set up to capture these logs.

## Dependencies

- `com.linkedin.common.*`
- `com.linkedin.domain.*`
- `com.progressive.minds.chimera.core.datahub.*`
- `com.progressive.minds.chimera.foundational.logging.*`

Ensure that these dependencies are included in your project to use the `ManageDomain` class.