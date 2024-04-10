@service-integration-tests
Feature: API-USER: This Gherkin file tests the User-service

  Scenario: Paginated User Listing with Correct Counts
    Given the database has been populated with multiple user entries
      | name        | email                   |
      | John Doe    | john.doe@example.com    |
      | Jane Doe    | jane.doe@example.com    |
      | Alex Smith  | alex.smith@example.com  |
      | Emily Jones | emily.jones@example.com |
    When I request page 1 of users with a limit of 2
    Then the response should correctly show page 1 with 2 users displayed out of a total of 4 users

  Scenario: Requesting a page number beyond the total pages available
    Given the database has been populated with multiple user entries
      | name        | email                   |
      | John Doe    | john.doe@example.com    |
      | Jane Doe    | jane.doe@example.com    |
    When I request page 3 of users with a limit of 2
    Then the response should correctly show page 3 with 0 users displayed out of a total of 2 users
