package nl.timtwiest.simplejavaapi.app.entity;

import nl.timtwiest.simplejavaapi.api.models.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserEntityTest {

    @Test
    void shouldExposeConstructorValues() {
        UserEntity user = new UserEntity("Luke Skywalker", "luke.skywalker@example.com");

        assertEquals("luke.skywalker@example.com", user.getEmail());
        assertEquals("Luke Skywalker", user.getName());
        assertEquals(UserRole.REBEL, user.getRole());
    }

    @Test
    void shouldUpdateMutableFields() {
        UserEntity user = new UserEntity("Anakin Skywalker", "anakin.skywalker@example.com", UserRole.JEDI);

        user.setId(2L);
        user.setEmail("darth.vader@example.com");
        user.setName("Darth Vader");

        assertEquals(2L, user.getId());
        assertEquals("darth.vader@example.com", user.getEmail());
        assertEquals("Darth Vader", user.getName());
    }

    @Test
    void shouldRenderUsefulDiagnosticString() {
        UserEntity user = new UserEntity("Han Solo", "han.solo@example.com", UserRole.SMUGGLER);

        String userString = user.toString();

        assertTrue(userString.contains("Han Solo"));
        assertTrue(userString.contains("han.solo@example.com"));
        assertTrue(userString.contains("SMUGGLER"));
    }
}
