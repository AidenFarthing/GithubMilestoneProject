package com.sparta.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.rest.RestTestBase;
import com.sparta.utils.Config;
import io.cucumber.java.After;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeletionStepDefs extends RestTestBase {

    private Response lastResponse;

    private static final String GRAPHQL_URL = Config.getGitHubBaseUri();
    private static final String REST_URL = Config.getRESTBaseUri();
    private static final String TOKEN = Config.getToken();
    private static final String OWNER = Config.getOwner();


    // --- JSON escaping helper ---
    private static String escapeForJson(String raw) {
        try {
            return new ObjectMapper().writeValueAsString(raw);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // --- Load GraphQL from resources ---
    private String loadGraphQLFile(String path) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) throw new RuntimeException("Could not find GraphQL file: " + path);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // --- Create repository via GraphQL ---
    private Response sendGraphQLMutation(String mutation, Map<String,Object> variables) {

        String payload =
                "{ \"query\": " + escapeForJson(mutation)
                        + ", \"variables\": " + new ObjectMapper().valueToTree(variables).toString()
                        + " }";

        return RestAssured
                .given()
                .baseUri(GRAPHQL_URL)
                .header("Authorization", "Bearer " + TOKEN)
                .header("Content-Type", "application/json")
                .body(payload)
                .post();
    }


    // --- Step Definitions ---

    @Given("a repository named {string} exists")
    public void repo_exists(String repoName) {
        String mutation = loadGraphQLFile("graphql/CreateRepo.graphql");

        Map<String, Object> variables = Map.of(
                "name", repoName,
                "visibility", "PRIVATE",
                "description", "Repository created automatically by GraphQL"
        );

        lastResponse = sendGraphQLMutation(mutation, variables);
        System.out.println("GRAPHQL CREATE RESPONSE: " + lastResponse.asString());
    }


    @Given("no repository exists with the name {string}")
    public void ensure_repo_missing(String repoName) {
        RestAssured
                .given()
                .baseUri(REST_URL)
                .header("Authorization", "Bearer " + TOKEN)
                .delete("/repos/" + OWNER + "/" + repoName);
    }


    @When("I delete the repository {string}")
    public void delete_repo(String repoName) {
        lastResponse = RestAssured
                .given()
                .baseUri(REST_URL)
                .header("Authorization", "Bearer " + TOKEN)
                .delete("/repos/" + OWNER + "/" + repoName);
    }


    @When("I delete the repository {string} without authentication")
    public void delete_no_auth(String repoName) {
        lastResponse = RestAssured
                .given()
                .baseUri(REST_URL)
                .delete("/repos/" + OWNER + "/" + repoName);
    }


    @When("I delete the repository {string} with an invalid token")
    public void delete_invalid_token(String repoName) {
        lastResponse = RestAssured
                .given()
                .baseUri(REST_URL)
                .header("Authorization", "Bearer BAD_TOKEN_123")
                .delete("/repos/" + OWNER + "/" + repoName);
    }


    @Then("the response status should be {int}")
    public void verify_status(int expectedStatus) {
        assertEquals(expectedStatus, lastResponse.statusCode());
    }


    @After
    public void cleanup() {
        // Always try to delete the repo after each scenario
        RestAssured
                .given()
                .baseUri(REST_URL)
                .header("Authorization", "Bearer " + TOKEN)
                .delete("/repos/" + OWNER + "/Automated-Test-Repo");
    }
}
