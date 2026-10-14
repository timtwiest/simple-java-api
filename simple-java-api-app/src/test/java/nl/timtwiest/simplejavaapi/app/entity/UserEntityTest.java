package nl.timtwiest.simplejavaapi.app.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserEntityTest {

    @Test
    void shouldExposeConstructorValues() {
        UserEntity user = new UserEntity("Test User", "test@example.com");

        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getName());
    }

    @Test
    void shouldUpdateMutableFields() {
        UserEntity user = new UserEntity("Initial User", "initial@example.com");

        user.setId(2L);
        user.setEmail("another@example.com");
        user.setName("Another Test");

        assertEquals(2L, user.getId());
        assertEquals("another@example.com", user.getEmail());
        assertEquals("Another Test", user.getName());
    }

    @Test
    void shouldRenderUsefulDiagnosticString() {
        UserEntity user = new UserEntity("User Name", "user@example.com");

        String userString = user.toString();

        assertTrue(userString.contains("User Name"));
        assertTrue(userString.contains("user@example.com"));
    }
}
