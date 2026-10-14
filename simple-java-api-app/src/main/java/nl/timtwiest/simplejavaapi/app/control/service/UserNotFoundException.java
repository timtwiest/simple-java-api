package nl.timtwiest.simplejavaapi.app.control.service;

public class UserNotFoundException extends RuntimeException {
    private final long userId;

    public UserNotFoundException(long userId) {
        super("User %d was not found".formatted(userId));
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
