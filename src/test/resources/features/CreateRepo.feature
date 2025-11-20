Feature: Create New Repository

  As a Github user
  I want to create repositories using GraphQL
  So that I can automate the github process from the start

  @Happy
  Scenario: Query runs correctly
    Given a valid Github Token
    And a valid Repository Name
    When I run the createRepository Query
    Then the response status code should be 200
    And the GraphQL response should contain no errors
    And I should receive a repository object