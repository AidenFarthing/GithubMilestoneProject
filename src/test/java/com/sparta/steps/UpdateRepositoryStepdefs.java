package com.sparta.steps;

import com.sparta.graphql.TestBase;
import com.sparta.utils.Config;
import com.sparta.utils.GitHubRestClient;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UpdateRepositoryStepdefs extends TestBase {

    private final Map<String, Object> variables = new HashMap<>();
    private Response response;
    private static String tempRepoName;
    private static String tempRepoId;

    @Before
    public void resetTempRepo() throws IOException {
        TOKEN = Config.getToken();

        String createQuery = readQuery("CreateRepo.graphql");
        tempRepoName = "update-test-repo-" + System.currentTimeMillis();

        Map<String, Object> vars = Map.of(
                "name", tempRepoName,
                "visibility", "PRIVATE",
                "description", "Temp repo for Read tests"
        );
        Response createResponse = TestBase.executeQuery(
                createQuery,
                "CreateRepository",
                vars
        );

        tempRepoId = createResponse.jsonPath().getString("data.createRepository.repository.id");

    }


    @After
    public void deleteTempRepo() {
        if (tempRepoName != null && tempRepoId != null) {
            GitHubRestClient.deleteRepository(OWNER, tempRepoName);
        }
    }

    @Given("I have a valid repository ID")
    public void iHaveAValidRepositoryID() {
        variables.clear();
        variables.put("repositoryId", tempRepoId);
    }

    @And("an existing repository")
    public void anExistingRepository() {
    }

    @When("I send an updateRepository mutation with name {string} and description {string}")
    public void iSendAnUpdateRepositoryMutationWithNameAndDescription(String name, String description) throws IOException {
        variables.put("name", name);
        variables.put("description", description);

        String query = readQuery("UpdateRepository.graphql");
        response = executeQuery(query, "UpdateRepository", variables);
    }

    @Then("the repository name should be {string}")
    public void theRepositoryNameShouldBe(String expectedName) {
        String actual = response.jsonPath()
                .getString("data.updateRepository.repository.name");

        assertThat(actual, is(expectedName));
    }

    @And("the repository description should be {string}")
    public void theRepositoryDescriptionShouldBe(String expectedDescription) {
        String actual = response.jsonPath()
                .getString("data.updateRepository.repository.description");

        assertThat(actual, is(expectedDescription));
    }

    @When("I update only the description to {string}")
    public void iUpdateOnlyTheDescriptionTo(String description) throws IOException {
        variables.put("description", description);

        String query = readQuery("UpdateRepository.graphql");
        response = executeQuery(query, "UpdateRepository", variables);
    }

    @When("I update the homepageUrl to {string}")
    public void iUpdateTheHomepageUrlTo(String url) throws IOException {
        variables.put("homepageUrl", url);

        String query = readQuery("UpdateRepository.graphql");
        response = executeQuery(query, "UpdateRepository", variables);
    }

    @Then("the repository homepageUrl should be {string}")
    public void theRepositoryHomepageUrlShouldBe(String expectedUrl) {
        String actual = response.jsonPath()
                .getString("data.updateRepository.repository.homepageUrl");

        assertThat(actual, is(expectedUrl));
    }

    @Given("a repository ID that does not exist")
    public void aRepositoryIDThatDoesNotExist() {
        variables.put("repositoryId", "R_FAKE_999999999");
    }

    @When("I send an updateRepository mutation")
    public void iSendAnUpdateRepositoryMutation() throws IOException {
        String query = readQuery("UpdateRepository.graphql");
        response = executeQuery(query, "UpdateRepository", variables);
    }

    @Then("the response should contain GraphQL errors")
    public void theResponseShouldContainGraphQLErrors() {
        assertThat(response.jsonPath().getList("errors"), notNullValue());
    }

    @And("the error message should indicate that the repository was not found")
    public void theErrorMessageShouldIndicateThatTheRepositoryWasNotFound() {
        String message = response.jsonPath().getString("errors[0].message");
        assertThat(message.toLowerCase(), containsString("could not resolve to a node with the global id of 'r_fake_999999999'"));
    }

    @When("I send an updateRepository mutation with an empty name")
    public void iSendAnUpdateRepositoryMutationWithAnEmptyName() throws IOException {
        variables.put("name", "");

        String query = readQuery("UpdateRepository.graphql");
        response = executeQuery(query, "UpdateRepository", variables);
    }

    @And("the error message should indicate that {string}")
    public void theErrorMessageShouldIndicateThat(String text) {
        String message = response.jsonPath().getString("errors[0].message");
        assertThat(message.toLowerCase(), containsString(text.toLowerCase()));
    }

    @Given("I have an invalid or expired token")
    public void iHaveAnInvalidOrExpiredToken() {
        TOKEN = "INVALID_TOKEN_123";
    }

    @When("I send the updateRepository mutation with invalid authentication")
    public void iSendTheUpdateRepositoryMutationWithInvalidAuthentication() throws IOException {

        String query = readQuery("UpdateRepository.graphql");

        response = RestAssured
                .given()
                .baseUri(BASE_URI)
                .header("Authorization", "Bearer INVALID_TOKEN_123")
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "query", query,
                        "operationName", "UpdateRepository",
                        "variables", variables
                ))
                .post()
                .then()
                .extract().response();
    }

    @Then("the request should fail with an authentication error")
    public void theRequestShouldFailWithAnAuthenticationError() {
        assertThat(response.statusCode(), is(401));
    }

    @Given("no authentication header is provided")
    public void noAuthenticationHeaderIsProvided() {
    }

    @When("I send the updateRepository mutation with no authentication")
    public void iSendTheUpdateRepositoryMutationWithNoAuthentication() throws IOException {

        String query = readQuery("UpdateRepository.graphql");

        response = RestAssured
                .given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "query", query,
                        "operationName", "UpdateRepository",
                        "variables", variables
                ))
                .post()
                .then()
                .extract().response();
    }

    @Then("the response should indicate missing authentication")
    public void theResponseShouldIndicateMissingAuthentication() {
        assertThat(response.statusCode(), is(403));
    }

    @And("an invalid field value for homepageUrl {int}")
    public void anInvalidFieldValueForHomepageUrl(int url) {
        variables.put("homepageUrl", url);
    }

    @When("I send the updateRepository mutation")
    public void iSendTheUpdateRepositoryMutationAgain() throws IOException {
        String query = readQuery("UpdateRepository.graphql");
        response = executeQuery(query, "UpdateRepository", variables);
    }

    @Then("the updateRepository should be null")    public void theUpdateRepositoryShouldBeNull() {
        String updateRepo = response.jsonPath().getString("data.updateRepository");
        assertThat(updateRepo, is(nullValue()));    }
}
