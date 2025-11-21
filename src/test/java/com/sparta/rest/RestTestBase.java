package com.sparta.rest;

import com.sparta.utils.Config;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class RestTestBase {

    public static final String REST_BASE = Config.getRESTBaseUri();
    public static final String GRAPHQL_BASE = Config.getGitHubBaseUri();
    public static final String TOKEN = Config.getToken();
    public static final String OWNER = Config.getOwner();


    // simple REST deletion
    protected Response deleteRepository(String repoName) {
        return RestAssured
                .given()
                .baseUri(REST_BASE)
                .header("Authorization", "Bearer " + TOKEN)
                .delete("/repos/" + OWNER + "/" + repoName)
                .andReturn();
    }

    // load .graphql file
    protected String loadGraphQL(String path) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) throw new RuntimeException("Could not find GraphQL file: " + path);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

