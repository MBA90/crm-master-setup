# CRM Master Setup Service

A Spring Boot microservice for managing crm master setup data.

## Tech Stack

- Java 21
- Spring Boot 3.5.15 (Web, Data JPA, Validation, Security, OAuth2 Resource Server)
- PostgreSQL (`postgresql` JDBC driver)
- Liquibase (schema migrations)
- MapStruct 1.5.5 (entity/DTO mapping)
- Lombok
- Maven

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+ (or use the included Maven Wrapper)
- Docker & Docker Compose (for the PostgreSQL database)

### Configuration

Connection and server settings live in `src/main/resources/application.yaml`.
Defaults:

| Setting        | Default                                |
|----------------|----------------------------------------|
| Server port    | `8202`                                 |
| Datasource URL | `jdbc:postgresql://localhost:5432/crm` |
| Username       | `crm_master_setup`                     |
| Schema         | `crm_master_setup`                     |

Liquibase runs on startup using `classpath:db/changelog/master.xml` to create the
`MASTER SETUP` tables, sequence, and indexes. JPA `ddl-auto` is `none` — the schema is
owned entirely by Liquibase.

### Database Setup

A `docker-compose.yml` at the project root starts a PostgreSQL 16 instance
(container `postgres_db`) with a superuser `admin` / `admin` and a `crm`
database, exposed on port `5432`:

```bash
docker compose up -d
```

The compose file only creates the `admin` superuser and `crm` database. The
application connects as a dedicated `crm_master_setup` role that owns its own schema,
so after the container is up, create the role and schema once:

```sql

CREATE USER crm_master_setup
  WITH PASSWORD 'admin';

CREATE SCHEMA crm_master_setup
  AUTHORIZATION crm_master_setup;

```
Run them against the `crm` database.

With the role and schema in place, Liquibase can create its tracking tables and
apply the changelog on the next application start.

### Build & Run

```bash
# Run the app
./mvnw spring-boot:run

# Build a jar
./mvnw clean package

# Run tests
./mvnw test
```

The service starts on http://localhost:8203.

## Docker

A `Dockerfile` is provided that packages the built JAR on top of an
`eclipse-temurin:21-jre-alpine` base image. Build the JAR first, then the image:

```bash
./mvnw clean package
docker build -t crm-master-setup .
```

Run the container, mapping the service's port (8202):

```bash
docker run --rm -p 8203:8203 crm-master-setup
```

## 🔐 Security

This service is a pure **OAuth2 resource server** — it validates bearer JWTs
issued by a Keycloak realm on every request. There's no login flow or session
state here; Keycloak owns authentication, this service only enforces it.

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8180/realms/crm-realm
```

Spring uses that issuer to auto-discover Keycloak's JWKS and verify each
token's signature, expiry, and issuer. Only `/api/**` is locked down
(`SecurityConfig`); everything else is `permitAll()`. Sessions are stateless
and CSRF is disabled — bearer tokens don't need either.

### From token to typed principal

Keycloak ships roles under `realm_access.roles`, not the `scope` claim Spring
expects by default, and hands you a raw `Jwt` rather than a domain object. Two
small converters close that gap:

| Class                       | Job                                                          |
|------------------------------|---------------------------------------------------------------|
| `KeycloakRealmRoleConverter` | maps `realm_access.roles` → `ROLE_*` authorities              |
| `UserPrincipalJwtConverter`  | builds a typed `UserPrincipal` (userId, username, email, name, department, phone, mobile) as the authentication's principal |

Anywhere in the codebase, `SecurityUtil.getUserPrincipal()`.

### Who can call what

Role names live as constants in `SecurityRoles` (`ORG_ADMIN`, `SALES_REP`,
`ACCOUNT_MANAGER`, `MARKETING_MANAGER`, `SALES_MANAGER`), ready for per-endpoint
`@PreAuthorize` checks as they're added. Today the security filter chain only
requires an authenticated principal on `/api/**` — it does not yet
differentiate access by role.

## CI/CD

A `Jenkinsfile` defines a declarative pipeline that:

- **Build** — runs `mvn clean package -DskipTests` and archives the resulting JAR.
- **Test** — runs `mvn test` and publishes the JUnit surefire reports.
- **Docker Build & Push** — logs in to Docker Hub, builds the image tagged with
  the Jenkins `BUILD_NUMBER`, and pushes it, then removes the local image
  afterward.

The pipeline requires JDK 21 (`jdk-21`) and Maven (`maven-3.9`) tool
installations configured in Jenkins, plus a `docker-hub-credentials`
username/password credential. Update the `DOCKER_HUB_USER` build parameter (or
its default) in the `Jenkinsfile` to your own Docker Hub account.
