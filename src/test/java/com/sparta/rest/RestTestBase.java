package com.sparta.rest;

import com.sparta.utils.Config;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RestTestBase {

    public static final String REST_BASE = Config.getRESTBaseUri();
    public static final String GRAPHQL_BASE = Config.getGitHubBaseUri();
    public static final String TOKEN = Config.getToken();
    public static final String OWNER = Config.getOwner();


    // Load .graphql file
    protected String loadGraphQL(String path) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) throw new RuntimeException("Could not find GraphQL file: " + path);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Generic GraphQL request sender
    protected Response sendGraphQL(String query, Map<String, Object> variables, String token) {

        Map<String, Object> payload = Map.of(
                "query", query,
                "variables", variables
        );

        return RestAssured
                .given()
                .baseUri(GRAPHQL_BASE)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .post()
                .andReturn();
    }

    // Generic REST delete
    protected Response restDelete(String repoName, String token) {
        return RestAssured
                .given()
                .baseUri(REST_BASE)
                .header("Authorization", "Bearer " + token)
                .delete("/repos/" + OWNER + "/" + repoName)
                .andReturn();
    }
}
