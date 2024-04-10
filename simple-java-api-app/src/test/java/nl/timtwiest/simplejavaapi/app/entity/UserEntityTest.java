package nl.timtwiest.simplejavaapi.app.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserEntityTest {

    @Test
    void testBuilderAndGetters() {
        Date now = new Date();
        UserEntity user = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getName());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void testSetters() {
        UserEntity user = new UserEntity();
        user.setId(2L);
        user.setEmail("another@example.com");
        user.setName("Another Test");

        assertEquals(2L, user.getId());
        assertEquals("another@example.com", user.getEmail());
        assertEquals("Another Test", user.getName());
    }

    @Test
    void testToString() {
        Date now = new Date();
        UserEntity user = UserEntity.builder()
                .id(3L)
                .email("user@example.com")
                .name("User Name")
                .createdAt(now)
                .updatedAt(now)
                .build();

        String userString = user.toString();

        assertNotNull(userString, "The toString method should not return null.");
        assertTrue(userString.contains("User Name"), "The toString output should contain the user's name.");
        assertTrue(userString.contains("user@example.com"), "The toString output should contain the user's email.");
    }

}
