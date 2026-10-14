package nl.timtwiest.simplejavaapi.app.control.service;

import nl.timtwiest.simplejavaapi.api.models.PaginatedUserResponse;
import nl.timtwiest.simplejavaapi.api.models.UserResponse;
import nl.timtwiest.simplejavaapi.app.entity.UserEntity;
import nl.timtwiest.simplejavaapi.app.entity.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_LIMIT = 10;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PaginatedUserResponse users(Integer page, Integer limit) {
        int requestedPage = page == null ? DEFAULT_PAGE : page;
        int requestedLimit = limit == null ? DEFAULT_LIMIT : limit;
        Pageable pageable = PageRequest.of(Math.max(requestedPage - 1, 0), Math.max(requestedLimit, 1));

        Page<UserEntity> userPage = userRepository.findAll(pageable);

        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(entity -> new UserResponse(entity.getName(), entity.getEmail()))
                .toList();

        return new PaginatedUserResponse(
                userPage.getNumber() + 1,
                userPage.getTotalPages(),
                userPage.getTotalElements(),
                userResponses);
    }
}
