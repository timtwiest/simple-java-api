ALTER TABLE users
    ALTER COLUMN updated_at TYPE TIMESTAMP(6) WITH TIME ZONE
        USING updated_at AT TIME ZONE current_setting('TIMEZONE'),
    ALTER COLUMN created_at TYPE TIMESTAMP(6) WITH TIME ZONE
        USING created_at AT TIME ZONE current_setting('TIMEZONE');
