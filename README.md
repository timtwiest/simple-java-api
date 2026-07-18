# Simple Java API

[![Build](https://github.com/timtwiest/simple-java-api/actions/workflows/build.yml/badge.svg)](https://github.com/timtwiest/simple-java-api/actions/workflows/build.yml)
[![Java 25](https://img.shields.io/badge/Java-25-ED8B00?logo=openjdk&logoColor=white)](https://adoptium.net/temurin/releases/?version=25)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.14-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A compact, contract-first REST API built with Java, Spring Boot, PostgreSQL, and OpenAPI. The project demonstrates generated API interfaces, a layered application module, pagination, containerized local development, and both unit and behavior-driven tests.

## Highlights

- **Contract first:** OpenAPI documents generate the Spring controller interfaces and response models.
- **Clear module boundaries:** API contracts and generated code are separate from application code.
- **Ready for local development:** Run PostgreSQL by itself or start the complete stack with Docker Compose.
- **Built-in API explorer:** Swagger UI is available while the application is running.
- **Tested at multiple levels:** JUnit 5, Mockito, Cucumber, REST Assured, and JaCoCo are configured.
- **Automated builds:** GitHub Actions verifies every branch and pull request.

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

Stop the stack with `docker compose down`. Add `--volumes` if you also want to remove the database volume.

## Run from source

You need Java 25, Maven 3.9+, and Docker with Compose.

```bash
# Start only PostgreSQL
docker compose up -d postgres-db

# Build and test all modules
mvn clean verify

# Run the application
mvn spring-boot:run -pl simple-java-api-app
```

Development defaults are intentionally local-friendly (`postgres` / `postgres`). Override them through environment variables outside local development; see [development documentation](docs/development.md#configuration).

## API

| Method | Path | Purpose |
| --- | --- | --- |
| `GET` | `/liveness` | Report whether the service is running. |
| `GET` | `/api/v1/users?page=1&limit=10` | Return a paginated list of users. |

The source contracts live in [`simple-java-api-api/src/main/resources/contract`](simple-java-api-api/src/main/resources/contract). Maven generates the Java interfaces and models during the `generate-sources` phase; generated files are never edited by hand.

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
- [Contributing](CONTRIBUTING.md)
- [Security policy](SECURITY.md)
- [Code of conduct](CODE_OF_CONDUCT.md)

## Roadmap

- Add create, update, and delete operations for users.
- Add schema migrations with Flyway.
- Add validation and consistent problem-detail responses.
- Add authentication and authorization.
- Publish a versioned container image.

## License

Distributed under the [MIT License](LICENSE).
