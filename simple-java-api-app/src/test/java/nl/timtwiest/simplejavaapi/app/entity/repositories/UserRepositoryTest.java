package nl.timtwiest.simplejavaapi.app.entity.repositories;

import nl.timtwiest.simplejavaapi.app.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(showSql = false)
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Test
    void shouldPersistAndPageUsers() {
        underTest.save(new UserEntity("John Doe", "user@example.com"));

        Page<UserEntity> result = underTest.findAll(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        UserEntity savedUser = result.getContent().getFirst();
        assertNotNull(savedUser.getId());
        assertEquals("user@example.com", savedUser.getEmail());
        assertEquals("John Doe", savedUser.getName());
        assertNotNull(savedUser.getCreatedAt());
        assertNotNull(savedUser.getUpdatedAt());
    }
}
