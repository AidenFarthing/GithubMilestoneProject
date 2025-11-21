package com.sparta.steps;

import com.sparta.graphql.TestBase;
import com.sparta.pojos.ReadRepoResponse;
import com.sparta.utils.Config;
import com.sparta.utils.GitHubRestClient;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class ReadStepdefs extends TestBase{

    private  Response response;
    private  String query;
    private  Map<String, Object> variables;
    private  ReadRepoResponse pojoResponse;

    private static String tempRepoName;

    @Before
    public void resetTempRepo() throws IOException {
        TOKEN = Config.getToken();

        String createQuery = readQuery("CreateRepo.graphql");
        tempRepoName = "read-test-repo-" + System.currentTimeMillis();

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

    }

    @After
    public void deleteTempRepo() {
        if (tempRepoName != null) {
            GitHubRestClient.deleteRepository(OWNER, tempRepoName);
        }
    }



    @Given("I have a valid GitHub token")
    public void iHaveAValidGitHubToken() throws IOException {
        query = readQuery("ReadRepo.graphql");
        variables = Map.of("owner", OWNER, "name", tempRepoName);

    }

    @And("an existing repository with a known owner and name")
    public void anExistingRepositoryWithAKnownOwnerAndName() {
    }

    @When("I send a ReadRepository GraphQL query")
    public void iSendAReadRepositoryGraphQLQuery() {
        response = executeQuery(query, "ReadRepo", variables);

        boolean hasErrors = response.jsonPath().get("errors") == null;
        boolean hasMessage = response.jsonPath().get("message") == null;

        //  Only map to POJO if response is clean
        if (hasErrors && hasMessage && response.jsonPath().get("data.repository") != null) {
            pojoResponse = response.as(ReadRepoResponse.class);
        } else {
            pojoResponse = null;
        }
    }


    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int code) {
        assertThat(response.statusCode(), is(code));
    }

    @And("the GraphQL response should contain no errors")
    public void theGraphQLResponseShouldContainNoErrors() {
        assertThat(response.jsonPath().getString("errors"), is(nullValue()));
    }

    @And("the repository details should be returned")
    public void theRepositoryDetailsShouldBeReturned() {

        assertThat(pojoResponse.getData().getRepository().getId(), notNullValue());
        assertThat(pojoResponse.getData().getRepository().getName(), is(tempRepoName));
        assertThat(pojoResponse.getData().getRepository().getOwner().getLogin(), is(OWNER));
    }

    @And("a repository name that does not exist")
    public void aRepositoryNameThatDoesNotExist() {
        variables = Map.of("owner", OWNER, "name", "this-repo-does-not-exist-123");

    }

    @Then("the GraphQL response should return a null repository object")
    public void theGraphQLResponseShouldReturnANullRepositoryObject() {
        assertThat(response.jsonPath().get("data.repository"), is(nullValue()));
        assertThat(response.jsonPath().getString("errors[0].type"), is("NOT_FOUND"));
    }

    @Given("I have an invalid or expired GitHub token")
    public void iHaveAnInvalidOrExpiredGitHubToken() throws IOException {
        query = readQuery("ReadRepo.graphql");
        TOKEN = "invalid_token_value";
        variables = Map.of("owner", OWNER, "name", tempRepoName);
    }

    @Then("the response should return an authentication error")
    public void theResponseShouldReturnAnAuthenticationError() {
        assertThat(response.statusCode(), is(401));
        assertThat(response.jsonPath().getString("message"), containsString("Bad credentials"));
    }

    @Given("I have no authentication header")
    public void iHaveNoAuthenticationHeader() throws IOException {
        query = readQuery("ReadRepo.graphql");
        variables = Map.of("owner", OWNER, "name", tempRepoName);
        TOKEN = "";
    }

    @Then("the response should return a missing authentication error")
    public void theResponseShouldReturnAMissingAuthenticationError() {
        assertThat(response.jsonPath().getString("message"), containsString("Bad credentials"));
    }

    @And("a malformed or illegal repository name")
    public void aMalformedOrIllegalRepositoryName() {
        variables = Map.of("owner", OWNER, "name", "%%%%%%");
    }

    @Then("the GraphQL response should include validation errors")
    public void theGraphQLResponseShouldIncludeValidationErrors() {
        assertThat(response.jsonPath().getString("errors[0].message"), notNullValue());
    }


}
