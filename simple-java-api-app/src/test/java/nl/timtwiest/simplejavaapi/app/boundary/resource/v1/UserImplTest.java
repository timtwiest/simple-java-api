package nl.timtwiest.simplejavaapi.app.boundary.resource.v1;

import nl.timtwiest.simplejavaapi.api.models.PaginatedUserResponse;
import nl.timtwiest.simplejavaapi.api.models.UserDetailResponse;
import nl.timtwiest.simplejavaapi.api.models.UserRequest;
import nl.timtwiest.simplejavaapi.app.control.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserImplTest {

    @InjectMocks
    private UserImpl underTest;

    @Mock
    private UserService userService;

    @Test
    void should_retrieve_users() {
        PaginatedUserResponse expectedResponse = new PaginatedUserResponse(1, 0, 0L, List.of());
        when(userService.users(anyInt(), anyInt())).thenReturn(expectedResponse);

        ResponseEntity<PaginatedUserResponse> responseEntity = underTest.getUsers(1, 10);

        assertNotNull(responseEntity, "The response entity should not be null.");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "The status code should be OK.");

        PaginatedUserResponse actualResponse = responseEntity.getBody();
        assertNotNull(actualResponse, "The body of the response entity should not be null.");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldCreateUserWithLocation() {
        UserRequest request = new UserRequest("Luke Skywalker", "luke.skywalker@example.com");
        UserDetailResponse expected = new UserDetailResponse(42L, request.getName(), request.getEmail());
        when(userService.create(request)).thenReturn(expected);

        ResponseEntity<UserDetailResponse> response = underTest.createUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("/api/v1/users/42", response.getHeaders().getLocation().toString());
        assertEquals(expected, response.getBody());
    }

    @Test
    void shouldRetrieveOneUser() {
        UserDetailResponse expected = new UserDetailResponse(42L, "Luke Skywalker", "luke.skywalker@example.com");
        when(userService.get(42L)).thenReturn(expected);

        ResponseEntity<UserDetailResponse> response = underTest.getUser(42L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void shouldUpdateUser() {
        UserRequest request = new UserRequest("Leia Organa", "leia.organa@example.com");
        UserDetailResponse expected = new UserDetailResponse(42L, request.getName(), request.getEmail());
        when(userService.update(42L, request)).thenReturn(expected);

        ResponseEntity<UserDetailResponse> response = underTest.updateUser(42L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void shouldDeleteUser() {
        ResponseEntity<Void> response = underTest.deleteUser(42L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).delete(42L);
    }
}
