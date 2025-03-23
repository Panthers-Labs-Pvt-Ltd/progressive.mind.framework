# ManageGlossaryTerms

The `ManageGlossaryTerms` class is responsible for managing glossary terms within the DataHub platform. It provides methods to create glossary terms and manage related terms.

## Methods

### `createGlossaryTerm`

Creates a new glossary term with the specified properties.

#### Parameters

- `glossaryTerms` (List<GlossaryTerm>): List of glossary terms to be created.

#### Returns

- `String`: Result of the glossary term creation process.

#### Throws

- `Exception`

## Usage

To use the `ManageGlossaryTerms` class, call the `createGlossaryTerm` method with the appropriate parameters.

```java
List<GlossaryTerm> glossaryTerms = new ArrayList<>();
// Populate glossaryTerms list
String result = ManageGlossaryTerms.createGlossaryTerm(glossaryTerms);
```

## Test Cases

To ensure the functionality of the `ManageGlossaryTerms` class, you can write test cases using a testing framework like JUnit.

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ManageGlossaryTermsTest {

    @Test
    public void testCreateGlossaryTerm() {
        List<GlossaryTerm> glossaryTerms = new ArrayList<>();
        // Populate glossaryTerms list

        assertDoesNotThrow(() -> {
            String result = ManageGlossaryTerms.createGlossaryTerm(glossaryTerms);
            assertNotNull(result);
        });
    }

    // Add more test cases as needed
}
```

# ManageGlossaryNodes

The `ManageGlossaryNodes` class is responsible for managing glossary nodes within the DataHub platform. It provides methods to create and manage glossary nodes.

## Methods

### `createGlossaryNode`

Creates a new glossary node with the specified properties.

#### Parameters

- `glossaryNodeName` (String): Name of the glossary node to be created.
- `glossaryNodeDescription` (String): Description of the glossary node.

#### Returns

- `String`: Result of the glossary node creation process.

#### Throws

- `Exception`

## Usage

To use the `ManageGlossaryNodes` class, call the `createGlossaryNode` method with the appropriate parameters.

```java
String result = ManageGlossaryNodes.createGlossaryNode("SampleNode", "This is a sample glossary node.");
```

## Test Cases

To ensure the functionality of the `ManageGlossaryNodes` class, you can write test cases using a testing framework like JUnit.

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ManageGlossaryNodesTest {

    @Test
    public void testCreateGlossaryNode() {
        assertDoesNotThrow(() -> {
            String result = ManageGlossaryNodes.createGlossaryNode("SampleNode", "This is a sample glossary node.");
            assertNotNull(result);
        });
    }

    // Add more test cases as needed
}
```

## Logging

Both classes use `ChimeraLogger` for logging various steps and actions performed during the execution of their methods. Ensure that the logging configuration is properly set up to capture these logs.

## Dependencies

- `com.linkedin.common.*`
- `com.linkedin.glossary.*`
- `com.progressive.minds.chimera.core.datahub.*`
- `com.progressive.minds.chimera.foundational.logging.*`

Ensure that these dependencies are included in your project to use the `ManageGlossaryTerms` and `ManageGlossaryNodes` classes.