# Production operations

## Deployment configuration

Run with `SPRING_PROFILES_ACTIVE=prod` and inject database credentials from the deployment platform's secret manager. PostgreSQL TLS defaults to `verify-full`; install the database CA certificate in the runtime trust configuration and keep the management port private.

The application emits ECS JSON to standard output. Send stdout and stderr to the platform's centralized log collector rather than writing application log files inside the container.

## Health checks

The production management port defaults to `9081` and exposes only:

- `/actuator/health`
- `/actuator/health/liveness`
- `/actuator/health/readiness`

Use liveness to decide whether the process must restart and readiness to decide whether it should receive traffic. Do not route the management port through the public ingress.

## Database backup and restore

The database provider should take encrypted automated backups with retention and point-in-time recovery. Before every schema migration, create an additional on-demand backup.

The following PostgreSQL client commands create a portable custom-format backup and inspect it:

```bash
pg_dump --format=custom --no-owner --no-privileges \
  --dbname="$DATABASE_URL" --file=simplejavaapi.dump
pg_restore --list simplejavaapi.dump
```

Test restoration against a disposable empty database before relying on a backup:

```bash
pg_restore --exit-on-error --no-owner --no-privileges \
  --dbname="$RESTORE_DATABASE_URL" simplejavaapi.dump
```

Never use the production database as `RESTORE_DATABASE_URL`. Record the backup timestamp, Flyway version, restore duration, and verification result. A restore is only considered tested after the restored application starts with `ddl-auto=validate` and its health and API checks pass.

## Migration rollout

1. Take and verify an on-demand backup.
2. Inspect pending Flyway migrations and legacy-data prerequisites.
3. Deploy one application instance and wait for migration and readiness success.
4. Roll out the remaining instances.
5. Verify API behavior, health probes, logs, and error rates.

Applied Flyway migration files are immutable. Roll forward with a new migration; do not edit or delete an applied migration.

## Rollback

Application releases should use immutable image digests. If application code fails but the migration remains backward-compatible, redeploy the previous digest. If a non-backward-compatible migration fails, stop writes and follow the database provider's reviewed restore or point-in-time recovery procedure. Practice both paths in staging before production rollout.
