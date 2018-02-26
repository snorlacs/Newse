package com.snorlacs.newse.controller;

import com.snorlacs.newse.ApiIntegrationTest;
import com.snorlacs.newse.domain.Article;
import com.snorlacs.newse.repository.ArticleRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityLinks;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import java.text.SimpleDateFormat;

import static com.snorlacs.newse.domain.JsonDateSerializer.DATE_FORMAT;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.TestUtils.generateTestArticle;
import static utils.TestUtils.generateUpdatedArticle;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application.properties")
public class ArticleControllerTest extends ApiIntegrationTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntityLinks entityLinks;

    @Value("${editor.username}")
    private String username;

    @Value("${editor.password}")
    private String password;

    private String authorizationSecret;

    @Before
    public void setUp() throws Exception {
        authorizationSecret = username + ":" + password;
        articleRepository.clear();
    }

    @Test
    public void testCreateFirstArticleAndCorrectnessOfArticle() throws Exception {
        Article article = generateTestArticle();
        String payload = toJsonString(article);


        ResultActions resultActions = post("/article", payload, authorizationSecret);
        resultActions.andExpect(status().isCreated());

        assertArticleStructure(resultActions, article);
        Assert.assertEquals(1, articleRepository.getCount());
    }

    @Test
    public void createArticleWithEmptyPayloadReturnsBadRequest() throws Exception {
        ResultActions resultActions = post("/article", "", authorizationSecret);
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void createArticleWithNullPayloadReturnsBadRequest() throws Exception {
        ResultActions resultActions = post("/article", "{}", authorizationSecret);
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void createArticleWithNullHeaderReturnsBadRequest() throws Exception {
        String payloadWithEmptyKeyword = "{\"shortDescription\":\"Test\",\"text\":\"This article is for testing\",\"publishedOn\":\"2018-02-24T15:20:50.567+0530\",\"authors\":[{\"name\":\"test-author\"}],\"keywords\":[\"test\"]}";
        ResultActions resultActions = post("/article", payloadWithEmptyKeyword, authorizationSecret);
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void createArticleWithNullTextReturnsBadRequest() throws Exception {
        String payloadWithEmptyKeyword = "{\"header\":\"Test Article\",\"shortDescription\":\"Test\",\"publishedOn\":\"2018-02-24T15:20:50.567+0530\",\"authors\":[{\"name\":\"test-author\"}],\"keywords\":[\"test\"]}";
        ResultActions resultActions = post("/article", payloadWithEmptyKeyword, authorizationSecret);
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void createArticleWithNullShortDescriptionReturnsBadRequest() throws Exception {
        String payloadWithEmptyKeyword = "{\"header\":\"Test Article\",\"text\":\"This article is for testing\",\"publishedOn\":\"2018-02-24T15:20:50.567+0530\",\"authors\":[{\"name\":\"test-author\"}],\"keywords\":[\"test\"]}";
        ResultActions resultActions = post("/article", payloadWithEmptyKeyword, authorizationSecret);
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void createArticleWithNullPublishedOnReturnsBadRequest() throws Exception {
        String payloadWithEmptyKeyword = "{\"header\":\"Test Article\",\"shortDescription\":\"Test\",\"text\":\"This article is for testing\",\"authors\":[{\"name\":\"test-author\"}],\"keywords\":[\"test\"]}";
        ResultActions resultActions = post("/article", payloadWithEmptyKeyword, authorizationSecret);
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void createArticleWithNullAuthorNameReturnsBadRequest() throws Exception {
        String payloadWithEmptyKeyword = "{\"header\":\"Test Article\",\"shortDescription\":\"Test\",\"text\":\"This article is for testing\",\"publishedOn\":\"2018-02-24T15:20:50.567+0530\",\"authors\":[{}],\"keywords\":[\"test\"]}";
        ResultActions resultActions = post("/article", payloadWithEmptyKeyword, authorizationSecret);
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void createArticleWithEmptyReturnsBadRequest() throws Exception {
        String payloadWithEmptyKeyword = "{\"header\":\"Test Article\",\"shortDescription\":\"Test\",\"text\":\"This article is for testing\",\"publishedOn\":\"2018-02-24T15:20:50.567+0530\",\"keywords\":[\"test\"]}";
        ResultActions resultActions = post("/article", payloadWithEmptyKeyword, authorizationSecret);
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void createArticleWithEmptyKeywordBadRequest() throws Exception {
        String payloadWithEmptyKeyword = "{\"header\":\"Test Article\",\"shortDescription\":\"Test\",\"text\":\"This article is for testing\",\"publishedOn\":\"2018-02-24T15:20:50.567+0530\",\"authors\":[{\"name\":\"test-author\"}]}";
        ResultActions resultActions = post("/article", payloadWithEmptyKeyword, authorizationSecret);
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAnArticleGivesCorrectResponse() throws Exception {
        Article newArticle = createArticle();

        ResultActions resultActions = get("/article/{id}", newArticle.getId());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newArticle.getId()));

        assertReferenceLinks(resultActions, newArticle);
        assertArticleStructure(resultActions, newArticle);

    }

    @Test
    public void getAnArticleForInvalidReturnsNotFound() throws Exception {
        long invalidId = 999L;
        get("/article/{id}", invalidId)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteAnExistingArticleSuccessfully() throws Exception {
        Article newArticle = createArticle();

        delete("/article/{id}", authorizationSecret, newArticle.getId())
                .andExpect(status().isNoContent());

    }

    @Test
    public void deletingAnInvalidArticleReturnNotFound() throws Exception {
        delete("/article/{id}", authorizationSecret, 1L).andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateAnArticleSuccessfully() throws Exception {
        Article originalArticle = createArticle();
        Article updatedArticle = generateUpdatedArticle(originalArticle);

        ResultActions resultActions = put("/article/{id}", updatedArticle, authorizationSecret, String.valueOf(originalArticle.getId()));
        resultActions.andExpect(status().isOk());
        assertArticleStructure(resultActions, updatedArticle);
    }

    @Test
    public void updatingAnInvalidArticleReturnsNotFound() throws Exception {
        put("/article/{id}", generateTestArticle(), authorizationSecret, 999L).andExpect(status().isNotFound());
    }

    @Test
    public void getArticlesWithinAPeriodReturnsTheMatchedArticles() throws Exception {
        Article article1 = createArticle();
        Article article2 = createArticle();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        get("/articles?from=" + dateFormat.format(article1.getPublishedOn()) + "&to=" + dateFormat.format(article2.getPublishedOn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getArticlesWithoutQueryParamsReturnAllArticles() throws Exception {
        createArticle();
        createArticle();
        get("/articles")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getArticlesByAuthorNameReturnsOkStatusWithMatchedArticles() throws Exception {
        Article article1 = createArticle();
        Article article2 = generateUpdatedArticle(generateTestArticle());
        articleRepository.create(article2);

        ResultActions resultActions = get("/articles?author=" + article1.getAuthors().get(0).getName());
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getArticlesByKeywordReturnOkStatusWithMatchedArticles() throws Exception {
        Article article1 = createArticle();
        Article article2 = generateUpdatedArticle(generateTestArticle());
        articleRepository.create(article2);

        ResultActions resultActions = get("/articles?keyword=" + article1.getKeywords().get(0));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getArticlesReturnNotFoundIfThereIsNoMatch() throws Exception {

        ResultActions resultActions = get("/articles?keyword=blah");
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void getArticlesByMultipleCriteriaReturnMatchedArticles() throws Exception {
        Article article1 = createArticle();
        Article article2 = generateUpdatedArticle(generateTestArticle());
        articleRepository.create(article2);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        ResultActions resultActions = get("/articles?keyword=" + article1.getKeywords().get(0) +
                "&author=" + article1.getAuthors().get(0).getName() +
                "&from=" + dateFormat.format(article1.getPublishedOn()));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(1)));
    }


    private Article createArticle() {
        Article article = generateTestArticle();
        articleRepository.create(article);
        return article;
    }

    private void assertReferenceLinks(ResultActions resultActions, Article article) throws Exception {
        String refLink = entityLinks.linkForSingleResource(article).toString();

        resultActions.andExpect(jsonPath("$._links.self.href").value(refLink));
        resultActions.andExpect(jsonPath("$._links.update.href").value(refLink));
        resultActions.andExpect(jsonPath("$._links.delete.href").value(refLink));
    }

    private void assertArticleStructure(ResultActions resultActions, Article article) throws Exception {
        resultActions
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.header").value(article.getHeader()))
                .andExpect(jsonPath("$.shortDescription").value(article.getShortDescription()))
                .andExpect(jsonPath("$.text").value(article.getText()))
                .andExpect(jsonPath("$.publishedOn").value(new SimpleDateFormat(DATE_FORMAT).format(article.getPublishedOn())))
                .andExpect(jsonPath("$.authors", hasSize(article.getAuthors().size())))
                .andExpect(jsonPath("$.authors[0].name").value(article.getAuthors().get(0).getName()))
                .andExpect(jsonPath("$.keywords", hasSize(article.getKeywords().size())))
                .andExpect(jsonPath("$.keywords[0]").value(article.getKeywords().get(0)));
    }

}