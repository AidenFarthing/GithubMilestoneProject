package com.sparta.steps;

import com.sparta.graphql.TestBase;
import com.sparta.utils.GitHubRestClient;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateRepoStepDefs extends TestBase {

    private static String strToken;
    private static String repoName;

    enum Visiblity {
        PRIVATE,
        PUBLIC
    }


    private static Visiblity visibility = Visiblity.PRIVATE;


    private static String description;

    private static String query;
    private static Map<String, Object> variables;
    private static Response response;

    @Given("a valid Github Token")
    public void aValidGithubToken() {
        strToken = TOKEN;
    }

    @And("a valid Repository Name")
    public void aValidRepositoryName() {
        repoName = "Automated-Test-Repo";
    }

    @When("I run the createRepository Query")
    public void iRunTheCreateRepositoryQuery() throws IOException {
        query = readQuery("CreateRepo.graphql");
        variables = Map.of(
                "name",repoName,
                "visibility",visibility,
                "description", description
        );

        response = executeQuery(query,"CreateRepository",variables);

    }

    @Then("the status code of the response should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        assertThat(response.statusCode(), is(statusCode));
    }

    @And("the response from GraphQL should contain no errors")
    public void theGraphQLResponseShouldContainNoErrors() {
        assertThat(response.jsonPath().getList("errors"), is(nullValue()));
    }

    @And("I should receive a repository object")
    public void iShouldReceiveARepositoryObject() {
        assertThat(response.path("data.createRepository.repository"), not(nullValue()));
    }

    @And("a Description")
    public void aDescription() {
        description = "This Repository was created automatically using GraphQl!";
    }

    @Then("the response should have an ID field")
    public void theResponseShouldHaveAnIDField() {
        assertThat(response.path("data.createRepository.repository.id"), not(nullValue()));
    }

    @And("the Repository Name should match my Input")
    public void theRepositoryNameShouldMatchMyInput() {
        assertThat(response.path("data.createRepository.repository.name"), is(repoName));
    }

    @And("the Repository Description should match my Input")
    public void theRepositoryDescriptionShouldMatchMyInput() {
        assertThat(response.path("data.createRepository.repository.description"), is(description));
    }

    @And("the Creation Timestamp should match today's date")
    public void theCreationTimestampShouldMatchTodaySDate() {

        String createdAtStr = response.path("data.createRepository.repository.createdAt");

        Instant createdAt = Instant.parse(createdAtStr);

        LocalDate createDate = createdAt.atZone(ZoneOffset.UTC).toLocalDate();
        LocalDate today = LocalDate.now(ZoneOffset.UTC);

        assertThat(createDate, is(today));
    }

    @And("the repository should have a valid url")
    public void theRepositoryShouldHaveAValidUrl() {
        assertThat(response.path("data.createRepository.repository.url"),
                matchesPattern("^https://github\\.com/[^/]+/" + repoName + "$"));
    }

    @And("the Visibility should be Private")
    public void theVisibilityShouldBePrivate() {
        assertThat(response.path("data.createRepository.repository.visibility"), is("PRIVATE"));
    }

    @After("not @Keep")
    public void cleanUp(){
        GitHubRestClient.deleteRepository(OWNER, repoName);
    }

    @And("no Description")
    public void noDescription() {
        description = "";
    }

    @Then("the response should have a null description")
    public void theResponseShouldHaveAnEmptyDescription() {
        assertThat(response.path("data.createRepository.repository.description"), is(nullValue()));
    }

    @And("Visibility is Public")
    public void visibilityIsPublic() {
        visibility = Visiblity.PUBLIC;
    }

    @Then("the response should have visibility set to Public")
    public void theResponseShouldHaveVisibilitySetToPublic() {
        assertThat(response.path("data.createRepository.repository.visibility"), is("PUBLIC"));
    }

    @Given("an Invalid Github Token")
    public void anInvalidGithubToken() {
        strToken = "INVALID_TOKEN";
    }

    @When("I run the createRepository Query with my Invalid Token")
    public void iRunTheCreateRepositoryQueryWithMyInvalidToken() throws IOException {
        query = readQuery("CreateRepo.graphql");
        variables = Map.of(
                "name",repoName,
                "visibility",visibility,
                "description", description
        );

        response = executeQuery(query,"CreateRepository",variables, strToken);

    }



    private Response executeQuery(String query, String operationName, Map<String, Object> variables, String token){
        Map<String, Object> body = Map.of(
                "query", query,
                "operationName", operationName,
                "variables", variables
        );

        return RestAssured
                .given()
                .baseUri(BASE_URI)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body)
                .log().all()
                .when()
                .post()
                .then()
                .log().all()
                .extract().response();
    }


    @And("a Repository Name that already Exists")
    public void aRepositoryNameThatAlreadyExists() {
        repoName = "GraphQL-Repository-that-already-exists";
    }

    @Then("I should receive an error saying Name already exists on this account")
    public void iShouldReceiveAnErrorSayingNameAlreadyExistsOnThisAccount() {
        assertThat(response.path("errors[0].message"),is("Name already exists on this account"));
    }

    @And("a Blank repository name")
    public void aBlankRepositoryName() {
        repoName = "";
    }

    @Then("I should receive an error saying Name can't be blank")
    public void iShouldReceiveAnErrorSayingNameCanTBeBlank() {
        assertThat(response.path("errors[0].message"),is("Name can't be blank, Name is too short (minimum is 1 character)"));
    }
}
