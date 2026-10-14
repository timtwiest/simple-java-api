package nl.timtwiest.simplejavaapi.integration;

import nl.timtwiest.simplejavaapi.app.App;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DatabaseMigrationIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldApplyFlywayMigrationsToPostgreSql() {
        String databaseVersion = jdbcTemplate.queryForObject("SELECT version()", String.class);
        Integer successfulMigrations = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM flyway_schema_history WHERE success", Integer.class);
        String currentVersion = jdbcTemplate.queryForObject(
                "SELECT version FROM flyway_schema_history WHERE success ORDER BY installed_rank DESC LIMIT 1",
                String.class);
        String timestampType = jdbcTemplate.queryForObject("""
                SELECT data_type
                FROM information_schema.columns
                WHERE table_schema = 'public'
                  AND table_name = 'users'
                  AND column_name = 'created_at'
                """, String.class);
        Integer requiredColumns = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM information_schema.columns
                WHERE table_schema = 'public'
                  AND table_name = 'users'
                  AND column_name IN ('id', 'email', 'name', 'role', 'updated_at', 'created_at')
                  AND is_nullable = 'NO'
                """, Integer.class);

        assertTrue(databaseVersion.startsWith("PostgreSQL 16."));
        assertEquals(4, successfulMigrations);
        assertEquals("4", currentVersion);
        assertEquals("timestamp with time zone", timestampType);
        assertEquals(6, requiredColumns);
    }
}
