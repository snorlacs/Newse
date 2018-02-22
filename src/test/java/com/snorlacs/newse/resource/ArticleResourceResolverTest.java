package com.snorlacs.newse.resource;

import com.snorlacs.newse.domain.Article;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;

import java.util.ArrayList;

import static com.snorlacs.newse.resource.ArticleResourceResolver.DELETE_REL;
import static com.snorlacs.newse.resource.ArticleResourceResolver.UPDATE_REL;
import static org.mockito.Mockito.when;
import static utils.TestUtils.generateTestArticle;

@RunWith(MockitoJUnitRunner.class)
public class ArticleResourceResolverTest {

    @Mock
    private EntityLinks entityLinks;

    @InjectMocks
    private ArticleResourceResolver articleResourceResolver;

    @Test
    public void shouldCreateArticleResourceWithRefLinks() throws Exception {
        Article article = generateTestArticle();
        article.setId(1L);
        Link link = new Link("http://localhost/article/1");

        when(entityLinks.linkToSingleResource(article)).thenReturn(link);

        ArticleResource articleResource = new ArticleResource(article);
        articleResource.add(new ArrayList<Link>() {{
            add(link.withSelfRel());
            add(link.withRel(UPDATE_REL));
            add(link.withRel(DELETE_REL));
        }});
        Assert.assertEquals(articleResourceResolver.toResource(article), articleResource);

    }


}