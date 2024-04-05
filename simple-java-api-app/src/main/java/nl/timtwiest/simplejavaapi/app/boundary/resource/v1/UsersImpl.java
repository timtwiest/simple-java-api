package nl.timtwiest.simplejavaapi.app.boundary.resource.v1;

import lombok.AllArgsConstructor;
import nl.timtwiest.simplejavaapi.api.UsersApiController;
import nl.timtwiest.simplejavaapi.api.models.PaginatedUserResponse;
import nl.timtwiest.simplejavaapi.app.control.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user-related operations.
 * This class provides endpoints for retrieving user information within the application.
 */
@RestController
@AllArgsConstructor
public class UsersImpl implements UsersApiController {
    private final UsersService usersService;

    /**
     * Retrieves a list of users.
     * Returns a response entity containing a list of users with their details.
     *
     * @return ResponseEntity containing the list of user responses.
     */
    @Override
    public ResponseEntity<PaginatedUserResponse> getUsers(Integer page, Integer limit) {
        PaginatedUserResponse response = usersService.users(page, limit);
        return ResponseEntity.ok(response);
    }
}
