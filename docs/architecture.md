# Architecture

Simple Java API is a small contract-first Spring Boot application split into two Maven modules.

## Modules

### `simple-java-api-api`

This module owns the OpenAPI contracts and shared schemas. The OpenAPI Generator Maven plugin turns those YAML files into Java controller interfaces and response models during `generate-sources`.

Keeping generated types in a dedicated module makes the HTTP contract explicit and prevents transport concerns from leaking into persistence code.

### `simple-java-api-app`

This module contains the executable Spring Boot application:

- `boundary/resource` implements generated controller interfaces.
- `boundary/resource/ApiExceptionHandler` maps malformed input and unexpected failures to the contract's `ErrorResponse` shape without exposing internal exception details.
- `control/service` contains application behavior and maps entities to API models.
- `entity` contains JPA entities and repositories.
- `resources/config` separates server, database, OpenAPI, and production settings.
- `resources/db/migration` contains the append-only Flyway schema history.

## Request flow

```text
HTTP request
    │
    ▼
Generated API interface
    │ implemented by
    ▼
REST resource
    │
    ▼
Application service
    │
    ▼
Spring Data repository
    │
    ▼
PostgreSQL
```

Responses travel back through the same layers. The service maps persistence entities to generated API response models so database types are not exposed directly.

Flyway applies versioned SQL before JPA starts. Hibernate uses `ddl-auto=validate`, so it detects drift but never mutates the database. Repository, migration, and HTTP integration tests all use a real PostgreSQL 16 container.

The user listing is ordered by primary key before pagination so page boundaries are deterministic. The persistence schema enforces all response-required fields as non-null.

In production, Spring Boot Actuator runs on a separate management port with only health and info exposed. Health details and component names remain hidden, while liveness and readiness probe groups are available to the deployment platform.

## Contract workflow

1. Change a YAML contract under `simple-java-api-api/src/main/resources/contract` or a schema under `common`.
2. Run `./mvnw clean verify` from the repository root.
3. Implement any new generated interface methods in the application module.
4. Update tests, [`docs/api.md`](api.md), and requests under `http/`.

Generated code is written below `target/generated-sources/openapi` and is intentionally ignored by Git.
