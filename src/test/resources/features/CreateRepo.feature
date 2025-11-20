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

  @Happy
  Scenario: Returned Repositories must include key fields
    Given a valid Github Token
    And a valid Repository Name
    And a Description
    When I run the createRepository Query
    Then the response should have an ID field
    And the Repository Name should match my Input
    And the Repository Description should match my Input
    And the Creation Timestamp should match today's date
    And the repository should have a valid url

