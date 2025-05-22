# MyBatis DB Starter

A reusable Spring Boot starter library to simplify MyBatis integration with PostgreSQL, HikariCP, and code generation.

## Features

- Auto-configured `DataSource` with HikariCP
- MyBatis Dynamic SQL support
- Preconfigured MyBatis Generator with Lombok and other plugins
- Schema-safe code generation

## Installation

Add the dependency to your service's `pom.xml`:

```xml

<dependency>
    <groupId>com.progressive.minds</groupId>
    <artifactId>chimera-api-nexus-db</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Also, include the MyBatis Generator plugin:

```xml

<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>1.4.2</version>
    <configuration>
        <configurationFile>src/main/resources/mybatis-generator-config.xml</configurationFile>
        <overwrite>true</overwrite>
    </configuration>
    <dependencies>
        <!-- Lombok Plugin for MyBatis Generator -->
        <dependency>
            <groupId>com.softwareloop</groupId>
            <artifactId>mybatis-generator-lombok-plugin</artifactId>
            <version>1.0</version>
        </dependency>

        <!-- PostgreSQL Driver -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.4</version>
        </dependency>
    </dependencies>
</plugin>
```

## Configuration

### 1. Database Properties (`application.yml`)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_db?currentSchema=your_schema
    username: your_user
    password: your_password
    driver-class-name: org.postgresql.Driver
```

### 2. MyBatis Generator Config (`src/main/resources/mybatis-generator-config.xml`)

Copy the base configuration from:

```
chimera-nexus/chimera-api-nexus-db/src/main/resources/META-INF/mybatis-generator-config.xml
```

Then, modify it to include your table details:

```xml

<generatorConfiguration>
    <!-- Update the target package for generated models and mappers -->
    <javaModelGenerator
            targetPackage="com.progressive.minds.chimera.model"
            targetProject="src/main/java"/>

    <javaClientGenerator
            type="ANNOTATEDMAPPER"
            targetPackage="com.progressive.minds.chimera.mapper"
            targetProject="src/main/java"/>

    <!-- Add your service-specific tables -->
    <table schema="your_schema" tableName="YOUR_TABLE">
        <generatedKey column="id" sqlStatement="PostgreSQL" identity="true"/>
    </table>
</generatorConfiguration>
```

### Note

Ensure that the SQL table is created before running the generator, as it generates mappers and models based on existing
tables.

## Protecting Custom Mapper Changes

To preserve custom code in Mapper interfaces during regeneration:

### 1. Implementation Strategy

#### Generated Mappers

```java
// target/generated-sources/mybatis-generator/com/example/mapper/GeneratedUserMapper.java
@Mapper
public interface GeneratedUserMapper {
    BasicColumn[] selectColumns = /* generated code */;
    // Auto-generated methods
}
```

#### Custom Mapper (in a different package)

```java
// src/main/java/com/example/mapper/custom/CustomUserMapper.java
@Mapper
public interface CustomUserMapper extends GeneratedUserMapper {
    @Select("SELECT * FROM users WHERE active = true")
    List<User> findActiveUsers();
}
```

### 2. Generator-safe Patterns

- Never modify generated files directly
- Extend generated classes/interfaces
- Use separate packages:
  ```
  com.example.mapper.generated  # Auto-generated
  com.example.mapper.custom     # Your implementations
  ```

## Usage in Services

```java

@Service
@RequiredArgsConstructor
public class UserService {
    private final CustomUserMapper userMapper;

    public User getUserById(Long id) {
        return userMapper.selectOne(c ->
                c.where(UserDynamicSqlSupport.id, isEqualTo(id)));
    }

    public void createUser(User user) {
        userMapper.insert(user);
    }
}
```

## Code Generation

Run the generator:

```bash
mvn clean mybatis-generator:generate
```

Generated files will be placed in:

```
target/generated-sources/mybatis-generator/
├── com/progressive/minds/chimera/models/  # Entities
└── com/progressive/minds/chimera/ # Mapper interfaces
```

## Best Practices

1. **Version Control**:
    - Add `target/generated-sources/` to `.gitignore`
    - Regenerate code during the build process

2. **IDE Setup**:
    - Mark `generated-sources` as the generated sources root
    - Enable annotation processing for Lombok

3. **Merge Conflicts**:
    - Always commit changes before regeneration
    - Use diff tools to resolve conflicts in merged files

Here's a cleaned-up and corrected version of the **"Common Errors"** section of your Markdown file. The rest of the content looks great as you mentioned.

---

## Troubleshooting

### Common Errors

| Error                            | Solution                                                                                                                                               |
|----------------------------------| ------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `Cannot instantiate LombokPlugin` | Ensure the Lombok plugin dependency is added under the MyBatis plugin in `pom.xml`.                                                                    |
| `Table not found`                | Verify that the table name and schema are correct. PostgreSQL is case-sensitive; use double quotes in XML if needed.                                   |
|`Mapper/model class deleted`       | If a table entry is removed from `mybatis-generator-config.xml`, its corresponding generated classes (mapper/model) will be deleted upon regeneration. |

### What happens if the table is not found?

If the table specified in the generator config does not exist in the database:

* **No files are generated** for that table.
* **No errors are thrown** by default, but the generator logs a warning.
* Ensure the table is created **before running** the code generation process.

To make debugging easier, you can enable verbose logging.

### Debugging

Enable verbose mode in your plugin configuration:

```xml
<configuration>
    <configurationFile>src/main/resources/mybatis-generator-config.xml</configurationFile>
    <overwrite>true</overwrite>
    <verbose>true</verbose> <!-- Add this line -->
</configuration>
```

---

Let me know if you’d like me to polish any other part of the file or help add more common error scenarios!


### Debugging

Add this configuration to `pom.xml`:

```xml

<configuration>
    <verbose>true</verbose>
</configuration>
```

## Plugin Reference

| Plugin                   | Purpose                                |
|--------------------------|----------------------------------------|
| `MergeJavaPlugin`        | Preserves manual changes in Java files |
| `LombokPlugin`           | Reduces boilerplate in model classes   |
| `MapperAnnotationPlugin` | Adds `@Mapper` annotation              |
| `SerializablePlugin`     | Implements `Serializable` in models    |

