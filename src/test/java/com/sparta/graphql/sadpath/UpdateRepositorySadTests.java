package com.sparta.graphql.sadpath;

import com.sparta.graphql.TestBase;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UpdateRepositorySadTests extends TestBase {

    private static Response response;
    private static Response invalidAuthResponse;
    private static final String FAKE_REPO_ID = "R_FAKE_123456789";

    @BeforeAll
    static void beforeAll() throws IOException {

        Map<String, Object> variables = new HashMap<>();

        variables.put("repositoryId", FAKE_REPO_ID);
        variables.put("name", "ThisShouldFail");

        response = executeQuery(
                readQuery("UpdateRepository.graphql"),
                "UpdateRepository",
                variables
        );
        String query = readQuery("UpdateRepository.graphql");

        Map<String, Object> body = Map.of(
                "query", query,
                "operationName", "UpdateRepository",
                "variables", Map.of(
                        "repositoryId", REPOSITORY_ID,
                        "name", "ShouldNotWork"
                )
        );

        invalidAuthResponse = RestAssured
                .given()
                .baseUri(BASE_URI)
                .header("Authorization", "Bearer INVALID_TOKEN_123")
                .contentType(ContentType.JSON)
                .body(body)
                .post()
                .then()
                .extract().response();
    }

    @Test
    @DisplayName("Response should contain GraphQL errors")
    void responseShouldContainErrors() {

        assertThat(
                response.jsonPath().getList("errors"),
                not(empty())
        );
    }

    @Test
    @DisplayName("Error message should say repository not found")
    void errorMessageShouldSayRepoNotFound() {

        String errorMessage = response.jsonPath()
                .getString("errors[0].message");

        assertThat(errorMessage.toLowerCase(), containsString("could not resolve to a node with the global id of 'r_fake_123456789'"));
    }

    @Test
    @DisplayName("Response should not contain updateRepository data")
    void responseShouldNotContainRepositoryData() {

        String repoData = response.jsonPath()
                .getString("data.updateRepository");

        assertThat(repoData, is(nullValue()));
    }
    @Test
    @DisplayName("Update repository should fail with invalid authentication")
    void updateRepositoryWithInvalidAuthentication() {

        assertThat(invalidAuthResponse.statusCode(), anyOf(is(401), is(403)));
    }
}
