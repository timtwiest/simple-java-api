package nl.timtwiest.simplejavaapi.app.control.service;

import nl.timtwiest.simplejavaapi.api.models.PaginatedUserResponse;
import nl.timtwiest.simplejavaapi.api.models.UserDetailResponse;
import nl.timtwiest.simplejavaapi.api.models.UserRequest;
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
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final Sort BY_ID = Sort.by("id").ascending();

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService underTest;

    @Test
    void should_get_all_users() {
        UserEntity userEntity1 = new UserEntity("Luke Skywalker", "luke.skywalker@example.com");
        UserEntity userEntity2 = new UserEntity("Leia Organa", "leia.organa@example.com");

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
        assertEquals("Luke Skywalker", response.getUsers().get(0).getName());
        assertEquals("luke.skywalker@example.com", response.getUsers().get(0).getEmail());
        assertEquals("Leia Organa", response.getUsers().get(1).getName());
        assertEquals("leia.organa@example.com", response.getUsers().get(1).getEmail());
    }

    @Test
    void shouldUseDocumentedDefaultsWhenPaginationIsMissing() {
        Page<UserEntity> emptyPage = Page.empty(PageRequest.of(0, 10));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(emptyPage);

        PaginatedUserResponse response = underTest.users(null, null);

        verify(userRepository).findAll(PageRequest.of(0, 10, BY_ID));
        assertEquals(1, response.getCurrentPage());
        assertEquals(0, response.getTotalItems());
    }

    @Test
    void shouldPreserveClampingForNonPositivePagination() {
        Page<UserEntity> emptyPage = Page.empty(PageRequest.of(0, 1));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(emptyPage);

        underTest.users(0, 0);

        verify(userRepository).findAll(PageRequest.of(0, 1, BY_ID));
    }

    @Test
    void shouldCreateUser() {
        doAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            user.setId(42L);
            return user;
        }).when(userRepository).save(any(UserEntity.class));

        UserDetailResponse response = underTest.create(new UserRequest("Luke Skywalker", "luke.skywalker@example.com"));

        assertEquals(42L, response.getId());
        assertEquals("Luke Skywalker", response.getName());
        assertEquals("luke.skywalker@example.com", response.getEmail());
    }

    @Test
    void shouldGetUser() {
        UserEntity user = new UserEntity("Luke Skywalker", "luke.skywalker@example.com");
        user.setId(42L);
        when(userRepository.findById(42L)).thenReturn(java.util.Optional.of(user));

        UserDetailResponse response = underTest.get(42L);

        assertEquals(42L, response.getId());
        assertEquals("Luke Skywalker", response.getName());
    }

    @Test
    void shouldUpdateUser() {
        UserEntity user = new UserEntity("Anakin Skywalker", "anakin.skywalker@example.com");
        user.setId(42L);
        when(userRepository.findById(42L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserDetailResponse response = underTest.update(42L, new UserRequest("Darth Vader", "darth.vader@example.com"));

        assertEquals("Darth Vader", response.getName());
        assertEquals("darth.vader@example.com", response.getEmail());
    }

    @Test
    void shouldDeleteUser() {
        UserEntity user = new UserEntity("Han Solo", "han.solo@example.com");
        user.setId(42L);
        when(userRepository.findById(42L)).thenReturn(java.util.Optional.of(user));

        underTest.delete(42L);

        verify(userRepository).delete(user);
    }

    @Test
    void shouldRejectMissingUser() {
        when(userRepository.findById(42L)).thenReturn(java.util.Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> underTest.get(42L));

        assertEquals(42L, exception.getUserId());
    }
}
