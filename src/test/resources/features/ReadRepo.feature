Feature: Read Repository
  As a GitHub repository maintainer
  I want to view an existing repository using the GitHub GraphQL API
  So that I can confirm the repository exists and validate its details

  # -------------------------
  # Happy Path Scenarios
  # -------------------------

  Scenario: H1 - Read an existing repository
    Given I have a valid GitHub token
    And an existing repository with a known owner and name
    When I send a ReadRepository GraphQL query
    Then the response status code should be 200
    And the GraphQL response should contain no errors
    And the repository details should be returned


  # -------------------------
  # Sad Path Scenarios
  # -------------------------

  Scenario: S1 - Read a repository that does not exist
    Given I have a valid GitHub token
    And a repository name that does not exist
    When I send a ReadRepository GraphQL query
    Then the GraphQL response should return a null repository object

  Scenario: S2 - Read repository with an invalid or expired token
    Given I have an invalid or expired GitHub token
    When I send a ReadRepository GraphQL query
    Then the response should return an authentication error

  Scenario: S3 - Read repository with no authentication
    Given I have no authentication header
    When I send a ReadRepository GraphQL query
    Then the response should return a missing authentication error

  Scenario: S4 - Read repository using a malformed repository name
    Given I have a valid GitHub token
    And a malformed or illegal repository name
    When I send a ReadRepository GraphQL query
    Then the GraphQL response should include validation errors