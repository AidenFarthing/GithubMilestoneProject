package com.sparta.steps;

import com.sparta.rest.RestTestBase;
import io.cucumber.java.After;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeletionStepDefs extends RestTestBase {

    private Response lastResponse;

    @Given("a repository named {string} exists")
    public void repo_exists(String repoName) {

        String mutation = loadGraphQL("graphql/CreateRepo.graphql");

        lastResponse = sendGraphQL(
                mutation,
                Map.of(
                        "name", repoName,
                        "visibility", "PRIVATE",
                        "description", "Created automatically by tests"
                ),
                TOKEN
        );

        System.out.println("CREATE REPO RESPONSE:");
        System.out.println(lastResponse.asString());
    }

    @Given("no repository exists with the name {string}")
    public void ensure_repo_missing(String repoName) {

        lastResponse = restDelete(repoName, TOKEN);

        System.out.println("DELETE (ensure missing) RESPONSE:");
        System.out.println(lastResponse.asString());
    }

    @When("I delete the repository {string}")
    public void delete_repo(String repoName) {
        lastResponse = restDelete(repoName, TOKEN);

        System.out.println("DELETE RESPONSE:");
        System.out.println(lastResponse.asString());
    }

    @When("I delete the repository {string} without authentication")
    public void delete_no_auth(String repoName) {
        lastResponse = restDelete(repoName, ""); // ← visible

        System.out.println("DELETE NO AUTH RESPONSE:");
        System.out.println(lastResponse.asString());
    }

    @When("I delete the repository {string} with an invalid token")
    public void delete_invalid_token(String repoName) {
        lastResponse = restDelete(repoName, "BAD_TOKEN_123"); // ← visible

        System.out.println("DELETE INVALID TOKEN RESPONSE:");
        System.out.println(lastResponse.asString());
    }

    @Then("the response status should be {int}")
    public void verify_status(int expectedStatus) {
        assertEquals(expectedStatus, lastResponse.statusCode());
    }

    @After
    public void cleanup() {
        restDelete("Automated-Test-Repo", TOKEN);
    }
}
