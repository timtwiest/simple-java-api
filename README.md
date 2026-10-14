# Simple Java API

[![Build](https://github.com/timtwiest/simple-java-api/actions/workflows/build.yml/badge.svg)](https://github.com/timtwiest/simple-java-api/actions/workflows/build.yml)
[![Java 25](https://img.shields.io/badge/Java-25-ED8B00?logo=openjdk&logoColor=white)](https://adoptium.net/temurin/releases/?version=25)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A compact, contract-first REST API built with Java, Spring Boot, PostgreSQL, and OpenAPI. The project demonstrates generated API interfaces, a layered application module, pagination, containerized local development, and both unit and behavior-driven tests.

## Highlights

- **Contract first:** OpenAPI documents generate the Spring controller interfaces and response models.
- **Clear module boundaries:** API contracts and generated code are separate from application code.
- **Versioned database changes:** Flyway owns the PostgreSQL schema and Hibernate validates it at startup.
- **Production-like tests:** Integration tests use PostgreSQL 16 through Testcontainers instead of an in-memory substitute.
- **Built-in API explorer:** Swagger UI is available while the application is running.
- **Quality gates:** JUnit 6, Mockito, Cucumber, REST Assured, JaCoCo, and SpotBugs run in Maven verification.
- **Supply-chain checks:** CI publishes a CycloneDX SBOM and scans it with OSV-Scanner.
- **Production operations:** Actuator readiness/liveness probes, ECS JSON logs, TLS-verified database connections, and backup guidance are included.

## Quick start

The quickest way to run the complete stack is Docker Compose:

```bash
git clone https://github.com/timtwiest/simple-java-api.git
cd simple-java-api
docker compose up --build
```

Once both services are healthy:

- API: <http://localhost:9080>
- Swagger UI: <http://localhost:9080/api/ui>
- OpenAPI document: <http://localhost:9080/api/yaml>
- Liveness check: <http://localhost:9080/liveness>

Try the API from another terminal:

```bash
curl http://localhost:9080/liveness
curl "http://localhost:9080/api/v1/users?page=1&limit=10"
```

Stop the stack with `docker compose down`. Database data remains in `./.volumes/postgres/data`; remove that directory separately only when you intentionally want to reset local data.

## Run from source

You need Java 25 and Docker with Compose. The Maven Wrapper downloads the pinned Maven 3.10.0 distribution automatically, so a system Maven installation is optional. SDKMAN users can install and select the repository JDK with `sdk env install`.

```bash
# Start only PostgreSQL
docker compose up -d postgres-db

# Build and test all modules
./mvnw clean verify

# Run the application
./mvnw spring-boot:run -pl simple-java-api-app
```

Development defaults are intentionally local-friendly (`postgres` / `postgres`). The `prod` profile instead requires explicit database settings, disables Swagger/OpenAPI, and keeps Hibernate in validation-only mode. See [development documentation](docs/development.md#configuration).

## API

| Method | Path | Purpose |
| --- | --- | --- |
| `GET` | `/liveness` | Report whether the service is running. |
| `GET` | `/api/v1/users?page=1&limit=10` | Return a paginated list of users. |
| `POST` | `/api/v1/users` | Create a user. |
| `GET` | `/api/v1/users/{userId}` | Retrieve a user. |
| `PUT` | `/api/v1/users/{userId}` | Fully replace a user. |
| `DELETE` | `/api/v1/users/{userId}` | Delete a user. |

The source contracts live in [`simple-java-api-api/src/main/resources/contract`](simple-java-api-api/src/main/resources/contract). Maven generates the Java interfaces and models during the `generate-sources` phase with Spring Boot 4 settings; generated files are never edited by hand.

See [API documentation](docs/api.md) for response examples and contract details. Ready-to-run requests for JetBrains HTTP Client are available in [`http/`](http/).

## Project layout

```text
simple-java-api/
├── simple-java-api-api/   OpenAPI contracts and generated Java API types
├── simple-java-api-app/   Spring Boot application, persistence, and tests
├── docs/                  Architecture, API, and development guides
├── http/                  Example HTTP requests
├── Dockerfile             Multi-stage application image
└── docker-compose.yml     API and PostgreSQL development stack
```

Read [architecture documentation](docs/architecture.md) for the request flow and design decisions.

## Documentation

- [Architecture](docs/architecture.md)
- [API guide](docs/api.md)
- [Development and configuration](docs/development.md)
- [Production operations](docs/operations.md)
- [Contributing](CONTRIBUTING.md)
- [Security policy](SECURITY.md)
- [Code of conduct](CODE_OF_CONDUCT.md)

## Roadmap

- Add authentication and authorization.

## Container releases

Tags matching `vMAJOR.MINOR.PATCH` publish a tested and vulnerability-scanned image to `ghcr.io/timtwiest/simple-java-api`. Each release receives full, major/minor, major, and `latest` tags plus build provenance and an SBOM.

```bash
git tag -a v1.0.1 -m "Release v1.0.1"
git push origin v1.0.1
docker pull ghcr.io/timtwiest/simple-java-api:1.0.1
```

Only tag commits already present on `master`. Creating the tag starts the release workflow and makes the container package externally visible according to its GHCR package settings.

## License

Distributed under the [MIT License](LICENSE).
