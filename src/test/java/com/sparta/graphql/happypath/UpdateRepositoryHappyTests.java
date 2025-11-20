package com.sparta.graphql.happypath;

import com.sparta.graphql.TestBase;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UpdateRepositoryHappyTests extends TestBase {

    public static Response response;

    private static String originalName;
    private static String originalDescription;
    private static String newName;
    private static String newDescription;

    @BeforeAll
    static void beforeAll() throws IOException {

        Response readResponse = executeQuery(
                readQuery("ReadRepo.graphql"),
                "ReadRepo",
                Map.of(
                        "owner", OWNER,
                        "name", REPO
                )
        );

        originalName = readResponse.jsonPath().getString("data.repository.name");
        originalDescription = readResponse.jsonPath().getString("data.repository.description");

        newName = originalName + "-updated";
        newDescription = originalDescription + " updated by test";

        response = executeQuery(
                readQuery("UpdateRepository.graphql"),
                "UpdateRepository",
                Map.of(
                        "repositoryId", REPOSITORY_ID,
                        "name", newName,
                        "description", newDescription,
                        "visibility", "PRIVATE"
                )
        );
    }

    @AfterAll
    static void afterAll() throws IOException {

        Map<String, Object> variables = new java.util.HashMap<>();

        variables.put("repositoryId", REPOSITORY_ID);
        variables.put("name", originalName);

        if (originalDescription != null) {
            variables.put("description", originalDescription);
        }

        executeQuery(
                readQuery("UpdateRepository.graphql"),
                "UpdateRepository",
                variables
        );
    }

    @Test
    @DisplayName("Updating repo status returns 200")
    void testStatusCode() {
        assertThat(response.statusCode(), is(200));
    }

    @Test @DisplayName("No graphQL errors")
    void testErrors() {
        assertThat(response.jsonPath().getList("errors"), is(nullValue()));
    }

    @Test
    @DisplayName("Repository name should be updated")
    void testRepositoryNameUpdated() {

        String returnedName = response.jsonPath().getString("data.updateRepository.repository.name");


        assertThat(returnedName, is(newName));
    }

    @Test
    @DisplayName("Repository description should be updated")
    void testRepositoryDescriptionUpdated() {

        String returnedDescription = response.jsonPath().getString("data.updateRepository.repository.description");

        assertThat(returnedDescription, is(newDescription));
    }

    // GitHub’s GraphQL API doesn't allow changing repository visibility, so even if you send the update,
    // it just returns the original value instead of changing it.
    @Test
    @DisplayName("Attempt to Update repository visibility")
    void updateRepositoryVisibility() throws IOException {

        String visibility = response.jsonPath().getString("data.updateRepository.repository.visibility");

        assertThat(visibility, is("PUBLIC"));
    }


    @Test
    @DisplayName("Repository response should not be null")
    void testRepositoryResponseNotNull() {
        assertThat(response, is(notNullValue()));
    }
}
