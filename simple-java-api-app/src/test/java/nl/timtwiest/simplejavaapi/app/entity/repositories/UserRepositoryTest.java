package nl.timtwiest.simplejavaapi.app.entity.repositories;

import nl.timtwiest.simplejavaapi.app.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository underTest;

    @Test
    void should_find_all_pageables() {
        UserEntity userEntity = UserEntity.builder()
                .email("user@example.com")
                .name("John Doe")
                .build();

        Page<UserEntity> userPage = new PageImpl<>(List.of(userEntity));

        when(underTest.findAll(any(Pageable.class))).thenReturn(userPage);

        Page<UserEntity> result = underTest.findAll(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        assertEquals("user@example.com", result.getContent().getFirst().getEmail());
        assertEquals("John Doe", result.getContent().getFirst().getName());
    }
}
