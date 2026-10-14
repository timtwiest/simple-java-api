# Development guide

## Prerequisites

- Java 25 (Temurin recommended)
- Maven Wrapper (included, pinned to Maven 3.10.0)
- Docker Engine with Docker Compose v2

## Common commands

```bash
# Start the development database
docker compose up -d postgres-db

# Compile and run every test
./mvnw clean verify

# Run only the application module and its required API module
./mvnw test -pl simple-java-api-app -am

# Start the application from source
./mvnw spring-boot:run -pl simple-java-api-app

# Build and start the complete container stack
docker compose up --build
```

JaCoCo writes its HTML coverage report to `simple-java-api-app/target/site/jacoco/index.html` during `verify`.

If you use SDKMAN, run `sdk env install` once from the repository root to install the JDK declared in [`.sdkmanrc`](../.sdkmanrc). The build fails early with a clear message when it is run with an unsupported Java or Maven version.

## Configuration

| Variable | Default | Purpose |
| --- | --- | --- |
| `PORT` | `9080` | HTTP port exposed by the application. |
| `POSTGRES_HOST` | `localhost` | PostgreSQL hostname. Compose sets this to `postgres-db`. |
| `POSTGRES_DATABASE` | `simplejavaapi` | Database name. |
| `POSTGRES_USER` | `postgres` | Database user. |
| `POSTGRES_PASSWORD` | `postgres` | Database password for local development. |
| `JPA_DDL_AUTO` | `update` | Hibernate schema-management mode. |
| `JPA_SHOW_SQL` | `false` | Log SQL statements when set to `true`. |
| `OPENAPI_ENABLED` | `true` | Enable Swagger UI and the runtime OpenAPI endpoint. Set to `false` when they should not be public. |

Copy [`.env.example`](../.env.example) to `.env` when you want Compose-specific overrides. Never commit real credentials.

## Tests

- Unit tests use JUnit 6 and Mockito.
- Repository and resource tests verify Spring-facing behavior.
- Cucumber features under `simple-java-api-app/src/test/resources/features` exercise the running application with REST Assured.
- Tests use an in-memory H2 database in PostgreSQL compatibility mode, while normal application runs use PostgreSQL.
- GitHub Actions runs `./mvnw --batch-mode --no-transfer-progress clean verify` without external service dependencies, builds the container image, and uploads the JaCoCo report.

## Troubleshooting

If port `5432` is already in use, stop the local PostgreSQL instance or change the host-side Compose port. If the API starts before PostgreSQL is ready, inspect both services with `docker compose ps` and `docker compose logs`.

To reset all local database data:

```bash
docker compose down --volumes
```
