package com.sparta.steps;

import com.sparta.graphql.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateRepoStepDefs extends TestBase {

    private static String validToken;
    private static String repoName;

    private static String visibility = "PRIVATE";
    private static String description = "";

    private static String query;
    private static Map<String, Object> variables;
    private static Response response;

    @Given("a valid Github Token")
    public void aValidGithubToken() {
        validToken = TOKEN;
    }

    @And("a valid Repository Name")
    public void aValidRepositoryName() {
        repoName = "Automated-Test-Repo";
    }

    @When("I run the createRepository Query")
    public void iRunTheCreateRepositoryQuery() throws IOException {
        query = readQuery("CreateRepo.graphql");
        variables = Map.of(
                "name",repoName,
                "visibility",visibility,
                "ownerId",OWNER,
                "description", description
        );

        response = executeQuery(query,"CreateRepository",variables);

    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        assertThat(response.statusCode(), is(statusCode));
    }

    @And("the GraphQL response should contain no errors")
    public void theGraphQLResponseShouldContainNoErrors() {
        assertThat(response.jsonPath().getList("errors"), is(nullValue()));
    }

    @And("I should receive a repository object")
    public void iShouldReceiveARepositoryObject() {
        assertThat(response.path("data.createRepository.repository"), not(nullValue()));
    }
}
