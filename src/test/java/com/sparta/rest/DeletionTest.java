package com.sparta.rest;

import com.sparta.utils.Config;
import com.sparta.utils.GitHubRestClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeletionTest {

    @Test
    void testDeleteRepository() {
        String owner = Config.getOwner();
        String repo = "automation-test-repo";


        Response response = GitHubRestClient.deleteRepository(owner, repo);
        assertEquals(204, response.statusCode());
    }
}
