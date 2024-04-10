package nl.timtwiest.simplejavaapi.integration.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import nl.timtwiest.simplejavaapi.integration.common.SpringStepDefinition;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class LivenessSteps extends SpringStepDefinition {
    private Response healthCheckResponse;

    @Given("the application is up and running")
    public void applicationIsUpAndRunning() {
        log.info("Checking that the application is active and ready to use.");
    }

    @When("I check if the application is responding")
    public void checkApplicationIsResponding() {
        healthCheckResponse = given().relaxedHTTPSValidation().get("/liveness");
    }

    @Then("I should get a confirmation that the application is operational")
    public void confirmApplicationIsOperational() {
        log.info("Verifying that the application is responding correctly.");

        assertEquals(200, healthCheckResponse.getStatusCode(), "Expected: application is up and running.");
        assertEquals("200 OK", healthCheckResponse.jsonPath().getString("status"), "Expected: response status is '200 OK'.");
        assertTrue(healthCheckResponse.jsonPath().getLong("timestamp") > 0, "Expected: a valid timestamp in the response.");
    }
}