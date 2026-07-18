# Development guide

## Prerequisites

- Java 25 (Temurin recommended)
- Maven 3.9 or newer
- Docker Engine with Docker Compose v2

## Common commands

```bash
# Start the development database
docker compose up -d postgres-db

# Compile and run every test
mvn clean verify

# Run only the application module and its required API module
mvn test -pl simple-java-api-app -am

# Start the application from source
mvn spring-boot:run -pl simple-java-api-app

# Build and start the complete container stack
docker compose up --build
```

JaCoCo writes its HTML coverage report to `simple-java-api-app/target/site/jacoco/index.html` during `verify`.

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

Copy [`.env.example`](../.env.example) to `.env` when you want Compose-specific overrides. Never commit real credentials.

## Tests

- Unit tests use JUnit 5 and Mockito.
- Repository and resource tests verify Spring-facing behavior.
- Cucumber features under `simple-java-api-app/src/test/resources/features` exercise the running application with REST Assured.
- Tests use an in-memory H2 database in PostgreSQL compatibility mode, while normal application runs use PostgreSQL.
- GitHub Actions runs `mvn --batch-mode --no-transfer-progress clean verify` without external service dependencies.

## Troubleshooting

If port `5432` is already in use, stop the local PostgreSQL instance or change the host-side Compose port. If the API starts before PostgreSQL is ready, inspect both services with `docker compose ps` and `docker compose logs`.

To reset all local database data:

```bash
docker compose down --volumes
```
