package com.sparta.steps;

import com.sparta.graphql.TestBase;
import com.sparta.utils.GitHubRestClient;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateRepoStepDefs extends TestBase {

    private static String validToken;
    private static String repoName;

    private static String visibility = "PRIVATE";
    private static String description = "";

    private static String query;
    private static Map<String, Object> variables;
    private static Response response;

    @Given("a valid Github Token")
    public void aValidGithubToken() {
        validToken = TOKEN;
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

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        assertThat(response.statusCode(), is(statusCode));
    }

    @And("the GraphQL response should contain no errors")
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


    @After
    public void cleanUp(){

        GitHubRestClient.deleteRepository(OWNER, repoName);
    }

}
