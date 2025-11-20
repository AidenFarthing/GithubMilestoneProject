Feature: Repository deletion
  The system should correctly allow or reject deletion of GitHub repositories
  depending on authentication, repository existence, and request validity.

  Scenario: Successfully delete an existing repository
    Given a repository named "Automated-Test-Repo" exists
    When I delete the repository "Automated-Test-Repo"
    Then the response status should be 204

  Scenario: Deleting the same repository twice should fail
    Given a repository named "Automated-Test-Repo" exists
    When I delete the repository "Automated-Test-Repo"
    And I delete the repository "Automated-Test-Repo"
    Then the response status should be 404

  Scenario: Deleting a non-existent repository should return 404
    Given no repository exists with the name "repo-that-does-not-exist-12345"
    When I delete the repository "repo-that-does-not-exist-12345"
    Then the response status should be 404

  Scenario: Deleting a repository without authentication should return 401
    Given a repository named "Automated-Test-Repo" exists
    When I delete the repository "Automated-Test-Repo" without authentication
    Then the response status should be 401

  Scenario: Deleting a repository with an invalid token should return 401
    Given a repository named "Automated-Test-Repo" exists
    When I delete the repository "Automated-Test-Repo" with an invalid token
    Then the response status should be 401
