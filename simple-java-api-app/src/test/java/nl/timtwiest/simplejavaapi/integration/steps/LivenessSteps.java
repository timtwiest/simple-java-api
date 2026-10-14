package nl.timtwiest.simplejavaapi.integration.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import nl.timtwiest.simplejavaapi.integration.common.SpringStepDefinition;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LivenessSteps extends SpringStepDefinition {
    private Response healthCheckResponse;
    private Response missingPageResponse;

    @Given("the application is up and running")
    public void applicationIsUpAndRunning() {
    }

    @When("I check if the application is responding")
    public void checkApplicationIsResponding() {
        healthCheckResponse = given().relaxedHTTPSValidation().get("/liveness");
    }

    @Then("I should get a confirmation that the application is operational")
    public void confirmApplicationIsOperational() {
        assertEquals(200, healthCheckResponse.getStatusCode(), "Expected: application is up and running.");
        assertEquals("200 OK", healthCheckResponse.jsonPath().getString("status"), "Expected: response status is '200 OK'.");
        assertTrue(healthCheckResponse.jsonPath().getLong("timestamp") > 0, "Expected: a valid timestamp in the response.");
        assertEquals("The Force is strong with this API", healthCheckResponse.jsonPath().getString("message"));
        assertEquals("online", healthCheckResponse.jsonPath().getString("hyperdrive"));
        assertEquals("balanced", healthCheckResponse.getHeader("X-Force-Status"));
    }

    @When("I request a page that does not exist")
    public void requestMissingPage() {
        missingPageResponse = given().relaxedHTTPSValidation().get("/page/not/found");
    }

    @Then("I should receive a not found response")
    public void confirmNotFoundResponse() {
        assertEquals(404, missingPageResponse.getStatusCode());
        assertEquals(404, missingPageResponse.jsonPath().getInt("code"));
        assertEquals("Not Found", missingPageResponse.jsonPath().getString("message"));
    }
}
