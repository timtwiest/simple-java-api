package nl.timtwiest.simplejavaapi.app.control.service;

import nl.timtwiest.simplejavaapi.api.models.PaginatedUserResponse;
import nl.timtwiest.simplejavaapi.app.entity.UserEntity;
import nl.timtwiest.simplejavaapi.app.entity.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService underTest;

    @Test
    void should_get_all_users() {
        UserEntity userEntity1 = UserEntity.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .build();

        UserEntity userEntity2 = UserEntity.builder()
                .name("Jane Doe")
                .email("janedoe@example.com")
                .build();

        List<UserEntity> mockUserEntities = List.of(userEntity1, userEntity2);

        Page<UserEntity> mockPage = new PageImpl<>(mockUserEntities, PageRequest.of(0, 10), mockUserEntities.size());
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        int samplePage = 1;
        int sampleLimit = 10;
        PaginatedUserResponse response = underTest.users(samplePage, sampleLimit);

        assertEquals(samplePage, response.getCurrentPage());
        assertEquals(1, response.getTotalPages());
        assertEquals(mockUserEntities.size(), response.getTotalItems());
        assertEquals(2, response.getUsers().size());
        assertEquals("John Doe", response.getUsers().get(0).getName());
        assertEquals("johndoe@example.com", response.getUsers().get(0).getEmail());
        assertEquals("Jane Doe", response.getUsers().get(1).getName());
        assertEquals("janedoe@example.com", response.getUsers().get(1).getEmail());
    }
}
