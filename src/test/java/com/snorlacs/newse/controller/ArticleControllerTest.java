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


@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleControllerTest extends ApiIntegrationTest{

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
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.header").value(article.getHeader()))
                .andExpect(jsonPath("$.shortDescription").value(article.getShortDescription()))
                .andExpect(jsonPath("$.text").value(article.getText()))
                .andExpect(jsonPath("$.publishedOn").value(article.getPublishedOn()))
                .andExpect(jsonPath("$.authors", hasSize(1)))
                .andExpect(jsonPath("$.authors[0].name").value( article.getAuthors().get(0).getName()))
                .andExpect(jsonPath("$.keywords", hasSize(1)))
                .andExpect(jsonPath("$.keywords[0]").value(article.getKeywords().get(0)));

        Assert.assertEquals(1, articleRepository.getCount());
    }

}