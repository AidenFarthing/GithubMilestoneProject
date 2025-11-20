Feature: Update an existing GitHub repository
  As a user
  I want to update an existing repository
  So that I can keep the repository’s details accurate when project requirements change


  @Happy
  Scenario: H1 Update repository name and description
    Given I have a valid repository ID
    And an existing repository
    When I send an updateRepository mutation with name "Updated Name" and description "Updated Description"
    Then the repository name should be "Updated-Name"
    And the repository description should be "Updated Description"


  @Happy
  Scenario: H2 Update one field only (description)
    Given I have a valid repository ID
    And an existing repository
    When I update only the description to "New Description Only"
    Then the repository description should be "New Description Only"


  @Happy
  Scenario: H3 Update repository homepage URL
    Given I have a valid repository ID
    And an existing repository
    When I update the homepageUrl to "https://thisisatest.com"
    Then the repository homepageUrl should be "https://thisisatest.com"


  @Sad
  Scenario: S1 Update a repository that does not exist
    Given a repository ID that does not exist
    When I send an updateRepository mutation
    Then the response should contain GraphQL errors
    And the error message should indicate that the repository was not found


  @Sad
  Scenario: S2 Update fails due to empty name
    Given I have a valid repository ID
    And an existing repository
    When I send an updateRepository mutation with an empty name
    Then the response should contain GraphQL errors
    And the error message should indicate that "name is too short (minimum is 1 character)"


  @Sad
  Scenario: S4 Update with invalid or expired token
    Given I have an invalid or expired token
    And I have a valid repository ID
    When I send the updateRepository mutation with invalid authentication
    Then the request should fail with an authentication error


  @Sad
  Scenario: S5 Update with no authentication
    Given no authentication header is provided
    And I have a valid repository ID
    When I send the updateRepository mutation with no authentication
    Then the response should indicate missing authentication


  @Sad
  Scenario: S6 Update fails with invalid fields
    Given I have a valid repository ID
    And an empty repository name
    When I send the updateRepository mutation
    Then the response should contain GraphQL errors
    And the updateRepository should be null
