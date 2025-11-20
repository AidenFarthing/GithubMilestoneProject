package com.sparta.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.rest.RestTestBase;
import com.sparta.utils.Config;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeletionStepDefs extends RestTestBase {

    private Response lastResponse;


    private static String escapeForJson(String raw) {
        try {
            return new ObjectMapper().writeValueAsString(raw);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createRepoViaGraphQL(String repoName) {
        String mutation = loadGraphQL("graphql/CreateRepository.graphql")
                .replace("Automated Test Repo", repoName);

        String payload = "{ \"query\": " + escapeForJson(mutation) + " }";

        lastResponse = RestAssured
                .given()
                .baseUri(Config.getGitHubBaseUri())
                .header("Authorization", "Bearer " + Config.getToken())
                .header("Content-Type", "application/json")
                .body(payload)
                .post();
    }

    @Given("a repository named {string} exists")
    public void a_repository_named_exists(String repoName) {
        createRepoViaGraphQL(repoName);
        if (lastResponse.statusCode() != 200) {
            throw new RuntimeException("Repo creation failed: " + lastResponse.asString());
        }
    }

    @Given("no repository exists with the name {string}")
    public void no_repo_exists(String repoName) {
        RestAssured
                .given()
                .baseUri(Config.getRESTBaseUri())
                .header("Authorization", "Bearer " + Config.getToken())
                .delete("/repos/" + Config.getOwner() + "/" + repoName);
    }

    @When("I delete the repository {string}")
    public void i_delete_repo(String repoName) {
        lastResponse = RestAssured
                .given()
                .baseUri(Config.getRESTBaseUri())
                .header("Authorization", "Bearer " + Config.getToken())
                .delete("/repos/" + Config.getOwner() + "/" + repoName);
    }

    @When("I delete the repository {string} without authentication")
    public void delete_repo_no_auth(String repoName) {
        lastResponse = RestAssured
                .given()
                .baseUri(Config.getRESTBaseUri())
                .delete("/repos/" + Config.getOwner() + "/" + repoName);
    }

    @When("I delete the repository {string} with an invalid token")
    public void delete_repo_invalid_token(String repoName) {
        lastResponse = RestAssured
                .given()
                .baseUri(Config.getRESTBaseUri())
                .header("Authorization", "Bearer INVALID_TOKEN_123")
                .delete("/repos/" + Config.getOwner() + "/" + repoName);
    }

    @Then("the response status should be {int}")
    public void verify_status(int statusCode) {
        assertEquals(statusCode, lastResponse.statusCode());
    }
}
