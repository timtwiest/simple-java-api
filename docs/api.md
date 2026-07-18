# API guide

The service exposes JSON endpoints on port `9080` by default. Interactive Swagger UI is available at `/api/ui`, and the generated OpenAPI document is available at `/api/yaml`.

## Liveness

`GET /liveness` reports that the process can serve requests.

```bash
curl http://localhost:9080/liveness
```

Example response:

```json
{
  "status": "200 OK",
  "timestamp": 1784376000000
}
```

## List users

`GET /api/v1/users` returns one page of users.

| Query parameter | Default | Meaning |
| --- | ---: | --- |
| `page` | `1` | One-based page number. Values below one use the first page. |
| `limit` | `10` | Page size. Values below one use a size of one. |

```bash
curl "http://localhost:9080/api/v1/users?page=1&limit=10"
```

Example response:

```json
{
  "currentPage": 1,
  "totalPages": 1,
  "totalItems": 1,
  "users": [
    {
      "name": "Jane Doe",
      "email": "jane@example.com"
    }
  ]
}
```

A new database contains no users, so the `users` array is empty until data is inserted. Create, update, and delete endpoints are planned but are not currently part of the contract.

## Contract sources

- [`liveness-contract.yaml`](../simple-java-api-api/src/main/resources/contract/liveness-contract.yaml)
- [`user-contract.yaml`](../simple-java-api-api/src/main/resources/contract/v1/user-contract.yaml)
- [Shared schemas](../simple-java-api-api/src/main/resources/common)

The contracts are the source of truth. Do not edit Java files under `target/generated-sources/openapi`.
