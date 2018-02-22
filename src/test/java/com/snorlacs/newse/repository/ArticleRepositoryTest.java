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

        Optional<Article> foundArticle = repository.findById(article.getId());
        Assert.assertTrue(foundArticle.isPresent());
    }

    @Test
    public void testDeleteArticleIfArticleIsPresent() {
        when(idGenerator.getId()).thenReturn(1L);

        Article article = repository.create(TestUtils.generateTestArticle());

        boolean isDeleted = repository.delete(article.getId());
        Assert.assertTrue(isDeleted);
        Assert.assertEquals( 0, repository.getCount());
    }

    @Test
    public void testDeleteArticleOnAbsenceOfArticle() {
        boolean isDeleted = repository.delete(2L);
        Assert.assertFalse(isDeleted);
    }

}