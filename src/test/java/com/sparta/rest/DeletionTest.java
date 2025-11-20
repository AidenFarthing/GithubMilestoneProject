package com.sparta.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.utils.Config;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeletionTest extends RestTestBase{

    private static final String GRAPHQL_URL = Config.getGitHubBaseUri();
    private static final String REST_URL = Config.getRESTBaseUri();
    private static final String TOKEN = Config.getToken();
    private static final String OWNER = Config.getOwner();
    private static final String TEST_REPO = "Automated-Test-Repo";

    private static String escapeForJson(String raw) {
        try {
            return new ObjectMapper().writeValueAsString(raw);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void createRepo() {
        String mutation = loadGraphQL("graphql/CreateRepository.graphql");
        // Build proper JSON
        String payload = "{ \"query\": " + escapeForJson(mutation) + " }";
        System.out.println(payload);
        Response response = RestAssured.given()
                .baseUri(GRAPHQL_URL)
                .header("Authorization", "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(payload)
                .post()
                .andReturn();

        System.out.println("GRAPHQL RESPONSE:\n" + response.asString());
        assertEquals(200, response.statusCode());
    }

    // DELETION TESTS
    @Test
    void deleteRepo() {
        Response response = deleteRepository(TEST_REPO);
        assertEquals(204, response.statusCode());
    }

    @Test
    void deleteRepoTwice() {
        Response first = deleteRepository(TEST_REPO);
        assertEquals(204, first.statusCode());

        Response second = deleteRepository(TEST_REPO);
        assertEquals(404, second.statusCode());
    }

    @Test
    void deleteNonExistent() {
        Response response = deleteRepository("nope-nope-nope-404");
        assertEquals(404, response.statusCode());
    }

    @Test
    void deleteWithoutAuth() {
        Response response = RestAssured
                .given()
                .baseUri("https://api.github.com")
                .delete("/repos/" + OWNER + "/" + TEST_REPO);

        assertEquals(401, response.statusCode());
    }

    @Test
    void deleteWithBadToken() {
        Response response = RestAssured
                .given()
                .baseUri("https://api.github.com")
                .header("Authorization", "Bearer INVALID")
                .delete("/repos/" + OWNER + "/" + TEST_REPO);

        assertEquals(401, response.statusCode());
    }
}

