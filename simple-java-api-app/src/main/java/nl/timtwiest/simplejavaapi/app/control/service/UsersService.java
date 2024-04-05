package nl.timtwiest.simplejavaapi.app.control.service;

import nl.timtwiest.simplejavaapi.api.models.PaginatedUserResponse;
import nl.timtwiest.simplejavaapi.api.models.UserResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UsersService {
    private static final List<UserResponse> MOCK_USERS = List.of(
            UserResponse.builder().name("John Doe").email("johndoe@example.com").build(),
            UserResponse.builder().name("Jane Smith").email("janesmith@example.com").build(),
            UserResponse.builder().name("Alex Johnson").email("alexjohnson@example.com").build()
    );

    public PaginatedUserResponse users(int page, int limit) {
        int totalItems = MOCK_USERS.size();

        limit = Math.max(limit, 1);
        int totalPages = (int) Math.ceil((double) totalItems / limit);

        page = page < 1 ? 1 : Math.min(page, totalPages);
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, totalItems);

        List<UserResponse> paginatedUsers = start > totalItems ? Collections.emptyList() : MOCK_USERS.subList(start, end);

        return PaginatedUserResponse.builder()
                .currentPage(page)
                .totalPages(totalPages)
                .totalItems(totalItems)
                .users(paginatedUsers)
                .build();
    }
}
