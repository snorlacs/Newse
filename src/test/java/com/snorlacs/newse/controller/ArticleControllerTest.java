package com.snorlacs.newse.controller;

import com.snorlacs.newse.ApiIntegrationTest;
import com.snorlacs.newse.domain.Article;
import com.snorlacs.newse.repository.ArticleRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityLinks;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.TestUtils.generateTestArticle;
import static utils.TestUtils.generateUpdatedArticle;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleControllerTest extends ApiIntegrationTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntityLinks entityLinks;

    @Before
    public void setUp() throws Exception {
        articleRepository.clear();
    }

    @Test
    public void testCreateFirstArticleAndCorrectnessOfArticle() throws Exception {
        Article article = generateTestArticle();
        String payload = toJsonString(article);

        ResultActions resultActions = post("/article", payload);
        resultActions.andExpect(status().isCreated());

        assertArticleStructure(resultActions, article);
        Assert.assertEquals(1, articleRepository.getCount());
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

        delete("/article/{id}", newArticle.getId())
                .andExpect(status().isNoContent());

    }

    @Test
    public void deletingAnInvalidArticleReturnNotFound() throws Exception {
        delete("/article/{id}", 1L).andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateAnArticleSuccessfully() throws Exception {
        Article originalArticle = createArticle();
        Article updatedArticle = generateUpdatedArticle(originalArticle);

        ResultActions resultActions = put("/article/{id}", updatedArticle, String.valueOf(originalArticle.getId()));
        resultActions.andExpect(status().isOk());
        assertArticleStructure(resultActions, updatedArticle);
    }

    @Test
    public void updatingAnInvalidArticleReturnsNotFound() throws Exception {
        put("/article/{id}", generateTestArticle(), 999L).andExpect(status().isNotFound());
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
                .andExpect(jsonPath("$.publishedOn").value(article.getPublishedOn()))
                .andExpect(jsonPath("$.authors", hasSize(article.getAuthors().size())))
                .andExpect(jsonPath("$.authors[0].name").value(article.getAuthors().get(0).getName()))
                .andExpect(jsonPath("$.keywords", hasSize(article.getKeywords().size())))
                .andExpect(jsonPath("$.keywords[0]").value(article.getKeywords().get(0)));
    }

}