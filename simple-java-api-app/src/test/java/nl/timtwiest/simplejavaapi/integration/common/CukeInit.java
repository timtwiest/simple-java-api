package nl.timtwiest.simplejavaapi.integration.common;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import nl.timtwiest.simplejavaapi.app.entity.repositories.UserRepository;
import nl.timtwiest.simplejavaapi.integration.config.ConfigManager;
import org.springframework.boot.test.web.server.LocalServerPort;

public class CukeInit extends SpringStepDefinition {
    private final UserRepository userRepository;
    private static final String BASE_URL = ConfigManager.get("base.url");

    @LocalServerPort
    protected int port;

    @Inject
    public CukeInit(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Before
    public void before() {
        lazyConfigureRestAssured();
    }

    @After
    public void after() {
        RestAssured.reset();
        userRepository.deleteAllInBatch();
    }

    private synchronized void lazyConfigureRestAssured() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.port = port;
    }
}
