package nl.timtwiest.simplejavaapi.integration;

import nl.timtwiest.simplejavaapi.app.App;
import nl.timtwiest.simplejavaapi.app.entity.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.oneOf;

@ActiveProfiles("test")
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserCrudIT {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clearUsers() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCompleteTheRebelAllianceCrudMission() {
        long userId = given()
                .port(port)
                .contentType("application/json")
                .body("""
                        {"name":"Luke Skywalker","email":"luke.skywalker@example.com","role":"JEDI"}
                        """)
                .post("/api/v1/users")
                .then()
                .statusCode(201)
                .header("Location", matchesPattern("/api/v1/users/[0-9]+"))
                .body("name", equalTo("Luke Skywalker"))
                .body("email", equalTo("luke.skywalker@example.com"))
                .body("role", equalTo("JEDI"))
                .extract()
                .jsonPath()
                .getLong("id");

        given().port(port)
                .get("/api/v1/users/{userId}", userId)
                .then()
                .statusCode(200)
                .header("X-Force-Status", equalTo("balanced"))
                .body("id", equalTo((int) userId))
                .body("name", equalTo("Luke Skywalker"));

        given().port(port)
                .contentType("application/json")
                .body("""
                        {"name":"Leia Organa","email":"leia.organa@example.com","role":"SENATOR"}
                        """)
                .put("/api/v1/users/{userId}", userId)
                .then()
                .statusCode(200)
                .body("id", equalTo((int) userId))
                .body("name", equalTo("Leia Organa"))
                .body("email", equalTo("leia.organa@example.com"))
                .body("role", equalTo("SENATOR"));

        given().port(port)
                .delete("/api/v1/users/{userId}", userId)
                .then()
                .statusCode(204);

        given().port(port)
                .get("/api/v1/users/{userId}", userId)
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("message", equalTo("Not Found"));
    }

    @Test
    void shouldRejectInvalidUserInput() {
        given().port(port)
                .contentType("application/json")
                .body("""
                        {"name":"","email":"not-an-email"}
                        """)
                .post("/api/v1/users")
                .then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("message", equalTo("Bad request"))
                .body("errors.size()", equalTo(2));
    }

    @Test
    void shouldReceiveWisdomFromAJvmFarFarAway() {
        given().port(port)
                .get("/api/v1/quotes/random")
                .then()
                .statusCode(200)
                .header("X-Force-Status", equalTo("balanced"))
                .body("quote", oneOf(
                        "May your builds be green and your dependencies current.",
                        "The API awakens when the database is ready.",
                        "A clean build is the path to the light side.",
                        "Trust the contract, but verify the response.",
                        "In a JVM far, far away, every test still passed."));
    }
}
