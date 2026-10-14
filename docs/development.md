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
The same lifecycle enforces at least 80% line coverage and 60% branch coverage, runs SpotBugs, and creates the aggregate CycloneDX SBOM at `target/bom.json`. Integration tests require a running Docker-compatible container engine because they start PostgreSQL with Testcontainers.

If you use SDKMAN, run `sdk env install` once from the repository root to install the JDK declared in [`.sdkmanrc`](../.sdkmanrc). The build fails early with a clear message when it is run with an unsupported Java or Maven version.

## Configuration

| Variable | Default | Purpose |
| --- | --- | --- |
| `PORT` | `9080` | HTTP port exposed by the application. |
| `SPRING_PROFILES_ACTIVE` | `openapi,server,db` | Active configuration profiles. Use `prod` in production. |
| `POSTGRES_HOST` | `localhost` | PostgreSQL hostname. Compose sets this to `postgres-db`. Required explicitly by `prod`. |
| `POSTGRES_PORT` | `5432` | PostgreSQL port used by the `prod` profile. |
| `POSTGRES_DATABASE` | `simplejavaapi` | Database name. |
| `POSTGRES_USER` | `postgres` | Database user. |
| `POSTGRES_PASSWORD` | `postgres` | Database password for local development. |
| `POSTGRES_SSL_MODE` | `verify-full` in `prod` | PostgreSQL TLS verification mode. Only use `disable` for an isolated local database. |
| `MANAGEMENT_PORT` | `9081` in `prod` | Separate port for Actuator health probes; keep it private. |
| `JPA_SHOW_SQL` | `false` | Log SQL statements when set to `true`. |
| `FLYWAY_BASELINE_ON_MIGRATE` | `false` | One-time adoption switch for a pre-Flyway schema; keep disabled normally. |
| `OPENAPI_ENABLED` | `true` | Enable Swagger UI and the runtime OpenAPI endpoint. Set to `false` when they should not be public. |

Copy [`.env.example`](../.env.example) to `.env` when you want Compose-specific overrides. Never commit real credentials.

### Production profile

The `prod` profile has no fallback database credentials and disables Swagger UI and the runtime OpenAPI document regardless of `OPENAPI_ENABLED`. Supply all database values through the deployment environment:

```bash
SPRING_PROFILES_ACTIVE=prod \
POSTGRES_HOST=db.example.internal \
POSTGRES_PORT=5432 \
POSTGRES_DATABASE=simplejavaapi \
POSTGRES_USER=simplejavaapi \
POSTGRES_PASSWORD='replace-with-a-secret' \
./mvnw spring-boot:run -pl simple-java-api-app
```

Use a secret manager in a real deployment. The example above only shows the required variable names; do not put production secrets in `.env` or shell history. Production uses PostgreSQL `sslmode=verify-full`, writes ECS-formatted JSON logs, and serves `/actuator/health`, `/actuator/health/liveness`, and `/actuator/health/readiness` on the separate management port. Do not expose that port publicly.

## Database migrations

Flyway migrations live in `simple-java-api-app/src/main/resources/db/migration`. Hibernate never changes the schema: `ddl-auto=validate` makes startup fail when the mapped entity and migrated schema disagree. Once a migration has been applied, do not edit it; add the next versioned migration instead.

For a new database, start the application normally and Flyway applies all migrations. A database previously managed by Hibernate has a `users` table but no Flyway history. After taking a backup and confirming that its schema matches `V1__create_users.sql`, adopt it once with:

```bash
FLYWAY_BASELINE_ON_MIGRATE=true docker compose up --build
```

Before baselining, confirm that `email`, `name`, `updated_at`, and `created_at` contain no nulls. This records version 1 as the baseline, converts timestamps to timezone-aware PostgreSQL columns, and makes the API-required fields non-null. Stop the stack and set `FLYWAY_BASELINE_ON_MIGRATE=false` immediately afterward. Never enable automatic baselining permanently or against an uninspected production schema.

## Tests

- Unit tests use JUnit 6 and Mockito.
- Repository and migration tests run against PostgreSQL 16 through Testcontainers.
- Cucumber features under `simple-java-api-app/src/test/resources/features` exercise the running application with REST Assured.
- The production profile has an integration test that verifies its safe configuration defaults.
- GitHub Actions runs the full Maven lifecycle, scans the SBOM and image, and uploads JaCoCo, test, and SBOM artifacts. Docker is required locally and available on GitHub-hosted runners.
- Green `master` builds publish `edge` and `sha-<commit>` images. Release Please derives versions and changelogs from conventional commits; merging its release pull request creates an immutable Git tag and starts the versioned GHCR release. Never move or reuse a release tag.

## Troubleshooting

If port `5432` is already in use, stop the local PostgreSQL instance or change the host-side Compose port. If the API starts before PostgreSQL is ready, inspect both services with `docker compose ps` and `docker compose logs`.

The Compose database is named `death-star-database` and uses the bind-mounted `./.volumes/postgres/data` directory. `docker compose down --volumes` does not remove that directory. To reset local data, stop the stack and remove that directory manually only when the data is disposable.

```bash
docker compose down
rm -rf .volumes/postgres/data
```
