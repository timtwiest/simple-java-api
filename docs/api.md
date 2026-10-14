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
      "name": "Luke Skywalker",
      "email": "luke.skywalker@example.com"
    }
  ]
}
```

A new database contains no users, so the `users` array is empty until data is inserted.

## Manage users

Create a user with `POST /api/v1/users`:

```bash
curl --request POST http://localhost:9080/api/v1/users \
  --header 'Content-Type: application/json' \
  --data '{"name":"Luke Skywalker","email":"luke.skywalker@example.com"}'
```

The response uses HTTP `201`, includes a `Location` header, and returns the assigned `id`. Individual users can then be read, fully replaced, or deleted:

```bash
curl http://localhost:9080/api/v1/users/1

curl --request PUT http://localhost:9080/api/v1/users/1 \
  --header 'Content-Type: application/json' \
  --data '{"name":"Leia Organa","email":"leia.organa@example.com"}'

curl --request DELETE http://localhost:9080/api/v1/users/1
```

Replacement returns HTTP `200`; deletion returns `204` without a body. Missing users return the shared error format with HTTP `404`. Names are required and limited to 255 characters; email addresses are required, validated, and limited to 255 characters.

Malformed query parameters return the shared error format. For example, `page=invalid` returns HTTP `400`:

```json
{
  "code": 400,
  "message": "Bad request",
  "errors": [
    "Invalid value for parameter 'page'"
  ]
}
```

## Contract sources

- [`liveness-contract.yaml`](../simple-java-api-api/src/main/resources/contract/liveness-contract.yaml)
- [`user-contract.yaml`](../simple-java-api-api/src/main/resources/contract/v1/user-contract.yaml)
- [Shared schemas](../simple-java-api-api/src/main/resources/common)

The contracts are the source of truth. Do not edit Java files under `target/generated-sources/openapi`.
