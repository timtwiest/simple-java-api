package nl.timtwiest.simplejavaapi.app.entity.repositories;

import nl.timtwiest.simplejavaapi.app.App;
import nl.timtwiest.simplejavaapi.app.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class UserRepositoryIT {

    @Autowired
    private UserRepository underTest;

    @Test
    void shouldPersistAndPageUsersInPostgreSql() {
        underTest.saveAndFlush(new UserEntity("Han Solo", "han.solo@example.com"));

        Page<UserEntity> result = underTest.findAll(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        UserEntity savedUser = result.getContent().getFirst();
        assertNotNull(savedUser.getId());
        assertEquals("han.solo@example.com", savedUser.getEmail());
        assertEquals("Han Solo", savedUser.getName());
        assertNotNull(savedUser.getCreatedAt());
        assertNotNull(savedUser.getUpdatedAt());
    }
}
