package nl.timtwiest.simplejavaapi.app.boundary.resource.v1;

import nl.timtwiest.simplejavaapi.api.UserApiController;
import nl.timtwiest.simplejavaapi.api.models.PaginatedUserResponse;
import nl.timtwiest.simplejavaapi.api.models.UserDetailResponse;
import nl.timtwiest.simplejavaapi.api.models.UserRequest;
import nl.timtwiest.simplejavaapi.app.control.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * REST controller for user-related operations.
 * This class provides endpoints for retrieving user information within the application.
 */
@RestController
public class UserImpl implements UserApiController {
    private final UserService userService;

    public UserImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a list of users.
     * Returns a response entity containing a list of users with their details.
     *
     * @return ResponseEntity containing the list of user responses.
     */
    @Override
    public ResponseEntity<PaginatedUserResponse> getUsers(Integer page, Integer limit) {
        PaginatedUserResponse response = userService.users(page, limit);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserDetailResponse> createUser(UserRequest userRequest) {
        UserDetailResponse response = userService.create(userRequest);
        return ResponseEntity.created(URI.create("/api/v1/users/" + response.getId())).body(response);
    }

    @Override
    public ResponseEntity<UserDetailResponse> getUser(Long userId) {
        return ResponseEntity.ok(userService.get(userId));
    }

    @Override
    public ResponseEntity<UserDetailResponse> updateUser(Long userId, UserRequest userRequest) {
        return ResponseEntity.ok(userService.update(userId, userRequest));
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
