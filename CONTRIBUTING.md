# Contributing

Thanks for taking the time to improve Simple Java API.

## Development workflow

1. Fork the repository and create a focused branch.
2. Start PostgreSQL with `docker compose up -d postgres-db`.
3. Make the smallest change that solves the problem.
4. Run `./mvnw clean verify` before opening a pull request.
5. Update the OpenAPI contract and documentation when behavior changes.

Use [Conventional Commits](https://www.conventionalcommits.org/): `fix:` creates a patch, `feat:` a minor release, and `!` or `BREAKING CHANGE` a major release. Include tests for changed behavior. Do not commit generated sources; Maven recreates them from `simple-java-api-api/src/main/resources/contract`.

## Pull requests

Describe what changed, why it changed, and how you verified it. Keep unrelated refactors in separate pull requests so reviews stay easy to follow.

By contributing, you agree that your contribution is licensed under the repository's [MIT License](LICENSE).

All participation is subject to the project [Code of Conduct](CODE_OF_CONDUCT.md). Report security vulnerabilities according to the [Security Policy](SECURITY.md), not through a public issue.
