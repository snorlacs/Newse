package com.snorlacs.newse.automation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.snorlacs.newse.automation.util.StepsUtil;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.Map;

public class Steps extends StepsUtil {

    private static final String TEST_ARTICLE = "{\"header\":\"Test Article\",\"shortDescription\":\"Test\",\"text\":\"This article is for testing\",\"publishedOn\":\"2018-02-24T15:20:50.567+0530\",\"authors\":[{\"name\":\"test-author\"}],\"keywords\":[\"test\"]}";
    private static final String UPDATED_TEST_ARTICLE = "{\"header\":\"Test Article updated\",\"shortDescription\":\"Test updated\",\"text\":\"This article is for testing updated\",\"publishedOn\":\"2018-02-25T15:20:50.567+0530\",\"authors\":[{\"name\":\"updated-test-author\"}],\"keywords\":[\"updated-test\"]}";
    private static final TypeReference<Map<String, Object>> RESOURCE_TYPE = new TypeReference<Map<String, Object>>() {};

    @Given("^an article exists$")
    public void anArticleExists() throws Exception {
        createArticle();
    }

    @When("^the user gets the article$")
    public void userRetrievesTheArticle() throws Exception {
        get("/article/{id}", getArticleId());
    }

    @Then("^receives (\\d+) status code$")
    public void theUserReceivesStatusCodeOf(int statusCode) throws Exception {
        Assert.assertEquals(statusCode, getLastStatusCode());
    }

    @When("^the editor updates the article$")
    public void editorUpdatesTheArticle() throws Exception {
        put("/article/{id}", UPDATED_TEST_ARTICLE ,getArticleId());
    }


    @And("^the retrieved article is correct$")
    public void theRetrievedArticleIsCorrect() throws Exception {
        Map<String, Object> expectedResource = getLastPostContentAs(RESOURCE_TYPE);
        Map<String, Object> actualResource = getLastGetContentAs(RESOURCE_TYPE);
        assertOnResource(expectedResource, actualResource);
    }

    @And("^the article is updated")
    public void theArticleIsUpdated() throws Exception {
        Map<String, Object> expectedResource = getLastPutContentAs(RESOURCE_TYPE);
        Map<String, Object> actualResource = getLastGetContentAs(RESOURCE_TYPE);
        assertOnResource(expectedResource, actualResource);
    }

    private void assertOnResource(Map<String, Object> expectedResource, Map<String, Object> actualResource) {
        Assert.assertEquals(actualResource.size(), expectedResource.size());

        expectedResource.forEach((key,value) -> Assert.assertEquals(expectedResource.get(key), actualResource.get(key)));
    }

    @And("^the article is created successfully$")
    public void theArticleIsCreatedSuccessfully() throws Exception {
        Assert.assertEquals(201, getLastPostResponse().getStatus());
    }

    @When("^the editor creates an article$")
    public void editorCreatesArticle() throws Exception {
        createArticle();
    }

    @When("^the editor deletes the article$")
    public void editorDeletesArticle() throws Exception {
        delete("/article/{id}", getArticleId());
    }

    private void createArticle() throws Exception {
        post("/article", TEST_ARTICLE);
    }

    private Object getArticleId() throws Exception {
        return getLastPostContentAs(RESOURCE_TYPE).get("id");
    }
}
