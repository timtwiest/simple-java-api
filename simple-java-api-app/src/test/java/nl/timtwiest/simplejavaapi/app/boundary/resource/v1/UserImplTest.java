package nl.timtwiest.simplejavaapi.app.boundary.resource.v1;

import nl.timtwiest.simplejavaapi.api.models.PaginatedUserResponse;
import nl.timtwiest.simplejavaapi.app.control.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserImplTest {

    @InjectMocks
    private UserImpl underTest;

    @Mock
    private UserService userService;

    @Test
    void should_retrieve_users() {
        PaginatedUserResponse expectedResponse = PaginatedUserResponse.builder().build();
        when(userService.users(anyInt(), anyInt())).thenReturn(expectedResponse);

        ResponseEntity<PaginatedUserResponse> responseEntity = underTest.getUsers(1, 10);

        assertNotNull(responseEntity, "The response entity should not be null.");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "The status code should be OK.");

        PaginatedUserResponse actualResponse = responseEntity.getBody();
        assertNotNull(actualResponse, "The body of the response entity should not be null.");
    }
}
