
# Metadata Operations


This project provides a simple implementation of a Metadata Management Application. It follows best practices such as the SOLID principles, supports dynamic filtering, and adheres to a clean MVC architecture. It uses HikariCP for database connection pooling and supports operations like fetching, updating, and deleting user records dynamically.

## Features

- Dynamic Filtering : Filter users dynamically by passing conditions at runtime.
- CRUD Operations
    - Fetch all users or filtered users.
    - Update user data dynamically based on filters.
    - Delete users with specified conditions.
- Connection Pooling: Efficient database connections using HikariCP.
- Clean Architecture
    - Repository Layer : Database interaction.
    - Service Layer : Business logic.
    - Exception Handling : Separate validation and database exceptions.
- Test Environment : Use H2 Database for testing and PostgreSQL for production.



## Tech Stack

- Java 17 (or higher)
- HikariCP: For database connection pooling
- JDBC: For database access
- JUnit: For unit testing
- H2 Database: For testing
- PostgreSQL: For production
- Maven: Build automation tool
- Yaml
## Environment Variables

To run this project, you will need to add the following environment variables to your .env file or session, valid values are dev/sit/prod and test (which will use H2 Database)

`CHIMERA_EXE_ENV`

## RDS Configuration Paramaters

| Paramater Name  |Paramater Values| Default Values|
| -------------   |:-------------:|  :-------------:|
url|DEF| "jdbc:postgresql|DEF|//localhost|DEF|5432/postgres"
username |DEF| "postgres"
password |DEF| "root"
DriverClassName |DEF| "org.postgresql.Driver"
MaximumPoolSize |DEF| "10"
MinimumIdle |DEF| "2"
IdleTimeout |DEF| "600000"
MaxLifetime |DEF| "1800000"
ConnectionTimeout |DEF| "30000"
Default |DEF| "N"
CloudProvider |DEF| "AWS"
RDSSecretName |DEF| "AWS SECRET NAME"
GCPProjectId |DEF| ""
AZUREKeyVaultURL |DEF| ""

## Project Structure

```css
  src/
├── main/
│   ├── java/
│   │   ├── com.progressive.minds.chimera.core.databaseOps/
│   │   │   ├── config/
│   │   │   │   └── DataSourceConfig.java
│   │   │   ├── modal/
│   │   │   │   └── User.java
│   │   │   ├── exception/
│   │   │   │   ├── DatabaseException.java
│   │   │   │   └── ValidationException.java
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java
│   │   │   ├── service/
│   │   │   │   └── UserService.java
│   │   │   ├── validation/
│   │   │   │   └── InputValidator.java
│   │   │   ├── utility/
│   │   │   │   └── readYamlConfig.java
│   │   │   └── MainApplication.java
│   │   │── resource/
│   │   │   └── rdsConfig.yml
├── test/
│   ├── java/
│   │   └── com.progressive.minds.chimera.core.databaseOps/
│   │       └── service/
│   │           └── UserServiceTest.java

