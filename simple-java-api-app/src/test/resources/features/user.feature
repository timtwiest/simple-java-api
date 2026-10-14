@service-integration-tests
Feature: API-USER: This Gherkin file tests the User-service

  Scenario: Paginated User Listing with Correct Counts
    Given the database has been populated with multiple user entries
      | name           | email                      |
      | Luke Skywalker | luke.skywalker@example.com |
      | Leia Organa    | leia.organa@example.com    |
      | Han Solo       | han.solo@example.com       |
      | Obi-Wan Kenobi | obi-wan.kenobi@example.com |
    When I request page 1 of users with a limit of 2
    Then the response should correctly show page 1 with 2 users displayed out of a total of 4 users

  Scenario: Requesting a page number beyond the total pages available
    Given the database has been populated with multiple user entries
      | name           | email                      |
      | Luke Skywalker | luke.skywalker@example.com |
      | Leia Organa    | leia.organa@example.com    |
    When I request page 3 of users with a limit of 2
    Then the response should correctly show page 3 with 0 users displayed out of a total of 2 users

  Scenario: Rejecting a non-numeric page parameter consistently
    When I request users with a non-numeric page
    Then the API should return the standard bad-request response
