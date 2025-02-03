# ManageDataProduct

The `ManageDataProduct` class is responsible for managing data products within the DataHub platform. It provides methods to create data products, add assets to data products, and map various properties such as owners, domains, global tags, and glossary terms.

## Methods

### `createDataProduct`

Creates a new data product with the specified properties.

#### Parameters

- `dataProductName` (String): Name of the data product to be created.
- `dataProductDescription` (String): Description of the data product.
- `externalURL` (String, nullable): External URL to be associated with the data product.
- `domainName` (String, nullable): Domain name to be associated with the data product.
- `globalTags` (String[], nullable): Array of global tags to be added to the data product.
- `glossaryTerms` (String[], nullable): Array of glossary terms to be added to the data product.
- `DataAssets` (Map<String, Pair<String, String>>, nullable): Map of data assets with their name, type, and platform.
- `Owners` (Map<String, String>, nullable): Map of owners with their name and ownership type.
- `customProperties` (Map<String, String>, nullable): Map of custom properties with their name and value.

#### Returns

- `String`: URN of the created data product.

### `addAssetToDataProduct`

Adds an asset to an existing data product.

#### Parameters

- `dataProductName` (String): Name of the data product.
- `assetsName` (String): Name of the asset to be added.

#### Throws

- `URISyntaxException`
- `IOException`
- `ExecutionException`
- `InterruptedException`

## Usage

To use the `ManageDataProduct` class, instantiate it and call the desired methods with the appropriate parameters.

```java
ManageDataProduct manageDataProduct = new ManageDataProduct();
String dataProductUrn = manageDataProduct.createDataProduct(
    "SampleDataProduct",
    "This is a sample data product.",
    "http://example.com",
    "sampleDomain",
    new String[]{"tag1", "tag2"},
    new String[]{"term1", "term2"},
    dataAssetsMap,
    ownersMap,
    customPropertiesMap
);

String returnVal = manageDataProduct.addAssetToDataProduct("SampleDataProduct", "urn:li:dataset:(urn:li:dataPlatform:hive,SampleDataset,PROD)");
```

## Logging

The class uses `ChimeraLogger` for logging various steps and actions performed during the execution of its methods. Ensure that the logging configuration is properly set up to capture these logs.

## Dependencies

- `com.linkedin.common.*`
- `com.linkedin.dataproduct.*`
- `com.progressive.minds.chimera.core.datahub.common.*`
- `com.progressive.minds.chimera.foundational.logging.*`
- `datahub.shaded.org.apache.commons.lang3.*`

Ensure that these dependencies are included in your project to use the `ManageDataProduct` class.