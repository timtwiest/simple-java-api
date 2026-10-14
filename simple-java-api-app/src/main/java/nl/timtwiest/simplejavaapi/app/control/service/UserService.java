package nl.timtwiest.simplejavaapi.app.control.service;

import nl.timtwiest.simplejavaapi.api.models.PaginatedUserResponse;
import nl.timtwiest.simplejavaapi.api.models.UserDetailResponse;
import nl.timtwiest.simplejavaapi.api.models.UserRequest;
import nl.timtwiest.simplejavaapi.api.models.UserResponse;
import nl.timtwiest.simplejavaapi.app.entity.UserEntity;
import nl.timtwiest.simplejavaapi.app.entity.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_LIMIT = 10;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public PaginatedUserResponse users(Integer page, Integer limit) {
        int requestedPage = page == null ? DEFAULT_PAGE : page;
        int requestedLimit = limit == null ? DEFAULT_LIMIT : limit;
        Pageable pageable = PageRequest.of(
                Math.max(requestedPage - 1, 0),
                Math.max(requestedLimit, 1),
                Sort.by("id").ascending());

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

    @Transactional
    public UserDetailResponse create(UserRequest request) {
        UserEntity savedUser = userRepository.save(new UserEntity(request.getName(), request.getEmail()));
        return toDetailResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserDetailResponse get(long userId) {
        return toDetailResponse(findUser(userId));
    }

    @Transactional
    public UserDetailResponse update(long userId, UserRequest request) {
        UserEntity user = findUser(userId);
        user.update(request.getName(), request.getEmail());
        return toDetailResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(long userId) {
        userRepository.delete(findUser(userId));
    }

    private UserEntity findUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private UserDetailResponse toDetailResponse(UserEntity entity) {
        return new UserDetailResponse(entity.getId(), entity.getName(), entity.getEmail());
    }
}
