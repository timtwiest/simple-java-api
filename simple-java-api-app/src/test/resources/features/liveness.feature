@service-integration-tests
Feature: Checking if the Application is Alive

  Background:
    Given the application is up and running

  Scenario: Confirming the application's readiness via a health check
    When I check if the application is responding
    Then I should get a confirmation that the application is operational