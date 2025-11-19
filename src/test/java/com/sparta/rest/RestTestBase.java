package com.sparta.rest;

import com.sparta.utils.Config;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RestTestBase {

    protected static final String BASE_URI = Config.getGitHubBaseUri();
    protected static final String TOKEN = Config.getToken();
    protected static final String OWNER = Config.getOwner();
    protected static final String REPO = Config.getRepo();

    protected static Response deleteRepository() {
        return RestAssured
                .given()
                .baseUri(BASE_URI)
                .header("Authorization", "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .delete("/repos/" + OWNER + "/" + REPO)
                .then()
                .log().all()
                .extract().response();
    }
}