package com.sparta.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GitHubRestClient {

    private static final String TOKEN = Config.getToken();

    public static Response deleteRepository(String owner, String repoName) {

        return RestAssured.given()
                .header("Authorization", "Bearer " + TOKEN)
                .header("Accept", "application/vnd.github+json")
                .delete("https://api.github.com/repos/" + owner + "/" + repoName)
                .andReturn();
    }
}