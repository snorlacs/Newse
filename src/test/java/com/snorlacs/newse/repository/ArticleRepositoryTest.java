package com.snorlacs.newse.repository;

import com.snorlacs.newse.domain.Article;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import utils.TestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ArticleRepositoryTest {

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private ArticleRepository repository;

    @Before
    public void setUp() throws Exception {
        repository.clear();
    }

    @Test
    public void testFindById() throws Exception {
        when(idGenerator.getId()).thenReturn(1L);

        Article article = repository.create(TestUtils.generateTestArticle());

        Optional<Article> foundArticle = repository.findOne(article.getId());
        Assert.assertTrue(foundArticle.isPresent());
    }

    @Test
    public void testDeleteArticleIfArticleIsPresent() {
        when(idGenerator.getId()).thenReturn(1L);

        Article article = repository.create(TestUtils.generateTestArticle());

        boolean isDeleted = repository.delete(article.getId());
        Assert.assertTrue(isDeleted);
        Assert.assertEquals(0, repository.getCount());
    }

    @Test
    public void testDeleteArticleOnAbsenceOfArticle() {
        boolean isDeleted = repository.delete(2L);
        Assert.assertFalse(isDeleted);
    }

    @Test
    public void updateArticleIfArticleIsPresentShouldReturnTrue() throws Exception {
        when(idGenerator.getId()).thenReturn(1L);

        Article article = repository.create(TestUtils.generateTestArticle());

        Assert.assertTrue(repository.update(article.getId(), TestUtils.generateUpdatedArticle(article)));
    }

    @Test
    public void updateArticleIfArticleIsAbsentShouldReturnFalse() throws Exception {
        Assert.assertFalse(repository.update(2L, TestUtils.generateTestArticle()));
    }

    @Test
    public void updateArticleReturnsFalseForUpdatingArticleToNull() throws Exception {
        Assert.assertFalse(repository.update(2L, null));
    }

    @Test
    public void findArticleByKeyWordReturnsTheMatchedArticles() throws Exception {
        when(idGenerator.getId()).thenReturn(1L).thenReturn(2L);

        Article article1 = repository.create(TestUtils.generateTestArticle());
        Article article2 = repository.create(TestUtils.generateTestArticle());

        Assert.assertEquals(Arrays.asList(article1, article2),repository.findByKeyword(article1.getKeywords().get(0)));
    }

    @Test
    public void findArticlesByKeyWordReturnsEmptyListIfNoArticlesAreMatched() throws Exception {
        Assert.assertEquals(Collections.emptyList(), repository.findByKeyword("blah"));
    }

    @Test
    public void findArticleByAuthorReturnsTHeMatchedArticles() throws Exception {
        when(idGenerator.getId()).thenReturn(1L).thenReturn(2L);

        Article article1 = repository.create(TestUtils.generateTestArticle());
        Article article2 = repository.create(TestUtils.generateTestArticle());

        Assert.assertEquals(Arrays.asList(article1, article2), repository.findByAuthorName(article1.getAuthors().get(0).getName()));
    }

    @Test
    public void findArticleByAuthorReturnsEmptyListIfNoArticlesAreMatched() throws Exception {
        Assert.assertEquals(Collections.emptyList(), repository.findByAuthorName("batman"));
    }
}