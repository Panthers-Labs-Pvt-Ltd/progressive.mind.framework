# Modal

The `modal` folder contains various classes that represent the data models used within the DataHub platform. These models are used to define the structure and properties of datasets, glossary terms, and related entities.

## Classes

### `Dataset`

Represents a dataset with its properties and fields.

#### Properties

- `dataProductName` (String): Name of the data product associated with the dataset.
- `name` (String): Name of the dataset.
- `displayName` (String): Display name of the dataset.
- `description` (String): Description of the dataset.
- `FabricType` (String): Fabric type of the dataset (e.g., "DEV").
- `datasetPlatformName` (String): Platform name of the dataset.
- `qualifiedName` (String): Qualified name of the dataset.
- `uri` (String): URI of the dataset.
- `domain` (String): Domain associated with the dataset.
- `tags` (List<Tag>): List of tags associated with the dataset.
- `properties` (List<Property>): List of custom properties associated with the dataset.
- `owners` (List<Owner>): List of owners associated with the dataset.
- `glossaryTerm` (List<GlossaryTerm>): List of glossary terms associated with the dataset.
- `fields` (List<Field>): List of fields in the dataset.

### `Field`

Represents a field within a dataset.

#### Properties

- `name` (String): Name of the field.
- `type` (String): Data type of the field.
- `displayName` (String): Display name of the field.
- `description` (String): Description of the field.
- `fieldCanonicalName` (String): Canonical name of the field.
- `maxLength` (int): Maximum length of the field.
- `isPartitionKey` (boolean): Indicates if the field is a partition key.
- `isPrimaryKey` (boolean): Indicates if the field is a primary key.
- `isNullable` (boolean): Indicates if the field is nullable.
- `foreignKey` (List<ForeignKey>): List of foreign keys associated with the field.
- `tags` (List<Tag>): List of tags associated with the field.
- `glossaryTerm` (List<GlossaryTerm>): List of glossary terms associated with the field.

### `GlossaryTerm`

Represents a glossary term.

#### Properties

- `glossaryTermName` (String): Name of the glossary term.
- `documentations` (String): Documentation of the glossary term.
- `sourceRef` (String): Source reference of the glossary term.
- `sourceURL` (String): Source URL of the glossary term.
- `glossaryNodeRecord` (GlossaryNode): Glossary node associated with the term.
- `glossaryRelatedTermsRecord` (List<GlossaryRelatedTerm>): List of related terms associated with the glossary term.

### `GlossaryRelatedTerm`

Represents a related term in a glossary.

#### Properties

- `RelatedTermName` (String[]): Array of related term names.
- `RelationType` (String): Type of relation (e.g., "INHERITED BY").

### `ForeignKey`

Represents a foreign key in a dataset.

#### Properties

- `datasetPlatform` (String): Platform of the foreign dataset.
- `datasetName` (String): Name of the foreign dataset.
- `origin` (String): Origin of the foreign dataset.
- `ForeignKeyName` (String): Name of the foreign key.

### `Owner`

Represents an owner of a dataset.

#### Properties

- `name` (String): Name of the owner.
- `type` (String): Type of the owner (e.g., "BUSINESS_OWNER").

### `Tag`

Represents a tag associated with a dataset or field.

#### Properties

- `name` (String): Name of the tag.
- `value` (String): Value of the tag.

### `Property`

Represents a custom property associated with a dataset.

#### Properties

- `name` (String): Name of the property.
- `value` (String): Value of the property.

## Usage

These classes are used to define the structure and properties of datasets, glossary terms, and related entities within the DataHub platform. They are typically used in conjunction with the `DatasetManager`, `ManageGlossaryTerms`, and other classes to manage and manipulate these entities.

## Dependencies

- `com.fasterxml.jackson.databind.*`
- `com.linkedin.common.*`
- `com.linkedin.dataset.*`
- `com.linkedin.glossary.*`
- `com.progressive.minds.chimera.core.datahub.*`
- `com.progressive.minds.chimera.foundational.logging.*`

Ensure that these dependencies are included in your project to use the classes within the `modal` folder.