package nl.timtwiest.simplejavaapi.integration.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import nl.timtwiest.simplejavaapi.api.models.PaginatedUserResponse;
import nl.timtwiest.simplejavaapi.app.entity.UserEntity;
import nl.timtwiest.simplejavaapi.app.entity.repositories.UserRepository;
import nl.timtwiest.simplejavaapi.integration.common.SpringStepDefinition;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserSteps extends SpringStepDefinition {

    private final UserRepository userRepository;

    private PaginatedUserResponse paginatedUserResponse;
    private Response errorResponse;

    public UserSteps(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Given("the database has been populated with multiple user entries")
    public void populateDatabaseWithUserEntries(List<Map<String, String>> usersTable) {
        usersTable.forEach(row -> {
            UserEntity user = new UserEntity(row.get("name"), row.get("email"));
            userRepository.save(user);
        });

        verifyUserCountInDatabase(usersTable.size());
    }

    private void verifyUserCountInDatabase(int expectedCount) {
        long actualCount = userRepository.count();
        assertEquals(expectedCount, actualCount,
                "Expected and actual user counts in the database do not match.");
    }

    @When("I request page {int} of users with a limit of {int}")
    public void requestPageOfUsersWithLimit(int page, int limit) {
        paginatedUserResponse = given()
                .relaxedHTTPSValidation()
                .get(String.format("/api/v1/users?page=%d&limit=%d", page, limit))
                .as(PaginatedUserResponse.class);
    }

    @When("I request users with a non-numeric page")
    public void requestUsersWithInvalidPage() {
        errorResponse = given()
                .relaxedHTTPSValidation()
                .queryParam("page", "invalid")
                .queryParam("limit", 10)
                .get("/api/v1/users");
    }

    @Then("the response should correctly show page {int} with {int} users displayed out of a total of {int} users")
    public void verifyPaginatedResponse(int expectedPage, int expectedUsersPerPage, int expectedTotalUsers) {
        assertNotNull(paginatedUserResponse, "Paginated response should not be null.");
        assertEquals(expectedPage, paginatedUserResponse.getCurrentPage(),
                "Current page does not match the expected.");
        assertEquals(expectedUsersPerPage, paginatedUserResponse.getUsers().size(),
                "Number of users per page does not match the expected.");
        assertEquals(expectedTotalUsers, paginatedUserResponse.getTotalItems().intValue(),
                "Total number of users does not match the expected.");
    }

    @Then("the API should return the standard bad-request response")
    public void verifyBadRequestResponse() {
        assertEquals(400, errorResponse.statusCode());
        assertEquals(400, errorResponse.jsonPath().getInt("code"));
        assertEquals("Bad request", errorResponse.jsonPath().getString("message"));
        assertEquals("Invalid value for parameter 'page'", errorResponse.jsonPath().getString("errors[0]"));
    }
}
