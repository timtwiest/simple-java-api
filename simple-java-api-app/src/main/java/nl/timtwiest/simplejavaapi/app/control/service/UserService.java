package nl.timtwiest.simplejavaapi.app.control.service;

import lombok.AllArgsConstructor;
import nl.timtwiest.simplejavaapi.api.models.PaginatedUserResponse;
import nl.timtwiest.simplejavaapi.api.models.UserResponse;
import nl.timtwiest.simplejavaapi.app.entity.UserEntity;
import nl.timtwiest.simplejavaapi.app.entity.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public PaginatedUserResponse users(int page, int limit) {
        page = Math.max(page - 1, 0);
        limit = Math.max(limit, 1);

        Pageable pageable = PageRequest.of(page, limit);

        Page<UserEntity> userPage = userRepository.findAll(pageable);

        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(entity -> UserResponse.builder()
                        .name(entity.getName())
                        .email(entity.getEmail())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));

        return PaginatedUserResponse.builder()
                .currentPage(userPage.getNumber() + 1)
                .totalPages(userPage.getTotalPages())
                .totalItems(userPage.getTotalElements())
                .users(userResponses)
                .build();
    }
}
