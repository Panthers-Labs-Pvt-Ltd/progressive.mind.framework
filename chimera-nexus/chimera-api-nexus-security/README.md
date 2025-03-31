Here's a comprehensive `README.md` for your Keycloak starter:

```markdown
# Keycloak Spring Boot Starter

A Spring Boot starter for automatic Keycloak configuration and management.

## Features

- Auto-creates realms, clients, roles, groups & users
- Configures client credentials and protocol mappers
- Supports both realm and client roles
- Idempotent operations (safe for redeploys)

## Prerequisites

- Keycloak server running (version 22+)
- JDK 17+
- Spring Boot 3.1+

## Installation

1. Add dependency to your `pom.xml`:

```xml
<dependency>
      <groupId>com.progressive.minds</groupId>
      <artifactId>chimera-api-nexus-security</artifactId>
      <version>1.0-SNAPSHOT</version>
</dependency>
```

2. Add configuration to `application.yml`:

```yaml
keycloak:
  admin:
    server-url: http://localhost:3000/
    username: admin
    password: admin[keyclock setup password]
    realm: master
  setup:
    realm: chimera
    client:
      clientId: chimera_api_client
      publicClient: false
      standardFlowEnabled: true
      directAccessGrantsEnabled: true
      redirectUris:
        - "http://localhost:8080/*"
      protocolMappers:
        - name: audience
          protocol: openid-connect
          protocolMapper: oidc-audience-mapper
          config:
            included.client.audience: chimera_api_client
    roles:
      - name: chimera_user
      - name: chimera_admin
    users:
      - username: user_test
        password: user_pass
        roles: [ chimera_user ]
      - username: admin_test
        password: admin_pass
        roles: [ chimera_admin ]
```

## Usage

### Automatic Setup

The starter will automatically configure Keycloak when your Spring Boot application starts:

1. Creates specified realm
2. Configures OIDC client with secret
3. Creates roles and users
4. Assigns roles and groups

### Retrieving OAuth2 Token

#### Using curl:

```bash
curl --location --request POST 'http://localhost:8080/realms/chimera/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=chimera_api_client' \
--data-urlencode 'client_secret=<CLIENT_SECRET>' \
--data-urlencode 'username=user_test' \
--data-urlencode 'password=user_pass' \
--data-urlencode 'grant_type=password'
```

#### Using Postman:

1. Create new POST request to:
   ```
   http://localhost:8080/realms/chimera/protocol/openid-connect/token
   ```
2. Set body as `x-www-form-urlencoded` with:
   | Key | Value |
   |---------------|------------------------|
   | client_id | chimera_api_client |
   | client_secret | <Get from Keycloak UI> |
   | username | user_test |
   | password | user_pass |
   | grant_type | password |

### Using the Token

Include the access token in requests:

```bash
curl --location --request GET 'http://your-api-endpoint' \
--header 'Authorization: Bearer <ACCESS_TOKEN>'
```

## Security Considerations

- 🔒 Never commit client secrets to source control
- 🔄 Rotate secrets regularly in production
- 🛡️ Use proper HTTPS configuration in production
- 🔐 Store passwords securely (use Vault or KMS)

## Troubleshooting

| Error              | Likely Cause            | Solution                           |
|--------------------|-------------------------|------------------------------------|
| 401 Unauthorized   | Invalid credentials     | Verify client secret/user password |
| 403 Forbidden      | Missing roles           | Check role assignments in config   |
| 409 Conflict       | Resource already exists | Safe to ignore - idempotent setup  |
| Connection refused | Keycloak not running    | Verify Keycloak server status      |

## Documentation Links

- [Keycloak Official Docs](https://www.keycloak.org/documentation)
- [Spring Security OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)

```
