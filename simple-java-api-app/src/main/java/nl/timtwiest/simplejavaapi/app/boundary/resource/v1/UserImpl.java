package nl.timtwiest.simplejavaapi.app.boundary.resource.v1;

import nl.timtwiest.simplejavaapi.api.UserApiController;
import nl.timtwiest.simplejavaapi.api.models.PaginatedUserResponse;
import nl.timtwiest.simplejavaapi.app.control.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
}
