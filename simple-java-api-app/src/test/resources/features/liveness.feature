@service-integration-tests
Feature: API-LIVENESS: Checking if the Application is Alive

  Background:
    Given the application is up and running

  Scenario: Confirming the application's readiness via a health check
    When I check if the application is responding
    Then I should get a confirmation that the application is operational

  Scenario: Requesting a page that does not exist
    When I request a page that does not exist
    Then I should receive a not found response
