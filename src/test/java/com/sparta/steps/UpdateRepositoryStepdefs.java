package com.sparta.steps;

import com.sparta.graphql.TestBase;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateRepoStepdefs extends TestBase {
    private final Map<String, Object> variables = new HashMap<>();
    private Response response;

    @Given("I have a valid repository ID")
    public void iHaveAValidRepositoryID() {
        variables.put("repositoryId", REPOSITORY_ID);
    }

    @And("an existing repository")
    public void anExistingRepository() {
    }

    @When("I send an updateRepository mutation with name {string} and description {string}")
    public void iSendAnUpdateRepositoryMutationWithNameAndDescription(String arg0, String arg1) throws IOException {
        variables.put("name", name );
        variables.put("description", description);
        String query = readQuery("UpdateRepository.graphql");
        response = executeQuery(query, "UpdateRepository", variables);
    }

    @Then("the repository name should be {string}")
    public void theRepositoryNameShouldBe(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("the repository description should be {string}")
    public void theRepositoryDescriptionShouldBe(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("I update only the description to {string}")
    public void iUpdateOnlyTheDescriptionTo(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("I update the homepageUrl to {string}")
    public void iUpdateTheHomepageUrlTo(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("the repository homepageUrl should be {string}")
    public void theRepositoryHomepageUrlShouldBe(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("a repository ID that does not exist")
    public void aRepositoryIDThatDoesNotExist() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("I send an updateRepository mutation")
    public void iSendAnUpdateRepositoryMutation() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("the response should contain GraphQL errors")
    public void theResponseShouldContainGraphQLErrors() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("the error message should indicate that the repository was not found")
    public void theErrorMessageShouldIndicateThatTheRepositoryWasNotFound() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("I send an updateRepository mutation with an empty name")
    public void iSendAnUpdateRepositoryMutationWithAnEmptyName() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("the error message should indicate that {string}")
    public void theErrorMessageShouldIndicateThat(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("I have an invalid or expired token")
    public void iHaveAnInvalidOrExpiredToken() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("I send the updateRepository mutation with invalid authentication")
    public void iSendTheUpdateRepositoryMutationWithInvalidAuthentication() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("the request should fail with an authentication error")
    public void theRequestShouldFailWithAnAuthenticationError() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("no authentication header is provided")
    public void noAuthenticationHeaderIsProvided() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("I send the updateRepository mutation with no authentication")
    public void iSendTheUpdateRepositoryMutationWithNoAuthentication() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("the response should indicate missing authentication")
    public void theResponseShouldIndicateMissingAuthentication() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("an invalid field value for homepageUrl {string}")
    public void anInvalidFieldValueForHomepageUrl(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("I send the updateRepository mutation")
    public void iSendTheUpdateRepositoryMutation() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("the error message should indicate invalid input formatting")
    public void theErrorMessageShouldIndicateInvalidInputFormatting() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
