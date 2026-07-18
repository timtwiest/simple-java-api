# Architecture

Simple Java API is a small contract-first Spring Boot application split into two Maven modules.

## Modules

### `simple-java-api-api`

This module owns the OpenAPI contracts and shared schemas. The OpenAPI Generator Maven plugin turns those YAML files into Java controller interfaces and response models during `generate-sources`.

Keeping generated types in a dedicated module makes the HTTP contract explicit and prevents transport concerns from leaking into persistence code.

### `simple-java-api-app`

This module contains the executable Spring Boot application:

- `boundary/resource` implements generated controller interfaces.
- `control/service` contains application behavior and maps entities to API models.
- `entity` contains JPA entities and repositories.
- `resources/config` separates server, database, and OpenAPI settings.

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

## Contract workflow

1. Change a YAML contract under `simple-java-api-api/src/main/resources/contract` or a schema under `common`.
2. Run `mvn clean verify` from the repository root.
3. Implement any new generated interface methods in the application module.
4. Update tests, [`docs/api.md`](api.md), and requests under `http/`.

Generated code is written below `target/generated-sources/openapi` and is intentionally ignored by Git.
